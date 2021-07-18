package net.programmer.igoodie.serialization.stringify;

import java.util.UUID;

public class UUIDStringifier extends DataStringifier<UUID> {

    @Override
    public String stringify(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID objectify(String string) {
        return UUID.fromString(string);
    }

}
