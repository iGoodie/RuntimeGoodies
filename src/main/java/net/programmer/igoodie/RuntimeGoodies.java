package net.programmer.igoodie;

import net.programmer.igoodie.configuration.validation.logic.*;
import net.programmer.igoodie.registry.Registry;
import net.programmer.igoodie.serialization.goodiefy.FieldGoodiefier;
import net.programmer.igoodie.serialization.goodiefy.PojoGoodifier;
import net.programmer.igoodie.serialization.goodiefy.PrimitiveGoodiefier;
import net.programmer.igoodie.serialization.stringify.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RuntimeGoodies {

    public static final List<FieldGoodiefier<?>> FIELD_GOODIEFIERS = Collections.unmodifiableList(Arrays.asList(
            new PrimitiveGoodiefier(),
            new PojoGoodifier()
    ));

    public static Registry<Class<?>, DataStringifier<?>> DATA_STRINGIFIERS = new Registry<>(
            new DateStringifier(),
            new InstantStringifier(),
            new UUIDStringifier(),
            new RandomStringifier()
    );

    public static Registry<Class<?>, ValidatorLogic<? extends Annotation>> VALIDATORS = new Registry<>(
            new GoodieCustomTypeLogic(),
            new GoodieIntegerLogic(),
            new GoodieLongLogic(),
            new GoodieFloatLogic(),
            new GoodieDoubleLogic(),
            new GoodieStringLogic(),
            new GoodieListLogic()
    );

}
