package collection;

import nl.saxion.cds.collection.SaxArrayList;
import nl.saxion.cds.collection.exceptions.ElementNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxArrayList {
    // Make sure a lot of resizing has to be done
    private static final int BIG_NUMBER_OF_ELEMENTS = 5000;
    private SaxArrayList<String> list;

    @BeforeEach
    void createExampleList() {
        // Example from the Linear Data Structures and Hashing sheets
        list = new SaxArrayList<>();
        list.addElement("2");
        list.addElement("23");
        list.addElement("a");
        list.addElement("dd");
        list.addElement("7a");
    }
    @Test
    void testBinarySearchFoundAndNotFound() throws ElementNotFoundException {
        SaxArrayList<Integer> sorted = new SaxArrayList<>();
        sorted.addElement(1);
        sorted.addElement(2);
        sorted.addElement(3);
        sorted.sort(Integer::compareTo);

        assertEquals(0, sorted.getIndexOfElement(1));
        assertEquals(1, sorted.getIndexOfElement(2));
        assertThrows(ElementNotFoundException.class, () -> sorted.getIndexOfElement(99));
    }

    @Test
    void testIteratorRemoveDirectly() {
        SaxArrayList<String> list = new SaxArrayList<>();
        list.addElement("first");
        list.addElement("second");

        var it = list.iterator();
        it.next();
        it.remove();
        assertEquals(1, list.getSize());
        assertEquals("second", list.getElementAt(0));
    }

    @Test
    void GivenEmptyList_WhenCallingGetters_ConfirmListIsActuallyEmpty() {
        SaxArrayList<Object> saxArrayList = new SaxArrayList<>();
        assertEquals(0, saxArrayList.getSize());
        assertEquals("[]", saxArrayList.toString());
        assertThrows(IndexOutOfBoundsException.class, saxArrayList::getLastElement);
    }

    @Test
    void GivenSheetsList_WhenNoChanges_ConfirmInitialContent() {
        assertEquals(5, list.getSize());
        assertEquals("[2, 23, a, dd, 7a]", list.toString());
        assertEquals("7a", list.getLastElement());

        // Testing GraphViz can best be done manually (copy past to https://dreampuf.github.io/GraphvizOnline)
        System.out.println(list.graphViz());
    }

    @Test
    void GivenEmptyList_WhenAccessingInvalidIndex_ThenIndexOutOfBoundsThrown() {
        SaxArrayList<Object> myEmptyList = new SaxArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.getElementAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.getElementAt(1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.addElementAt(666, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.addElementAt(666, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.setElementAt(666, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.setElementAt(666, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.removeElementAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.removeElementAt(myEmptyList.getSize() - 1));
    }

    @Test
    void GivenSheetsList_WhenCleared_ConfirmEmptyList() {
        list.clear();
        assertEquals(0, list.getSize());
        assertEquals("[]", list.toString());
    }

    @Test
    void GivenSheetsList_WhenCallingContains_ConfirmCorrectResponses() {
        // Test edge cases in contains()
        assertTrue(list.contains("2"));
        assertTrue(list.contains("7a"));
        assertFalse(list.contains("huh?"));
    }

    @Test
    void GivenSheetsList_WhenAddingAtBeginning_ConfirmChangesAreCorrect() {
        // Insert at front
        list.addElementAt("b3", 0);
        assertEquals(6, list.getSize());
        assertEquals("[b3, 2, 23, a, dd, 7a]", list.toString());
        assertFalse(list.contains("huh?"));
    }

    @Test
    void GivenSheetsList_WhenAddingAtIndex_ConfirmChangesAreCorrect() {
        // Insert before
        list.addElementAt("b3", 4);
        assertEquals(6, list.getSize());
        assertEquals("[2, 23, a, dd, b3, 7a]", list.toString());
    }

    @Test
    void GivenSheetsList_WhenSettingAtIndex_ConfirmChangesAreCorrect() {
        // Set begin
        list.setElementAt("b1", 0);
        assertEquals(5, list.getSize());
        assertEquals("[b1, 23, a, dd, 7a]", list.toString());
        // Set midst
        list.setElementAt("b2", 3);
        assertEquals(5, list.getSize());
        assertEquals("[b1, 23, a, b2, 7a]", list.toString());
        // Set last
        list.setElementAt("b3", 4);
        assertEquals(5, list.getSize());
        assertEquals("[b1, 23, a, b2, b3]", list.toString());
    }

    @Test
    void GivenSheetsList_WhenRemovingElement_ConfirmChangesAreCorrect() {
        // Remove specific element
        list.removeElementAt(list.getIndexOfElement("dd"));
        assertEquals("[2, 23, a, 7a]", list.toString());
        assertEquals(4, list.getSize());
    }

    @Test
    void GivenSheetsList_WhenRemovingAllElement_ConfirmChangesAreCorrect() {
        // Edge cases remove
        list.removeElementAt(list.getIndexOfElement("7a"));
        list.removeElementAt(list.getIndexOfElement("2"));
        assertEquals("[23, a, dd]", list.toString());
        assertEquals(3, list.getSize());
        // Further empty the list.
        assertEquals("23", list.removeElementAt(0));
        assertEquals("dd", list.removeElementAt(list.getSize() - 1));
        assertEquals("a", list.removeElementAt(list.getSize() - 1));
        // Confirm emptiness of the list.
        assertEquals("[]", list.toString());
        assertEquals(0, list.getSize());
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(0));
    }

    @Test
    void GivenSheetsList_WhenAccessingIndexOutOfBounds_ThenExceptionIsThrown() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(list.getSize()));
    }

    @Test
    void GivenSheetsList_WhenAddingNullValues_ThenListContainsNullValues() {
        list.addElementAt(null, 0); // check addAt with index equals first element
        list.addElementAt(null, 5); // check addAt with index equals last element
        list.addElementAt(null, 7); // check addAt with index just after last element
        assertEquals("[NULL, 2, 23, a, dd, NULL, 7a, NULL]", list.toString());
        assertEquals(8, list.getSize());
        assertFalse(isSortedStringList(list));

        // Testing GraphViz can best be done manually (copy past to https://dreampuf.github.io/GraphvizOnline)
        System.out.println(list.graphViz());

        // Remove specific element
        list.removeElementAt(4); // dd
        list.removeElementAt(0); // first null
        list.removeElementAt(5); // last null
        list.removeElementAt(3); // remaining null
        // Remove element using the iterator.
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (element.compareTo("a") == 0) {
                iterator.remove();
            }
        }
        assertEquals(3, list.getSize());
        assertEquals("[2, 23, 7a]", list.toString());

        // Confirm exception is thrown when no more null values are present.
        assertFalse(list.contains(null));
    }

    public boolean isSortedStringList(SaxArrayList<String> list) {
        String previous = list.getElementAt(0);
        for (var element : list) {
            if (element == null || previous.compareTo(element) > 0) {
                return false; // previous is bigger than element; not sorted ascending
            }
            previous = element;
        }

        return true;
    }

    @Test
    void GivenListWithIntegers_WhenSorted_ThenListIsSorted() {
        SaxArrayList<Integer> list3 = createIntegerArrayList();
        assertFalse(list3.isSorted());
        list3.sort(Integer::compareTo);
        System.out.println(list3);
        assertTrue(list3.isSorted());
        assertTrue(isSortedIntegerList(list3));
    }

    SaxArrayList<Integer> createIntegerArrayList() {
        SaxArrayList<Integer> list = new SaxArrayList<>();
        list.addElement(1);
        list.addElement(8);
        list.addElement(18);
        list.addElement(5);
        list.addElement(14);
        list.addElement(4);
        list.addElement(15);
        list.addElement(12);
        list.addElement(6);
        list.addElement(19);
        list.addElement(2);
        list.addElement(11);
        list.addElement(10);
        list.addElement(10); // deliberate adding a duplicate element
        list.addElement(7);
        list.addElement(9);
        list.addElement(3);
        list.addElement(13);
        list.addElement(17);
        list.addElement(16);
        list.addElement(20);
        System.out.println(list);
        return list;
    }

    public boolean isSortedIntegerList(SaxArrayList<Integer> list) {
        Integer previous = list.getElementAt(0);
        for (var element : list) {
            if (element == null || previous.compareTo(element) > 0) {
                return false; // previous is bigger than element; not sorted ascending
            }
            previous = element;
        }

        return true;
    }

    @Test
    void GivenLargeList_WhenSortingAndMakingChanges_ConfirmStateRemainsCorrect() {
        SaxArrayList<Integer> list = new SaxArrayList<>();
        for (int i = 0; i < BIG_NUMBER_OF_ELEMENTS; ++i) {
            list.addElement(i);
        }
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list.getSize());


        // Create a list of random integers to test with sort()
        var random = new Random();
        SaxArrayList<Integer> list3 = new SaxArrayList<>();
        for (int i = 0; i < BIG_NUMBER_OF_ELEMENTS; ++i) {
            list3.addElement(random.nextInt(0, BIG_NUMBER_OF_ELEMENTS));
        }
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list3.getSize());
        assertFalse(list3.isSorted());
        list3.sort(Integer::compareTo);
        assertTrue(list3.isSorted());
        assertTrue(isSortedIntegerList(list3));

        // Test search
        for (int i = 0; i < BIG_NUMBER_OF_ELEMENTS; ++i) {
            int v = list3.getElementAt(i);
            int index = list3.getIndexOfElement(v);
            assertEquals(v, list3.getElementAt(index));
        }
        assertThrows(ElementNotFoundException.class, () -> list3.getIndexOfElement(BIG_NUMBER_OF_ELEMENTS + 1));

        // Test removing all elements one by one
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list.getSize());
        for(int i = list.getSize(); i > 0; --i ) {
            int index = random.nextInt(i);
            int value = list.getElementAt(index);
            assertEquals(value, list.removeElementAt(index));
        }
        assertEquals(0, list.getSize());
    }
}
