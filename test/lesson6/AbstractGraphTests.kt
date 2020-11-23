package lesson6

import lesson6.impl.GraphBuilder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractGraphTests {

    private fun Graph.Edge.isNeighbour(other: Graph.Edge): Boolean {
        return begin == other.begin || end == other.end || begin == other.end || end == other.begin
    }

    private fun List<Graph.Edge>.assert(shouldExist: Boolean, graph: Graph) {
        val edges = graph.edges
        if (shouldExist) {
            assertEquals(edges.size, size, "Euler loop should traverse all edges")
        } else {
            assertTrue(isEmpty(), "Euler loop should not exist")
        }
        for (edge in this) {
            assertTrue(edge in edges, "Edge $edge is not inside graph")
        }
        for (i in 0 until size - 1) {
            assertTrue(this[i].isNeighbour(this[i + 1]), "Edges ${this[i]} & ${this[i + 1]} are not incident")
        }
        if (size > 1) {
            assertTrue(this[0].isNeighbour(this[size - 1]), "Edges ${this[0]} & ${this[size - 1]} are not incident")
        }
    }

    fun findEulerLoop(findEulerLoop: Graph.() -> List<Graph.Edge>) {
        val emptyGraph = GraphBuilder().build()
        val emptyLoop = emptyGraph.findEulerLoop()
        assertTrue(emptyLoop.isEmpty(), "Euler loop should be empty for the empty graph")
        val noEdgeGraph = GraphBuilder().apply {
            addVertex("A")
            addVertex("B")
            addVertex("C")
        }.build()
        val noEdgeLoop = noEdgeGraph.findEulerLoop()
        noEdgeLoop.assert(shouldExist = false, graph = noEdgeGraph)
        val simpleGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            addConnection(a, b)
        }.build()
        val simpleLoop = simpleGraph.findEulerLoop()
        simpleLoop.assert(shouldExist = false, graph = simpleGraph)
        val unconnected = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            addConnection(a, b)
            addConnection(c, d)
        }.build()
        val unconnectedLoop = unconnected.findEulerLoop()
        unconnectedLoop.assert(shouldExist = false, graph = unconnected)
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val loop = graph.findEulerLoop()
        loop.assert(shouldExist = true, graph = graph)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val loop2 = graph2.findEulerLoop()
        loop2.assert(shouldExist = true, graph = graph2)
        // Seven bridges of Koenigsberg
        //    A1 -- A2 ---
        //    |      |    |
        //    B1 -- B2 -- C
        //    |     |     |
        //    D1 -- D2 ---
        val graph3 = GraphBuilder().apply {
            val a1 = addVertex("A1")
            val a2 = addVertex("A2")
            val b1 = addVertex("B1")
            val b2 = addVertex("B2")
            val c = addVertex("C")
            val d1 = addVertex("D1")
            val d2 = addVertex("D2")
            addConnection(a1, a2)
            addConnection(b1, b2)
            addConnection(d1, d2)
            addConnection(a1, b1)
            addConnection(b1, d1)
            addConnection(a2, b2)
            addConnection(b2, d2)
            addConnection(a2, c)
            addConnection(b2, c)
            addConnection(d2, c)
        }.build()
        val loop3 = graph3.findEulerLoop()
        loop3.assert(shouldExist = false, graph = graph3)

        //My graph:
        //
        //       W -- X
        //       |    |
        //  A -- V -- U -- T -- M
        //  |    |    |         |
        //  B -- C -- F -- G -- H ------- L
        //       |    |         |         |
        //       D -- E         I -- J -- K


        val myGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val l = addVertex("L")
            val m = addVertex("M")
            val t = addVertex("T")
            val u = addVertex("U")
            val v = addVertex("V")
            val w = addVertex("W")
            val x = addVertex("X")
            addConnection(a, b)
            addConnection(a, v)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(c, f)
            addConnection(c, v)
            addConnection(d, e)
            addConnection(e, f)
            addConnection(f, u)
            addConnection(f, g)
            addConnection(g, h)
            addConnection(h, i)
            addConnection(h, l)
            addConnection(h, m)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, l)
            addConnection(m, t)
            addConnection(t, u)
            addConnection(u, x)
            addConnection(u, v)
            addConnection(x, w)
            addConnection(w, v)
        }.build()
        val myLoop = myGraph.findEulerLoop()
        myLoop.assert(shouldExist = true, graph = myGraph)
    }

    fun minimumSpanningTree(minimumSpanningTree: Graph.() -> Graph) {
        val emptyGraph = GraphBuilder().build()
        assertTrue(emptyGraph.minimumSpanningTree().edges.isEmpty())
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val tree = graph.minimumSpanningTree()
        assertEquals(2, tree.edges.size)
        assertEquals(2, tree.findBridges().size)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val tree2 = graph2.minimumSpanningTree()
        assertEquals(10, tree2.edges.size)
        assertEquals(10, tree2.findBridges().size)
        // Cross
        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, e)
            addConnection(b, e)
            addConnection(c, e)
            addConnection(d, e)
        }.build()
        val tree3 = graph3.minimumSpanningTree()
        assertEquals(4, tree3.edges.size)
        assertEquals(4, tree3.findBridges().size)
        //My graph:
        //
        //       W -- X
        //       |    |
        //  A -- V -- U -- T -- M
        //  |    |    |         |
        //  B -- C -- F -- G -- H ------- L
        //       |    |         |         |
        //       D -- E         I -- J -- K

        // Expected Minimum Spanning Tree:
        //       W    X
        //       |    |
        //  A -- V -- U -- T -- M
        //  |
        //  B -- C -- F -- G -- H         L
        //       |    |         |         |
        //       D    E         I -- J -- K


        val myGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val l = addVertex("L")
            val m = addVertex("M")
            val t = addVertex("T")
            val u = addVertex("U")
            val v = addVertex("V")
            val w = addVertex("W")
            val x = addVertex("X")
            addConnection(a, b)
            addConnection(a, v)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(c, f)
            addConnection(c, v)
            addConnection(d, e)
            addConnection(e, f)
            addConnection(f, u)
            addConnection(f, g)
            addConnection(g, h)
            addConnection(h, i)
            addConnection(h, l)
            addConnection(h, m)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, l)
            addConnection(m, t)
            addConnection(t, u)
            addConnection(u, x)
            addConnection(u, v)
            addConnection(x, w)
            addConnection(w, v)
        }.build()
        val myTree = myGraph.minimumSpanningTree()
        assertEquals(17, myTree.edges.size)
        assertEquals(17, myTree.findBridges().size)
    }

    fun largestIndependentVertexSet(largestIndependentVertexSet: Graph.() -> Set<Graph.Vertex>) {
        val emptyGraph = GraphBuilder().build()
        assertTrue(emptyGraph.largestIndependentVertexSet().isEmpty())
        val simpleGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            addConnection(a, b)
        }.build()
        assertEquals(
            setOf(simpleGraph["A"]),
            simpleGraph.largestIndependentVertexSet()
        )
        val noEdgeGraph = GraphBuilder().apply {
            addVertex("A")
            addVertex("B")
            addVertex("C")
        }.build()
        assertEquals(
            setOf(noEdgeGraph["A"], noEdgeGraph["B"], noEdgeGraph["C"]),
            noEdgeGraph.largestIndependentVertexSet()
        )
        val unconnected = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, b)
            addConnection(c, d)
            addConnection(d, e)
        }.build()
        assertEquals(
            setOf(unconnected["A"], unconnected["C"], unconnected["E"]),
            unconnected.largestIndependentVertexSet()
        )
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            addConnection(a, b)
            addConnection(a, c)
            addConnection(b, d)
            addConnection(c, e)
            addConnection(c, f)
            addConnection(b, g)
            addConnection(d, i)
            addConnection(g, h)
            addConnection(h, j)
        }.build()
        assertEquals(
            setOf(graph["A"], graph["D"], graph["E"], graph["F"], graph["G"], graph["J"]),
            graph.largestIndependentVertexSet()
        )
        val cross = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, e)
            addConnection(b, e)
            addConnection(c, e)
            addConnection(d, e)
        }.build()
        assertEquals(
            setOf(cross["A"], cross["B"], cross["C"], cross["D"]),
            cross.largestIndependentVertexSet()
        )
        val myGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val o = addVertex("O")
            val p = addVertex("P")
            val q = addVertex("Q")
            val r = addVertex("R")
            val s = addVertex("S")
            val t = addVertex("T")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(b, i)
            addConnection(i, j)
            addConnection(c, f)
            addConnection(c, f)
            addConnection(c, d)
            addConnection(c, h)
            addConnection(f, g)
            addConnection(d, e)
            addConnection(h, k)
            addConnection(k, l)
            addConnection(k, m)
            addConnection(k, n)
            addConnection(l, s)
            addConnection(s, t)
            addConnection(m, q)
            addConnection(m, r)
            addConnection(n, o)
            addConnection(n, p)

        }.build()
        assertEquals(
            setOf(myGraph["A"], myGraph["C"], myGraph["I"], myGraph["G"], myGraph["E"], myGraph["K"],
                  myGraph["S"], myGraph["Q"], myGraph["R"], myGraph["O"], myGraph["P"]),
            myGraph.largestIndependentVertexSet()
        )
    }

    fun longestSimplePath(longestSimplePath: Graph.() -> Path) {
        val emptyGraph = GraphBuilder().build()
        assertEquals(0, emptyGraph.longestSimplePath().length)

        val noEdgeGraph = GraphBuilder().apply {
            addVertex("A")
            addVertex("B")
            addVertex("C")
        }.build()
        val longestNoEdgePath = noEdgeGraph.longestSimplePath()
        assertEquals(0, longestNoEdgePath.length)

        val unconnected = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, b)
            addConnection(c, d)
            addConnection(d, e)
        }.build()
        val longestUnconnectedPath = unconnected.longestSimplePath()
        assertEquals(2, longestUnconnectedPath.length)

        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val longestPath = graph.longestSimplePath()
        assertEquals(2, longestPath.length)

        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val longestPath2 = graph2.longestSimplePath()
        assertEquals(10, longestPath2.length)
        // Seven bridges of Koenigsberg
        //    A1 -- A2 ---
        //    |      |    |
        //    B1 -- B2 -- C
        //    |     |     |
        //    D1 -- D2 ---
        val graph3 = GraphBuilder().apply {
            val a1 = addVertex("A1")
            val a2 = addVertex("A2")
            val b1 = addVertex("B1")
            val b2 = addVertex("B2")
            val c = addVertex("C")
            val d1 = addVertex("D1")
            val d2 = addVertex("D2")
            addConnection(a1, a2)
            addConnection(b1, b2)
            addConnection(d1, d2)
            addConnection(a1, b1)
            addConnection(b1, d1)
            addConnection(a2, b2)
            addConnection(b2, d2)
            addConnection(a2, c)
            addConnection(b2, c)
            addConnection(d2, c)
        }.build()
        val longestPath3 = graph3.longestSimplePath()
        assertEquals(6, longestPath3.length)
    }

    fun baldaSearcher(baldaSearcher: (String, Set<String>) -> Set<String>) {
        assertEquals(
            setOf("ТРАВА", "КРАН", "АКВА", "НАРТЫ"),
            baldaSearcher("input/balda_in1.txt", setOf("ТРАВА", "КРАН", "АКВА", "НАРТЫ", "РАК"))
        )
        assertEquals(
            setOf("БАЛДА"),
            baldaSearcher("input/balda_in2.txt", setOf("БАЛАБОЛ", "БАЛДА", "БАЛДАЗАВР"))
        )
        assertEquals(
            setOf(
                "АПЕЛЬСИН", "МАРОККО", "ПЕРЕМЕНЫ", "ГРАВИТАЦИЯ",
                "РАССУДИТЕЛЬНОСТЬ", "КОНСТАНТИНОПОЛЬ", "ПРОГРАММИРОВАНИЕ", "ПОМЕХОУСТОЙЧИВОСТЬ", "АППРОКСИМАЦИЯ",
                "ЭЙНШТЕЙН"
            ),
            baldaSearcher(
                "input/balda_in3.txt", setOf(
                    "АПЕЛЬСИН", "МАРОККО", "ЭФИОПИЯ", "ПЕРЕМЕНЫ", "ГРАВИТАЦИЯ",
                    "РАССУДИТЕЛЬНОСТЬ", "БЕЗРАССУДНОСТЬ", "КОНСТАНТИНОПОЛЬ", "СТАМБУЛ", "ПРОГРАММИРОВАНИЕ",
                    "ПРОСТРАНСТВО", "ДИАЛЕКТИКА", "КВАЛИФИКАЦИЯ", "ПОМЕХОУСТОЙЧИВОСТЬ", "КОГЕРЕНТНОСТЬ",
                    "АППРОКСИМАЦИЯ", "ИНТЕРПОЛЯЦИЯ", "МАЙЕВТИКА", "ШРЕДИНГЕР", "ЭЙНШТЕЙН"
                )
            )
        )
    }
}