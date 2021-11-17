package net.programmer.igoodie.util;

import java.util.Locale;

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

    public static String shrink(String text, int left, int right) {
        return text.substring(left, text.length() - right);
    }

    public static String[] splitWords(String sentence) {
        return sentence.split("\\s+");
    }

    public static String upperFirstLetters(String phrase) {
        StringBuilder builder = new StringBuilder();

        for (String word : splitWords(phrase)) {
            if (word.isEmpty()) continue;
            char firstLetter = Character.toUpperCase(word.charAt(0));
            String rest = allLower(word.substring(1));
            builder.append(firstLetter).append(rest).append(' ');
        }

        return builder.toString().trim();
    }

    public static String allUpper(String phrase) {
        return phrase.toUpperCase(Locale.ENGLISH);
    }

    public static String allLower(String phrase) {
        return phrase.toLowerCase(Locale.ENGLISH);
    }

    public static String upperSnake(String phrase) {
        return allUpper(phrase.replaceAll(" ", "_"));
    }

}
