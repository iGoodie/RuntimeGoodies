package automated;

import automated.data.User;
import com.google.gson.JsonObject;
import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieObjectifierTests {

    @Test
    public void testFilling() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> format = new GsonGoodieFormat();

        JsonObject jsonObject = format.readFromString(TestFiles.loadData("user.json"));
        System.out.println("JSON:\t" + jsonObject);

        GoodieObject goodieObject = format.writeToGoodie(jsonObject);
        System.out.println("Goodie:\t" + goodieObject);

        User configGoodie = new User();

        GoodieValidator goodieValidator = new GoodieValidator(configGoodie, goodieObject);
        goodieValidator.validateAndFixFields();

        configGoodie.deserialize(goodieObject);
        System.out.println("Object:\t" + configGoodie);
    }

}
