package data;

import net.programmer.igoodie.configuration.ConfiGoodieJson;
import net.programmer.igoodie.serialization.annotation.Goodie;

import java.util.UUID;

public class UUIDs extends ConfiGoodieJson {

    @Goodie
    private UUID validUUID;

    @Goodie
    private UUID invalidUUID;

    @Override
    public String toString() {
        return "UUIDs{" +
                "validUUID=" + validUUID +
                ", invalidUUID=" + invalidUUID +
                '}';
    }

}
