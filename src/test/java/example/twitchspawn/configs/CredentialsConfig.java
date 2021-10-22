package example.twitchspawn.configs;

import example.twitchspawn.configs.goodies.Streamer;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieList;
import net.programmer.igoodie.serialization.annotation.Goodie;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CredentialsConfig extends JsonConfiGoodie {

    @Goodie
    List<String> moderatorsMinecraft = Arrays.asList("iGoodie", "CoconutOrange");

    @Goodie
    List<String> moderatorsTwitch = Arrays.asList("iGoodie", "CoconutOrange");

    @Goodie
    @GoodieList(allowNull = false)
    List<Streamer> streamers = defaultValue(() -> {
        List<Streamer> streamers = new LinkedList<>();
        streamers.add(new Streamer());
        return streamers;
    });

}
