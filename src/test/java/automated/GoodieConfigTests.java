package automated;

import automated.data.User;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class GoodieConfigTests {

    @Test
    public void testWholeProcess() throws IOException {
        User user = new User().readConfig(TestFiles.loadData("user.json"));
        System.out.println(user);
    }

}
