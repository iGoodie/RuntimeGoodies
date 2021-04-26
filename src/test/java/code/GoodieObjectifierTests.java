package code;

import com.google.gson.JsonObject;
import data.FillableObject;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.serialization.GoodieObjectifier;
import org.junit.Test;

public class GoodieObjectifierTests {

    @Test
    public void testFilling() {
        GoodieFormat<JsonObject, GoodieObject> format = new GsonGoodieFormat();

        JsonObject jsonObject = format.readFromString("{\"firstName\":\"Seda\",\"lastName\":\"Sen\",\"myAge\":21,\"friendNames\":[\"Anilcan\",\"Yasin\",\"Zahir\"]}");
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

        FillableObject fillableObject = new FillableObject();
//
        GoodieObjectifier objectifier = new GoodieObjectifier();
        objectifier.fillFields(fillableObject, goodieObject);
        System.out.println("Object:\t" + fillableObject);
    }

}
