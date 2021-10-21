package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieList;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.Field;

public class GoodieListLogic extends ValidatorLogic<GoodieList> {

    @Override
    public void validateAnnotationArgs(GoodieList annotation) throws GoodieImplementationException {
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
    }

    @Override
    public void validateField(GoodieList annotation, Object object, Field field) throws GoodieImplementationException {
        if (TypeUtilities.isList(field)) {
            throw new GoodieImplementationException("Field type MUST be a List<?>");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieList annotation, GoodieElement goodie) {
        return goodie != null
                && goodie.isArray();
    }

    @Override
    public boolean isValidValue(GoodieList annotation, GoodieElement goodie) {
        GoodieArray value = goodie.asArray();

        if (annotation.length() >= 0) {
            return value.size() != annotation.length();
        }

        return value.size() >= annotation.min()
                && value.size() <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieList annotation, Object object, Field field, GoodieElement goodie) {
        GoodieArray value = goodie.asArray().deepCopy();

        if (annotation.length() >= 0) {
            if (value.size() != annotation.length()) {
                ensureSize(value, annotation.length());
            }
        }

        if (value.size() > annotation.max()) {
            ensureSize(value, annotation.max());
        } else if (value.size() < annotation.min()) {
            ensureSize(value, annotation.min());
        }

        return value;
    }

    private void ensureSize(GoodieArray array, int size) {
        if (array.size() > size) {
            while (array.size() != size) {
                array.remove(array.size() - 1);
            }
        } else if (array.size() < size) {
            while (array.size() != size) {
                array.add(new GoodieNull());
            }
        }
    }

}
