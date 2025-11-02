package collection;

import nl.saxion.cds.collection.SaxHashMap;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.collection.exceptions.DuplicateKeyException;
import nl.saxion.cds.collection.exceptions.KeyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestSaxHashMap {

    private SaxHashMap<Integer, String> testMap;

    @BeforeEach
    void setup() {
        testMap = new SaxHashMap<>(11);
        testMap.put(44, "forty-four");
        testMap.put(15, "fifteen");
        testMap.put(28, "twenty-eight");
        testMap.put(41, "forty-one");
        testMap.put(19, "nineteen");
        testMap.put(32, "thirty-two");
        testMap.put(54, "fifty-four");
    }

    @Test
    void testContainsKey_ExistingAndNonExisting() {
        System.out.println("Expected: true | Actual: " + testMap.containsKey(15));
        assertTrue(testMap.containsKey(15));

        System.out.println("Expected: false | Actual: " + testMap.containsKey(99));
        assertFalse(testMap.containsKey(99));
    }

    @Test
    void testGet_ValidKey_ReturnsCorrectValue() {
        String actual = testMap.get(15);
        System.out.println("Expected: 'fifteen' | Actual: '" + actual + "'");
        assertEquals("fifteen", actual);
    }

    @Test
    void testPut_NewKey_AddsSuccessfully() {
        testMap.put(66, "sixty-six");
        System.out.println("Expected: 'sixty-six' | Actual: '" + testMap.get(66) + "'");
        assertEquals("sixty-six", testMap.get(66));
        System.out.println("Expected containsKey(66): true | Actual: " + testMap.containsKey(66));
        assertTrue(testMap.containsKey(66));
    }

    @Test
    void testRemove_ExistingKey_ReturnsCorrectValue() {
        String removedValue = testMap.remove(15);
        System.out.println("Expected removed: 'fifteen' | Actual: '" + removedValue + "'");
        assertEquals("fifteen", removedValue);
        System.out.println("Expected containsKey(15): false | Actual: " + testMap.containsKey(15));
        assertFalse(testMap.containsKey(15));
    }

    @Test
    void testGetKeys_ReturnsAllInsertedKeys() {
        SaxList<Integer> keys = testMap.getKeys();
        System.out.println("Expected size: 7 | Actual: " + keys.getSize());
        assertEquals(7, keys.getSize());
        System.out.println("Expected keys to contain 44: " + keys);
        assertTrue(keys.toString().contains("44"));
    }

    @Test
    void testIterator_IteratesAllValues() {
        Iterator<String> it = testMap.iterator();
        int count = 0;
        while (it.hasNext()) {
            String value = it.next();
            System.out.println("Iterated value: " + value);
            assertNotNull(value);
            count++;
        }
        System.out.println("Expected iteration count: 7 | Actual: " + count);
        assertEquals(7, count);
    }

    @Test
    void testClear_RemovesAllElements() {
        testMap.clear();
        System.out.println("After clear: containsKey(44) = " + testMap.containsKey(44));
        assertFalse(testMap.containsKey(44));
        System.out.println("Expecting KeyNotFoundException for get(44) after clear()");
        assertThrows(KeyNotFoundException.class, () -> testMap.get(44));
    }

    @Test
    void testToString_ReturnsReadableOutput() {
        String result = testMap.toString();
        System.out.println("ToString output: " + result);
        assertTrue(result.contains("forty-four") || result.contains("fifteen"));
    }

    @Test
    void testPut_DuplicateKey_ThrowsException() {
        System.out.println("Expecting DuplicateKeyException for key 44");
        DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> testMap.put(44, "duplicate"));
        System.out.println("Exception message: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("44"));
    }

    @Test
    void testGet_NonExistingKey_ThrowsException() {
        System.out.println("Expecting KeyNotFoundException for key 99");
        KeyNotFoundException ex = assertThrows(KeyNotFoundException.class, () -> testMap.get(99));
        System.out.println("Exception message: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void testRemove_NonExistingKey_ThrowsException() {
        System.out.println("Expecting KeyNotFoundException for key 999");
        KeyNotFoundException ex = assertThrows(KeyNotFoundException.class, () -> testMap.remove(999));
        System.out.println("Exception message: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void testConstructor_WithInvalidCapacity_DefaultsTo16() {
        SaxHashMap<Integer, String> map = new SaxHashMap<>(0);
        map.put(1, "one");
        System.out.println("Expected: 'one' | Actual: '" + map.get(1) + "'");
        assertEquals("one", map.get(1));
    }

    @Test
    void testIterator_EmptyMap_NoElements() {
        SaxHashMap<Integer, String> empty = new SaxHashMap<>();
        Iterator<String> it = empty.iterator();
        System.out.println("Expected iterator.hasNext(): false | Actual: " + it.hasNext());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_MultipleBucketsDifferentSizes() {
        SaxHashMap<Integer, String> map = new SaxHashMap<>(3);
        map.put(1, "one");
        map.put(4, "four");
        map.put(2, "two");

        Iterator<String> it = map.iterator();
        int count = 0;
        while (it.hasNext()) {
            System.out.println("Iterated value: " + it.next());
            count++;
        }
        System.out.println("Expected count: 3 | Actual: " + count);
        assertEquals(3, count);
    }

    @Test
    void testRemove_FromBucketWithMultipleEntries() {
        SaxHashMap<Integer, String> map = new SaxHashMap<>(3);
        map.put(1, "one");
        map.put(4, "four");
        map.put(7, "seven");

        String removed = map.remove(4);
        System.out.println("Expected removed: 'four' | Actual: '" + removed + "'");
        assertEquals("four", removed);

        System.out.println("Expecting KeyNotFoundException for removed key 4");
        assertThrows(KeyNotFoundException.class, () -> map.get(4));
    }

    @Test
    void testDefaultConstructor_WorksAndStoresElements() {
        SaxHashMap<Integer, String> map = new SaxHashMap<>();
        map.put(100, "hundred");
        System.out.println("Expected containsKey(100): true | Actual: " + map.containsKey(100));
        assertTrue(map.containsKey(100));
    }

    @Test
    void courseExample_GraphVizVisualVerification() {
        SaxHashMap<Integer, String> exampleMap = new SaxHashMap<>(5);
        exampleMap.put(1, "one");
        exampleMap.put(2, "two");
        exampleMap.put(7, "seven");

        String dot = exampleMap.graphViz("SaxHashMapExample");
        System.out.println("\nCDS GraphViz Output for SaxHashMap");
        System.out.println(dot);

        assertTrue(dot.contains("digraph SaxHashMapExample"));
    }
}
