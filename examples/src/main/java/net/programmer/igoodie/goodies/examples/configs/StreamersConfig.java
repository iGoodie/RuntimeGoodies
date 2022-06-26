package net.programmer.igoodie.goodies.examples.configs;

import net.programmer.igoodie.goodies.configuration.TomlConfiGoodie;
import net.programmer.igoodie.goodies.examples.entries.StreamerCredential;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

import java.util.LinkedList;
import java.util.List;

public class StreamersConfig extends TomlConfiGoodie {

    enum IndicatorDisplay {
        CIRCLE_ONLY,
        ENABLED,
        DISABLED
    }

    @Goodie
    IndicatorDisplay indicatorDisplay;

    @Goodie
    List<String> moderators = defaultValue(() -> {
        LinkedList<String> value = new LinkedList<>();
        value.add("iGoodie");
        value.add("iGoodiex");
        return value;
    });

    @Goodie
    List<StreamerCredential> streamers = defaultValue(() -> {
        LinkedList<StreamerCredential> value = new LinkedList<>();
        value.add(new StreamerCredential("iGoodie", "my:token"));
        value.add(new StreamerCredential("iGoodiex", "my:token2"));
        return value;
    });

    @Override
    public String toString() {
        return "StreamersConfig{" +
                "indicatorDisplay=" + indicatorDisplay +
                ", moderators=" + moderators +
                ", streamers=" + streamers +
                '}';
    }

}
