package net.programmer.igoodie.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TypeUtilities {

    private static final Set<Class<? extends Number>> INTEGRAL_WRAPPER_TYPES = new HashSet<>();
    private static final Set<Class<? extends Number>> NUMERIC_WRAPPER_TYPES = new HashSet<>();
    private static final Set<Class<?>> NON_NUMERIC_WRAPPER_TYPES = new HashSet<>();
    private static final Set<Class<?>> INTEGRAL_PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> NUMERIC_PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> NON_NUMERIC_PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> PRIMITIVE_TYPES = new HashSet<>();

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

    public static boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || isPrimitiveNumeric(type);
    }

    private static boolean isPrimitiveNumeric(Class<?> type) {
        return NUMERIC_PRIMITIVE_TYPES.contains(type);
    }

    public static boolean isIntegral(Class<?> type) {
        return INTEGRAL_PRIMITIVE_TYPES.contains(type)
                || INTEGRAL_WRAPPER_TYPES.stream().anyMatch(wrapperType -> wrapperType.isAssignableFrom(type));
    }

    public static boolean isPrimitiveType(Class<?> type) {
        return isNumericType(type)
                || NON_NUMERIC_PRIMITIVE_TYPES.contains(type)
                || NON_NUMERIC_WRAPPER_TYPES.stream().anyMatch(wrapperType -> wrapperType.isAssignableFrom(type));
    }

    public static boolean isPrimitive(Field field) {
        Class<?> type = field.getType();
        return TypeUtilities.isPrimitiveType(type);
    }

    public static boolean isList(Field field) {
        Class<?> type = field.getType();
        return List.class.isAssignableFrom(type);
    }

}
