package net.programmer.igoodie.goodies.runtime;

import java.util.*;

public class GoodieObject extends GoodieElement implements Map<String, GoodieElement> {

    private final Map<String, GoodieElement> map = new HashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Map<String, GoodieElement> get() {
        return map;
    }

    public boolean has(String key) {
        return get(key) != null;
    }

    @Override
    public GoodieElement get(Object key) {
        return map.get(key);
    }

    public boolean hasPrimitive(String key) {
        if (!has(key)) return false;
        GoodieElement goodieElement = get(key);
        return goodieElement.isPrimitive();
    }

    public boolean hasArray(String key) {
        if (!has(key)) return false;
        GoodieElement goodieElement = get(key);
        return goodieElement.isArray();
    }

    public boolean hasObject(String key) {
        if (!has(key)) return false;
        GoodieElement goodieElement = get(key);
        return goodieElement.isObject();
    }

    public boolean hasBoolean(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).map(GoodiePrimitive::isBoolean).orElse(false);
    }

    public boolean hasNumber(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).map(GoodiePrimitive::isNumber).orElse(false);
    }

    public boolean hasString(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).map(GoodiePrimitive::isString).orElse(false);
    }

    public boolean hasCharacter(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).map(GoodiePrimitive::isCharacter).orElse(false);
    }

    public Optional<GoodiePrimitive> getPrimitive(String key) {
        return hasPrimitive(key)
                ? Optional.of(get(key).asPrimitive())
                : Optional.empty();
    }

    public Optional<GoodieArray> getArray(String key) {
        return hasArray(key)
                ? Optional.of(get(key).asArray())
                : Optional.empty();
    }

    public Optional<GoodieObject> getObject(String key) {
        return hasObject(key)
                ? Optional.of(get(key).asObject())
                : Optional.empty();
    }

    public Optional<Boolean> getBoolean(String key) {
        return hasBoolean(key)
                ? getPrimitive(key).map(GoodiePrimitive::getBoolean)
                : Optional.empty();
    }

    public Optional<Byte> getByte(String key) {
        return getNumber(key).map(Number::byteValue);
    }

    public Optional<Short> getShort(String key) {
        return getNumber(key).map(Number::shortValue);
    }

    public Optional<Integer> getInteger(String key) {
        return getNumber(key).map(Number::intValue);
    }

    public Optional<Long> getLong(String key) {
        return getNumber(key).map(Number::longValue);
    }

    public Optional<Float> getFloat(String key) {
        return getNumber(key).map(Number::floatValue);
    }

    public Optional<Double> getDouble(String key) {
        return getNumber(key).map(Number::doubleValue);
    }

    public Optional<Number> getNumber(String key) {
        return hasNumber(key)
                ? getPrimitive(key).map(GoodiePrimitive::getNumber)
                : Optional.empty();
    }

    public Optional<String> getString(String key) {
        return hasString(key)
                ? getPrimitive(key).map(GoodiePrimitive::getString)
                : Optional.empty();
    }

    public Optional<Character> getCharacter(String key) {
        return hasNumber(key)
                ? getPrimitive(key).map(GoodiePrimitive::getCharacter)
                : Optional.empty();
    }

    @Override
    public GoodieElement put(String key, GoodieElement value) {
        return map.put(key, value);
    }

    public GoodieElement put(String key, Boolean bool) {
        return map.put(key, new GoodiePrimitive(bool));
    }

    public GoodieElement put(String key, Number number) {
        return map.put(key, new GoodiePrimitive(number));
    }

    public GoodieElement put(String key, String string) {
        return map.put(key, new GoodiePrimitive(string));
    }

    public GoodieElement put(String key, Character character) {
        return map.put(key, new GoodiePrimitive(character));
    }

    @Override
    public GoodieElement remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends GoodieElement> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<GoodieElement> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, GoodieElement>> entrySet() {
        return map.entrySet();
    }

    @Override
    public GoodieObject deepCopy() {
        GoodieObject goodieObject = new GoodieObject();
        map.forEach((key, value) -> {
            goodieObject.put(key, value.deepCopy());
        });
        return goodieObject;
    }

    @Override
    public String toString() {
        return "g" + map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodieObject that = (GoodieObject) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

}
