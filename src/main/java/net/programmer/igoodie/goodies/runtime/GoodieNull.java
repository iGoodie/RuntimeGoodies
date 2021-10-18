package net.programmer.igoodie.goodies.runtime;

public class GoodieNull extends GoodieElement {

    @Override
    public GoodieElement deepCopy() {
        return new GoodieNull();
    }

    @Override
    public String toString() {
        return "Null";
    }

}
