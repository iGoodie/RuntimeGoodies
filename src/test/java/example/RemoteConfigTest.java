package example;

import net.programmer.igoodie.goodies.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RemoteConfigTest extends JsonConfiGoodie {

    @Goodie
    List<Product> products;

    public static class Product {

        @Goodie
        int id;

        @Goodie
        String title;

        @Goodie
        List<String> images;

        @Override
        public String toString() {
            return "Product{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    @Test
    public void test() throws MalformedURLException {
        URL url = new URL("https://dummyjson.com/products");
        ConfiGoodieOptions options = ConfiGoodieOptions.fromURL(url);

        RemoteConfigTest config = new RemoteConfigTest().readConfig(options);

        config.products.forEach(System.out::println);
    }

}
