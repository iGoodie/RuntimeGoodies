package net.programmer.igoodie.query.accessor;

import net.programmer.igoodie.exception.GoodieQueryError;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class ArrayIndexAccessor extends GoodieQueryAccessor {

    int index;

    public ArrayIndexAccessor(int index) {
        this.index = index;
    }

    @Override
    public boolean canAccess(GoodieElement goodieElement) {
        return goodieElement.isArray();
    }

    @Override
    public GoodieElement makeAccessible(GoodieElement parent, GoodieQueryAccessor parentAccessor, GoodieElement goodieElement) {
        if (canAccess(goodieElement)) return goodieElement;

        GoodieArray newCurrent = new GoodieArray();
        fillIndices(newCurrent);
        GoodieNull next = new GoodieNull();
        newCurrent.set(index, next);

        if (parent instanceof GoodieObject) {
            GoodieObject parentObject = parent.asObject();
            parentObject.put(((ObjectFieldAccessor) parentAccessor).fieldName, newCurrent);
        }

        if (parent instanceof GoodieArray) {
            GoodieArray parentArray = parent.asArray();
            ((ArrayIndexAccessor) parentAccessor).fillIndices(parentArray);
            parentArray.set(((ArrayIndexAccessor) parentAccessor).index, newCurrent);
        }

        return newCurrent;
    }

    @Override
    public GoodieElement access(GoodieElement goodieElement) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot access indices of non-array elements");
        }

        return goodieElement.asArray().get(index);
    }

    @Override
    public GoodieElement accessOrCreate(GoodieElement parent, GoodieQueryAccessor parentAccessor, GoodieElement goodieElement) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot access indices of non-array elements");
        }

        GoodieArray currentArray = goodieElement.asArray();

        if (index >= currentArray.size()) {
            fillIndices(currentArray);
        }

        return currentArray.get(index);
    }

    @Override
    public void setValue(GoodieElement goodieElement, GoodieElement value) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot set an index of non-array elements");
        }

        goodieElement.asArray().set(index, value);
    }

    public void fillIndices(GoodieArray array) {
        int fillCount = index - array.size() + 1;
        if (fillCount <= 0) return;
        for (int i = 0; i < fillCount; i++) {
            array.add(new GoodieNull());
        }
    }

    @Override
    public void delete(GoodieElement goodieElement) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot access indices of non-array elements");
        }

        goodieElement.asArray().remove(index);
    }

    @Override
    public String toString() {
        return "[" + index + "]";
    }

}
