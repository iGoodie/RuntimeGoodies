package net.programmer.igoodie.goodies.runtime;

public class GoodieNull extends GoodieElement {

    public static final GoodieNull INSTANCE = new GoodieNull();

    private GoodieNull() {}

    @Override
    public GoodieNull deepCopy() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "gNull";
    }

}
