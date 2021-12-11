package net.programmer.igoodie.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Registry<I, T extends Registrable<I>> {

    private final Map<I, T> registry = new HashMap<>();

    public Registry() {}

    public Registry(T... initialItems) {
        for (T initialItem : initialItems) {
            register(initialItem);
        }
    }

    public <E extends T> E register(E item) {
        @SuppressWarnings("unchecked")
        E registeredItem = (E) registry.put(item.getId(), item);
        return registeredItem;
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

    @Override
    public String toString() {
        return "Registry{" + registry + '}';
    }

}
