package net.programmer.igoodie.goodies;

import net.programmer.igoodie.goodies.configuration.validation.logic.*;
import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.goodies.serialization.goodiefy.*;
import net.programmer.igoodie.goodies.serialization.stringify.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RuntimeGoodies {

    public static final List<DataGoodiefier<?>> DATA_GOODIEFIERS = Collections.unmodifiableList(Arrays.asList(
            new GoodieElementGoodiefier(),
            new EnumGoodiefier(),
            new StringifiablesGoodifier(),
            new PrimitiveGoodiefier(),
            new ListGoodiefier(),
            new MapGoodiefier(),
            new PojoGoodiefier()
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
            new GoodieListLogic(),
            new GoodieMapLogic(),
            new GoodieEnumLogic()
    );

}
