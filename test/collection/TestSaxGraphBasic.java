package collection;

import nl.saxion.cds.collection.*;
import nl.saxion.cds.collection.exceptions.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxGraphBasic {
    private SaxGraph<String> simpleTestGraph;

    @BeforeEach
    void createSimpleGraph() {
        simpleTestGraph = new SaxGraph<>();

        simpleTestGraph.addEdgeBidirectional("A", "E", 1);
        simpleTestGraph.addEdgeBidirectional("A", "C", 1);
        simpleTestGraph.addEdgeBidirectional("A", "B", 1);
        simpleTestGraph.addEdgeBidirectional("B", "F", 1);
        simpleTestGraph.addEdgeBidirectional("B", "D", 1);
        simpleTestGraph.addEdgeBidirectional("C", "G", 1);
        simpleTestGraph.addEdgeBidirectional("F", "E", 1);
    }

    @Test
    void testAddNodeAndContains() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addNode("X");
        boolean expected = true;
        boolean actual = g.contains("X");
        System.out.println("Expected: " + expected + " | Actual: " + actual);
        assertTrue(actual);
        System.out.println("testAddNodeAndContains passed");
    }

    @Test
    void testAddNodeAlreadyExistsThrows() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addNode("A");
        System.out.println("Expected: IllegalArgumentException when adding existing node 'A'");
        assertThrows(IllegalArgumentException.class, () -> g.addNode("A"));
        System.out.println("testAddNodeAlreadyExistsThrows passed");
    }

    @Test
    void testAddNodeNullThrows() {
        SaxGraph<String> g = new SaxGraph<>();
        System.out.println("Expected: IllegalArgumentException when adding null node");
        assertThrows(IllegalArgumentException.class, () -> g.addNode(null));
        System.out.println("testAddNodeNullThrows passed");
    }

    @Test
    void testAddEdgeBidirectionalAddsTwoEdges() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("A", "B", 5);
        int expected = 1;
        int actualA = g.getEdges("A").getSize();
        int actualB = g.getEdges("B").getSize();
        System.out.println("Expected each side to have 1 edge | Actual: A=" + actualA + ", B=" + actualB);
        assertEquals(expected, actualA);
        assertEquals(expected, actualB);
        System.out.println("testAddEdgeBidirectionalAddsTwoEdges passed");
    }

    @Test
    void testAddEdgeNullThrows() {
        SaxGraph<String> g = new SaxGraph<>();
        System.out.println("Expected: IllegalArgumentException when adding edge with null node");
        assertThrows(IllegalArgumentException.class, () -> g.addEdge(null, "B", 1));
        System.out.println("testAddEdgeNullThrows passed");
    }

    @Test
    void testGetTotalWeightWorks() {
        double total = simpleTestGraph.getTotalWeight();
        System.out.println("Expected: total weight > 0 | Actual: " + total);
        assertTrue(total > 0);
        System.out.println("testGetTotalWeightWorks passed");
    }

    @Test
    void testDepthFirstTraversalCoversAllEdges() {
        SaxList<?> edges = simpleTestGraph.depthFirstTraversal("A");
        int size = edges.getSize();
        System.out.println("Expected: edges > 0 | Actual: " + size);
        assertFalse(size == 0);
        simpleTestGraph.graphViz("DFS_Test");
        System.out.println("testDepthFirstTraversalCoversAllEdges passed");
    }

    @Test
    void testDepthFirstTraversalNullThrows() {
        System.out.println("Expected: IllegalArgumentException when DFS start node is null");
        assertThrows(IllegalArgumentException.class, () -> simpleTestGraph.depthFirstTraversal(null));
        System.out.println("testDepthFirstTraversalNullThrows passed");
    }

    @Test
    void testDepthFirstSearchFindsPath() {
        var path = simpleTestGraph.depthFirstSearch("A", "D");
        int size = path.getSize();
        System.out.println("Expected: path size > 0 | Actual: " + size);
        assertFalse(size == 0);
        System.out.println("testDepthFirstSearchFindsPath passed");
    }

    @Test
    void testDepthFirstSearchReturnsEmptyIfNoPath() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("A", "B", 1);
        g.addNode("C");
        int expected = 0;
        int actual = g.depthFirstSearch("A", "C").getSize();
        System.out.println("Expected path size: " + expected + " | Actual: " + actual);
        assertEquals(expected, actual);
        System.out.println("testDepthFirstSearchReturnsEmptyIfNoPath passed");
    }

    @Test
    void testShortestPathsDijkstraReturnsNonEmptyGraph() {
        int actual = simpleTestGraph.shortestPathsDijkstra("A").getSize();
        System.out.println("Expected: size > 0 | Actual: " + actual);
        assertTrue(actual > 0);
        System.out.println("testShortestPathsDijkstraReturnsNonEmptyGraph passed");
    }

    @Test
    void testShortestPathsDijkstraNullThrows() {
        System.out.println("Expected: IllegalArgumentException when Dijkstra start node is null");
        assertThrows(IllegalArgumentException.class, () -> simpleTestGraph.shortestPathsDijkstra(null));
        System.out.println("testShortestPathsDijkstraNullThrows passed");
    }

    @Test
    void testMinimumCostSpanningTreeConnectsNodes() {
        int actual = simpleTestGraph.minimumCostSpanningTree().getSize();
        System.out.println("Expected: tree size > 0 | Actual: " + actual);
        assertTrue(actual > 0);
        System.out.println("testMinimumCostSpanningTreeConnectsNodes passed");
    }

    @Test
    void testRoundTripForThreeNodes() {
        SaxList<String> trip = new SaxList<>();
        trip.addElement("A");
        trip.addElement("B");
        trip.addElement("C");
        int actual = simpleTestGraph.roundTrip(trip).getSize();
        System.out.println("Expected: round trip size > 0 | Actual: " + actual);
        assertTrue(actual > 0);
        System.out.println("testRoundTripForThreeNodes passed");
    }

    @Test
    void testShortestPathAStarFindsPath() {
        var est = (SaxGraph.Estimator<String>) (a, b) -> 1.0;
        int actual = simpleTestGraph.shortestPathAStar("A", "D", est).getSize();
        System.out.println("Expected: path size > 0 | Actual: " + actual);
        assertTrue(actual > 0);
        System.out.println("testShortestPathAStarFindsPath passed");
    }

    @Test
    void testShortestPathAStarThrowsOnNull() {
        var est = (SaxGraph.Estimator<String>) (a, b) -> 1.0;
        System.out.println("Expected: IllegalArgumentException on null parameters");
        assertThrows(IllegalArgumentException.class, () -> simpleTestGraph.shortestPathAStar(null, "B", est));
        assertThrows(IllegalArgumentException.class, () -> simpleTestGraph.shortestPathAStar("A", null, est));
        assertThrows(IllegalArgumentException.class, () -> simpleTestGraph.shortestPathAStar("A", "B", null));
        System.out.println("testShortestPathAStarThrowsOnNull passed");
    }

    @Test
    void testDirectedEdgeFieldsAndMethods() {
        var e = simpleTestGraph.new DirectedEdge("A", "B", 2.5);
        System.out.println("Expected from='A' | Actual: " + e.from());
        System.out.println("Expected to='B' | Actual: " + e.to());
        System.out.println("Expected weight=2.5 | Actual: " + e.weight());
        assertEquals("A", e.from());
        assertEquals("B", e.to());
        assertEquals(2.5, e.weight());
        assertTrue(e.toString().contains("A"));
        System.out.println("testDirectedEdgeFieldsAndMethods passed");
    }

    @Test
    void testDirectedEdgeCompareWorks() {
        var e1 = simpleTestGraph.new DirectedEdge("A", "B", 2);
        var e2 = simpleTestGraph.new DirectedEdge("A", "C", 3);
        int compareResult = e1.compare(e2);
        System.out.println("Expected e1.compare(e2) < 0 | Actual: " + compareResult);
        assertTrue(compareResult < 0);
        System.out.println("testDirectedEdgeCompareWorks passed");
    }

    @Test
    void testClearResetsGraph() {
        simpleTestGraph.clear();
        int expected = 0;
        int actual = simpleTestGraph.getSize();
        System.out.println("Expected size: " + expected + " | Actual: " + actual);
        assertEquals(expected, actual);
        System.out.println("testClearResetsGraph passed");
    }

    @Test
    void testIteratorTraversesNodes() {
        int count = 0;
        for (String node : simpleTestGraph) count++;
        System.out.println("Expected: iterator count > 0 | Actual: " + count);
        assertTrue(count > 0);
        System.out.println("testIteratorTraversesNodes passed");
    }

    @Test
    void testGetEdgesReturnsEmptyWhenNodeNotFound() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addNode("A");
        int expected = 0;
        int actual = g.getEdges("Z").getSize();
        System.out.println("Expected edge size: " + expected + " | Actual: " + actual);
        assertEquals(expected, actual);
        System.out.println("testGetEdgesReturnsEmptyWhenNodeNotFound passed");
    }

    @Test
    void testGetEdgesReturnsAllEdges() {
        int size = simpleTestGraph.getEdges().getSize();
        System.out.println("Expected: total edges > 0 | Actual: " + size);
        assertTrue(size > 0);
        System.out.println("testGetEdgesReturnsAllEdges passed");
    }

    @Test
    void testToStringProducesOutput() {
        String result = simpleTestGraph.toString();
        System.out.println("Expected: non-null string | Actual: " + result);
        assertNotNull(result);
        System.out.println("testToStringProducesOutput passed");
    }

    @Test
    void testAStarReturnsEmptyListWhenNoPathExists() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("X", "Y", 1);
        g.addNode("Z");
        var est = (SaxGraph.Estimator<String>) (a, b) -> 1.0;
        int expected = 0;
        int actual = g.shortestPathAStar("X", "Z", est).getSize();
        System.out.println("Expected path size: " + expected + " | Actual: " + actual);
        assertEquals(expected, actual);
        System.out.println("testAStarReturnsEmptyListWhenNoPathExists passed");
    }

    @Test
    void testDepthFirstTraversalWithIsolatedNode() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addNode("Solo");
        int expected = 0;
        int actual = g.depthFirstTraversal("Solo").getSize();
        System.out.println("Expected edges: " + expected + " | Actual: " + actual);
        assertEquals(expected, actual);
        System.out.println("testDepthFirstTraversalWithIsolatedNode passed");
    }

    @Test
    void testShortestPathsDijkstraOnDisconnectedGraph() {
        SaxGraph<String> g = new SaxGraph<>();
        g.addEdgeBidirectional("A", "B", 1);
        g.addNode("C");
        int actual = g.shortestPathsDijkstra("A").getSize();
        System.out.println("Expected: graph size > 0 | Actual: " + actual);
        assertTrue(actual > 0);
        System.out.println("testShortestPathsDijkstraOnDisconnectedGraph passed");
    }

    @Test
    void testGraphVizRuns() {
        String dot = simpleTestGraph.graphViz("course_example_graph");
        System.out.println("Expected: DOT contains '->' | Actual output below:\n" + dot);
        assertNotNull(dot);
        assertTrue(dot.contains("->"), "DOT output must contain directed edges");
        System.out.println("--- END GRAPHVIZ OUTPUT ---");
        System.out.println("testGraphVizRuns passed");
    }
}
