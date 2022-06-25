package net.programmer.igoodie.goodies.configuration.transformation;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import org.jetbrains.annotations.NotNull;

@Deprecated
public abstract class GoodieTransformerLogic {

    public abstract @NotNull GoodieElement transform(GoodieElement goodieElement);

}
