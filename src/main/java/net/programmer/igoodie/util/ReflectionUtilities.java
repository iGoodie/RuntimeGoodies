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
            if (TypeUtilities.isNumeric(field.getType()) && TypeUtilities.isNumeric(value.getClass()))
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

    public static <T> T newInstanceOrDefault(Class<T> type, T defaultValue) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
