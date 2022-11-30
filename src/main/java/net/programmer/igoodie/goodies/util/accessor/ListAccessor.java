package net.programmer.igoodie.goodies.util.accessor;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

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

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object o) {
        return false;
    }

    public List<T> subList(int from, int to) {
        if (list.size() == 0) return Collections.emptyList();
        int fromIndex = Math.max(from, 0);
        int toIndex = Math.min(to, list.size());
        return Collections.unmodifiableList(list.subList(fromIndex, toIndex));
    }

    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public Stream<T> parallelStream() {
        return list.parallelStream();
    }

    @NotNull
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    @NotNull
    public Object @NotNull [] toArray() {
        return list.toArray();
    }

    @NotNull
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        return list.toArray(a);
    }

    public boolean add(T t) {
        return list.add(t);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    public boolean addAll(@NotNull Collection<? extends T> c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    public boolean removeIf(Predicate<? super T> filter) {
        return list.removeIf(filter);
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    public void replaceAll(UnaryOperator<T> operator) {
        list.replaceAll(operator);
    }

    public void sort(Comparator<? super T> c) {
        list.sort(c);
    }

    public void clear() {
        list.clear();
    }

    public void add(int index, T element) {
        list.add(index, element);
    }

    public T remove(int index) {
        return list.remove(index);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @NotNull
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
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
