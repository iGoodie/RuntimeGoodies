package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.registry.Registrable;
import net.programmer.igoodie.util.ArrayAccessor;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Supplier;

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

    public abstract void validateField(A annotation, Object object, Field field) throws GoodieImplementationException;

    public abstract boolean isValidGoodie(A annotation, @NotNull GoodieElement goodie);

    public abstract boolean isValidValue(A annotation, @NotNull GoodieElement goodie);

    public abstract GoodieElement fixedGoodie(A annotation, Object object, Field field, @NotNull GoodieElement goodie);

    protected Object getDefaultValue(Object object, Field field) {
        return getDefaultValueOr(object, field, () -> null);
    }

    protected <T> T getDefaultValueOr(Object object, Field field, Supplier<T> supplier) {
        @SuppressWarnings("unchecked")
        T value = (T) ReflectionUtilities.getValue(object, field);
        return value == null ? supplier.get() : value;
    }

}
