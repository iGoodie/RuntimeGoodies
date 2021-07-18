package net.programmer.igoodie;

import net.programmer.igoodie.registry.Registry;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.serialization.stringify.NumberStringifier;
import net.programmer.igoodie.serialization.stringify.UUIDStringifier;

public class RuntimeGoodies {

    public static Registry<Class<?>, DataStringifier<?>> DATA_STRINGIFIERS = new Registry<>(
            new UUIDStringifier(),
            new NumberStringifier(),
            new NumberStringifier.DoubleStringifier(),
            new NumberStringifier.FloatStringifier(),
            new NumberStringifier.LongStringifier(),
            new NumberStringifier.IntegerStringifier()
    );

}
