import com.google.gson.JsonObject;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
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
    }

}
