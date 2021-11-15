package net.programmer.igoodie.util;

public class StringUtilities {

    public static String toString(Object value) {
        if (value == null) {
            return "null";
        } else {
            return value.toString();
        }
    }

    public static String sanitizeForPrint(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Character) {
            return "'" + value + "'";
        } else if (value instanceof Number) {
            return value + numericAppendix(value.getClass());
        } else {
            return value.toString();
        }
    }

    public static String numericAppendix(Class<?> type) {
        if (Long.class.isAssignableFrom(type)) return "l";
        if (Integer.class.isAssignableFrom(type)) return "i";
        if (Short.class.isAssignableFrom(type)) return "s";
        if (Byte.class.isAssignableFrom(type)) return "b";
        if (Float.class.isAssignableFrom(type)) return "f";
        if (Double.class.isAssignableFrom(type)) return "d";
        return "number";
    }

}
