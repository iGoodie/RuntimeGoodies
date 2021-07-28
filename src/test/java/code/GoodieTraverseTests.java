package code;

import data.User;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.Test;

public class GoodieTraverseTests {

    @Test
    public void testTraverse() {
        User userData = new User();
        GoodieTraverser traverser = new GoodieTraverser();

        traverser.traverseGoodies(userData, (object, field, goodieKey) -> {
            System.out.println(goodieKey + " => " + field);
        });
    }

}
