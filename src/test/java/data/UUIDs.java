package data;

import net.programmer.igoodie.configuration.ConfiGoodieJson;
import net.programmer.igoodie.configuration.validation.annotation.GoodieCustomType;
import net.programmer.igoodie.serialization.annotation.Goodie;
import validators.DateCustomValidator;

import java.util.Date;
import java.util.UUID;

public class UUIDs extends ConfiGoodieJson {

    @Goodie
    @GoodieCustomType(DateCustomValidator.class)
    private Date date;

    @Goodie
    private UUID validUUID;

    @Goodie
    private UUID invalidUUID;

    @Override
    public String toString() {
        return "UUIDs{" +
                "date=" + date +
                ", validUUID=" + validUUID +
                ", invalidUUID=" + invalidUUID +
                '}';
    }

}
