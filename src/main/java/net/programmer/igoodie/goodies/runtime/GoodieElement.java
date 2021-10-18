package net.programmer.igoodie.goodies.runtime;

public abstract class GoodieElement {

    public abstract GoodieElement deepCopy();

    public abstract String toString();

    public final boolean isPrimitive() {
        return this instanceof GoodiePrimitive;
    }

    public final boolean isArray() {
        return this instanceof GoodieArray;
    }

    public final boolean isObject() {
        return this instanceof GoodieObject;
    }

    public final boolean isNull() {
        return this instanceof GoodieNull;
    }

    public final GoodiePrimitive asPrimitive() {
        return ((GoodiePrimitive) this);
    }

    public final GoodieArray asArray() {
        return ((GoodieArray) this);
    }

    public final GoodieObject asObject() {
        return ((GoodieObject) this);
    }

    public final GoodieNull asNull() {
        return ((GoodieNull) this);
    }

}
