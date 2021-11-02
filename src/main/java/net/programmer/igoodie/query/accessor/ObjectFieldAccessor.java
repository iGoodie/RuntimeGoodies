package net.programmer.igoodie.query.accessor;

import net.programmer.igoodie.exception.GoodieQueryError;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class ObjectFieldAccessor extends GoodieQueryAccessor {

    String fieldName;

    public ObjectFieldAccessor(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean canAccess(GoodieElement goodieElement) {
        return goodieElement.isObject();
    }

    @Override
    public GoodieElement makeAccessible(GoodieElement parent, GoodieQueryAccessor parentAccessor, GoodieElement goodieElement) {
        if (canAccess(goodieElement)) return goodieElement;

        GoodieObject newCurrent = new GoodieObject();
        GoodieNull next = new GoodieNull();
        newCurrent.put(fieldName, next);

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
            throw new GoodieQueryError("Cannot access a field of non-object elements");
        }

        return goodieElement.asObject().get(fieldName);
    }

    @Override
    public GoodieElement accessOrCreate(GoodieElement parent, GoodieQueryAccessor parentAccessor, GoodieElement goodieElement) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot access a field of non-object elements");
        }

        GoodieObject currentObject = goodieElement.asObject();

        if (!currentObject.containsKey(fieldName)) {
            currentObject.put(fieldName, new GoodieNull());
        }

        return currentObject.get(fieldName);
    }

    @Override
    public void setValue(GoodieElement goodieElement, GoodieElement value) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot set a field of non-object elements");
        }

        goodieElement.asObject().put(fieldName, value);
    }

    @Override
    public void delete(GoodieElement goodieElement) {
        if (!canAccess(goodieElement)) {
            throw new GoodieQueryError("Cannot delete a field of non-object elements");
        }

        goodieElement.asObject().remove(fieldName);
    }

    @Override
    public String toString() {
        return "." + fieldName;
    }

}
