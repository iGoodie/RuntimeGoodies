package net.programmer.igoodie.query.accessor;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class ObjectFieldAccessor extends GoodieQueryAccessor {

    private final String fieldName;

    public ObjectFieldAccessor(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public GoodieElement access(GoodieElement goodieElement) {
        if (!goodieElement.isObject()) throw new IllegalArgumentException();

        GoodieObject goodieObject = goodieElement.asObject();

        return goodieObject.get(fieldName);
    }

}
