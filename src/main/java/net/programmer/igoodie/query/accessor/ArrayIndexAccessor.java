package net.programmer.igoodie.query.accessor;

import net.programmer.igoodie.goodies.runtime.*;

import java.util.regex.Pattern;

public class ArrayIndexAccessor extends GoodieQueryAccessor {

    public static final Pattern PATTERN = Pattern.compile("(\\w+)\\[(\\d+)]");

    private final String arrayName;
    private final int index;

    public String getArrayName() {
        return arrayName;
    }

    public int getIndex() {
        return index;
    }

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

    @Override
    public GoodieElement accessOrCreate(GoodieElement goodieElement) {
        if (!goodieElement.isObject()) throw new IllegalArgumentException();

        GoodieObject goodieObject = goodieElement.asObject();

        if (goodieObject.containsKey(arrayName)) {
            GoodieArray goodieArray = goodieObject.get(arrayName).asArray();
            fillUndefinedIndices(goodieArray);
            return goodieArray.get(index);
        }

        GoodieArray createdArray = new GoodieArray();

        fillUndefinedIndices(createdArray);

        goodieObject.put(arrayName, createdArray);
        return createdArray.get(index);
    }

    @Override
    public void setValue(GoodieElement goodieElement, GoodieElement value) {
        if (!goodieElement.isObject()) throw new IllegalArgumentException();

        accessOrCreate(goodieElement);

        GoodieArray array = goodieElement.asObject().getArray(arrayName);
        fillUndefinedIndices(array);
        array.set(index, value);
    }

    private void fillUndefinedIndices(GoodieArray array) {
        if (index >= array.size()) {
            for (int i = array.size(); i <= index; i++) {
                array.add(new GoodieObject());
            }
        }
    }

}
