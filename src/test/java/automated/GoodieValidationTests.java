package automated;

import automated.data.UUIDs;
import automated.data.User;
import com.google.gson.JsonObject;
import net.programmer.igoodie.goodies.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieValidationTests {

    @Test
    public void testValidator() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> gsonFormat = new GsonGoodieFormat();

        GoodieObject goodieObject = gsonFormat.readGoodieFromString(TestFiles.loadData("user.json"));
        User user = new User();

        System.out.println("Before Fix: " + goodieObject);
        GoodieValidator goodieValidator = new GoodieValidator(user, goodieObject);
        goodieValidator.validateAndFixFields();
        System.out.println("After Fix:  " + goodieObject);
    }

    @Test
    public void testDataStringifierFixer() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> gsonFormat = new GsonGoodieFormat();

        GoodieObject goodieObject = gsonFormat.readGoodieFromString(TestFiles.loadData("uuids.json"));
        System.out.println("Goodie Object: " + goodieObject);

        ConfiGoodieOptions options = ConfiGoodieOptions
                .fromText(TestFiles.loadData("uuids.json"))
                .onFixed((opts, fixedGoodie, confiGoodie) -> {
                    System.out.println("Fixed Goodie: " + fixedGoodie);
                });

        UUIDs uuids = new UUIDs().readConfig(options);

        System.out.println(uuids);
    }

}
