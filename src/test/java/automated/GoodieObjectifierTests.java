package automated;

import com.google.gson.JsonObject;
import automated.data.User;
import net.programmer.igoodie.legacy.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.legacy.GoodieObjectifier;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieObjectifierTests {

    @Test
    public void testFilling() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> format = new GsonGoodieFormat();
        GoodieValidator goodieValidator = new GoodieValidator();

        JsonObject jsonObject = format.readFromString(TestFiles.loadData("user.json"));
        System.out.println("JSON:\t" + jsonObject);

        GoodieObject goodieObject = format.writeToGoodie(jsonObject);
        System.out.println("Goodie:\t" + goodieObject);

        User fillableObject = new User();

        goodieValidator.validateAndFix(fillableObject, goodieObject);

        GoodieObjectifier objectifier = new GoodieObjectifier();
        objectifier.fillFields(fillableObject, goodieObject);
        System.out.println("Object:\t" + fillableObject);
    }

}
