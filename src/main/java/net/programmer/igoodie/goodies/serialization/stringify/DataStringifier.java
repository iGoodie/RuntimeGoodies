package net.programmer.igoodie.goodies.serialization.stringify;

import net.programmer.igoodie.goodies.registry.Registrable;
import net.programmer.igoodie.goodies.util.TypeUtilities;

import java.lang.reflect.Type;

public abstract class DataStringifier<T> implements Registrable<Class<?>> {

    @Override
    public Class<?> getId() {
        Type[] genericTypes = TypeUtilities.getSuperGenericTypes(this);
        if (genericTypes == null) throw new InternalError();
        return ((Class<?>) genericTypes[0]);
    }

    public abstract String stringify(T value);

    public abstract T objectify(String string) throws Exception;

    public boolean canObjectify(String string) {
        try {
            objectify(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public abstract T defaultObjectValue();

    public String defaultStringValue() {
        return stringify(defaultObjectValue());
    }

}
