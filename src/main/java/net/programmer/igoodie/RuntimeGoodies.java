package net.programmer.igoodie;

import net.programmer.igoodie.configuration.validation.logic.*;
import net.programmer.igoodie.registry.Registry;
import net.programmer.igoodie.serialization.stringify.*;

import java.lang.annotation.Annotation;

public class RuntimeGoodies {

    public static Registry<Class<?>, DataStringifier<?>> DATA_STRINGIFIERS = new Registry<>(
            new DateStringifier(),
            new InstantStringifier(),
            new UUIDStringifier()
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
