package automated;

import automated.data.User;
import com.google.gson.JsonObject;
import net.programmer.igoodie.goodies.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieTransformerTests {

    @Test
    public void testTransformer() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> format = new GsonGoodieFormat();

        // Read JSON -> Goodie
        GoodieObject goodieObject = format.readGoodieFromString(TestFiles.loadData("user.json"));
        System.out.println("Goodie:\t" + goodieObject);

        Assertions.assertFalse(goodieObject.containsKey("nonExistingScore"));

        // Create DTO
        User fillableObject = new User();

        // Validate and fix the Goodie
        GoodieValidator goodieValidator = new GoodieValidator(fillableObject, goodieObject);
        goodieValidator.validateAndFixFields();
        System.out.println("Is Fixed: " + goodieValidator.changesMade());
        System.out.println("Fixed Goodie: " + goodieObject);

        Assertions.assertTrue(goodieObject.getFloat("nonExistingScore").isPresent());
        Assertions.assertEquals(0f, goodieObject.getFloat("nonExistingScore").get());

        fillableObject.deserialize(goodieObject);
        Assertions.assertEquals(200f, fillableObject.nonExistingScore);
    }

}
