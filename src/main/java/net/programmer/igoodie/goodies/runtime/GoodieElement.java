package net.programmer.igoodie.goodies.runtime;

import net.programmer.igoodie.goodies.RuntimeGoodies;
import net.programmer.igoodie.goodies.query.GoodieQuery;
import net.programmer.igoodie.goodies.serialization.stringify.DataStringifier;
import net.programmer.igoodie.goodies.util.GoodieTraverser;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.goodies.util.TypeUtilities;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public abstract class GoodieElement {

    public static GoodieElement from(Object value) {
        if (value == null) {
            return GoodieNull.INSTANCE;
        } else if (value instanceof GoodieElement) {
            return ((GoodieElement) value).deepCopy();
        } else if (TypeUtilities.isPrimitive(value.getClass())) {
            return GoodiePrimitive.from(value);
        } else if (TypeUtilities.isArray(value.getClass())) {
            return fromArray(value);
        } else if (TypeUtilities.isList(value.getClass())) {
            return fromList(((List<?>) value));
        } else if (TypeUtilities.isMap(value.getClass())) {
            return fromMap(((Map<?, ?>) value));
        } else if (TypeUtilities.isEnum(value.getClass())) {
            return fromEnum((Enum<?>) value);
        } else {
            GoodieObject goodieObject = new GoodieObject();
            new GoodieTraverser().traverseGoodieFields(value, (object, field, goodiePath) -> {
                Object fieldValue = ReflectionUtilities.getValue(object, field);
                GoodieQuery.set(goodieObject, goodiePath, fromField(field, fieldValue));
            });
            return goodieObject;
        }
    }

    public static GoodieArray fromArray(Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        Class<?> arrayType = array.getClass().getComponentType();
        TypeUtilities.isPrimitive(arrayType);

        GoodieArray goodieArray = new GoodieArray();

        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object value = Array.get(array, i);
            goodieArray.add(from(value));
        }

        return goodieArray;
    }

    public static <T> GoodieArray fromList(List<T> list) {
        return list.stream()
                .map(GoodieElement::from)
                .collect(GoodieArray::new, GoodieArray::add, GoodieArray::addAll);
    }

    public static <K, V> GoodieObject fromMap(Map<K, V> map) {
        GoodieObject goodieObject = new GoodieObject();
        map.forEach((key, value) -> {
            @SuppressWarnings("unchecked")
            DataStringifier<K> dataStringifier = (DataStringifier<K>) RuntimeGoodies.DATA_STRINGIFIERS.get(key.getClass());
            goodieObject.put(dataStringifier == null ? key.toString() : dataStringifier.stringify(key), from(value));
        });
        return goodieObject;
    }

    public static <T extends Enum<T>> GoodiePrimitive fromEnum(Enum<T> enumeration) {
        return GoodiePrimitive.from(enumeration.name());
    }

    private static <T> GoodieElement fromField(Field field, T value) {
        if (value == null) return GoodieNull.INSTANCE;

        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) field.getClass();

        @SuppressWarnings("unchecked")
        DataStringifier<T> dataStringifier = (DataStringifier<T>) RuntimeGoodies.DATA_STRINGIFIERS.get(type);

        return dataStringifier != null
                ? GoodiePrimitive.from(dataStringifier.stringify(value))
                : from(value);
    }

    /* ------------------------------ */

    public abstract GoodieElement deepCopy();

    public abstract String toString();

    public final boolean isPrimitive() {
        return this instanceof GoodiePrimitive;
    }

    public final boolean isArray() {
        return this instanceof GoodieArray;
    }

    public final boolean isObject() {
        return this instanceof GoodieObject;
    }

    public final boolean isNull() {
        return this instanceof GoodieNull;
    }

    public final GoodiePrimitive asPrimitive() {
        return ((GoodiePrimitive) this);
    }

    public final GoodieArray asArray() {
        return ((GoodieArray) this);
    }

    public final GoodieObject asObject() {
        return ((GoodieObject) this);
    }

    public final GoodieNull asNull() {
        return ((GoodieNull) this);
    }

}
