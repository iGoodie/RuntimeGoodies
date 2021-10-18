import com.google.gson.JsonObject;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.query.GoodieQuery;
import org.junit.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieQueryTests {

    @Test
    public void testQueries() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> gsonFormat = new GsonGoodieFormat();

        GoodieObject goodieObject = gsonFormat.readGoodieFromString(TestFiles.loadData("user.json"));

        System.out.println(GoodieQuery.query(goodieObject, "$.myAge"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames[0]"));
        System.out.println(GoodieQuery.query(goodieObject, "$.nonExisting"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames[99999]"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames[0].error"));
    }

    @Test
    public void testMutators() throws IOException {
        GoodieObject goodieObject = new GoodieObject();

        GoodieQuery.set(goodieObject, "$.a", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        // TODO: Fix
//        GoodieQuery.set(goodieObject, "$.a.b", GoodiePrimitive.from(1));
//        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.x.y.z", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[0]", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[2]", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[3].x", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[3].y", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[0]", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[1].x", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[1].override[0]", GoodiePrimitive.from(1));
        System.out.println(goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[1].override", GoodiePrimitive.from(1));
        System.out.println(goodieObject);
    }

}
