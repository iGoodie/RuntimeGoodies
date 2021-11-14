package automated;

import com.google.gson.JsonObject;
import automated.data.UUIDs;
import automated.data.User;
import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.legacy.GoodieValidator;
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
        GoodieValidator goodieValidator = new GoodieValidator();
        goodieValidator.validateAndFix(user, goodieObject);
        System.out.println("After Fix:  " + goodieObject);
    }

    @Test
    public void testDataStringifierFixer() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> gsonFormat = new GsonGoodieFormat();

        GoodieObject goodieObject = gsonFormat.readGoodieFromString(TestFiles.loadData("uuids.json"));
        System.out.println("Goodie Object: " + goodieObject);

        ConfiGoodieOptions options = new ConfiGoodieOptions()
                .useText(TestFiles.loadData("uuids.json"))
                .onFixed(fixedGoodie -> {
                    System.out.println("Fixed Goodie: " + fixedGoodie);
                });

        UUIDs uuids = new UUIDs().readConfig(options);

        System.out.println(uuids);
    }

}
