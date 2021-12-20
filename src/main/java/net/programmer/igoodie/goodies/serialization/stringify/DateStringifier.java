package net.programmer.igoodie.goodies.serialization.stringify;

import java.time.Instant;
import java.util.Date;

public class DateStringifier extends DataStringifier<Date> {

    @Override
    public String stringify(Date value) {
        return value.toInstant().toString();
    }

    @Override
    public Date objectify(String string) throws Exception {
        return Date.from(Instant.parse(string));
    }

    @Override
    public Date defaultObjectValue() {
        return new Date();
    }

}
