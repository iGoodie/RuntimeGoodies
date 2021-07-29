import com.google.gson.JsonObject;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.junit.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieFormatTests {

    @Test
    public void testGSONFormat() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> format = new GsonGoodieFormat();

        JsonObject jsonObject = format.readFromString(TestFiles.loadData("user.json"));
        System.out.println("JSON:\t" + jsonObject);

        GoodieObject goodieObject = format.writeToGoodie(jsonObject);
        System.out.println("Goodie:\t" + goodieObject);

        System.out.println(jsonObject.get("myAge"));
        System.out.println(goodieObject.get("myAge").asPrimitive().getType());
    }

}
