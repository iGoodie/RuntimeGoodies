package automated;

import com.google.gson.JsonObject;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class GoodieFormatTests {

    public static class Streamer {
        @Goodie
        String nickname;
    }

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

    @Test
    public void testFrom() {
        ArrayList<Object> elements = new ArrayList<>();
        elements.add(1);
        elements.add(2);
        elements.add("3");
        elements.add(new Object());
        elements.add(new LinkedList<>());

        HashMap<Object, Object> map = new HashMap<>();
        map.put(new UUID(0, 0), "Adam");
        map.put("list", elements);
        map.put("array", new int[]{1, 2, 3});
        map.put("streamer", new Streamer());

        System.out.println(GoodieElement.fromMap(map));
    }

}
