package net.programmer.igoodie.configuration.validator.logic;

import net.programmer.igoodie.exception.GoodieImplementationException;
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

    public abstract void validateAnnotationArgs(A annotation) throws GoodieImplementationException;

    public abstract boolean isValidField(Object object, Field field);

    public abstract boolean isValidGoodie(GoodieElement goodie);

    public abstract boolean isValidValue(A annotation, GoodieElement goodie);

    public abstract GoodieElement defaultGoodie(A annotation, Object object, Field field, GoodieElement goodie);

}
