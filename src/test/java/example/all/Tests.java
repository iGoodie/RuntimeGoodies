package example.all;

import example.all.configs.AnimalConfig;
import example.all.configs.GeneralConfig;
import example.all.configs.ListConfig;
import example.all.goodies.Deer;
import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.ReflectionUtilities;
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

    @Test
    public void testArrays() throws IOException {
        ListConfig config = new ListConfig().readConfig(TestFiles.loadData("lists_config.json"));
        new GoodieTraverser().traverseGoodieFields(config, (object, field, goodiePath) -> {
            try {
                field.setAccessible(true);
                System.out.println(field + " = " + field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testExtendables() throws IOException, IllegalAccessException {
        AnimalConfig config = new AnimalConfig().readConfig(new ConfiGoodieOptions()
                .useText(TestFiles.loadData("animals.config.json"))
                .onFixed(System.out::println));

        System.out.println(config);

        new GoodieTraverser().summarizeObject(config).forEach(System.out::println);

        System.out.println(config);
    }

}
