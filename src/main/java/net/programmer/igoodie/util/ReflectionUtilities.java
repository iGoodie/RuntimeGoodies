package net.programmer.igoodie.util;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public final class ReflectionUtilities {

    private ReflectionUtilities() {}

    public static void setValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new InternalException("Access error while setting value -> " + field);
        } catch (IllegalArgumentException e) {
            if (isNumericType(field.getType()) && isNumericType(value.getClass()))
                setNumericValue(object, field, (Number) value);
            else throw e;
        }
    }

    public static void setNumericValue(Object object, Field field, Number number) {
        Class<?> type = field.getType();
        if (type == Long.class || type == long.class)
            setValue(object, field, number.longValue());
        else if (type == Integer.class || type == int.class)
            setValue(object, field, number.intValue());
        else if (type == Short.class || type == short.class)
            setValue(object, field, number.shortValue());
        else if (type == Byte.class || type == byte.class)
            setValue(object, field, number.byteValue());
        else if (type == Double.class || type == double.class)
            setValue(object, field, number.doubleValue());
        else if (type == Float.class || type == float.class)
            setValue(object, field, number.floatValue());
        else throw new IllegalArgumentException("Field type expected to be numeric, instead found -> " + type);
    }

    public static Object getValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new InternalException("Access error while getting value -> " + field);
        }
    }

    public static Constructor<?> findDefaultConstructor(Class<?> type) {
        for (Constructor<?> constructor : type.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    public static boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || isUnboxedNumeric(type);
    }

    private static boolean isUnboxedNumeric(Class<?> type) {
        return type == long.class
                || type == int.class
                || type == short.class
                || type == byte.class
                || type == double.class
                || type == float.class;
    }

    public static boolean isIntegral(Class<?> type) {
        return assignableOrPrimitive(type, Long.class, long.class)
                || assignableOrPrimitive(type, Integer.class, int.class)
                || assignableOrPrimitive(type, Short.class, short.class)
                || assignableOrPrimitive(type, Byte.class, byte.class);
    }

    public static boolean isPrimitiveType(Class<?> type) {
        return isNumericType(type)
                || assignableOrPrimitive(type, Character.class, char.class)
                || assignableOrPrimitive(type, Boolean.class, boolean.class)
                || String.class.isAssignableFrom(type);
    }

    private static boolean assignableOrPrimitive(Class<?> type, Class<?> boxType, Class<?> primitiveType) {
        return boxType.isAssignableFrom(type) || type == primitiveType;
    }

}
