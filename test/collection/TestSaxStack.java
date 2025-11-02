package collection;

import nl.saxion.cds.collection.SaxStack;
import nl.saxion.cds.collection.exceptions.EmptyCollectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxStack {

    private SaxStack<Integer> stack;

    @BeforeEach
    void setup() {
        stack = new SaxStack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
    }

    @Test
    void testPushAndPeek_ShouldReturnTopElement() throws EmptyCollectionException {
        System.out.println("\nSaxStack Push and Peek Test ");
        System.out.println("Stack content after pushes: " + stack.toString());

        assertEquals(3, stack.getSize(),
                "Expected stack size 3 after three pushes, but got " + stack.getSize());

        int peeked = stack.peek();
        System.out.println("Expected top: 30 | Actual top: " + peeked);
        assertEquals(30, peeked,
                "Expected top element 30, but got " + peeked);

        // size should remain same after peek
        assertEquals(3, stack.getSize(),
                "Peek should not remove the top element");
    }

    @Test
    void testPop_ShouldRemoveAndReturnTop() throws EmptyCollectionException {
        System.out.println("\nSaxStack Pop Test ");
        int popped = stack.pop();
        System.out.println("Expected popped: 30 | Actual: " + popped);

        assertEquals(30, popped,
                "Expected popped value 30, but got " + popped);
        assertEquals(2, stack.getSize(),
                "Expected stack size 2 after one pop, but got " + stack.getSize());
        assertEquals(20, stack.peek(),
                "Expected new top element 20 after pop");
    }

    @Test
    void testClear_ShouldEmptyStack() {
        System.out.println("\n SaxStack Clear Test");
        stack.clear();
        assertEquals(0, stack.getSize(),
                "Expected stack size 0 after clear()");
        stack.push(99);
        assertEquals(99, stack.peek(),
                "Expected to push new element 99 after clearing");
    }

    @Test
    void testIterator_TraversesFromTopToBottom() {
        System.out.println("\nSaxStack Iterator Traversal");
        Iterator<Integer> it = stack.iterator();
        int count = 0;
        while (it.hasNext()) {
            Integer value = it.next();
            System.out.println("Iterated value: " + value);
            assertNotNull(value, "Iterator returned null element");
            count++;
        }
        assertEquals(stack.getSize(), count,
                "Iterator should traverse all " + stack.getSize() + " elements");
    }

    @Test
    void testGraphViz_CalledForVisualVerification() {
        System.out.println("\nSaxStack GraphViz Output");
        String dot = stack.graphViz("StackGraph");
        System.out.println(dot);
        assertTrue(dot.contains("digraph StackGraph"),
                "Expected GraphViz output to contain the graph name 'StackGraph'");
    }

    @Test
    void testPopOnEmptyStack_ThrowsException() {
        System.out.println("\n SaxStack Pop Exception Test");
        SaxStack<Integer> empty = new SaxStack<>();
        assertThrows(EmptyCollectionException.class, empty::pop,
                "Expected EmptyCollectionException when popping from empty stack");
    }

    @Test
    void testPeekOnEmptyStack_ThrowsException() {
        System.out.println("\n SaxStack Peek Exception Test");
        SaxStack<Integer> empty = new SaxStack<>();
        assertThrows(EmptyCollectionException.class, empty::peek,
                "Expected EmptyCollectionException when peeking empty stack");
    }

    @Test
    void testMultiplePushPopSequence() throws EmptyCollectionException {
        System.out.println("\nSaxStack Multiple Operations Test");
        SaxStack<Integer> test = new SaxStack<>();
        test.push(1);
        test.push(2);
        test.push(3);
        System.out.println("After pushes: " + test.toString());
        assertEquals(3, test.peek(), "Expected top 3 after pushes");
        assertEquals(3, test.pop(), "Expected to pop 3");
        assertEquals(2, test.pop(), "Expected to pop 2");
        test.push(99);
        System.out.println("After pushing 99: " + test.toString());
        assertEquals(99, test.peek(), "Expected top 99 after new push");
        assertEquals(2, test.getSize(), "Expected stack size 2 after sequence");
    }
}
