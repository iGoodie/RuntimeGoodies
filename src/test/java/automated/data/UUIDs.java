package automated.data;

import automated.validators.DateCustomValidator;
import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieCustomValidator;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

import java.util.Date;
import java.util.UUID;

public class UUIDs extends JsonConfiGoodie {

    @Goodie
    @GoodieCustomValidator(DateCustomValidator.class)
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
