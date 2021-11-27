package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieList;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieListLogic extends ValidatorLogic<GoodieList> {

    @Override
    public void validateAnnotationArgs(GoodieList annotation) throws GoodieImplementationException {
        // Should be always valid
    }

    @Override
    public void validateField(GoodieList annotation, Object object, Field field) throws GoodieImplementationException {
        if (!TypeUtilities.isList(field)) {
            throw new GoodieImplementationException("Field type MUST be a List<?>");
        }
    }

    @Override
    public void validateDefaultValue(GoodieList annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {
        if (!TypeUtilities.isList(defaultValue.getClass())) {
            throw new GoodieImplementationException("Default value MUST be a List<?>");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieList annotation, @NotNull GoodieElement goodie) {
        return goodie.isArray();
    }

    @Override
    public boolean isValidValue(GoodieList annotation, @NotNull GoodieElement goodie) {
        GoodieArray value = goodie.asArray();

        if (!annotation.allowNullElements()) {
            return value.stream().noneMatch(GoodieElement::isNull);
        }

        return true;
    }

    @Override
    public GoodieElement fixedGoodie(GoodieList annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        GoodieArray value = goodie.asArray().deepCopy();

        if (!annotation.allowNullElements()) {
            value.removeIf(GoodieElement::isNull);
        }

        return value;
    }

}
