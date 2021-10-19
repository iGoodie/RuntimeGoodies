import com.google.gson.JsonObject;
import net.programmer.igoodie.exception.GoodieSyntaxException;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.query.GoodieQueryParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieQueryTests {

    @Test
    public void testParser() {
        Assertions.assertThrows(GoodieSyntaxException.class,
                () -> new GoodieQueryParser(""));
        Assertions.assertThrows(GoodieSyntaxException.class,
                () -> new GoodieQueryParser("$.in.between.$.root.expr").parse());

        String queryString = "$.arrayValue[0].val.x.y.z[2]";
        GoodieQueryParser parser = new GoodieQueryParser(queryString);

        GoodieQuery query = parser.parse();
        System.out.println(query.getAccessors());

        Assertions.assertEquals(7, query.getAccessors().size());
    }

    @Test
    public void testQueries() throws IOException {
        GoodieFormat<JsonObject, GoodieObject> gsonFormat = new GsonGoodieFormat();

        GoodieObject goodieObject = gsonFormat.readGoodieFromString(TestFiles.loadData("user.json"));
        System.out.println(goodieObject);

        System.out.println(GoodieQuery.query(goodieObject, "$.myAge"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames[0]"));
        System.out.println(GoodieQuery.query(goodieObject, "$.nonExisting"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames[99999]"));
        System.out.println(GoodieQuery.query(goodieObject, "$.friendNames[0].error"));
    }

    @Test
    public void testMutators() {
        GoodieObject goodieObject = new GoodieObject();

        GoodieQuery.set(goodieObject, "$.a", GoodiePrimitive.from(1));
        System.out.println("1 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.a.b", GoodiePrimitive.from(1));
        System.out.println("2 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.x.y.z", GoodiePrimitive.from(1));
        System.out.println("3 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[0]", GoodiePrimitive.from(1));
        System.out.println("4 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[0].val", GoodiePrimitive.from(1));
        System.out.println("5 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[2].val", GoodiePrimitive.from(1));
        System.out.println("6 -> " + goodieObject);

        GoodieQuery.set(goodieObject.getArray("arrayValue"), "$[0]", GoodiePrimitive.from(2));
        System.out.println(goodieObject);
        System.out.println("Query: " + GoodieQuery.query(goodieObject.getArray("arrayValue"), "$[0]"));

        GoodieQuery.set(goodieObject, "$.arrayValue[2]", GoodiePrimitive.from(1));
        System.out.println("7 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[3].x", GoodiePrimitive.from(1));
        System.out.println("8 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[3].y", GoodiePrimitive.from(1));
        System.out.println("9 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[0]", GoodiePrimitive.from(1));
        System.out.println("10 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[1].x", GoodiePrimitive.from(1));
        System.out.println("11 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[1].override[0]", GoodiePrimitive.from(1));
        System.out.println("12 -> " + goodieObject);

        GoodieQuery.set(goodieObject, "$.arrayValue[4].nestedArray[1].override", GoodiePrimitive.from(1));
        System.out.println("13 -> " + goodieObject);
    }

}
