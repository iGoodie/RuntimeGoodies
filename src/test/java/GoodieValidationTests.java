import com.google.gson.JsonObject;
import data.User;
import net.programmer.igoodie.configuration.validator.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.junit.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieValidationTests {

    @Test
    public void testValidator() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> gsonFormat = new GsonGoodieFormat();

        GoodieObject goodieObject = gsonFormat.readGoodieFromString(TestFiles.loadData("user.json"));
        User user = new User();

        GoodieValidator goodieValidator = new GoodieValidator();
        goodieValidator.validateAndFix(user, goodieObject);
    }

}
