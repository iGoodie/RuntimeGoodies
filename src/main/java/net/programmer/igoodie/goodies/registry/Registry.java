package net.programmer.igoodie.goodies.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Registry<I, T extends Registrable<I>> {

    private final Map<I, T> registry = new HashMap<>();

    public Registry() {}

    @SafeVarargs
    public Registry(T... initialItems) {
        this();
        for (T initialItem : initialItems) {
            register(initialItem);
        }
    }

    public <E extends T> E register(E item) {
        registry.put(item.getId(), item);
        return item;
    }

    public T get(I identity) {
        return registry.get(identity);
    }

    public T getOrDefault(I identity, T defaultValue) {
        return registry.getOrDefault(identity, defaultValue);
    }

    public Set<I> getKeys() {
        return registry.keySet();
    }

    public Collection<T> getValues() {
        return registry.values();
    }

    public Set<Map.Entry<I, T>> getEntries() {
        return registry.entrySet();
    }

    public void forEach(Consumer<T> consumer) {
        registry.values().forEach(consumer);
    }

    @Override
    public String toString() {
        return "Registry{" + registry + '}';
    }

}
