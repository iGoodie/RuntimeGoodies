package automated;

import automated.data.User;
import net.programmer.igoodie.goodies.configuration.ConfiGoodieOptions;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.File;
import java.io.IOException;

public class GoodieConfigTests {

    @Test
    public void testViaText() throws IOException {
        User user = new User().readConfig(TestFiles.loadData("user.json"));
        System.out.println(user);
    }

    @Test
    public void testViaFile() throws IOException {
        String configPath = System.getenv("APPDATA") + File.separator + ".igoodie/RuntimeGoodies/config.json";

        ConfiGoodieOptions options = ConfiGoodieOptions.fromFile(new File(configPath))
                .onFixed(ConfiGoodieOptions.FixHandlers.renameInvalidConfig((invalidConfigFile, fixedGoodie) ->
                        String.format("%s.%d.invalid", invalidConfigFile.getPath(), System.currentTimeMillis())));

        User user = new User().readConfig(options);
        System.out.println(user);
    }

}
