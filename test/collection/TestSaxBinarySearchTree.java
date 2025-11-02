package collection;

import nl.saxion.cds.collection.SaxBinarySearchTree;
import nl.saxion.cds.collection.TreeNode;
import nl.saxion.cds.collection.exceptions.ElementNotFoundException;
import nl.saxion.cds.collection.exceptions.NullNotAllowedException;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxBinarySearchTree {

    @Test
    void testAddAndContains() throws NullNotAllowedException {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(20); t.add(10); t.add(30);

        System.out.println("Expect true: contains 10 → " + t.contains(10));
        assertTrue(t.contains(10));
        System.out.println("Expect true: contains 20 → " + t.contains(20));
        assertTrue(t.contains(20));
        System.out.println("Expect true: contains 30 → " + t.contains(30));
        assertTrue(t.contains(30));
        System.out.println("Expect false: contains 40 → " + t.contains(40));
        assertFalse(t.contains(40));
    }

    @Test
    void testAddNullThrows() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        System.out.println("Expect exception when adding null");
        assertThrows(NullNotAllowedException.class, () -> t.add(null));
    }

    @Test
    void testRemoveLeafNode() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(20); t.add(10); t.add(30);
        t.remove(10);
        System.out.println("Expect false: contains 10 → " + t.contains(10));
        assertFalse(t.contains(10));
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveNodeWithOneChild() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(20); t.add(10); t.add(5);
        t.remove(10);
        System.out.println("Expect false: contains 10 → " + t.contains(10));
        assertFalse(t.contains(10));
        System.out.println("Expect true: contains 5 → " + t.contains(5));
        assertTrue(t.contains(5));
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveNodeWithTwoChildren() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(50); t.add(30); t.add(70); t.add(60); t.add(80);
        t.remove(70);
        System.out.println("Expect false: contains 70 → " + t.contains(70));
        assertFalse(t.contains(70));
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveNullThrows() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        System.out.println("Expect exception when removing null");
        assertThrows(NullNotAllowedException.class, () -> t.remove(null));
    }

    @Test
    void testRemoveNonExistingThrows() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        System.out.println("Expect exception when removing non-existing value (100)");
        assertThrows(ElementNotFoundException.class, () -> t.remove(100));
    }

    @Test
    void testLLRotationBalanceZero() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(30); t.add(20); t.add(10); t.add(25);
        t.remove(25);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRRRotationBalanceZero() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(10); t.add(20); t.add(30); t.add(15);
        t.remove(15);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testLeftRightRotation() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(30); t.add(10); t.add(20);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRightLeftRotation() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(10); t.add(30); t.add(20);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testClearTree() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(5); t.add(10);
        t.clear();
        System.out.println("Expected: 0, Actual: " + t.getSize());
        assertEquals(0, t.getSize());
        System.out.println("Expect false: contains 5 → " + t.contains(5));
        assertFalse(t.contains(5));
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testGetSize() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(1); t.add(2);
        System.out.println("Expected: 2, Actual: " + t.getSize());
        assertEquals(2, t.getSize());
        t.remove(2);
        System.out.println("Expected: 1, Actual: " + t.getSize());
        assertEquals(1, t.getSize());
    }

    @Test
    void testTreeHeight() throws Exception {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        System.out.println("Expected: -1 (empty), Actual: " + t.getHeight());
        assertEquals(-1, t.getHeight());
        t.add(5); t.add(3); t.add(7);
        System.out.println("Expected: 1, Actual: " + t.getHeight());
        assertEquals(1, t.getHeight());
    }

    @Test
    void testIsBalancedEmpty() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveTriggersLeftLeftRotation() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(50); t.add(40); t.add(30);
        t.remove(30);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveTriggersLeftRightRotation() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(50); t.add(30); t.add(40);
        t.remove(40);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveTriggersRightRightRotation() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(10); t.add(20); t.add(30);
        t.remove(10);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testRemoveTriggersRightLeftRotation() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(10); t.add(30); t.add(20);
        t.remove(10);
        System.out.println("Expect true: isBalanced → " + t.isBalanced());
        assertTrue(t.isBalanced());
    }

    @Test
    void testIteratorInOrderTraversal() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(10); t.add(5); t.add(15);

        Iterator<Integer> it = t.iterator();
        List<Integer> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        System.out.println("Expected: [5, 10, 15], Actual: " + result);
        assertEquals(List.of(5, 10, 15), result);
    }

    @Test
    void testGraphVizOutput() {
        SaxBinarySearchTree<Integer> t = new SaxBinarySearchTree<>(Integer::compareTo);
        t.add(10); t.add(5); t.add(15);
        String dot = t.graphViz("myTree");
        System.out.println("GraphViz output:\n" + dot);
        assertNotNull(dot);
        assertTrue(dot.contains("digraph"));
    }
}
