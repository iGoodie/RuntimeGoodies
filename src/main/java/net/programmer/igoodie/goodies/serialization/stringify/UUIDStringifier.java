package net.programmer.igoodie.goodies.serialization.stringify;

import java.util.UUID;

public class UUIDStringifier extends DataStringifier<UUID> {

    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public String stringify(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID objectify(String string) throws Exception {
        return UUID.fromString(string.contains("-") ? string
                : string.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5")
        );
    }

    @Override
    public UUID defaultObjectValue() {
        return NIL_UUID;
    }

}
