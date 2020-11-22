package lesson6;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */

    private static boolean isEulerGraph(Graph graph) {
        if (graph.getVertices().size() == 0) return false;
        int zeroCounter = 0;
        HashMap<Graph.Vertex, Integer> vertexDegree = new HashMap<>();
        for (Graph.Vertex vertex : graph.getVertices()) {
            vertexDegree.put(vertex, 0);
        }
        for (Graph.Edge edge : graph.getEdges()) {
            vertexDegree.put(edge.getBegin(), vertexDegree.get(edge.getBegin()) + 1);
            vertexDegree.put(edge.getEnd(), vertexDegree.get(edge.getEnd()) + 1);
        }
        for (Graph.Vertex vertex : graph.getVertices()) {
            if (vertexDegree.get(vertex) % 2 != 0) {
                return false;
            } else if (vertexDegree.get(vertex) == 0) {
                zeroCounter ++;
            }
        }
        return zeroCounter < graph.getVertices().size();
    }

    private static void mergeCircuits(int id, List<List<Graph.Edge>> circuits, List<List<Graph.Vertex>> circuitsPaths, List<Graph.Edge> mergeResult) {
        List<Graph.Edge> circuit = circuits.get(id);
        for (int k = 0; k < circuit.size(); k ++) {
            for (int i = 0; i < circuitsPaths.size(); i ++) {
                if (i != id && circuitsPaths.get(i).get(0) == circuitsPaths.get(id).get(k)) {
                    mergeCircuits(i, circuits, circuitsPaths, mergeResult);
                }
            }
            mergeResult.add(circuit.get(k));
        }
    }

    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        // Трудоемкость O(N^2)
        // Ресурсоемкость O (N)


        List<Graph.Edge> traversed = new ArrayList<>();
        List<List<Graph.Edge>> circuits = new ArrayList<>();
        List<List<Graph.Vertex>> circuitsPaths = new ArrayList<>();

        if (isEulerGraph(graph)) {
            for (Graph.Edge startingEdge : graph.getEdges()) {
                if (!(traversed.contains(startingEdge))) {
                    List<Graph.Edge> lastRun = new ArrayList<>();
                    List<Graph.Vertex> visited = new ArrayList<>();
                    Graph.Edge cursor = startingEdge;
                    visited.add(cursor.getBegin());
                    visited.add(cursor.getEnd());
                    do {
                        lastRun.add(cursor);
                        for (Graph.Edge edge : graph.getEdges()) {
                            if (!(traversed.contains(edge) || lastRun.contains(edge))) {
                                if (edge.getBegin().equals(visited.get(visited.size() - 1))) {
                                    visited.add(edge.getEnd());
                                    cursor = edge;
                                    break;
                                } else if (edge.getEnd().equals(visited.get(visited.size() - 1))) {
                                    visited.add(edge.getBegin());
                                    cursor = edge;
                                    break;
                                }
                            }
                        }
                    } while (!(visited.get(0).equals(visited.get(visited.size() - 1))));
                    lastRun.add(cursor);

                    traversed.addAll(lastRun);
                    circuits.add(lastRun);
                    circuitsPaths.add(visited);

                }
            }
        }

        List<Graph.Edge> result = new ArrayList<>();
        if (circuits.size() > 0) {
            mergeCircuits(0, circuits, circuitsPaths, result);
        }
        return result;
    }



    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
