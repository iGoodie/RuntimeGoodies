package example.twitchspawn;

import example.twitchspawn.configs.goodies.Streamer;
import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieObjectifier;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class TwitchSpawnTests {

    @Test
    public void testGoodies() throws IOException {
        GsonGoodieFormat goodieFormat = new GsonGoodieFormat();
        GoodieValidator goodieValidator = new GoodieValidator();
        GoodieObjectifier goodieObjectifier = new GoodieObjectifier();

        Streamer streamer = new Streamer();
        GoodieObject goodieObject = goodieFormat.readGoodieFromString(TestFiles.loadData("streamer.json"));
        System.out.println("Read: " + goodieObject);

        goodieValidator.validateAndFix(streamer, goodieObject);
        System.out.println("Fixed: " + goodieObject);
        System.out.println("Fixed: " + goodieFormat.readFromGoodie(goodieObject));

        goodieObjectifier.fillFields(streamer, goodieObject);
        new GoodieTraverser().traverseGoodies(streamer, (object, field, goodiePath) -> {
            try {
                System.out.println(goodiePath + " = " + field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
