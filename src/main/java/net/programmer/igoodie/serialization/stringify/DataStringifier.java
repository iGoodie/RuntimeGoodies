package net.programmer.igoodie.serialization.stringify;

import net.programmer.igoodie.registry.Registrable;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.Type;

public abstract class DataStringifier<T> implements Registrable<Class<?>> {

    public abstract String stringify(T value);

    public abstract T objectify(String string);

    @Override
    public Class<?> getId() {
        Type[] genericTypes = TypeUtilities.getSuperGenericTypes(this);
        if (genericTypes == null) throw new InternalError();
        return ((Class<?>) genericTypes[0]);
    }

}
