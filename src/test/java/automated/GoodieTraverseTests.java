package automated;

import automated.data.User;
import net.programmer.igoodie.goodies.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class GoodieTraverseTests {

    @Test
    public void testTraverse() {
        User userData = new User();
        GoodieTraverser traverser = new GoodieTraverser();

        traverser.traverseGoodieFields(userData, (object, field, goodiePath) -> {
            System.out.println(goodiePath + " => " + field);
        });
    }

    @Test
    public void testSummary() {
        User userData = new User();
        GoodieTraverser traverser = new GoodieTraverser();

        Set<String> summary = traverser.summarizeObjectPaths(userData);
        summary.forEach(System.out::println);
    }

}
