package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IOrderedEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
class ThenByTest extends TestCase {
    private static IEnumerable<Object[]> SortsLargeAscendingEnumerableCorrectly_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1);
        argsList.add(2);
        argsList.add(3);
        return argsList;
    }

    private static IEnumerable<Object[]> SortsLargeDescendingEnumerableCorrectly_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1);
        argsList.add(2);
        argsList.add(3);
        return argsList;
    }

    private static IEnumerable<Object[]> SortsLargeRandomizedEnumerableCorrectly_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1);
        argsList.add(2);
        argsList.add(3);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Tuple2<Integer, Integer>> q = Linq.of(new int[]{1, 6, 0, -1, 3})
                .selectMany(x -> Linq.of(new int[]{55, 49, 9, -100, 24, 25}), (x1, x2) -> Tuple.create(x1, x2));

        assertEquals(q.orderByDescending(e -> e.getItem1()).thenBy(f -> f.getItem2()),
                q.orderByDescending(e -> e.getItem1()).thenBy(f -> f.getItem2()));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<Tuple2<Integer, String>> q = Linq.of(new int[]{55, 49, 9, -100, 24, 25, -1, 0})
                .selectMany(x -> Linq.of("!@#$%^", "C", "AAA", "", null, "Calling Twice", "SoS", Empty).where(a -> !IsNullOrEmpty(a)), (x1, x2) -> Tuple.create(x1, x2));

        assertEquals(q.orderBy(e -> e.getItem2()).thenBy(f -> f.getItem1()),
                q.orderBy(e -> e.getItem2()).thenBy(f -> f.getItem1()));
    }

    @Test
    void SourceEmpty() {
        int[] source = {};

        assertEmpty(Linq.of(source).orderBy(e -> e).thenBy(e -> e));
    }

    @Test
    void SecondaryKeysAreUnique() {
        UserAddress[] source = new UserAddress[]{
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Tim", "Seattle", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Chris", "London", "UK"),
                new UserAddress("Rob", "Kent", "UK")
        };
        UserAddress[] expected = new UserAddress[]{
                new UserAddress("Rob", "Kent", "UK"),
                new UserAddress("Chris", "London", "UK"),
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Tim", "Seattle", "USA")
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Country).thenBy(e -> e.City));
    }

    @Test
    void OrderByAndThenByOnSameField() {
        UserAddress[] source = new UserAddress[]{
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Prakash", "Chennai", "India"),
                new UserAddress("Rob", "Kent", "UK")
        };
        UserAddress[] expected = new UserAddress[]{
                new UserAddress("Prakash", "Chennai", "India"),
                new UserAddress("Rob", "Kent", "UK"),
                new UserAddress("Jim", "Minneapolis", "USA")
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Country).thenBy(e -> e.Country, null));
    }

    @Test
    void SecondKeyRepeatAcrossDifferentPrimary() {
        UserAddress[] source = new UserAddress[]{
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Tim", "Seattle", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Chris", "Minneapolis", "USA"),
                new UserAddress("Rob", "Seattle", "USA")
        };
        UserAddress[] expected = new UserAddress[]{
                new UserAddress("Chris", "Minneapolis", "USA"),
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Rob", "Seattle", "USA"),
                new UserAddress("Tim", "Seattle", "USA")
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Name).thenBy(e -> e.City, null));
    }

    @Test
    void OrderIsStable() {
        String[] source = split("Because I could not stop for Death -\nHe kindly stopped for me -\nThe Carriage held but just Ourselves -\nAnd Immortality.", new char[]{' ', '\n', '\r', '-'}, true);
        String[] expected = new String[]{
                "me", "not", "for", "for", "but", "stop", "held", "just", "could", "kindly", "stopped",
                "I", "He", "The", "And", "Death", "Because", "Carriage", "Ourselves", "Immortality."
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(word -> Character.isUpperCase(word.charAt(0))).thenBy(word -> word.length()));
    }

    @Test
    void RunOnce() {
        String[] source = split("Because I could not stop for Death -\nHe kindly stopped for me -\nThe Carriage held but just Ourselves -\nAnd Immortality.", new char[]{' ', '\n', '\r', '-'}, true);
        String[] expected = new String[]{
                "me", "not", "for", "for", "but", "stop", "held", "just", "could", "kindly", "stopped",
                "I", "He", "The", "And", "Death", "Because", "Carriage", "Ourselves", "Immortality."
        };

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().orderBy(word -> Character.isUpperCase(word.charAt(0))).thenBy(word -> word.length()));
    }

    @Test
    void NullSource() {
        IOrderedEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.thenBy(i -> i));
    }

    @Test
    void NullKeySelector() {
        Func1<Date, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().orderBy(e -> e).thenBy(keySelector));
    }

    @Test
    void NullSourceComparer() {
        IOrderedEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.thenBy(i -> i, null));
    }

    @Test
    void NullKeySelectorComparer() {
        Func1<Date, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().orderBy(e -> e).thenBy(keySelector, null));
    }

    @ParameterizedTest
    @MethodSource("SortsLargeAscendingEnumerableCorrectly_TestData")
    void SortsLargeAscendingEnumerableCorrectly(int thenBys) {
        final int Items = 100_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> i);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(x -> 0);
        switch (thenBys) {
            case 1:
                ordered = ordered.thenBy(i -> i);
                break;
            case 2:
                ordered = ordered.thenBy(i -> 0).thenBy(i -> i);
                break;
            case 3:
                ordered = ordered.thenBy(i -> 0).thenBy(i -> 0).thenBy(i -> i);
                break;
        }

        assertEquals(expected, ordered);
    }

    @ParameterizedTest
    @MethodSource("SortsLargeDescendingEnumerableCorrectly_TestData")
    void SortsLargeDescendingEnumerableCorrectly(int thenBys) {
        final int Items = 100_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> Items - i - 1);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(x -> 0);
        switch (thenBys) {
            case 1:
                ordered = ordered.thenBy(i -> i);
                break;
            case 2:
                ordered = ordered.thenBy(i -> 0).thenBy(i -> i);
                break;
            case 3:
                ordered = ordered.thenBy(i -> 0).thenBy(i -> 0).thenBy(i -> i);
                break;
        }

        assertEquals(expected, ordered);
    }

    @ParameterizedTest
    @MethodSource("SortsLargeRandomizedEnumerableCorrectly_TestData")
    void SortsLargeRandomizedEnumerableCorrectly(int thenBys) {
        final int Items = 100_000;
        Random r = new Random(42);

        Integer[] randomized = Linq.range(0, Items).select(i -> r.nextInt()).toArray(Integer.class);

        IOrderedEnumerable<Integer> orderedEnumerable = Linq.of(randomized).orderBy(x -> 0);
        switch (thenBys) {
            case 1:
                orderedEnumerable = orderedEnumerable.thenBy(i -> i);
                break;
            case 2:
                orderedEnumerable = orderedEnumerable.thenBy(i -> 0).thenBy(i -> i);
                break;
            case 3:
                orderedEnumerable = orderedEnumerable.thenBy(i -> 0).thenBy(i -> 0).thenBy(i -> i);
                break;
        }
        Array<Integer> ordered = orderedEnumerable.toArray();

        Arrays.sort(randomized);
        assertEquals(Linq.of(randomized), orderedEnumerable);
    }


    private static class UserAddress extends ValueType {
        private final String Name;
        private final String City;
        private final String Country;

        private UserAddress(String name, String city, String country) {
            this.Name = name;
            this.City = city;
            this.Country = country;
        }
    }
}
