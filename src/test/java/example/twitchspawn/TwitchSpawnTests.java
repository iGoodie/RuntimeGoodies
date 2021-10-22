package example.twitchspawn;

import example.twitchspawn.configs.goodies.Streamer;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class TwitchSpawnTests {

    @Test
    public void testGoodies() throws IOException {
        GsonGoodieFormat goodieFormat = new GsonGoodieFormat();

        Streamer streamer = new Streamer().readConfig(TestFiles.loadData("streamer.json"), fixedGoodie -> {
            System.out.println("Fixed as Goodie: " + fixedGoodie);
            System.out.println("Fixed as JSON  : " + goodieFormat.readFromGoodie(fixedGoodie));
        });

        new GoodieTraverser().traverseGoodies(streamer, (object, field, goodiePath) -> {
            try {
                System.out.println(field.getName() + " = " + field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
