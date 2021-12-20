package net.programmer.igoodie.goodies.configuration.validation.logic;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieMap;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieMapLogic extends ValidatorLogic<GoodieMap> {

    @Override
    public void validateAnnotationArgs(GoodieMap annotation) throws GoodieImplementationException {
        // Should be always valid
    }

    @Override
    public void validateField(GoodieMap annotation, Object object, Field field) throws GoodieImplementationException {
        if (!TypeUtilities.isMap(field)) {
            throw new GoodieImplementationException("Field type MUST be a Map<?, ?>");
        }
    }

    @Override
    public void validateDefaultValue(GoodieMap annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {
        if (!TypeUtilities.isMap(defaultValue.getClass())) {
            throw new GoodieImplementationException("Default value MUST be a Map<?, ?>");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieMap annotation, @NotNull GoodieElement goodie) {
        return goodie.isObject();
    }

    @Override
    public boolean isValidValue(GoodieMap annotation, @NotNull GoodieElement goodie) {
        GoodieObject value = goodie.asObject();

        if (!annotation.allowNullValues()) {
            return value.values().stream().noneMatch(GoodieElement::isNull);
        }

        return true;
    }

    @Override
    public GoodieElement fixedGoodie(GoodieMap annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        GoodieObject value = goodie.asObject().deepCopy();

        if (!annotation.allowNullValues()) {
            value.values().removeIf(GoodieElement::isNull);
        }

        return value;
    }

}
