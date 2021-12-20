package net.programmer.igoodie.goodies.configuration.validation.annotation;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.util.TypeUtilities;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodieCustomType {

    Class<? extends Validator<?>> value();

    abstract class Validator<T> {

        @SuppressWarnings("unchecked")
        public Class<T> getFieldType() {
            Type[] genericTypes = TypeUtilities.getSuperGenericTypes(this);
            if (genericTypes == null) throw new InternalError();
            Type type = ArrayAccessor.of(genericTypes).get(0);
            if (!(type instanceof Class)) throw new InternalError();
            return (Class<T>) type;
        }

        public abstract boolean isValidGoodie(GoodieElement goodie);

        public abstract boolean isValidValue(GoodieElement goodie);

        public abstract GoodieElement fixedGoodie(Object object, Field field, GoodieElement goodie);

    }

}
