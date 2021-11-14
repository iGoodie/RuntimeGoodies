package net.programmer.igoodie.util;

import net.programmer.igoodie.goodies.runtime.GoodieElement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public final class TypeUtilities {

    private static final Set<Class<? extends Number>> INTEGRAL_WRAPPER_TYPES = new HashSet<>();
    private static final Set<Class<? extends Number>> NUMERIC_WRAPPER_TYPES = new HashSet<>();
    private static final Set<Class<?>> NON_NUMERIC_WRAPPER_TYPES = new HashSet<>();
    private static final Set<Class<?>> INTEGRAL_PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> NUMERIC_PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> NON_NUMERIC_PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> PRIMITIVE_TYPES = new HashSet<>();

    private static final Map<Class<?>, Object> DEFAULTS;

    static {
        Map<Class<?>, Object> map = new HashMap<>();
        put(map, boolean.class, false);
        put(map, char.class, '\0');
        put(map, String.class, "");
        put(map, byte.class, (byte) 0);
        put(map, Byte.class, (byte) 0);
        put(map, short.class, (short) 0);
        put(map, Short.class, (short) 0);
        put(map, int.class, 0);
        put(map, Integer.class, 0);
        put(map, long.class, 0L);
        put(map, Long.class, 0L);
        put(map, float.class, 0f);
        put(map, Float.class, 0f);
        put(map, double.class, 0d);
        put(map, Double.class, 0d);
        put(map, Number.class, 0d);
        DEFAULTS = Collections.unmodifiableMap(map);
    }

    private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
        map.put(type, value);
    }

    static {
        INTEGRAL_WRAPPER_TYPES.add(Long.class);
        INTEGRAL_WRAPPER_TYPES.add(Integer.class);
        INTEGRAL_WRAPPER_TYPES.add(Short.class);
        INTEGRAL_WRAPPER_TYPES.add(Byte.class);
    }

    static {
        NUMERIC_WRAPPER_TYPES.addAll(INTEGRAL_WRAPPER_TYPES);
        NUMERIC_WRAPPER_TYPES.add(Double.class);
        NUMERIC_WRAPPER_TYPES.add(Float.class);
    }

    static {
        INTEGRAL_PRIMITIVE_TYPES.add(long.class);
        INTEGRAL_PRIMITIVE_TYPES.add(int.class);
        INTEGRAL_PRIMITIVE_TYPES.add(short.class);
        INTEGRAL_PRIMITIVE_TYPES.add(byte.class);
    }

    static {
        NUMERIC_PRIMITIVE_TYPES.addAll(INTEGRAL_PRIMITIVE_TYPES);
        NUMERIC_PRIMITIVE_TYPES.add(double.class);
        NUMERIC_PRIMITIVE_TYPES.add(float.class);
    }

    static {
        NON_NUMERIC_WRAPPER_TYPES.add(String.class);
        NON_NUMERIC_WRAPPER_TYPES.add(Character.class);
        NON_NUMERIC_WRAPPER_TYPES.add(Boolean.class);
    }

    static {
        NON_NUMERIC_PRIMITIVE_TYPES.add(char.class);
        NON_NUMERIC_PRIMITIVE_TYPES.add(boolean.class);
    }

    static {
        PRIMITIVE_TYPES.addAll(NUMERIC_WRAPPER_TYPES);
        PRIMITIVE_TYPES.addAll(NUMERIC_PRIMITIVE_TYPES);
        PRIMITIVE_TYPES.addAll(NON_NUMERIC_WRAPPER_TYPES);
        PRIMITIVE_TYPES.addAll(NON_NUMERIC_PRIMITIVE_TYPES);
    }

    private TypeUtilities() {}

    public static boolean isNumeric(Class<?> type) {
        return Number.class.isAssignableFrom(type) || isPrimitiveNumeric(type);
    }

    public static boolean isNumeric(Field field) {
        return TypeUtilities.isNumeric(field.getType());
    }

    private static boolean isPrimitiveNumeric(Class<?> type) {
        return NUMERIC_PRIMITIVE_TYPES.contains(type);
    }

    public static boolean isPrimitiveNumeric(Field field) {
        return isPrimitiveNumeric(field.getType());
    }

    public static boolean isIntegral(Class<?> type) {
        return INTEGRAL_PRIMITIVE_TYPES.contains(type)
                || INTEGRAL_WRAPPER_TYPES.stream().anyMatch(wrapperType -> wrapperType.isAssignableFrom(type));
    }

    public static boolean isIntegral(Field field) {
        return isIntegral(field.getType());
    }

    public static boolean isPrimitive(Class<?> type) {
        return isNumeric(type)
                || NON_NUMERIC_PRIMITIVE_TYPES.contains(type)
                || NON_NUMERIC_WRAPPER_TYPES.stream().anyMatch(wrapperType -> wrapperType.isAssignableFrom(type));
    }

    public static boolean isPrimitive(Field field) {
        return isPrimitive(field.getType());
    }

    public static boolean isList(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    public static boolean isList(Field field) {
        return isList(field.getType());
    }

    public static boolean isArray(Class<?> type) {
        return type.isArray();
    }

    public static boolean isArray(Field field) {
        return isArray(field.getType());
    }

    public static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static boolean isMap(Field field) {
        return isMap(field.getType());
    }

    public static boolean isEnum(Class<?> type) {
        return type.isEnum();
    }

    public static boolean isEnum(Field field) {
        return isEnum(field.getType());
    }

    public static boolean isGoodie(Class<?> type) {
        return GoodieElement.class.isAssignableFrom(type);
    }

    public static boolean isGoodie(Field field) {
        return isGoodie(field.getType());
    }

    /* -------------------------- */

    public static Type[] getSuperGenericTypes(Class<?> type) {
        Type genericSuperclass = type.getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            return parameterizedType.getActualTypeArguments();
        }

        return null;
    }

    public static Type[] getSuperGenericTypes(Object object) {
        return getSuperGenericTypes(object.getClass());
    }

    public static Type[] getGenericTypes(Field field) {
        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments();
        }

        return null;
    }

    public static <T> T defaultValue(Class<T> type) {
        @SuppressWarnings("unchecked")
        T t = (T) DEFAULTS.get(type);
        return t;
    }

}
