package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.*;

public class Lists extends JsonConfiGoodie {

    @Goodie
    List<String> primitives;

    @Goodie
    List<POJO> pojos;

    @Goodie
    List<Random> stringifiables;

    @Goodie
    List<List<String>> stringLists;

    @Goodie
    List<List<List<String>>> stringListLists;

    @Goodie
    List<List<List<String>>> stringListListsWithDefault = defaultValue(() -> {
        List<List<List<String>>> list = new ArrayList<>();
        List<List<String>> list2 = new ArrayList<>();
        List<String> innerList = new ArrayList<>();
        innerList.add("A");
        list2.add(innerList);
        list.add(list2);
        return list;
    });

    public static class POJO {
        @Goodie
        String str;

        @Override
        public String toString() {
            return "POJO{" +
                    "str='" + str + '\'' +
                    '}';
        }
    }

    @Test
    public void testLists() {
        TestUtils.standardConfiGoodieTest(new Lists(), "{ " +
                "'primitives': ['Foo', 1, 2, 3, 'Bar']," +
                "'pojos': [{'str':'A'}, {'str':'B'}, 'invalid']," +
                "'stringifiables': [123, 456]," +
                "'stringLists': [['A', 'B'], ['C', 'D']]," +
                "'stringListLists': [[['A'], ['B']], [['C'], ['D']]]" +
                " }");
    }

}
