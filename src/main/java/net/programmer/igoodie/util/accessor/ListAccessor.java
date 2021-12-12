package net.programmer.igoodie.util.accessor;

import java.util.Arrays;
import java.util.List;

public class ListAccessor<T> extends IndexAccessor<T> {

    private List<T> list;

    public boolean outOfBounds(int index) {
        return list == null || index < 0 || index >= list.size();
    }

    @Override
    protected T unsafeGet(int index) {
        return list.get(index);
    }

    @Override
    protected void unsafeSet(int index, T value) {
        list.set(index, value);
    }

    /* ----------------------------------- */

    @SafeVarargs
    public static <T> ListAccessor<T> of(T... array) {
        return of(Arrays.asList(array));
    }

    public static <T> ListAccessor<T> of(List<T> list) {
        ListAccessor<T> accessor = new ListAccessor<>();
        accessor.list = list;
        return accessor;
    }

}
