package net.programmer.igoodie.goodies.util.accessor;

import java.util.Optional;

public abstract class IndexAccessor<T> {

    public abstract boolean outOfBounds(int index);

    protected abstract T unsafeGet(int index);

    protected abstract void unsafeSet(int index, T value);

    public Optional<T> get(int index) {
        if (outOfBounds(index))
            return Optional.empty();
        return Optional.ofNullable(unsafeGet(index));
    }

    public boolean set(int index, T value) {
        if (outOfBounds(index))
            return false;
        unsafeSet(index, value);
        return true;
    }

}
