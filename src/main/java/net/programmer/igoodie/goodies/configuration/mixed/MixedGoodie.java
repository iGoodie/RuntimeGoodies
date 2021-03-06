package net.programmer.igoodie.goodies.configuration.mixed;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;

public interface MixedGoodie<S extends MixedGoodie<S>> {

    default S instantiateDeserializedType(GoodieObject goodieObject) {
        Class<? extends S> type = deserializeType(goodieObject);
        return GoodieUtils.createNullaryInstance(type);
    }

    @NotNull Class<? extends S> deserializeType(GoodieObject goodieObject);

    void serializeType(Class<?> mixedGoodieClass, GoodieObject goodieObject);

}
