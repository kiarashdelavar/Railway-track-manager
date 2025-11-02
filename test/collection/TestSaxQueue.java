package collection;

import nl.saxion.cds.collection.SaxQueue;
import nl.saxion.cds.collection.exceptions.EmptyCollectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestSaxQueue {

    private SaxQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new SaxQueue<>();
    }

    @Test
    void testPushIncreasesSize() {
        System.out.println("[PushIncreasesSize] Initial size: " + queue.getSize());
        assertEquals(0, queue.getSize());
        queue.push(10);
        System.out.println("After push(10): " + queue.getSize());
        assertEquals(1, queue.getSize());
        queue.push(20);
        System.out.println("After push(20): " + queue.getSize());
        assertEquals(2, queue.getSize());
    }

    @Test
    void testPushAndPopOrder() throws EmptyCollectionException {
        queue.push(1);
        queue.push(2);
        queue.push(3);
        int first = queue.pop();
        int second = queue.pop();
        int third = queue.pop();
        System.out.println("[PushAndPopOrder] Expected: 1 2 3 | Actual: " + first + " " + second + " " + third);
        assertEquals(1, first);
        assertEquals(2, second);
        assertEquals(3, third);
    }

    @Test
    void testPeekReturnsFirstWithoutRemoving() throws EmptyCollectionException {
        queue.push(5);
        queue.push(6);
        int peek1 = queue.peek();
        int peek2 = queue.peek();
        int size = queue.getSize();
        System.out.println("[PeekTest] Expected: peek=5, peek again=5, size=2 | Actual: peek=" + peek1 + ", again=" + peek2 + ", size=" + size);
        assertEquals(5, peek1);
        assertEquals(5, peek2);
        assertEquals(2, size);
    }

    @Test
    void testClearRemovesAllElements() {
        queue.push(1);
        queue.push(2);
        queue.clear();
        System.out.println("[ClearTest] After clear(), size: " + queue.getSize());
        assertEquals(0, queue.getSize());
        System.out.println("Expecting EmptyCollectionException on peek and pop after clear()");
        assertThrows(EmptyCollectionException.class, () -> queue.peek());
        assertThrows(EmptyCollectionException.class, () -> queue.pop());
    }

    @Test
    void testIteratorWorksCorrectly() {
        queue.push(100);
        queue.push(200);
        queue.push(300);

        Iterator<Integer> it = queue.iterator();
        System.out.println("[IteratorTest] Values:");
        assertTrue(it.hasNext());
        System.out.println("Next: " + it.next());
        assertEquals(200, it.next());
        System.out.println("Next: 200");
        assertEquals(300, it.next());
        System.out.println("Next: 300");
        assertFalse(it.hasNext());
    }

    @Test
    void testPopOnEmptyThrowsException() {
        System.out.println("[PopOnEmpty] Expecting exception...");
        assertThrows(EmptyCollectionException.class, () -> queue.pop());
    }

    @Test
    void testPeekOnEmptyThrowsException() {
        System.out.println("[PeekOnEmpty] Expecting exception...");
        assertThrows(EmptyCollectionException.class, () -> queue.peek());
    }

    @Test
    void testIteratorOnEmptyQueue() {
        Iterator<Integer> it = queue.iterator();
        System.out.println("[EmptyIterator] hasNext(): " + it.hasNext());
        assertFalse(it.hasNext());
        System.out.println("Expecting NoSuchElementException when calling next()");
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testGraphVizCourseExample() {
        queue.push(1);
        queue.push(2);
        queue.push(3);

        String dot = queue.graphViz("CourseQueue");
        System.out.println("[GraphVizCourseExample]\n" + dot);
        assertTrue(dot.contains("digraph CourseQueue"));
        assertTrue(dot.contains("\"1\" -> \"2\""));
        assertTrue(dot.contains("\"2\" -> \"3\""));
        assertTrue(dot.endsWith("}"));
    }

    @Test
    void testGraphVizCallDoesNotCrash() {
        queue.push(1);
        queue.push(2);
        String dot = queue.graphViz("TestQueue");
        System.out.println("[GraphVizCallDoesNotCrash]\n" + dot);
        assertTrue(dot.contains("TestQueue"));
    }

    @Test
    void testPushNullAccepted() throws EmptyCollectionException {
        queue.push(null);
        System.out.println("[PushNull] Peek = " + queue.peek());
        assertNull(queue.peek());
        assertNull(queue.pop());
    }

    @Test
    void testToStringOnEmptyQueue() {
        System.out.println("[ToStringEmpty] Expected: [] | Actual: " + queue.toString());
        assertEquals("[]", queue.toString());
    }

    @Test
    void testToStringWithElements() {
        queue.push(1);
        queue.push(2);
        queue.push(3);
        String actual = queue.toString();
        System.out.println("[ToString] Expected: [1, 2, 3] | Actual: " + actual);
        assertEquals("[1, 2, 3]", actual);
    }

    @Test
    void testQueueWithStringsAndNullValue() throws EmptyCollectionException {
        SaxQueue<String> stringQueue = new SaxQueue<>();
        stringQueue.push("Hello");
        stringQueue.push(null);
        stringQueue.push("World");

        String first = stringQueue.pop();
        String second = stringQueue.pop();
        String third = stringQueue.pop();

        System.out.println("[QueueWithStringsAndNulls] Expected: Hello null World | Actual: " + first + " " + second + " " + third);

        assertEquals("Hello", first);
        assertNull(second, "The second element popped should be null.");
        assertEquals("World", third);
    }

    @Test
    void testClearOnAlreadyEmptyQueue() {
        System.out.println("[ClearEmpty] Size before clear: " + queue.getSize());
        assertEquals(0, queue.getSize());
        assertDoesNotThrow(() -> queue.clear());
        assertEquals(0, queue.getSize());
        assertEquals("[]", queue.toString());
    }
}
