package collection;

import nl.saxion.cds.collection.SaxHashSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSaxHashSet {

    @Test
    void testAddElement_NewValue_TriggersPut() {
        SaxHashSet<String> set = new SaxHashSet<>();
        set.add("X");
        assertTrue(set.contains("X"));
        System.out.println("Expected to contain X: " + set.contains("X"));
    }

    @Test
    void testAddElement_Duplicate_SkipsPut() {
        SaxHashSet<String> set = new SaxHashSet<>();
        set.add("Y");
        set.add("Y");
        assertTrue(set.contains("Y"));
        assertEquals(1, set.getSize());
        System.out.println("Expected size: 1 | Actual: " + set.getSize());
    }

    @Test
    void testClearEmptiesTheSet() {
        SaxHashSet<String> set = new SaxHashSet<>();
        set.add("A");
        set.add("B");
        set.clear(); // Call clear()

        assertEquals(0, set.getSize());
        System.out.println("After clear: size = " + set.getSize());
    }

    @Test
    void testToStringShowsContent() {
        SaxHashSet<String> set = new SaxHashSet<>();
        set.add("One");
        String str = set.toString();
        System.out.println("Set as string: " + str);
        assertTrue(str.contains("true"));
    }
    @Test
    void testIteratorReturnsAllKeys() {
        SaxHashSet<String> set = new SaxHashSet<>();
        set.add("X");
        set.add("Y");

        int count = 0;
        for (String s : set) {
            System.out.println("Iterated value: " + s);
            assertTrue(s.equals("X") || s.equals("Y"));
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testGraphVizCalled() {
        SaxHashSet<String> set = new SaxHashSet<>();
        set.add("Dot");
        String graph = set.graphViz("TestGraphVizSet");
        System.out.println("GraphViz Output:\n" + graph);
        assertTrue(graph.contains("TestGraphVizSet")); // For manual visual verification
    }


}
