package net.programmer.igoodie.query.accessor;

import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

import java.util.regex.Pattern;

public class ArrayIndexAccessor extends GoodieQueryAccessor {

    public static final Pattern PATTERN = Pattern.compile("(\\w+)\\[(\\d+)]");

    private final String arrayName;
    private final int index;

    public ArrayIndexAccessor(String listName, int index) {
        this.arrayName = listName;
        this.index = index;
    }

    @Override
    public GoodieElement access(GoodieElement goodieElement) {
        if (!goodieElement.isObject()) throw new IllegalArgumentException();

        GoodieObject goodieObject = goodieElement.asObject();

        GoodieArray goodieArray = goodieObject.getArray(arrayName);
        return goodieArray.get(index);
    }

}
