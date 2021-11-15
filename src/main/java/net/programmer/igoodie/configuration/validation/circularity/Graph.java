package net.programmer.igoodie.configuration.validation.circularity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Graph<T> {

    private final List<Vertex<T>> vertices = new LinkedList<>();
    private final Set<Vertex<T>> verticesBeingVisited = new HashSet<>();
    private final Set<Vertex<T>> verticesVisited = new HashSet<>();

    public Vertex<T> generateVertex(T data) {
        Vertex<T> foundVertex = findVertex(data);
        if (foundVertex == null) {
            Vertex<T> vertex = new Vertex<>(data);
            addVertex(vertex);
            return vertex;
        }
        return foundVertex;
    }

    public Vertex<T> findVertex(T data) {
        for (Vertex<T> vertex : vertices) {
            if (vertex.data == data) {
                return vertex;
            }
        }
        return null;
    }

    public void addVertex(Vertex<T> vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Vertex<T> from, Vertex<T> to) {
        from.addNeighbour(to);
    }

    private boolean hasCycle(Vertex<T> sourceVertex) {
        verticesBeingVisited.add(sourceVertex);

        for (Vertex<T> neighbor : sourceVertex.adjacencyList) {
            if (verticesBeingVisited.contains(neighbor)) {
                // backward edge exists
                return true;
            } else if (!verticesVisited.contains(neighbor) && hasCycle(neighbor)) {
                return true;
            }
        }

        verticesBeingVisited.remove(sourceVertex);
        verticesVisited.add(sourceVertex);
        return false;
    }

    public boolean hasCycle() {
        for (Vertex<T> vertex : vertices) {
            if (!verticesVisited.contains(vertex) && hasCycle(vertex)) {
                verticesBeingVisited.clear();
                verticesVisited.clear();
                return true;
            }
        }
        verticesBeingVisited.clear();
        verticesVisited.clear();
        return false;
    }

    public static class Vertex<T> {
        T data;
        List<Vertex<T>> adjacencyList = new LinkedList<>();

        public Vertex(T data) {
            this.data = data;
        }

        public void addNeighbour(Vertex<T> vertex) {
            adjacencyList.add(vertex);
        }
    }

}
