package net.programmer.igoodie.goodies.util;

public class NumberUtilities {

    public static Number fitToSmallestDatatype(Number number) {
        if (hasDecimalPoints(number)) {
            double value = number.doubleValue();
            if (Float.MIN_NORMAL <= value && value <= Float.MAX_VALUE) {
                return number.floatValue();
            } else {
                return number.doubleValue();
            }

        } else {
            long value = number.longValue();
            if (Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE) {
                return number.byteValue();
            } else if (Short.MIN_VALUE <= value && value <= Short.MAX_VALUE) {
                return number.shortValue();
            } else if (Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
                return number.intValue();
            } else {
                return number.longValue();
            }
        }
    }

    public static Number fitToLargestDatatype(Number number) {
        if (hasDecimalPoints(number)) {
            return number.doubleValue();
        } else {
            return number.longValue();
        }
    }

    public static boolean hasDecimalPoints(Number number) {
        return number.doubleValue() % 1 != 0;
    }

}
