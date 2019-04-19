package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.ArrayEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-07-19.
 */
@SuppressWarnings("Duplicates")
public final class Array<T> implements IList<T>, Cloneable {
    private final Object[] elements;

    private Array(Object[] elements) {
        this.elements = elements;
    }

    //region static

    public static <T> Array<T> create(Object[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        return new Array<>(ArrayUtils.clone(array));
    }

    //endregion

    @Override
    public IEnumerator<T> enumerator() {
        return new ArrayEnumerator<>(this.elements);
    }

    @Override
    public T get(int index) {
        //noinspection unchecked
        return (T) this.elements[index];
    }

    public void set(int index, T item) {
        this.elements[index] = item;
    }

    @Override
    public Collection<T> getCollection() {
        return ArrayUtils.toCollection(this.elements);
    }

    @Override
    public int _getCount() {
        return this.elements.length;
    }

    @Override
    public boolean _contains(T value) {
        return ArrayUtils.contains(this.elements, value);
    }

    @Override
    public void _copyTo(Object[] array, int arrayIndex) {
        System.arraycopy(this.elements, 0, array, arrayIndex, this.elements.length);
    }

    @Override
    public T[] _toArray(Class<T> clazz) {
        return ArrayUtils.toArray(this.elements, clazz);
    }

    @Override
    public Object[] _toArray() {
        return this.elements;
    }

    @Override
    public List<T> _toList() {
        return ArrayUtils.toList(this.elements);
    }

    @Override
    public Array<T> clone() {
        return new Array<>(ArrayUtils.clone(this.elements));
    }
}
