package collection;

import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.collection.SaxQueue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class TestSaxGraphAdvanced {
    static private SaxGraph<String> saxDijkstraGraph;
    static private SaxGraph<String> saxPrimKruskalGraph;

    /**
     * Create the Dijkstra example graph from the Graph algorithms sheets.
     */
    @BeforeAll
    static void createDijkstraGraph() {
        saxDijkstraGraph = new SaxGraph<>();
        saxDijkstraGraph.addEdgeBidirectional("a", "b", 1);
        saxDijkstraGraph.addEdgeBidirectional("a", "c", 4);
        saxDijkstraGraph.addEdgeBidirectional("b", "c", 2);
        saxDijkstraGraph.addEdgeBidirectional("b", "d", 3);
        saxDijkstraGraph.addEdgeBidirectional("b", "e", 10);
        saxDijkstraGraph.addEdgeBidirectional("c", "d", 6);
        saxDijkstraGraph.addEdgeBidirectional("c", "g", 3);
        saxDijkstraGraph.addEdgeBidirectional("d", "e", 5);
        saxDijkstraGraph.addEdgeBidirectional("d", "g", 1);
        saxDijkstraGraph.addEdgeBidirectional("e", "f", 7);
        saxDijkstraGraph.addEdgeBidirectional("e", "g", 2);
        saxDijkstraGraph.addEdgeBidirectional("f", "g", 5);
        System.out.println(saxDijkstraGraph.graphViz("DijkstraGraph"));
    }

    /**
     * Create the Prim and Kruskal example graph from the Graph algorithms sheets.
     */
    @BeforeAll
    static void createPrimKruskalGraph() {
        saxPrimKruskalGraph = new SaxGraph<>();
        saxPrimKruskalGraph.addEdgeBidirectional("a", "b", 4);
        saxPrimKruskalGraph.addEdgeBidirectional("a", "h", 8);
        saxPrimKruskalGraph.addEdgeBidirectional("b", "c", 8);
        saxPrimKruskalGraph.addEdgeBidirectional("b", "h", 11);
        saxPrimKruskalGraph.addEdgeBidirectional("c", "d", 7);
        saxPrimKruskalGraph.addEdgeBidirectional("c", "f", 4);
        saxPrimKruskalGraph.addEdgeBidirectional("f", "e", 10);
        saxPrimKruskalGraph.addEdgeBidirectional("d", "e", 9);
        saxPrimKruskalGraph.addEdgeBidirectional("d", "f", 14);
        saxPrimKruskalGraph.addEdgeBidirectional("g", "f", 2);
        saxPrimKruskalGraph.addEdgeBidirectional("h", "g", 1);
        saxPrimKruskalGraph.addEdgeBidirectional("h", "i", 7);
        saxPrimKruskalGraph.addEdgeBidirectional("i", "c", 2);
        saxPrimKruskalGraph.addEdgeBidirectional("i", "g", 6);
        System.out.println(saxPrimKruskalGraph.graphViz("PrimKruskalGraph"));
    }

    @Test
    void WhenSolvingMCST_ConfirmCorrectResults() {
        var mcst = saxPrimKruskalGraph.minimumCostSpanningTree();
        System.out.println(mcst.graphViz("PrimKruskalSolved"));

        double expected = 37.0;
        double actual = mcst.getTotalWeight();
        System.out.println("Expected total weight: " + expected + " | Actual total weight: " + actual);

        assertEquals(expected, actual, 0.0001, "MCST total weight should be 37.0");
    }

    @Test
    void testMinimumCostSpanningTreeCorrectWeight() {
        SaxGraph<String> mcst = saxPrimKruskalGraph.minimumCostSpanningTree();
        System.out.println(mcst.graphViz("PrimKruskalSolved")); // for visual check

        double expected = 37.0;
        double actual = mcst.getTotalWeight();
        System.out.println("Expected total weight: " + expected + " | Actual total weight: " + actual);

        assertEquals(expected, actual, 0.0001, "MCST should have total weight 37.0");
        System.out.println(" testMinimumCostSpanningTreeCorrectWeight passed");
    }

    @Test
    void testMinimumCostSpanningTreeNotEmpty() {
        SaxGraph<String> mcst = saxPrimKruskalGraph.minimumCostSpanningTree();

        boolean notNull = mcst != null;
        int actualSize = mcst != null ? mcst.getSize() : -1;
        System.out.println("Expected: non-null tree | Actual: " + notNull);
        System.out.println("Expected: size > 0 | Actual size: " + actualSize);

        assertNotNull(mcst);
        assertTrue(actualSize > 0, "MCST result should not be empty");
        System.out.println(" testMinimumCostSpanningTreeNotEmpty passed");
    }

    @Test
    void testShortestPathsDijkstraContainsStartNode() {
        SaxGraph<String> result = saxDijkstraGraph.shortestPathsDijkstra("a");

        boolean expected = true;
        boolean actual = result.contains("a");
        System.out.println("Expected: graph contains 'a' → " + expected + " | Actual: " + actual);

        assertTrue(actual, "Resulting graph should contain start node 'a'");
        System.out.println(" testShortestPathsDijkstraContainsStartNode passed");
    }

    @Test
    void testShortestPathsDijkstraIncludesAllReachableNodes() {
        SaxGraph<String> result = saxDijkstraGraph.shortestPathsDijkstra("a");

        int expected = saxDijkstraGraph.getSize();
        int actual = result.getSize();
        System.out.println("Expected reachable node count: " + expected + " | Actual: " + actual);

        assertEquals(expected, actual, "Dijkstra result should cover all nodes");
        System.out.println(" testShortestPathsDijkstraIncludesAllReachableNodes passed");
    }

    @Test
    void testShortestPathsDijkstraNullThrows() {
        System.out.println("Expected: IllegalArgumentException when passing null start node");
        assertThrows(IllegalArgumentException.class,
                () -> saxDijkstraGraph.shortestPathsDijkstra(null),
                "Dijkstra should throw on null input");
        System.out.println("testShortestPathsDijkstraNullThrows passed");
    }

    @Test
    void testAllEdgesAreExtractedCorrectly() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("a", "b", 1);
        g.addEdgeBidirectional("b", "c", 2);

        SaxList<SaxGraph<String>.DirectedEdge> allEdges = g.getEdges();
        int expected = 4;
        int actual = allEdges.getSize();
        System.out.println("Expected edge count: " + expected + " | Actual: " + actual);

        assertEquals(expected, actual);
        System.out.println("testAllEdgesAreExtractedCorrectly passed");
    }

    @Test
    void testDirectedEdgeToStringContainsExpectedFormat() {
        var edge = saxDijkstraGraph.new DirectedEdge("a", "b", 1);
        String str = edge.toString();

        System.out.println("Expected: 'a' and 'b' present in string | Actual toString(): " + str);
        assertTrue(str.contains("a") && str.contains("b"));
        System.out.println("testDirectedEdgeToStringContainsExpectedFormat passed");
    }

    @Test
    void testGetEdgesOfUnknownNodeReturnsEmpty() {
        SaxList<SaxGraph<String>.DirectedEdge> edges = saxDijkstraGraph.getEdges("ZZZ");
        int expected = 0;
        int actual = edges.getSize();
        System.out.println("Expected edge count: " + expected + " | Actual: " + actual);

        assertNotNull(edges, "Returned edges list should not be null");
        assertEquals(expected, actual, "There should be no edges for unknown node");
        System.out.println("testGetEdgesOfUnknownNodeReturnsEmpty passed");
    }

    @Test
    void testGraphVizPrimKruskalExample() {
        String dot = saxPrimKruskalGraph.graphViz("PrimKruskalGraphTest");
        System.out.println("Expected: DOT string containing '->' | Actual output:\n" + dot);
        assertNotNull(dot);
        assertTrue(dot.contains("->"));
        System.out.println("testGraphVizPrimKruskalExample passed");
    }

    @Test
    void testDepthFirstTraversalThrowsOnNull() {
        System.out.println("Expected: IllegalArgumentException for null input in DFS traversal");
        assertThrows(IllegalArgumentException.class, () -> saxDijkstraGraph.depthFirstTraversal(null));
        System.out.println("testDepthFirstTraversalThrowsOnNull passed");
    }

    @Test
    void testDepthFirstSearchThrowsOnNullEndNode() {
        System.out.println("Expected: IllegalArgumentException for null end node in DFS search");
        assertThrows(IllegalArgumentException.class, () -> saxDijkstraGraph.depthFirstSearch("a", null));
        System.out.println("testDepthFirstSearchThrowsOnNullEndNode passed");
    }

    @Test
    void testRoundTripEmptyInputReturnsEmptyQueue() {
        SaxList<String> emptyList = new SaxList<>();
        var result = saxDijkstraGraph.roundTrip(emptyList);
        int expected = 0;
        int actual = result.getSize();
        System.out.println("Expected queue size: " + expected + " | Actual: " + actual);

        assertNotNull(result);
        assertEquals(expected, actual);
        System.out.println("testRoundTripEmptyInputReturnsEmptyQueue passed");
    }

    @Test
    void testShortestPathsDijkstraHandlesUnreachableNode() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("X", "Y", 1);
        g.addNode("Z");
        SaxGraph<String> result = g.shortestPathsDijkstra("X");
        boolean containsZ = result.contains("Z");
        System.out.println("Expected: true (should contain unreachable node 'Z') | Actual: " + containsZ);

        assertTrue(containsZ);
        System.out.println("testShortestPathsDijkstraHandlesUnreachableNode passed");
    }

    @Test
    void testShortestPathAStarReturnsEmptyIfNoPath() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("X", "Y", 1);
        g.addNode("Z");
        SaxGraph.Estimator<String> est = (a, b) -> 1.0;
        var path = g.shortestPathAStar("X", "Z", est);
        int expected = 0;
        int actual = path.getSize();
        System.out.println("Expected path size: " + expected + " | Actual: " + actual);

        assertNotNull(path);
        assertEquals(expected, actual);
        System.out.println("testShortestPathAStarReturnsEmptyIfNoPath passed");
    }

    @Test
    void testGetEdgesReturnsEmptyForIsolatedNode() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addNode("Isolated");
        SaxList<?> edges = g.getEdges("Isolated");
        int expected = 0;
        int actual = edges.getSize();
        System.out.println("Expected edges: " + expected + " | Actual: " + actual);

        assertNotNull(edges);
        assertEquals(expected, actual);
        System.out.println("testGetEdgesReturnsEmptyForIsolatedNode passed");
    }

    @Test
    void testIteratorThrowsExceptionIfOverrun() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addNode("Only");
        Iterator<String> it = g.iterator();
        it.next();
        System.out.println("Expected: IndexOutOfBoundsException on next() after last element");
        assertThrows(IndexOutOfBoundsException.class, it::next);
        System.out.println("testIteratorThrowsExceptionIfOverrun passed");
    }

    @Test
    void testGraphVizOutputForAdvancedGraph() {
        String dot = saxDijkstraGraph.graphViz("GraphVizAdvanced");
        System.out.println("Expected: DOT output containing '->' | Actual output:\n" + dot);
        assertNotNull(dot);
        assertTrue(dot.contains("->"));
        System.out.println("testGraphVizOutputForAdvancedGraph passed");
    }

    @Test
    void testDepthFirstTraversalReturnsCorrectEdges() {
        SaxGraph<String> graph = new SaxGraph<>();
        graph.addEdgeBidirectional("A", "B", 1);
        graph.addEdgeBidirectional("A", "C", 1);
        graph.addEdgeBidirectional("C", "D", 1);

        SaxList<SaxGraph<String>.DirectedEdge> edges = graph.depthFirstTraversal("A");
        int expected = 3;
        int actual = edges.getSize();
        System.out.println("Expected edges: " + expected + " | Actual: " + actual);

        assertEquals(expected, actual);
        System.out.println("testDepthFirstTraversalReturnsCorrectEdges passed");
    }

    @Test
    void testDepthFirstSearchFindsPath() {
        SaxGraph<String> graph = new SaxGraph<>();
        graph.addEdgeBidirectional("A", "B", 1);
        graph.addEdgeBidirectional("B", "C", 1);

        SaxList<SaxGraph<String>.DirectedEdge> path = graph.depthFirstSearch("A", "C");
        int expectedSize = 2;
        int actualSize = path.getSize();
        System.out.println("Expected path size: " + expectedSize + " | Actual: " + actualSize);
        System.out.println("Path: " + path);

        assertEquals(expectedSize, actualSize);
        assertEquals("B", path.getElementAt(0).to());
        assertEquals("C", path.getElementAt(1).to());
        System.out.println("testDepthFirstSearchFindsPath passed");
    }

    @Test
    void testDepthFirstSearchReturnsEmptyWhenNoPath() {
        SaxGraph<String> graph = new SaxGraph<>();
        graph.addEdgeBidirectional("A", "B", 1);
        graph.addNode("C");

        SaxList<SaxGraph<String>.DirectedEdge> path = graph.depthFirstSearch("A", "C");
        int expected = 0;
        int actual = path.getSize();
        System.out.println("Expected path size: " + expected + " | Actual: " + actual);

        assertNotNull(path);
        assertEquals(expected, actual);
        System.out.println("testDepthFirstSearchReturnsEmptyWhenNoPath passed");
    }

    @Test
    void testAStarFindsPath() {
        SaxGraph<String> graph = new SaxGraph<>();
        graph.addEdgeBidirectional("A", "B", 1);
        graph.addEdgeBidirectional("B", "C", 2);

        SaxGraph.Estimator<String> estimator = (from, to) -> 1.0;
        SaxList<SaxGraph<String>.DirectedEdge> path = graph.shortestPathAStar("A", "C", estimator);

        int expected = 2;
        int actual = path.getSize();
        System.out.println("Expected path edges: " + expected + " | Actual: " + actual);
        System.out.println("Path: " + path);

        assertEquals(expected, actual);
        assertEquals("B", path.getElementAt(0).to());
        assertEquals("C", path.getElementAt(1).to());
        System.out.println("testAStarFindsPath passed");
    }

    @Test
    void testAStarThrowsOnNullStart() {
        SaxGraph<String> graph = new SaxGraph<>();
        SaxGraph.Estimator<String> estimator = (from, to) -> 1.0;
        System.out.println("Expected: IllegalArgumentException when start node is null");
        assertThrows(IllegalArgumentException.class, () -> {
            graph.shortestPathAStar(null, "B", estimator);
        });
        System.out.println("testAStarThrowsOnNullStart passed");
    }

    @Test
    void testAStarNoPathReturnsEmpty() {
        SaxGraph<String> graph = new SaxGraph<>();
        graph.addEdgeBidirectional("A", "B", 1);
        graph.addNode("C");

        SaxGraph.Estimator<String> estimator = (from, to) -> 1.0;
        SaxList<SaxGraph<String>.DirectedEdge> path = graph.shortestPathAStar("A", "C", estimator);

        int expected = 0;
        int actual = path.getSize();
        System.out.println("Expected path size: " + expected + " | Actual: " + actual);

        assertEquals(expected, actual);
        System.out.println("testAStarNoPathReturnsEmpty passed");
    }

    @Test
    void testRoundTripWithNoPathPrintsMessage() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("A", "B", 1);
        g.addNode("C");

        SaxList<String> trip = new SaxList<>();
        trip.addElement("A");
        trip.addElement("C");

        SaxQueue<SaxList<SaxGraph<String>.DirectedEdge>> result = g.roundTrip(trip);

        int expectedQueue = 1;
        int actualQueue = result.getSize();
        System.out.println("Expected queue size: " + expectedQueue + " | Actual: " + actualQueue);
        int expectedPath = 0;
        int actualPath = result.peek().getSize();
        System.out.println("Expected path size: " + expectedPath + " | Actual: " + actualPath);

        assertEquals(expectedQueue, actualQueue);
        assertEquals(expectedPath, actualPath);
        System.out.println("testRoundTripWithNoPathPrintsMessage passed");
    }

    @Test
    void testDepthFirstTraversalReturnsEdges() {
        SaxList<SaxGraph<String>.DirectedEdge> edges = saxDijkstraGraph.depthFirstTraversal("a");
        int actual = edges.getSize();
        System.out.println("Expected > 0 edges | Actual: " + actual);
        assertNotNull(edges);
        assertTrue(actual > 0);
        System.out.println("testDepthFirstTraversalReturnsEdges passed");
    }

    @Test
    void testDepthFirstSearchReturnsCorrectPath() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdge("a", "b", 1);
        g.addEdge("b", "c", 1);
        g.addEdge("c", "d", 1);
        g.addEdge("d", "e", 1);

        SaxList<SaxGraph<String>.DirectedEdge> path = g.depthFirstSearch("a", "e");
        int actual = path.getSize();
        System.out.println("Expected > 0 path length | Actual: " + actual);
        assertNotNull(path);
        assertTrue(actual > 0);
        System.out.println("testDepthFirstSearchReturnsCorrectPath passed");
    }

    @Test
    void testDepthFirstSearchReturnsEmptyOnNoPath() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("X", "Y", 1);
        g.addNode("Z");

        SaxList<SaxGraph<String>.DirectedEdge> result = g.depthFirstSearch("X", "Z");
        int expected = 0;
        int actual = result.getSize();
        System.out.println("Expected path size: " + expected + " | Actual: " + actual);

        assertEquals(expected, actual);
        System.out.println("testDepthFirstSearchReturnsEmptyOnNoPath passed");
    }

    @Test
    void testToStringNotNull() {
        String result = saxDijkstraGraph.toString();
        System.out.println("Expected: non-null string | Actual: " + result);
        assertNotNull(result);
        System.out.println("testToStringNotNull passed");
    }

    @Test
    void testClearEmptiesGraph() {
        saxDijkstraGraph.clear();
        int expected = 0;
        int actual = saxDijkstraGraph.getSize();
        System.out.println("Expected size: " + expected + " | Actual: " + actual);
        assertEquals(expected, actual);
        System.out.println("testClearEmptiesGraph passed");
    }

    @Test
    void testDirectedEdgeCompareWorks() {
        var e1 = saxDijkstraGraph.new DirectedEdge("a", "b", 1.0);
        var e2 = saxDijkstraGraph.new DirectedEdge("a", "c", 3.0);
        int compare1 = e1.compare(e2);
        int compare2 = e2.compare(e1);
        System.out.println("Expected e1 < e2 | compare1 = " + compare1);
        System.out.println("Expected e2 > e1 | compare2 = " + compare2);
        assertTrue(compare1 < 0);
        assertTrue(compare2 > 0);
        System.out.println("testDirectedEdgeCompareWorks passed");
    }

}

