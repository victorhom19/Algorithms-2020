package lesson6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Node {
    private Set<Node> children = new HashSet<>();
    private Graph.Vertex value;
    private List<Graph.Vertex> rootCollectedVertices;
    private int mark = 0;

    Node(Graph.Vertex rootVertex, Graph graph) {
        this.value = rootVertex;
        rootCollectedVertices = new ArrayList<>();
        rootCollectedVertices.add(rootVertex);
        for (Graph.Edge edge : graph.getEdges()) {
            if (edge.getBegin() == rootVertex) {
                children.add(new Node(edge.getEnd(), this, graph, rootCollectedVertices));
            } else if (edge.getEnd() == rootVertex) {
                children.add(new Node(edge.getBegin(), this, graph, rootCollectedVertices));
            }
        }
    }

    private Node(Graph.Vertex vertex, Node parentVertex, Graph graph, List<Graph.Vertex> collectedVertices) {
        this.value = vertex;
        collectedVertices.add(vertex);
        for (Graph.Edge edge : graph.getEdges()) {
            if (edge.getBegin() == vertex && !edge.getEnd().equals(parentVertex.value)) {
                children.add(new Node(edge.getEnd(), this, graph, collectedVertices));
            } else if (edge.getEnd() == vertex && !edge.getBegin().equals(parentVertex.value)) {
                children.add(new Node(edge.getBegin(), this, graph, collectedVertices));
            }
        }
    }


    private Set<Node> getChildren() {
        return children;
    }

    private Set<Node> getGrandchildren() {
        Set<Node> collected = new HashSet<>();
        if (this.hasGrandchildren()) {
            for (Node child : this.getChildren()) {
                collected.addAll(child.getChildren());
            }
        }
        return collected;
    }

    List<Graph.Vertex> getCollectedVertices() {
        return rootCollectedVertices;
    }

    private boolean hasChildren() {
        return this.children.size() > 0;
    }

    private boolean hasGrandchildren() {
        if (this.hasChildren()) {
            for (Node child : this.children) {
                if (child.hasChildren()) {
                    return true;
                }
            }
        }
        return false;
    }


    static int findWeights(Node node) {
        int childSum = 0;
        int grandchildSum = 0;
        for (Node child : node.getChildren()) {
            childSum += findWeights(child);
        }
        for (Node grChild : node.getGrandchildren()) {
            grandchildSum += findWeights(grChild);
        }
        node.mark = Math.max(1 + grandchildSum, childSum);

        return (node.mark);
    }

    static void findTree(Node node, Set<Graph.Vertex> collectedTree) {
        int childSum = 0;
        for (Node child : node.getChildren()) {
            childSum += child.mark;
        }
        if (childSum != node.mark) {
            collectedTree.add(node.value);
            for (Node grandchild : node.getGrandchildren()) {
                findTree(grandchild, collectedTree);
            }
        } else {
            collectedTree.add(node.value);
            for (Node child : node.getGrandchildren()) {
                findTree(child, collectedTree);
            }
        }
    }
}