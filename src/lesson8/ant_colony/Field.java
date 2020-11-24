package lesson8.ant_colony;

import lesson6.Graph;
import lesson6.impl.GraphBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class Field {

    public static void main(String[] args) {
        GraphBuilder graphBuilder = new GraphBuilder();
        Graph.Vertex nest = graphBuilder.addVertex("NEST");
        Graph.Vertex food = graphBuilder.addVertex("FOOD");
        List<Graph.Vertex> vertexList = new ArrayList<>();
        /*for (int i = 0; i < "ABCDEFG".length(); i ++) {
            vertexList.add(graphBuilder.addVertex("ABCDEFG".substring(i, i + 1)));
        }
        graphBuilder.addConnection(nest, vertexList.get(0), 3);
        graphBuilder.addConnection(nest, vertexList.get(1), 2);

        graphBuilder.addConnection(vertexList.get(0), vertexList.get(2), 2);
        graphBuilder.addConnection(vertexList.get(0), vertexList.get(3), 3);

        graphBuilder.addConnection(vertexList.get(1), vertexList.get(2), 4);
        graphBuilder.addConnection(vertexList.get(1), vertexList.get(4), 1);

        graphBuilder.addConnection(vertexList.get(2), vertexList.get(5), 7);

        graphBuilder.addConnection(vertexList.get(4), vertexList.get(5), 1);

        graphBuilder.addConnection(vertexList.get(3), food, 5);
        graphBuilder.addConnection(vertexList.get(5), food, 1);*/

        for (int i = 0; i < "ABCDEFGHIJKL".length(); i ++) {
            vertexList.add(graphBuilder.addVertex("ABCDEFGHIJKL".substring(i, i + 1)));
        }

        graphBuilder.addConnection(nest, vertexList.get(0), 5);
        graphBuilder.addConnection(nest, vertexList.get(1), 10);
        graphBuilder.addConnection(nest, vertexList.get(2), 1);

        graphBuilder.addConnection(vertexList.get(0), vertexList.get(1), 3);

        graphBuilder.addConnection(vertexList.get(1), vertexList.get(5), 4);

        graphBuilder.addConnection(vertexList.get(2), vertexList.get(3), 8);

        graphBuilder.addConnection(vertexList.get(3), vertexList.get(4), 3);

        graphBuilder.addConnection(vertexList.get(4), vertexList.get(11), 11);

        graphBuilder.addConnection(vertexList.get(5), vertexList.get(7), 5);
        graphBuilder.addConnection(vertexList.get(5), vertexList.get(8), 8);

        graphBuilder.addConnection(vertexList.get(6), vertexList.get(9), 6);

        graphBuilder.addConnection(vertexList.get(7), vertexList.get(9), 2);
        graphBuilder.addConnection(vertexList.get(7), vertexList.get(10), 5);

        graphBuilder.addConnection(vertexList.get(8), vertexList.get(10), 3);

        graphBuilder.addConnection(vertexList.get(9), food, 2);

        graphBuilder.addConnection(vertexList.get(10), food, 7);

        graphBuilder.addConnection(vertexList.get(11), vertexList.get(6), 4);
        graphBuilder.addConnection(vertexList.get(11), food, 3);



        Field field = new Field(graphBuilder.build(), 0.9, 1.1,
                5, 140, 1000, 0.2, 0.2, nest, food);

        System.out.println(field.doRuns());
    }

    private int passedAnts = 0;

    enum Type {
        RESEARCHER,
        NORMAL,
        PATHCOLLECTOR
    }

    public class Ant {

        Graph.Vertex position;
        Type antType;

        Ant(Type type) {
            position = nestPos;
            this.antType = type;
        }

        private void move() {
            List<Graph.Edge> possiblePath = new ArrayList<>();

            for (Graph.Edge edge : graph.getEdges()) {
                if (edge.getBegin().equals(position)) {
                    possiblePath.add(edge);
                }
            }
            Graph.Edge selectedPath = doPathChoice(possiblePath);
            this.position = selectedPath.getEnd();
            updatePheromones(selectedPath);

        }

        private Graph.Edge doPathChoice(List<Graph.Edge> possiblePaths) {
            switch (this.antType) {
                case NORMAL:
                    double probabilityFactorSum = 0;
                    for (Graph.Edge edge : possiblePaths) {
                        probabilityFactorSum += pheromoneNet.get(edge);
                    }
                    double random = randomNumber(probabilityFactorSum);
                    possiblePaths = possiblePaths.stream()
                            .sorted((Comparator.comparing(o -> pheromoneNet.get(o)))).collect(Collectors.toList());
                    for (Graph.Edge edge : possiblePaths) {
                        if (0.0 <= random && random <= pheromoneNet.get(edge)) {
                            return edge;
                        }
                        random -= pheromoneNet.get(edge);
                        probabilityFactorSum -= pheromoneNet.get(edge);
                    }

                case RESEARCHER:
                    return possiblePaths.get(randomInt(possiblePaths.size()));

                case PATHCOLLECTOR:
                    double trust = 0.0;
                    Graph.Edge maxTrustedPath = null;
                    for (Graph.Edge edge : possiblePaths) {
                        if (pheromoneNet.get(edge) > trust) {
                            trust = pheromoneNet.get(edge);
                            maxTrustedPath = edge;
                        }
                    }
                    return maxTrustedPath;

            }
            return null;
        }

        private int randomInt(int max) {
            Random random = new Random();
            return random.nextInt(max);
        }
        private double randomNumber(double max) {
            return Math.random() * max;
        }
    }


    private Graph graph;
    private Ant ant;
    private Map<Graph.Edge, Double> pheromoneNet;
    private double vaporizingConstant;
    private double pheromoneConstant;
    private double maxPheromone;
    private int antsPerWave;
    private double basePheromoneValue;
    private int totalRuns;
    private int researchingPhase;
    private int mainPhase;
    private int maxPathLength = 0;
    private int antsInOneRun;
    private Graph.Vertex nestPos;
    private Graph.Vertex foodPos;


    private Field(Graph graph, double vaporizingConstant, double pheromoneConstant, int antsPerWave, int antsInOneRun, int totalRuns,
                  double researchingPhaseFraction, double basePheromoneValue, Graph.Vertex nestPos, Graph.Vertex foodPos) {
        this.graph = graph;
        pheromoneNet = new HashMap<>();
        this.vaporizingConstant = vaporizingConstant;
        this.pheromoneConstant = pheromoneConstant;
        this.basePheromoneValue = basePheromoneValue;
        resetPheromoneNet();
        this.totalRuns = totalRuns;
        if (researchingPhaseFraction < 0 || researchingPhaseFraction > 1) {
            throw new IllegalArgumentException("Researching phase might be in range between 0 and 1");
        }
        this.researchingPhase = (int) (researchingPhaseFraction * antsInOneRun);
        this.mainPhase = antsInOneRun - this.researchingPhase;
        this.nestPos = nestPos;
        this.foodPos = foodPos;
        this.antsPerWave = antsPerWave;
        this.antsInOneRun = antsInOneRun;
        updateMaxPath();

    }

    private void doResearchingPhase() {
        for (int i = 0; i < researchingPhase; i ++) {
            ant = new Ant(Type.RESEARCHER);
            while (ant.position != foodPos) {
                ant.move();
            }
            passedAnts += 1;
            System.out.println(passedAnts / (antsInOneRun * (double) totalRuns));
        }
    }

    private void doMainPhase() {
        for (int wave = 0; wave < antsInOneRun / antsPerWave; wave ++) {
            for (int i = 0; i < antsInOneRun; i ++) {
                ant = new Ant(Type.NORMAL);
                while (ant.position != foodPos) {
                    ant.move();
                }
                passedAnts += 1;
                System.out.println(passedAnts / (antsInOneRun * (double) totalRuns));
            }
            vaporizePheromones();
        }
    }

    private List<Graph.Vertex> collectPath() {
        ant = new Ant(Type.PATHCOLLECTOR);
        List<Graph.Vertex> collectedPath = new ArrayList<>();
        do {
            collectedPath.add(ant.position);
            ant.move();
        } while (ant.position != foodPos);
        collectedPath.add(foodPos);
        return collectedPath;
    }

    private Map<List<Graph.Vertex>, Integer> doRuns() {
        List<List<Graph.Vertex>> collectedPaths = new ArrayList<>();
        Map<List<Graph.Vertex>, Integer> pathsFrequency = new HashMap<>();
        for (int i = 0; i < totalRuns; i ++) {
            doResearchingPhase();
            doMainPhase();
            collectedPaths.add(collectPath());
            resetPheromoneNet();
        }
        for (List<Graph.Vertex> collectedPath : collectedPaths) {
            if (pathsFrequency.containsKey(collectedPath)) {
                int newValue = pathsFrequency.get(collectedPath) + 1;
                pathsFrequency.put(collectedPath, newValue);
            } else {
                pathsFrequency.put(collectedPath, 1);
            }
        }
        return pathsFrequency;
    }

    private void resetPheromoneNet() {
        for (Graph.Edge edge : graph.getEdges()) {
            pheromoneNet.put(edge, this.basePheromoneValue);
        }
    }

    private void updatePheromones(Graph.Edge edge) {
        double newPheromoneValue = pheromoneNet.get(edge) * pheromoneConstant / edge.getWeight();

        this.pheromoneNet.put(edge, newPheromoneValue);
    }

    private void setMaxPheromone() {
        this.maxPheromone = this.antsInOneRun / 200.0;
    }


    private void updateMaxPath() {
        for (Graph.Edge edge : graph.getEdges()) {
            if (edge.getWeight() > maxPathLength) {
                maxPathLength = edge.getWeight();
            }
        }
    }

    private void vaporizePheromones() {
        for (Graph.Edge edge : graph.getEdges()) {
            double newPheromoneValue = pheromoneNet.get(edge) * vaporizingConstant;
            pheromoneNet.put(edge, newPheromoneValue);
        }
    }

}
