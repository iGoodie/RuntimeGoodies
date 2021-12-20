package net.programmer.igoodie.goodies.serialization.stringify;

import java.time.Instant;

public class InstantStringifier extends DataStringifier<Instant> {

    @Override
    public String stringify(Instant value) {
        return value.toString();
    }

    @Override
    public Instant objectify(String string) throws Exception {
        return Instant.parse(string);
    }

    @Override
    public Instant defaultObjectValue() {
        return Instant.now();
    }

}
