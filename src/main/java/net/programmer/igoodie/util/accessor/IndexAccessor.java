package net.programmer.igoodie.util.accessor;

public abstract class IndexAccessor<T> {

    public abstract boolean outOfBounds(int index);

    protected abstract T unsafeGet(int index);

    protected abstract void unsafeSet(int index, T value);

    public T get(int index) {
        return getOrDefault(index, null);
    }

    public T getOrDefault(int index, T defaultValue) {
        return outOfBounds(index) ? defaultValue : unsafeGet(index);
    }

    public boolean set(int index, T value) {
        if (outOfBounds(index))
            return false;
        unsafeSet(index, value);
        return true;
    }

}
