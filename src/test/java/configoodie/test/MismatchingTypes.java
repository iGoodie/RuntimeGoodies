package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MismatchingTypes extends JsonConfiGoodie {

    @Goodie
    int primitive;

    @Goodie
    Number wrapped;

    @Goodie
    List<Number> numbers;

    @Goodie
    List<List<Number>> nestedList;

    @Goodie
    List<List<Number>> nestedList2;

    @Goodie
    Map<String, Number> primitiveMap;

    @Goodie
    Map<String, List<String>> listMap;

    @Goodie
    Map<String, Map<String, Float>> mapMap;

    @Goodie
    Map<Random, Map<String, Float>> stringifiableMapMap;

    @Test
    public void testEnums() {
        TestUtils.standardConfiGoodieTest(new MismatchingTypes(), "{" +
                "'primitive': []," +
                "'wrapped': {}," +
                "'numbers': {}," +
                "'nestedList': [{}]," +
                "'nestedList2': {}," +
                "'primitiveMap': {}," +
                "'listMap': {'A': {}, 'B': ['Foo', 1, 2, 3, 'Bar']}," +
                "'mapMap': {'A': [], 'B': { 'Foo': 'Invalid', 'Bar': 15 }}," +
                "'stringifiableMapMap': {1: {}, 2: [], 3: { 'Foo': 'Invalid', 'Bar': 15 }}" +
                "}");
    }

}
