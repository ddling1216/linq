package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Action1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by 许崇雷 on 2019-08-07.
 */
class ToCollectionTest extends TestCase {
    @Test
    void ToCollection_AlwaysCreateACopy() {
        List<Integer> sourceList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> resultList = Linq.of(sourceList).toCollection(new ArrayList<>(), List::add);

        assertNotSame(sourceList, resultList);
        assertEquals(sourceList, resultList);
    }

    private <T> void RunToCollectionOnAllCollectionTypes(T[] items, Action1<List<T>> validation) {
        validation.apply(Linq.of(items).toCollection(new ArrayList<>(), List::add));
        validation.apply(Linq.of(Arrays.asList(items)).toCollection(new ArrayList<>(), List::add));
        validation.apply(new TestEnumerable<>(items).toCollection(new ArrayList<>(), List::add));
        validation.apply(new TestReadOnlyCollection<>(items).toCollection(new ArrayList<>(), List::add));
        validation.apply(new TestCollection<>(items).toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void ToCollection_WorkWithEmptyCollection() {
        this.RunToCollectionOnAllCollectionTypes(new Integer[0], resultList -> {
            assertNotNull(resultList);
            assertEquals(0, resultList.size());
        });
    }

    @Test
    void ToCollection_ProduceCorrectList() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        this.RunToCollectionOnAllCollectionTypes(sourceArray, resultList -> {
            assertEquals(sourceArray.length, resultList.size());
            assertEquals(Linq.of(sourceArray), Linq.of(resultList));
        });

        String[] sourceStringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        this.RunToCollectionOnAllCollectionTypes(sourceStringArray, resultStringList -> {
            assertEquals(sourceStringArray.length, resultStringList.size());
            for (int i = 0; i < sourceStringArray.length; i++)
                assertSame(sourceStringArray[i], resultStringList.get(i));
        });
    }

    @Test
    void RunOnce() {
        assertEquals(Linq.range(3, 9), Linq.of(Linq.range(3, 9).runOnce().aggregate(new ArrayList<>(), (x, y) -> {
            x.add(y);
            return x;
        })));
    }

    @Test
    void ToCollection_TouchCountWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        List<Integer> resultList = source.toCollection(new ArrayList<>(), List::add);

        assertEquals(source, Linq.of(resultList));
        assertEquals(0, source.CountTouched);
    }

    @Test
    void ToCollection_ThrowArgumentNullExceptionWhenSourceIsNull() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toCollection(new ArrayList<>(), List::add));
    }

    // Generally the optimal approach. Anything that breaks this should be confirmed as not harming performance.
    @Test
    void ToCollection_UseCopyToWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        List<Integer> resultList = source.toCollection(new ArrayList<>(), List::add);

        assertEquals(source, Linq.of(resultList));
        // This is different from C#. C# call ICollection.copy in List.ctor(), but java call Collection.toArray() then copy the array in ArrayList.ctor().
        assertEquals(0, source.CopyToTouched);
        assertEquals(0, source.ToListTouched);
    }

    @Test
    void ToCollection_ArrayWhereSelect() {
        this.ToCollection_ArrayWhereSelect(new int[]{}, new String[]{});
        this.ToCollection_ArrayWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToCollection_ArrayWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToCollection_ArrayWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = new ArrayList<>(Linq.of(sourceIntegers).toList());
        List<String> convertedList = new ArrayList<>(Linq.of(convertedStrings).toList());

        List<Integer> emptyIntegersList = new ArrayList<>();
        List<String> emptyStringsList = new ArrayList<>();

        assertEquals(convertedList, Linq.of(sourceIntegers).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));

        assertEquals(sourceList, Linq.of(sourceIntegers).where(i -> true).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyIntegersList, Linq.of(sourceIntegers).where(i -> false).toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceIntegers).where(i -> true).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyStringsList, Linq.of(sourceIntegers).where(i -> false).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s != null).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyStringsList, Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s == null).toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void ToCollection_ListWhereSelect() {
        this.ToCollection_ListWhereSelect(new int[]{}, new String[]{});
        this.ToCollection_ListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToCollection_ListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});

    }

    private void ToCollection_ListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = new ArrayList<>(Linq.of(sourceIntegers).toList());
        List<String> convertedList = new ArrayList<>(Linq.of(convertedStrings).toList());

        List<Integer> emptyIntegersList = new ArrayList<>();
        List<String> emptyStringsList = new ArrayList<>();

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));

        assertEquals(sourceList, Linq.of(sourceList).where(i -> true).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyIntegersList, Linq.of(sourceList).where(i -> false).toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceList).where(i -> true).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyStringsList, Linq.of(sourceList).where(i -> false).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s != null).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyStringsList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s == null).toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void ToCollection_IListWhereSelect() {
        this.ToCollection_IListWhereSelect(new int[]{}, new String[]{});
        this.ToCollection_IListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToCollection_IListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToCollection_IListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = Collections.unmodifiableList(Linq.of(sourceIntegers).toList());
        List<String> convertedList = Collections.unmodifiableList(Linq.of(convertedStrings).toList());

        List<Integer> emptyIntegersList = Collections.unmodifiableList(Linq.<Integer>empty().toCollection(new ArrayList<>(), List::add));
        List<String> emptyStringsList = Collections.unmodifiableList(Linq.<String>empty().toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));

        assertEquals(sourceList, Linq.of(sourceList).where(i -> true).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyIntegersList, Linq.of(sourceList).where(i -> false).toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceList).where(i -> true).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyStringsList, Linq.of(sourceList).where(i -> false).select(i -> i.toString()).toCollection(new ArrayList<>(), List::add));

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s != null).toCollection(new ArrayList<>(), List::add));
        assertEquals(emptyStringsList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s == null).toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void SameResultsRepeatCallsFromWhereOnIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.toCollection(new ArrayList<>(), List::add), q.toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void SameResultsRepeatCallsFromWhereOnStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.toCollection(new ArrayList<>(), List::add), q.toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void SourceIsEmptyICollectionT() {
        int[] source = {};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEmpty(Linq.of(Linq.of(source).toCollection(new ArrayList<>(), List::add)));
        assertEmpty(Linq.of(collection.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void SourceIsICollectionTWithFewElements() {
        Integer[] source = {-5, null, 0, 10, 3, -1, null, 4, 9};
        Integer[] expected = {-5, null, 0, 10, 3, -1, null, 4, 9};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEquals(Linq.of(expected), Linq.of(Linq.of(source).toCollection(new ArrayList<>(), List::add)));
        assertEquals(Linq.of(expected), Linq.of(collection.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void SourceNotICollectionAndIsEmpty() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 0);
        assertEmpty(Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void SourceNotICollectionAndHasElements() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 10);
        int[] expected = {-4, -3, -2, -1, 0, 1, 2, 3, 4, 5};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void SourceNotICollectionAndAllNull() {
        IEnumerable<Integer> source = RepeatedNullableNumberGuaranteedNotCollectionType(null, 5);
        Integer[] expected = {null, null, null, null, null};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void ConstantTimeCountPartitionSelectSameTypeToCollection() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void ConstantTimeCountPartitionSelectDiffTypeToCollection() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void ConstantTimeCountEmptyPartitionSelectSameTypeToCollection() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void ConstantTimeCountEmptyPartitionSelectDiffTypeToCollection() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void NonConstantTimeCountPartitionSelectSameTypeToCollection() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void NonConstantTimeCountPartitionSelectDiffTypeToCollection() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void NonConstantTimeCountEmptyPartitionSelectSameTypeToCollection() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void NonConstantTimeCountEmptyPartitionSelectDiffTypeToCollection() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.of(source.toCollection(new ArrayList<>(), List::add)));
    }

    @Test
    void testToCollection() {
        Object[] source = {1, 2, 3};
        List<Integer> target = Linq.of(source).cast(Integer.class).toCollection(new ArrayList<>(), List::add);
        assertEquals(3, target.size());
        assertTrue(Linq.of(source).cast(Integer.class).sequenceEqual(Linq.of(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        List<Integer> target2 = Linq.of(source2).toCollection(new ArrayList<>(), List::add);
        assertEquals(3, target2.size());
        assertTrue(Linq.of(target2).sequenceEqual(Linq.of(source2)));

        List<Character> lst = Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).toCollection(new ArrayList<>(), List::add);
        assertEquals(5, lst.size());
        assertEquals("o", lst.get(4).toString());

        List<Character> linkedLst = Linq.of(new LinkedList<>(Arrays.asList('h', 'e', 'l', 'l', 'o'))).toCollection(new ArrayList<>(), List::add);
        assertEquals(5, linkedLst.size());
        assertEquals("o", linkedLst.get(4).toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        List<Character> arr = Linq.of(arrChar).toCollection(new ArrayList<>(), List::add);
        assertEquals(5, arr.size());
        assertEquals("o", arr.get(4).toString());

        List<Character> hello = Linq.chars("hello").toCollection(new ArrayList<>(), List::add);
        assertEquals(5, hello.size());
        assertEquals("o", hello.get(4).toString());

        List<Character> h = Linq.singleton('h').toCollection(new ArrayList<>(), List::add);
        assertEquals(1, h.size());
        assertEquals("h", h.get(0).toString());

        List<Character> empty = Linq.<Character>empty().toCollection(new ArrayList<>(), List::add);
        assertEquals(0, empty.size());

        assertEquals(Arrays.asList(true, false, false), Linq.of(new boolean[]{true, false, false}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList((byte) 1, (byte) 2, (byte) 3), Linq.of(new byte[]{1, 2, 3}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList((short) 1, (short) 2, (short) 3), Linq.of(new short[]{1, 2, 3}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList(1, 2, 3), Linq.of(new int[]{1, 2, 3}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList(1L, 2L, 3L), Linq.of(new long[]{1, 2, 3}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList('a', 'b', 'c'), Linq.of(new char[]{'a', 'b', 'c'}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList(1f, 2f, 3f), Linq.of(new float[]{1f, 2f, 3f}).toCollection(new ArrayList<>(), List::add));
        assertEquals(Arrays.asList(1d, 2d, 3d), Linq.of(new double[]{1d, 2d, 3d}).toCollection(new ArrayList<>(), List::add));
    }

    @Test
    void testArgumentNull() {
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().toCollection((List<Integer>) null, List::add));
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().toCollection(new ArrayList<>(), null));
    }
}
