package net.programmer.igoodie.registry;

import java.util.HashMap;
import java.util.Map;

public class Registry<I, T extends Registrable<I>> {

    private Map<I, T> registry = new HashMap<>();

    public Registry() {}

    public Registry(T... initialItems) {
        for (T initialItem : initialItems) {
            register(initialItem);
        }
    }

    public T register(T item) {
        return registry.put(item.getId(), item);
    }

    public T get(I identity) {
        return registry.get(identity);
    }

    public T getOrDefault(I identity, T defaultValue) {
        return registry.getOrDefault(identity, defaultValue);
    }

}
