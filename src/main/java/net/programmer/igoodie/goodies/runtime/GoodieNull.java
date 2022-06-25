package net.programmer.igoodie.goodies.runtime;

import java.util.Objects;

public class GoodieNull extends GoodieElement {

    public static final GoodieNull INSTANCE = new GoodieNull();

    private GoodieNull() {} // Disallow instantiation

    @Override
    public GoodieNull deepCopy() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "gNull";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(o, INSTANCE);
    }

}
