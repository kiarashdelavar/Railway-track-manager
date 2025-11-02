package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.DuplicateKeyException;
import nl.saxion.cds.collection.exceptions.KeyNotFoundException;

import java.util.Iterator;

/**
 * Simple hash map implementation using separate chaining with {@link SaxList}.
 * <p>
 * Each bucket contains a linked list of {@code Entry<K, T>} elements to handle collisions.
 * This implementation supports insertion, removal, lookup and iteration over values.
 *
 * @param <K> the type of keys maintained by this map
 * @param <T> the type of mapped values
 */
public class SaxHashMap<K, T> implements SaxCollection<T> {
    private static final int DEFAULT_CAPACITY = 16;
    private SaxList<Entry<K, T>>[] buckets;
    private int size;

    /**
     * Constructs a new hash map with the default capacity (16 buckets).
     */
    @SuppressWarnings("unchecked")
    public SaxHashMap() {
        buckets = new SaxList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            buckets[i] = new SaxList<>();
        }
    }

    /**
     * Constructs a new hash map with a specified number of buckets.
     *
     * @param capacity initial number of buckets; if less than or equal to 0,
     *                 a default capacity of 16 will be used
     */
    @SuppressWarnings("unchecked")
    public SaxHashMap(int capacity) {
        if (capacity <= 0) capacity = DEFAULT_CAPACITY;
        buckets = new SaxList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new SaxList<>();
        }
    }

    /**
     * Computes the index in the bucket array for the given key.
     *
     * @param key the key
     * @return index in the buckets array
     */
    private int getIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    /**
     * Returns the number of key-value pairs stored in this hash map.
     *
     * @return the current size
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Removes all mappings from this hash map.
     * After this call, {@link #getSize()} will return 0.
     */
    @Override
    public void clear() {
        for (SaxList<Entry<K, T>> bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }

    /**
     * Returns a GraphViz representation of the hash map (excluded from test coverage).
     *
     * @param name name of the graph
     * @return GraphViz DOT string representing the structure
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder("digraph ").append(name).append(" {\n");
        sb.append("  node [shape=record];\n");
        for (int i = 0; i < buckets.length; i++) {
            sb.append("  bucket").append(i).append(" [label=\"{").append(i).append(" | ");
            for (int j = 0; j < buckets[i].getSize(); j++) {
                Entry<K, T> e = buckets[i].getElementAt(j);
                sb.append(e.key).append(" → ").append(e.value);
                if (j < buckets[i].getSize() - 1) sb.append(" | ");
            }
            sb.append("}\"];\n");
        }
        sb.append("}\n");
        return sb.toString();
    }


    /**
     * Returns a string representation of the hash map using {@link SaxCollection#defaultToString()}.
     *
     * @return string representation of all values
     */
    @Override
    public String toString() {
        return defaultToString();
    }

    /**
     * Checks whether this hash map contains the specified key.
     *
     * @param key key whose presence is to be tested
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    public boolean containsKey(K key) {
        int index = getIndex(key);
        SaxList<Entry<K, T>> bucket = buckets[index];
        for (int i = 0; i < bucket.getSize(); i++) {
            Entry<K, T> entry = bucket.getElementAt(i);
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the value mapped to the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value mapped to the specified key
     * @throws KeyNotFoundException if the key does not exist
     */
    public T get(K key) {
        int index = getIndex(key);
        SaxList<Entry<K, T>> bucket = buckets[index];
        for (int i = 0; i < bucket.getSize(); i++) {
            Entry<K, T> entry = bucket.getElementAt(i);
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        throw new KeyNotFoundException(key.toString());
    }

    /**
     * Inserts a new key-value mapping into this hash map.
     *
     * @param key   the key
     * @param value the value to associate with the key
     * @throws DuplicateKeyException if the key already exists
     */
    public void put(K key, T value) throws DuplicateKeyException {
        int index = getIndex(key);
        SaxList<Entry<K, T>> bucket = buckets[index];

        for (int i = 0; i < bucket.getSize(); i++) {
            Entry<K, T> entry = bucket.getElementAt(i);
            if (entry.key.equals(key)) {
                throw new DuplicateKeyException(key.toString());
            }
        }

        bucket.addElement(new Entry<>(key, value));
        size++;
    }

    /**
     * Removes the mapping for the specified key.
     *
     * @param key the key whose mapping is to be removed
     * @return the previously associated value
     * @throws KeyNotFoundException if the key does not exist
     */
    public T remove(K key) {
        int index = getIndex(key);
        SaxList<Entry<K, T>> bucket = buckets[index];

        for (int i = 0; i < bucket.getSize(); i++) {
            Entry<K, T> entry = bucket.getElementAt(i);
            if (entry.key.equals(key)) {
                bucket.removeElementAt(i);
                size--;
                return entry.value;
            }
        }

        throw new KeyNotFoundException(key.toString());
    }

    /**
     * Returns all keys currently stored in this hash map.
     *
     * @return a SaxList containing all keys
     */
    public SaxList<K> getKeys() {
        SaxList<K> keys = new SaxList<>();
        for (SaxList<Entry<K, T>> bucket : buckets) {
            for (int i = 0; i < bucket.getSize(); i++) {
                keys.addElement(bucket.getElementAt(i).key);
            }
        }
        return keys;
    }

    /**
     * Returns an iterator over the values stored in this hash map.
     * Iterates over values bucket by bucket.
     *
     * @return an iterator over all values
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int outer = 0;
            int inner = 0;

            @Override
            public boolean hasNext() {
                while (outer < buckets.length) {
                    if (inner < buckets[outer].getSize()) {
                        return true;
                    } else {
                        outer++;
                        inner = 0;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                T value = buckets[outer].getElementAt(inner).value;
                inner++;
                return value;
            }
        };
    }

    /**
     * Entry class used internally to store key-value pairs in each bucket.
     *
     * @param <K> key type
     * @param <T> value type
     */
    private static class Entry<K, T> {
        final K key;
        final T value;

        Entry(K key, T value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}