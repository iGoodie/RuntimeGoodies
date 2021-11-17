package net.programmer.igoodie.goodies.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public GoodiePrimitive getPrimitive(String key) {
        GoodieElement goodieElement = get(key);
        if (goodieElement instanceof GoodiePrimitive)
            return ((GoodiePrimitive) goodieElement);
        else throw new IllegalArgumentException();
    }

    public boolean hasArray(String key) {
        if (!has(key)) return false;
        GoodieElement goodieElement = get(key);
        return goodieElement.isArray();
    }

    public GoodieArray getArray(String key) {
        GoodieElement goodieElement = get(key);
        if (goodieElement instanceof GoodieArray)
            return ((GoodieArray) goodieElement);
        else throw new IllegalArgumentException();
    }

    public boolean hasObject(String key) {
        if (!has(key)) return false;
        GoodieElement goodieElement = get(key);
        return goodieElement.isObject();
    }

    public GoodieObject getObject(String key) {
        GoodieElement goodieElement = get(key);
        if (goodieElement instanceof GoodieObject)
            return ((GoodieObject) goodieElement);
        else throw new IllegalArgumentException();
    }

    public boolean hasBoolean(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).isBoolean();
    }

    public boolean getBoolean(String key) {
        return getPrimitive(key).getBoolean();
    }

    public boolean hasNumber(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).isNumber();
    }

    public byte getByte(String key) {
        return getPrimitive(key).getByte();
    }

    public short getShort(String key) {
        return getPrimitive(key).getShort();
    }

    public int getInteger(String key) {
        return getPrimitive(key).getInteger();
    }

    public long getLong(String key) {
        return getPrimitive(key).getLong();
    }

    public float getFloat(String key) {
        return getPrimitive(key).getFloat();
    }

    public double getDouble(String key) {
        return getPrimitive(key).getDouble();
    }

    public Number getNumber(String key) {
        return getPrimitive(key).getNumber();
    }

    public boolean hasString(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).isString();
    }

    public String getString(String key) {
        return getPrimitive(key).getString();
    }

    public boolean hasCharacter(String key) {
        if (!hasPrimitive(key)) return false;
        return getPrimitive(key).isCharacter();
    }

    public char getCharacter(String key) {
        return getPrimitive(key).getCharacter();
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
            map.put(key, value.deepCopy());
        });
        return goodieObject;
    }

    @Override
    public String toString() {
        return "g" + map.toString();
    }

}
