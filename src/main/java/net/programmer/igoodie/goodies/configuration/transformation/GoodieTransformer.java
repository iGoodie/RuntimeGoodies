package net.programmer.igoodie.goodies.configuration.transformation;

import java.lang.annotation.*;

@Repeatable(GoodieTransformer.List.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodieTransformer {

    Class<? extends GoodieTransformerLogic> value();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface List {
        GoodieTransformer[] value();
    }

}
