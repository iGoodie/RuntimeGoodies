package net.programmer.igoodie.goodies.serialization.stringify.key;

import net.programmer.igoodie.goodies.serialization.stringify.DataStringifier;
import net.programmer.igoodie.goodies.util.TypeUtilities;

import java.util.function.Function;

public class NumberStringifier<N extends Number> extends DataStringifier<N> {

    private final Class<N> type;
    private final Function<String, N> objectifier;

    public NumberStringifier(Class<N> type, Function<String, N> objectifier) {
        this.type = type;
        this.objectifier = objectifier;
    }

    @Override
    public Class<?> getId() {
        return type;
    }

    @Override
    public String stringify(N value) {
        return value.toString();
    }

    @Override
    public N objectify(String string) throws Exception {
        return objectifier.apply(string);
    }

    @Override
    public N defaultObjectValue() {
        return TypeUtilities.defaultValue(type);
    }

}
