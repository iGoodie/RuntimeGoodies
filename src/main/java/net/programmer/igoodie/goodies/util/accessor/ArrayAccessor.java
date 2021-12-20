package net.programmer.igoodie.goodies.util.accessor;

public class ArrayAccessor<T> extends IndexAccessor<T> {

    private T[] array;

    public boolean outOfBounds(int index) {
        return array == null || index < 0 || index >= array.length;
    }

    @Override
    protected T unsafeGet(int index) {
        return array[index];
    }

    @Override
    protected void unsafeSet(int index, T value) {
        array[index] = value;
    }

    /* ----------------------------------- */

    @SafeVarargs
    public static <T> ArrayAccessor<T> of(T... array) {
        ArrayAccessor<T> accessor = new ArrayAccessor<>();
        accessor.array = array;
        return accessor;
    }

}
