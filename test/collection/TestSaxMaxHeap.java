package collection;

import nl.saxion.cds.collection.SaxHeap;
import nl.saxion.cds.collection.exceptions.EmptyCollectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxMaxHeap {

    private SaxHeap<Integer> heap;

    @BeforeEach
    void createSampleHeap() {
        heap = new SaxHeap<Integer>(Comparator.reverseOrder());
        int[] values = {13, 21, 16, 19, 31, 24, 68, 65, 26, 32};
        for (int v : values) {
            heap.push(v);
        }
    }

    @Test
    void testPeekAndInitialStructure() throws EmptyCollectionException {
        System.out.println("\n MaxHeap Initial Content Test ");
        System.out.println("Heap toString(): " + heap.toString());
        System.out.println("Heap GraphViz:\n" + heap.graphViz("SaxMaxHeap"));

        System.out.println("Expected peek: 68 | Actual: " + heap.peek());
        assertEquals(68, heap.peek(),
                "Expected max element 68 at root but got " + heap.peek());

        System.out.println("Expected size: 10 | Actual: " + heap.getSize());
        assertEquals(10, heap.getSize(),
                "Expected size 10 but got " + heap.getSize());
    }

    @Test
    void testPopRemovesInDescendingOrder() throws EmptyCollectionException {
        System.out.println("\n MaxHeap Pop Order Test ");
        int[] expected = {68, 65, 32, 31, 26, 24, 21, 19, 16, 13};

        for (int exp : expected) {
            int actual = heap.pop();
            System.out.printf("Expected: %-3d | Actual: %-3d%n", exp, actual);
            assertEquals(exp, actual,
                    "Expected next largest element " + exp + " but got " + actual);
        }

        System.out.println("Expected size after emptying: 0 | Actual: " + heap.getSize());
        assertEquals(0, heap.getSize(), "Expected empty heap after all pops.");

        System.out.println("Expecting EmptyCollectionException on pop()");
        assertThrows(EmptyCollectionException.class, heap::pop,
                "Expected EmptyCollectionException when popping from empty heap.");
    }

    @Test
    void testPushMaintainsMaxProperty() throws EmptyCollectionException {
        System.out.println("\nMaxHeap Push Test ");
        heap.push(99);
        System.out.println("Expected peek after push: 99 | Actual: " + heap.peek());
        assertEquals(99, heap.peek(),
                "Expected new max element 99 at root but got " + heap.peek());

        System.out.println("Expected size after push: 11 | Actual: " + heap.getSize());
        assertEquals(11, heap.getSize(),
                "Expected size 11 after pushing 99 but got " + heap.getSize());
    }

    @Test
    void testIterator_Traversal() {
        System.out.println("\nMaxHeap Iterator Traversal ");
        Iterator<Integer> it = heap.iterator();
        int count = 0;
        while (it.hasNext()) {
            Integer val = it.next();
            System.out.println("Iterated value: " + val);
            count++;
        }
        System.out.println("Expected iterator count: " + heap.getSize() + " | Actual: " + count);
        assertEquals(heap.getSize(), count,
                "Iterator should traverse " + heap.getSize() + " elements.");
    }

    @Test
    void testGraphViz_CalledForVisualVerification() {
        System.out.println("\nGraphViz Output for MaxHeap");
        String dot = heap.graphViz("MaxHeapGraph");
        System.out.println(dot);
        System.out.println("Checking if GraphViz contains 'digraph MaxHeapGraph'");
        assertTrue(dot.contains("digraph MaxHeapGraph"),
                "Expected GraphViz output to contain graph name 'MaxHeapGraph'");
    }

    @Test
    void testPopOnEmptyHeapThrowsException() {
        System.out.println("\nMaxHeap Empty Pop Exception Test");
        SaxHeap<Integer> empty = new SaxHeap<Integer>(Comparator.reverseOrder());
        System.out.println("Expecting EmptyCollectionException on pop()");
        assertThrows(EmptyCollectionException.class, empty::pop,
                "Expected EmptyCollectionException when popping from empty heap.");
    }

    @Test
    void testPeekOnEmptyHeapThrowsException() {
        System.out.println("\nMaxHeap Empty Peek Exception Test");
        SaxHeap<Integer> empty = new SaxHeap<Integer>(Comparator.reverseOrder());
        System.out.println("Expecting EmptyCollectionException on peek()");
        assertThrows(EmptyCollectionException.class, empty::peek,
                "Expected EmptyCollectionException when peeking from empty heap.");
    }
}
