package net.programmer.igoodie.configuration.validator.logic;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.registry.Registrable;
import net.programmer.igoodie.util.ArrayAccessor;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public abstract class ValidatorLogic<A extends Annotation> implements Registrable<Class<?>> {

    @Override
    @SuppressWarnings("unchecked")
    public Class<A> getId() {
        Type[] genericTypes = TypeUtilities.getSuperGenericTypes(this);
        if (genericTypes == null) throw new InternalError();
        Type type = ArrayAccessor.of(genericTypes).get(0);
        if (!(type instanceof Class)) throw new InternalError();
        return (Class<A>) type;
    }

    public abstract boolean isValidField(Object object, Field field);

    public abstract boolean isValidGoodie(Object object, Field field, GoodieElement goodie);

    public abstract boolean isValid(A annotation, Object object, Field field, GoodieElement goodie);

    public abstract Object defaultValue(A annotation, Object object, Field field, GoodieElement goodie);

}
