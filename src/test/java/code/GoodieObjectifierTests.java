package code;

import com.google.gson.JsonObject;
import data.User;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieObjectifier;
import org.junit.Test;
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

//        GoodieObject goodieObject = new GoodieObject();
//        goodieObject.put("firstName", "Seda");
//        goodieObject.put("lastName", "Sen");
//        goodieObject.put("myAge", 24l);
//        GoodieArray goodieArray = new GoodieArray();
//        goodieArray.add(new GoodiePrimitive("Anilcan"));
//        goodieArray.add(new GoodiePrimitive("Yasin"));
//        goodieArray.add(new GoodiePrimitive("Zahir"));
//        goodieObject.put("friendNames", goodieArray);

        User fillableObject = new User();
//
        GoodieObjectifier objectifier = new GoodieObjectifier();
        objectifier.fillFields(fillableObject, goodieObject);
        System.out.println("Object:\t" + fillableObject);
    }

}
