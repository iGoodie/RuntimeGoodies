package net.programmer.igoodie.configuration.validation.circularity;

import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.Field;
import java.util.List;

public class GoodieCircularityTest {

    Graph<Class<?>> classGraph = new Graph<>();

    public GoodieCircularityTest(Object root) {
        this(root.getClass());
    }

    public GoodieCircularityTest(Class<?> root) {
        generateGraph(null, root);
    }

    private void generateGraph(Graph.Vertex<Class<?>> parent, Class<?> root) {
        for (Field field : ReflectionUtilities.getFieldsWithAnnotation(root, Goodie.class)) {
            Class<?> fieldType = field.getType();

            if (TypeUtilities.isArray(fieldType)) {
                // TODO

            } else if (TypeUtilities.isMap(fieldType)) {
                // TODO

            } else if (isGoodieRoot(fieldType)) {
                Graph.Vertex<Class<?>> childVertex = classGraph.generateVertex(fieldType);
                if (parent != null) classGraph.addEdge(parent, childVertex);
                if (classGraph.hasCycle()) return;
                generateGraph(childVertex, fieldType);
            }
        }
    }

    private boolean isGoodieRoot(Class<?> type) {
        List<Field> goodieFields = ReflectionUtilities.getFieldsWithAnnotation(type, Goodie.class);
        return goodieFields.size() > 0;
    }

    public boolean test() {
        return classGraph.hasCycle();
    }

}
