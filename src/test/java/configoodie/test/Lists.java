package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Lists extends JsonConfiGoodie {

    @Goodie
    List<String> primitives;

    @Goodie
    List<POJO> pojos;

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
        Lists config = new Lists().readConfig(new ConfiGoodieOptions()
                .useText("{ " +
                        "'primitives': ['Foo', 1, 2, 3, 'Bar']," +
                        "'pojos': [{'str':'A'}, {'str':'B'}, 'invalid']," +
                        "'stringLists': [['A', 'B'], ['C', 'D']]," +
                        "'stringListLists': [[['A'], ['B']], [['C'], ['D']]]" +
                        " }")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());

        System.out.println("\nSerialized back:");
        System.out.println(new GoodieSerializer().serializeFrom(config));
    }

}
