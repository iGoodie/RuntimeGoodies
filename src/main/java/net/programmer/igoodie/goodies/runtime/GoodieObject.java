package net.programmer.igoodie.goodies.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GoodieObject extends GoodieElement implements Map<String, GoodieElement> {

    private final Map<String, GoodieElement> elements = new HashMap<>();

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return elements.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return elements.containsValue(value);
    }

    @Override
    public GoodieElement get(Object key) {
        return elements.get(key);
    }

    @Override
    public GoodieElement put(String key, GoodieElement value) {
        return elements.put(key, value);
    }

    public GoodieElement put(String key, Boolean bool) {
        return elements.put(key, new GoodiePrimitive(bool));
    }

    public GoodieElement put(String key, Number number) {
        return elements.put(key, new GoodiePrimitive(number));
    }

    public GoodieElement put(String key, String string) {
        return elements.put(key, new GoodiePrimitive(string));
    }

    public GoodieElement put(String key, Character character) {
        return elements.put(key, new GoodiePrimitive(character));
    }

    @Override
    public GoodieElement remove(Object key) {
        return elements.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends GoodieElement> m) {
        elements.putAll(m);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public Set<String> keySet() {
        return elements.keySet();
    }

    @Override
    public Collection<GoodieElement> values() {
        return elements.values();
    }

    @Override
    public Set<Entry<String, GoodieElement>> entrySet() {
        return elements.entrySet();
    }

    @Override
    public GoodieElement deepCopy() {
        GoodieObject goodieObject = new GoodieObject();
        elements.forEach((key, value) -> {
            elements.put(key, value.deepCopy());
        });
        return goodieObject;
    }

    @Override
    public String toString() {
        return elements.toString();
    }

}
