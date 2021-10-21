package net.programmer.igoodie.serialization.stringify;

import java.time.Instant;
import java.util.Date;

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
