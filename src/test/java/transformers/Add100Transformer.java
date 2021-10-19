package transformers;

import net.programmer.igoodie.configuration.transformation.GoodieTransformerLogic;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import org.jetbrains.annotations.NotNull;

public class Add100Transformer extends GoodieTransformerLogic {

    @Override
    public @NotNull GoodieElement transform(GoodieElement goodieElement) {
        if (goodieElement.isPrimitive()) {
            GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();
            if (goodiePrimitive.isNumber()) {
                return GoodiePrimitive.from(goodiePrimitive.getDouble() + 100);
            }
        }

        return goodieElement;
    }

}
