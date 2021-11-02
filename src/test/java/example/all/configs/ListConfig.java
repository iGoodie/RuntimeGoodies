package example.all.configs;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.annotation.Goodie;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListConfig extends JsonConfiGoodie {

    @Goodie
    List<String> strings;

    @Goodie
    List<Number> numbers;

    @Goodie
    List<Double> doubles;

    @Goodie
    List<UUID> uuids;

    @Goodie
    List<GoodieObject> goodieObjects;

    @Goodie
    List<GoodieArray> goodieArrays;

    @Goodie
    List<GoodieElement> goodieElements;

    @Goodie
    List<List<Double>> nestedList;

//    @Goodie
//    List<Map<String, String>> nestedMap;

}
