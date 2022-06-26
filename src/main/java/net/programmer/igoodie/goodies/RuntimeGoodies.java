package net.programmer.igoodie.goodies;

import net.programmer.igoodie.goodies.configuration.validation.logic.*;
import net.programmer.igoodie.goodies.registry.ExtensionalRegistry;
import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.goodies.serialization.goodiefy.*;
import net.programmer.igoodie.goodies.serialization.stringify.*;
import net.programmer.igoodie.goodies.serialization.stringify.key.NumberStringifier;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    /**
     * Registry that holds <strong>Data Stringifiers</strong> acknowledged by the internal Goodie De/Serializers.
     * <br><br>
     * Data Stringifiers define a way to transform a runtime entity into String, and vice versa
     * in order to be able to store them as <strong>GoodiePrimitives</strong>
     */
    public static Registry<Class<?>, DataStringifier<?>> DATA_STRINGIFIERS = new Registry<>(
            new DateStringifier(),
            new InstantStringifier(),
            new UUIDStringifier(),
            new RandomStringifier()
    );

    /**
     * Registry that holds <strong>Data Stringifiers</strong> that are used to De/Serialize keys in Map-like data
     */
    public static Registry<Class<?>, DataStringifier<?>> KEY_STRINGIFIERS = new ExtensionalRegistry<>(DATA_STRINGIFIERS,
            new NumberStringifier<>(Byte.class, Byte::parseByte),
            new NumberStringifier<>(Short.class, Short::parseShort),
            new NumberStringifier<>(Integer.class, Integer::parseInt),
            new NumberStringifier<>(Long.class, Long::parseLong),
            new NumberStringifier<>(Float.class, Float::parseFloat),
            new NumberStringifier<>(Double.class, Double::parseDouble)
    );

    /**
     * Registry that holds Validators acknowledged by the internal Goodie De/Serializers.
     */
    public static Registry<Class<?>, ValidatorLogic<? extends Annotation>> VALIDATORS = new Registry<>(
            new GoodieCustomTypeLogic(),
            new GoodieIntegerLogic(),
            new GoodieLongLogic(),
            new GoodieFloatLogic(),
            new GoodieDoubleLogic(),
            new GoodieStringLogic(),
            new GoodieListLogic(),
            new GoodieMapLogic()
    );

    public static Set<Class<?>> getAcceptedTypes() {
        return DATA_STRINGIFIERS.getKeys();
    }

    public static Set<Class<?>> getAcceptedKeyTypes() {
        return KEY_STRINGIFIERS.getKeys();
    }

}
