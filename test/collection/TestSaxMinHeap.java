package collection;

import nl.saxion.cds.collection.SaxHeap;
import nl.saxion.cds.collection.exceptions.EmptyCollectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxMinHeap {

    private SaxHeap<Integer> heap;

    @BeforeEach
    void createSampleHeap() {
        heap = new SaxHeap<>(Integer::compareTo);
        int[] values = {13, 21, 16, 19, 31, 24, 68, 65, 26, 32};
        for (int v : values) heap.push(v);
    }

    @Test
    void GivenSampleHeap_ConfirmCorrectContent() throws EmptyCollectionException {
        System.out.println("\nMinHeap Initial Content Test");
        System.out.println("Heap toString(): " + heap.toString());
        System.out.println("Heap GraphViz:\n" + heap.graphViz("SaxHeap"));

        System.out.println("Expected size: 10 | Actual: " + heap.getSize());
        assertEquals(10, heap.getSize(),
                "Expected heap size 10 but got " + heap.getSize());

        System.out.println("Expected peek: 13 | Actual: " + heap.peek());
        assertEquals(13, heap.peek(),
                "Expected smallest element 13 but got " + heap.peek());

        String expectedStr = "[13, 19, 16, 21, 31, 24, 68, 65, 26, 32]";
        System.out.println("Expected toString(): " + expectedStr + " | Actual: " + heap.toString());
        assertEquals(expectedStr, heap.toString(),
                "Heap internal structure not matching expected order.");
    }

    @Test
    void GivenSampleHeap_WhenEmptying_ConfirmCorrectSequenceOfValues() throws EmptyCollectionException {
        System.out.println("\nMinHeap Pop Order Test");
        int[] expected = {13, 16, 19, 21, 24, 26, 31, 32, 65, 68};

        for (int exp : expected) {
            int actual = heap.pop();
            System.out.printf("Expected: %-3d | Actual: %-3d%n", exp, actual);
            assertEquals(exp, actual,
                    "Expected next smallest element " + exp + " but got " + actual);
        }

        System.out.println("Expected size after emptying: 0 | Actual: " + heap.getSize());
        assertEquals(0, heap.getSize(), "Expected empty heap after all pops.");

        System.out.println("Expecting EmptyCollectionException on pop()");
        assertThrows(EmptyCollectionException.class, heap::pop,
                "Expected EmptyCollectionException when popping empty heap.");
    }

    @Test
    void testIterator_Traversal() {
        System.out.println("\nMinHeap Iterator Traversal");
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
    void testPopAndPeekOnEmptyHeap_ThrowsException() {
        System.out.println("\nMinHeap Empty Exception Test");
        SaxHeap<Integer> empty = new SaxHeap<>(Integer::compareTo);

        System.out.println("Expecting EmptyCollectionException on peek()");
        assertThrows(EmptyCollectionException.class, empty::peek,
                "Expected EmptyCollectionException when peeking empty heap.");

        System.out.println("Expecting EmptyCollectionException on pop()");
        assertThrows(EmptyCollectionException.class, empty::pop,
                "Expected EmptyCollectionException when popping empty heap.");
    }
}
