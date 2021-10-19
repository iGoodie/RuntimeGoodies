package net.programmer.igoodie.util;

import java.util.stream.Stream;

public class ArrayAccessor<T> {

    private T[] array;

    public boolean outOfBounds(int index) {
        return array == null || index < 0 || index >= array.length;
    }

    public T get(int index) {
        return getOrDefault(index, null);
    }

    public T getOrDefault(int index, T defaultValue) {
        return outOfBounds(index) ? defaultValue : array[index];
    }

    public boolean set(int index, T value) {
        if (outOfBounds(index))
            return false;
        array[index] = value;
        return true;
    }

    /* ----------------------------------- */

    public static <T> ArrayAccessor<T> of(T... array) {
        ArrayAccessor<T> accessor = new ArrayAccessor<>();
        accessor.array = array;
        return accessor;
    }

}
