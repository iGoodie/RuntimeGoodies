package net.programmer.igoodie.goodies.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

// TODO
public class KeyNumberGoodiefier extends DataGoodiefier<GoodiePrimitive> {

    public static final KeyNumberGoodiefier INSTANCE = new KeyNumberGoodiefier();

    private KeyNumberGoodiefier() {}

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        return false;
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        return false;
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        return false;
    }

    @Override
    public GoodiePrimitive auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asPrimitive();
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodiePrimitive goodie) {
        return null;
    }

    @Override
    public @NotNull GoodiePrimitive generateDefaultGoodie(Type targetType) {
        return null;
    }

    @Override
    public @NotNull GoodiePrimitive serializeValueToGoodie(Object value) {
        return null;
    }

    /* --------------------------------------- */

    // TODO: Convert to number method(s)

}
