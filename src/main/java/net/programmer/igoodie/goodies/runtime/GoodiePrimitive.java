package net.programmer.igoodie.goodies.runtime;

import net.programmer.igoodie.goodies.util.NumberUtilities;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.goodies.util.TypeUtilities;

import java.util.Objects;

public class GoodiePrimitive extends GoodieElement {

    public static GoodiePrimitive from(Object value) {
        Class<?> type = value.getClass();
        if (TypeUtilities.isNumeric(type))
            return new GoodiePrimitive(((Number) value));
        if (value instanceof String)
            return new GoodiePrimitive(((String) value));
        if (value instanceof Character)
            return new GoodiePrimitive(((Character) value));
        if (value instanceof Boolean)
            return new GoodiePrimitive(((Boolean) value));
        throw new IllegalArgumentException("Expected value to be primitive typed, instead found -> " + type);
    }

    private final Object value;

    public GoodiePrimitive(Boolean bool) {
        this.value = Objects.requireNonNull(bool);
    }

    public GoodiePrimitive(Number number) {
        this.value = NumberUtilities.fitToLargestDatatype(Objects.requireNonNull(number));
    }

    public GoodiePrimitive(String string) {
        this.value = Objects.requireNonNull(string);
    }

    public GoodiePrimitive(Character character) {
        this.value = Objects.requireNonNull(character);
    }

    /* -------------------------------- */

    public Class<?> getType() {
        return value.getClass();
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isCharacter() {
        return value instanceof Character;
    }

    /* -------------------------------- */

    public Object get() {
        return value;
    }

    public boolean getBoolean() {
        return (Boolean) value;
    }

    public Number getNumber() {
        return (Number) value;
    }

    public long getLong() {
        return getNumber().longValue();
    }

    public int getInteger() {
        return getNumber().intValue();
    }

    public short getShort() {
        return getNumber().shortValue();
    }

    public byte getByte() {
        return getNumber().byteValue();
    }

    public double getDouble() {
        return getNumber().doubleValue();
    }

    public float getFloat() {
        return getNumber().floatValue();
    }

    public String getString() {
        return value instanceof String ? (String) value : value.toString();
    }

    public char getCharacter() {
        return (Character) value;
    }

    /* -------------------------------- */

    @Override
    public GoodiePrimitive deepCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "g" + StringUtilities.sanitizeForPrint(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GoodiePrimitive that = (GoodiePrimitive) object;
        return Objects.equals(value, that.value);
    }

}
