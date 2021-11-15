package automated;

import net.programmer.igoodie.configuration.validation.circularity.GoodieCircularityTest;
import net.programmer.igoodie.configuration.validation.circularity.Graph;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CircularityIntegrityTests {

    static class SubTypeA {
        @Goodie Object foo;
        @Goodie SubTypeA selfTyped;
    }

    static class SubTypeB {
        @Goodie Object foo;
        @Goodie Number bar;
        @Goodie SubTypeC baz;
    }

    static class SubTypeC {
        @Goodie Object foo;
    }

    @Goodie SubTypeA selfDependingSubType;

    @Test
    public void testCircularityTestIntegrity() {
        Assertions.assertTrue(new GoodieCircularityTest(CircularityIntegrityTests.class).test());
        Assertions.assertTrue(new GoodieCircularityTest(SubTypeA.class).test());
        Assertions.assertFalse(new GoodieCircularityTest(SubTypeC.class).test());
    }

    @Test
    public void testNonCircularGraph() {
        Graph<String> graph = new Graph<>();

        Graph.Vertex<String> vertexA = graph.generateVertex("A");
        Graph.Vertex<String> vertexB = graph.generateVertex("B");
        Graph.Vertex<String> vertexC = graph.generateVertex("C");
        Graph.Vertex<String> vertexD = graph.generateVertex("D");

        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexB, vertexC);
        graph.addEdge(vertexC, vertexD);

        Assertions.assertFalse(graph.hasCycle());
    }

    @Test
    public void testCircularGraph() {
        Graph<String> graph = new Graph<>();

        Graph.Vertex<String> vertexA = graph.generateVertex("A");
        Graph.Vertex<String> vertexB = graph.generateVertex("B");
        Graph.Vertex<String> vertexC = graph.generateVertex("C");
        Graph.Vertex<String> vertexD = graph.generateVertex("D");

        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexA, vertexC);
        graph.addEdge(vertexA, vertexD);
        graph.addEdge(vertexD, vertexA);

        Assertions.assertTrue(graph.hasCycle());
    }

}
