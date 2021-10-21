import com.google.gson.JsonObject;
import data.User;
import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieObjectifier;
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
        GoodieValidator goodieValidator = new GoodieValidator();
        goodieValidator.validateAndFix(fillableObject, goodieObject);
        System.out.println("Is Fixed: " + goodieValidator.changesMade());
        System.out.println("Fixed Goodie: " + goodieObject);

        Assertions.assertEquals(0f, goodieObject.getFloat("nonExistingScore"));

        // Finally, fill the DTO
        GoodieObjectifier objectifier = new GoodieObjectifier();
        objectifier.fillFields(fillableObject, goodieObject);
        System.out.println("Object:\t" + fillableObject);

        Assertions.assertEquals(200f, fillableObject.nonExistingScore);
    }

}
