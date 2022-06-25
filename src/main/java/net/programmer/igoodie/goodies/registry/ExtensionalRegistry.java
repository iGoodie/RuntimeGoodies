package net.programmer.igoodie.goodies.registry;

import java.util.*;
import java.util.function.Consumer;

public class ExtensionalRegistry<I, T extends Registrable<I>> extends Registry<I, T> {

    private final Registry<I, T> baseRegistry;

    public ExtensionalRegistry(Registry<I, T> baseRegistry) {
        this.baseRegistry = baseRegistry;
    }

    @SafeVarargs
    public ExtensionalRegistry(Registry<I, T> baseRegistry, T... initialItems) {
        this(baseRegistry);
        for (T initialItem : initialItems) {
            register(initialItem);
        }
    }

    @Override
    public T get(I identity) {
        T item = super.get(identity);
        if (item == null) return baseRegistry.get(identity);
        return item;
    }

    @Override
    public T getOrDefault(I identity, T defaultValue) {
        T item = super.get(identity);
        if (item == null) return baseRegistry.getOrDefault(identity, defaultValue);
        return item;
    }

    @Override
    public Set<I> getKeys() {
        Set<I> keys = new HashSet<>();
        keys.addAll(baseRegistry.getKeys());
        keys.addAll(super.getKeys());
        return keys;
    }

    @Override
    public Collection<T> getValues() {
        Collection<T> values = new LinkedList<>();
        values.addAll(baseRegistry.getValues());
        values.addAll(super.getValues());
        return values;
    }

    @Override
    public Set<Map.Entry<I, T>> getEntries() {
        Set<Map.Entry<I, T>> entries = new HashSet<>();
        entries.addAll(baseRegistry.getEntries());
        entries.addAll(super.getEntries());
        return entries;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        baseRegistry.forEach(consumer);
        super.forEach(consumer);
    }

}
