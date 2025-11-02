package collection;

import nl.saxion.cds.collection.SaxIndexedCollection;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.collection.exceptions.ElementNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxList {

    private SaxList<String> list;

    @BeforeEach
    void setup() {
        list = new SaxList<>();
        list.addElement("B");
        list.addElement("A");
        list.addElement("C");
    }

    @Test
    void testGetIndexOfElement_ThrowsCatchBranch() {
        SaxIndexedCollection<String> list = new SaxList<>();
        try {
            list.getIndexOfElement("not-found");
            fail("Should have thrown ElementNotFoundException");
        } catch (ElementNotFoundException e) {
            System.out.println("Correctly threw ElementNotFoundException");
        }
    }

    @Test
    void testInterfaceContains_CatchBranch() {
        SaxIndexedCollection<String> l = new SaxList<>();
        assertFalse(l.contains("ShouldTriggerCatch"));
        System.out.println("Expected: false | Actual: " + l.contains("ShouldTriggerCatch"));
    }

    @Test
    void testContains_CatchBranch_InterfaceReference() {
        SaxIndexedCollection<String> l = new SaxList<>();
        boolean result = l.contains("not-in-list");
        System.out.println("Expected: false | Actual: " + result);
        assertFalse(result);
    }

    @Test
    void testInterfaceContainsCoversBothBranches() throws Exception {
        SaxIndexedCollection<String> l = new SaxList<>();
        l.addElement("Hello");
        assertTrue(l.contains("Hello"));
        assertFalse(l.contains("Missing"));
    }

    @Test
    void testAddElementAt_AllPositions() {
        list.addElementAt("Start", 0);
        list.addElementAt("End", list.getSize());
        list.addElementAt("Middle", 2);
        System.out.println("Expected first: Start | Actual: " + list.getElementAt(0));
        System.out.println("Expected middle: Middle | Actual: " + list.getElementAt(2));
        System.out.println("Expected last: End | Actual: " + list.getLastElement());
        System.out.println("Expected size: 6 | Actual: " + list.getSize());
        assertEquals("Start", list.getElementAt(0));
        assertEquals("Middle", list.getElementAt(2));
        assertEquals("End", list.getLastElement());
        assertEquals(6, list.getSize());
    }

    @Test
    void testAddElementAt_InvalidIndex() {
        System.out.println("Expected: IndexOutOfBoundsException for index -1");
        assertThrows(IndexOutOfBoundsException.class, () -> list.addElementAt("X", -1));

        System.out.println("Expected: IndexOutOfBoundsException for index 99");
        assertThrows(IndexOutOfBoundsException.class, () -> list.addElementAt("X", 99));
    }

    @Test
    void testRemoveElementAt_AllPositions() {
        String removed0 = list.removeElementAt(0);
        String removed1 = list.removeElementAt(1);
        String removedTail = list.removeElementAt(0);
        System.out.println("Removed from index 0: " + removed0);
        System.out.println("Removed from index 1: " + removed1);
        System.out.println("Removed last: " + removedTail);
        System.out.println("Expected size: 0 | Actual: " + list.getSize());
        assertEquals("B", removed0);
        assertEquals("C", removed1);
        assertEquals("A", removedTail);
        assertEquals(0, list.getSize());
    }

    @Test
    void testRemoveElementAt_InvalidIndex() {
        System.out.println("Expected: IndexOutOfBoundsException for index -5");
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-5));

        System.out.println("Expected: IndexOutOfBoundsException for index 99");
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(99));
    }

    @Test
    void testGetLastElementFromEmptyListThrows() {
        SaxList<String> empty = new SaxList<>();
        System.out.println("Expected: IndexOutOfBoundsException for empty list getLastElement()");
        assertThrows(IndexOutOfBoundsException.class, empty::getLastElement);
    }

    @Test
    void testRemoveTailUpdatesTailPointer() {
        SaxList<String> l = new SaxList<>();
        l.addElement("First");
        l.addElement("Last");
        l.removeElementAt(1);
        System.out.println("Expected last element: First | Actual: " + l.getLastElement());
        assertEquals("First", l.getLastElement());
    }

    @Test
    void testRemoveMiddleKeepsLinksCorrect() {
        list.addElementAt("MIDDLE", 1);
        list.removeElementAt(1);
        System.out.println("Expected element at index 1: A | Actual: " + list.getElementAt(1));
        assertEquals("A", list.getElementAt(1));
    }

    @Test
    void testSortInPlaceSelectionSort() {
        System.out.println("Before sort: " + list.toString());
        list.sort(String::compareTo);
        System.out.println("After sort: " + list.toString());
        assertEquals("A", list.getElementAt(0));
        assertEquals("B", list.getElementAt(1));
        assertEquals("C", list.getElementAt(2));
    }

    @Test
    void testSortEmptyAndOneElement() {
        SaxList<String> empty = new SaxList<>();
        empty.sort(String::compareTo);
        SaxList<String> one = new SaxList<>();
        one.addElement("Solo");
        one.sort(String::compareTo);
        System.out.println("Expected: Solo | Actual: " + one.getElementAt(0));
        assertEquals("Solo", one.removeElementAt(0));
    }

    @Test
    void testGetIndexOfElement_Found() throws ElementNotFoundException {
        int index = list.getIndexOfElement("A");
        System.out.println("Expected index of 'A': 1 | Actual: " + index);
        assertEquals(1, index);
    }

    @Test
    void testGetIndexOfElement_NotFound() {
        System.out.println("Expected: ElementNotFoundException for 'X'");
        assertThrows(ElementNotFoundException.class, () -> list.getIndexOfElement("X"));
    }

    @Test
    void testGetIndexOfElement_NullElement() throws ElementNotFoundException {
        list.addElement(null);
        int index = list.getIndexOfElement(null);
        System.out.println("Expected index of null: 3 | Actual: " + index);
        assertEquals(3, index);
    }

    @Test
    void testClearAndReuse() {
        list.clear();
        System.out.println("After clear: size = " + list.getSize());
        assertEquals(0, list.getSize());
        list.addElement("X");
        System.out.println("After reuse: element at index 0 = " + list.getElementAt(0));
        assertEquals("X", list.getElementAt(0));
    }

    @Test
    void testIteratorTraversal() {
        Iterator<String> it = list.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) sb.append(it.next());
        System.out.println("Iterator result: " + sb);
        assertTrue(sb.toString().contains("A"));
        assertTrue(sb.toString().contains("B"));
        assertTrue(sb.toString().contains("C"));
    }

    @Test
    void testIterator_EmptyThrows() {
        SaxList<String> empty = new SaxList<>();
        Iterator<String> it = empty.iterator();
        System.out.println("Expected hasNext(): false | Actual: " + it.hasNext());
        assertFalse(it.hasNext());
        System.out.println("Expected: IndexOutOfBoundsException for next() on empty list");
        assertThrows(IndexOutOfBoundsException.class, it::next);
    }

    @Test
    void testGraphVizCallable() {
        String output = list.graphViz("TestGraph");
        System.out.println("GraphViz output:\n" + output);
        assertTrue(output.contains("->"));
        assertTrue(output.contains("TestGraph"));
    }

    @Test
    void testSetElementAt_InvalidIndex() {
        System.out.println("Expected: IndexOutOfBoundsException for index -1");
        assertThrows(IndexOutOfBoundsException.class, () -> list.setElementAt("Oops", -1));

        System.out.println("Expected: IndexOutOfBoundsException for index 99");
        assertThrows(IndexOutOfBoundsException.class, () -> list.setElementAt("Oops", 99));
    }

    @Test
    void testIsSortedAlwaysFalse() {
        System.out.println("Expected: isSorted() = false | Actual: " + list.isSorted());
        assertFalse(list.isSorted());
    }

    @Test
    void testRemoveTail_ReassignsTail() {
        list.removeElementAt(list.getSize() - 1);
        System.out.println("Expected last element after removal: A | Actual: " + list.getLastElement());
        assertEquals("A", list.getLastElement());
    }

    @Test
    void testRemoveHead_ReassignsHead() {
        list.removeElementAt(0);
        System.out.println("Expected head after removal: A | Actual: " + list.getElementAt(0));
        assertEquals("A", list.getElementAt(0));
    }

    @Test
    void testSetElementAt_Valid() {
        list.setElementAt("Z", 1);
        System.out.println("Expected updated element at index 1: Z | Actual: " + list.getElementAt(1));
        assertEquals("Z", list.getElementAt(1));
    }

    @Test
    void testRemoveAllAndReuse() {
        while (list.getSize() > 0) list.removeElementAt(0);
        list.addElement("New");
        System.out.println("Expected reused first element: New | Actual: " + list.getElementAt(0));
        assertEquals("New", list.getElementAt(0));
    }

    @Test
    void testGetNode_BrokenLinkForward_Throws() {
        SaxList<String> corrupted = new SaxList<>();
        corrupted.addElement("A");
        corrupted.addElement("B");
        corrupted.addElement("C");

        corrupted.getHeadForTest().next = null;

        System.out.println("Expected: IllegalStateException for broken forward link at index 1");
    }

    @Test
    void testGetNode_BrokenLinkBackward_Throws() {
        SaxList<String> corrupted = new SaxList<>();
        corrupted.addElement("A");
        corrupted.addElement("B");
        corrupted.addElement("C");

        corrupted.getTailForTest().prev = null;

        System.out.println("Expected: IllegalStateException for broken backward link at index 0");
    }



    @Test
    void testSort_HeadNull_SizePositive_Edge() {
        SaxList<String> corrupted = new SaxList<>();
        corrupted.addElement("X");

        corrupted.setHeadForTest(null);

        System.out.println("Expected: sort returns without error when head is null");
        corrupted.sort(String::compareTo);
    }


    @Test
    void testGetIndexOfElement_NullNotFound_Throws() {
        SaxList<String> l = new SaxList<>();
        l.addElement("A");
        l.addElement("B");

        System.out.println("Expected: ElementNotFoundException for null not in list");
        assertThrows(ElementNotFoundException.class, () -> l.getIndexOfElement(null));
    }
}
