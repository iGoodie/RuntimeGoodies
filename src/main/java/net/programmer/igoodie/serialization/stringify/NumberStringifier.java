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

}
