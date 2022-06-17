package example;

import net.programmer.igoodie.goodies.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;

public class ConfigReloadingTest {

    public static class TheConfig extends JsonConfiGoodie {

        @Goodie
        int count = 0;

    }

    public static String serializedData = "{}";
    public static TheConfig theConfig;

    @Test
    public void test() {
        System.out.println("Serialized: " + serializedData);

        initialize();
        System.out.println("Before: " + theConfig.isDirty());
        theConfig.count++;
        System.out.println("After: " + theConfig.isDirty());
        System.out.println(theConfig.serialize());

        System.out.println("Serialized: " + serializedData);

        initialize(); // Reload here
        System.out.println("Before: " + theConfig.isDirty());
        theConfig.count++;
        System.out.println("After: " + theConfig.isDirty());
        System.out.println(theConfig.serialize());
        if (theConfig.isDirty()) serializeToVariable(theConfig.serialize());

        System.out.println();
        System.out.println("Serialized: " + serializedData);

        initialize(); // Reload here
        System.out.println("Before: " + theConfig.isDirty());
        theConfig.count++;
        System.out.println("After: " + theConfig.isDirty());
        System.out.println(theConfig.serialize());
        if (theConfig.isDirty()) serializeToVariable(theConfig.serialize());

        System.out.println("Serialized: " + serializedData);
    }

    private static void initialize() {
        theConfig = new TheConfig();

        theConfig = theConfig.readConfig(new ConfiGoodieOptions()
                .useText(serializedData)
                .onFixed(ConfigReloadingTest::serializeToVariable));
    }

    private static void serializeToVariable(GoodieObject fixedGoodie) {
        serializedData = theConfig.getFormat().writeToString(fixedGoodie, false);
    }

}
