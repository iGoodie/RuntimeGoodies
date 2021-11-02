package example.all;

import example.all.configs.GeneralConfig;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class Tests {

    @Test
    public void testGeneralConfig() throws IOException {
        GeneralConfig config = new GeneralConfig().readConfig(TestFiles.loadData("general.json"));
        System.out.println(config);
        System.out.println(config.primitiveList.get(0).isNaN());
    }

}
