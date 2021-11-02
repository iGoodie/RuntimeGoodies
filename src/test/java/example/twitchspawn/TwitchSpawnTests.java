package example.twitchspawn;

import example.twitchspawn.configs.CredentialsConfig;
import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class TwitchSpawnTests {

    @Test
    public void testGoodies() throws IOException {
        GsonGoodieFormat goodieFormat = new GsonGoodieFormat();

        ConfiGoodieOptions options = new ConfiGoodieOptions()
                .useText(TestFiles.loadData("credentials_config.json"))
                .onFixed(fixedGoodie -> {
                    System.out.println("Fixed as Goodie: " + fixedGoodie);
                    System.out.println("Fixed as JSON  : " + goodieFormat.writeToString(fixedGoodie, true));
                });

        CredentialsConfig credentialsConfig = new CredentialsConfig().readConfig(options);

        new GoodieTraverser().traverseGoodieFields(credentialsConfig, (object, field, goodiePath) -> {
            try {
                field.setAccessible(true);
                System.out.println(field.getName() + " = " + field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
