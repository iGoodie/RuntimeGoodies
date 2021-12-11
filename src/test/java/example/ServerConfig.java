package example;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServerConfig extends JsonConfiGoodie {

    @Goodie // <-- Annotate the fields you want to be exposed with @Goodie
    String username, password;

    @Goodie
    Date launchDate; // Some values are converted from string!

    @Goodie
    int port = 4001; // You can declare default values!

    @Goodie
    DBConnection mongodbConnection; // Other ConfiGoodies/POJOs can be nested too!

    @Goodie
    DBConnection mysqlConnection = defaultValue(() -> {
        DBConnection connection = new DBConnection();
        connection.ports = Arrays.asList(3000, 3001, 3002);
        return connection;
    }); // You can declare default values via your own supplier!

    public static class DBConnection {

        @Goodie
        String uri = "localhost:3000";

        @Goodie
        List<Integer> ports; // You can have Lists as well!

        @Goodie
        Map<String, String> aliases; // Guess what, Maps too!

    }

    @Test
    public void test() {
        TestUtils.standardConfiGoodieTest(new ServerConfig(), "{}");
    }

}