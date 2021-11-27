package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class SubtypeDefaults extends JsonConfiGoodie {

    @Goodie
    @SuppressWarnings("UnnecessaryBoxing")
    Number primitive = new Double(15d);

    @Test
    public void testSubtypes() {
        TestUtils.standardConfiGoodieTest(new SubtypeDefaults(), "{}");
    }

}
