import com.moandjiezana.toml.Toml;
import net.programmer.igoodie.goodies.format.TomlGoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.junit.jupiter.api.Test;

public class TomlFormatTests {

    @Test
    public void should() {
        String officialExample = "# This is a TOML document.\n" +
                "\n" +
                "title = \"TOML Example\"\n" +
                "\n" +
                "[owner]\n" +
                "name = \"Tom Preston-Werner\"\n" +
                "dob = 1979-05-27T07:32:00-08:00 # First class dates\n" +
                "\n" +
                "[database]\n" +
                "server = \"192.168.1.1\"\n" +
                "ports = [ 8001, 8001, 8002 ]\n" +
                "connection_max = 5000\n" +
                "enabled = true\n" +
                "\n" +
                "[servers]\n" +
                "\n" +
                "  # Indentation (tabs and/or spaces) is allowed but not required\n" +
                "  [servers.alpha]\n" +
                "  ip = \"10.0.0.1\"\n" +
                "  dc = \"eqdc10\"\n" +
                "\n" +
                "  [servers.beta]\n" +
                "  ip = \"10.0.0.2\"\n" +
                "  dc = \"eqdc10\"\n" +
                "\n" +
                "[clients]\n" +
                "data = [ [\"gamma\", \"delta\"], [1, 2] ]\n" +
                "\n" +
                "# Line breaks are OK when inside arrays\n" +
                "hosts = [\n" +
                "  \"alpha\",\n" +
                "  \"omega\"\n" +
                "]";

        TomlGoodieFormat format = new TomlGoodieFormat();

        Toml readToml = format.readFromString(officialExample);
        System.out.println("TOML");
        System.out.println(readToml.toMap());
        System.out.println();

        GoodieObject wroteGoodie = format.writeToGoodie(readToml);
        System.out.println("TOML -> Goodie");
        System.out.println(wroteGoodie);
        System.out.println();

        Toml tomlFromGoodie = format.readFromGoodie(wroteGoodie);
        System.out.println("Goodie -> TOML");
        System.out.println(tomlFromGoodie.toMap());
        System.out.println();

        String serialized = format.writeToString(tomlFromGoodie, true);
        System.out.println("TOML -> String");
        System.out.println(serialized);
        System.out.println();
    }

}
