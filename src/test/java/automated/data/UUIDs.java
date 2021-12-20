package automated.data;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieCustomType;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import automated.validators.DateCustomValidator;

import java.util.Date;
import java.util.UUID;

public class UUIDs extends JsonConfiGoodie {

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
