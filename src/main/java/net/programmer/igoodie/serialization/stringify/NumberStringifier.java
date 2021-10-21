package net.programmer.igoodie.serialization.stringify;

public class NumberStringifier extends DataStringifier<Number> {

    @Override
    public String stringify(Number number) {
        return number.toString();
    }

    @Override
    public Number objectify(String string) {
        try {
            return parse(string);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    @Override
    public Number defaultObjectValue() {
        return 0.0D;
    }

    private static Number parse(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e1) {
                try {
                    return Integer.parseInt(str);
                } catch (NumberFormatException e2) {
                    return Long.parseLong(str);
                }
            }
        }
    }

    public static class DoubleStringifier extends DataStringifier<Double> {

        @Override
        public String stringify(Double number) {
            return String.valueOf(number);
        }

        @Override
        public Double objectify(String string) throws Exception {
            return Double.parseDouble(string);
        }

        @Override
        public Double defaultObjectValue() {
            return 0.0D;
        }

    }

    public static class FloatStringifier extends DataStringifier<Float> {

        @Override
        public String stringify(Float number) {
            return String.valueOf(number);
        }

        @Override
        public Float objectify(String string) throws Exception {
            return Float.parseFloat(string);
        }

        @Override
        public Float defaultObjectValue() {
            return 0.0F;
        }

    }

    public static class LongStringifier extends DataStringifier<Long> {

        @Override
        public String stringify(Long number) {
            return String.valueOf(number);
        }

        @Override
        public Long objectify(String string) throws Exception {
            return Long.parseLong(string);
        }

        @Override
        public Long defaultObjectValue() {
            return 0L;
        }

    }

    public static class IntegerStringifier extends DataStringifier<Integer> {

        @Override
        public String stringify(Integer number) {
            return String.valueOf(number);
        }

        @Override
        public Integer objectify(String string) throws Exception {
            return Integer.parseInt(string);
        }

        @Override
        public Integer defaultObjectValue() {
            return 0;
        }

    }

}
