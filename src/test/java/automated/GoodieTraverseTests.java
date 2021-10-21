package automated;

import automated.data.User;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

public class GoodieTraverseTests {

    @Test
    public void testTraverse() {
        User userData = new User();
        GoodieTraverser traverser = new GoodieTraverser();

        traverser.traverseGoodies(userData, (object, field, goodiePath) -> {
            System.out.println(goodiePath + " => " + field);
        });
    }

}
