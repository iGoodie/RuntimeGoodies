package net.programmer.igoodie.goodies.util.accessor;

import net.programmer.igoodie.goodies.util.StringUtilities;

public class StringCharAccessor extends IndexAccessor<Character> {

    private String string;

    @Override
    public boolean outOfBounds(int index) {
        return string == null || index < 0 || index >= string.length();
    }

    @Override
    protected Character unsafeGet(int index) {
        return string.charAt(index);
    }

    @Override
    protected void unsafeSet(int index, Character value) {
        this.string = StringUtilities.replaceIndices(string, index, index, String.valueOf(value));
    }

    public String getString() {
        return string;
    }

    /* ----------------------------------- */

    public static StringCharAccessor of(String string) {
        StringCharAccessor accessor = new StringCharAccessor();
        accessor.string = string;
        return accessor;
    }

}
