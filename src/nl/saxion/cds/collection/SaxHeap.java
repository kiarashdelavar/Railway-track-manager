package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.EmptyCollectionException;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Array-based generic binary heap implementation (min or max heap).
 * <p>
 * Uses {@link SaxArrayList} as internal storage. The heap property
 * is defined by the provided {@link Comparator}.
 * </p>
 * Example:
 * <pre>
 *     SaxHeap<Integer> minHeap = new SaxHeap<>(Comparator.naturalOrder());
 *     SaxHeap<Integer> maxHeap = new SaxHeap<>(Comparator.reverseOrder());
 * </pre>
 *
 * @param <T> type of elements stored in the heap
 */
public class SaxHeap<T> implements SaxOrderedCollection<T> {

    private final Comparator<T> comparator;
    private final SaxArrayList<T> arrayList = new SaxArrayList<>();

    /**
     * Creates a new heap that uses the given comparator to decide ordering.
     *
     * @param comparator comparator used to compare elements
     */
    public SaxHeap(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Checks if the heap contains the given value.
     *
     * @param value the value to check for
     * @return true if the value is in the heap, false otherwise
     */
    public boolean contains(T value) {
        for (int i = 0; i < arrayList.getSize(); i++) {
            if (arrayList.getElementAt(i).equals(value)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the current number of elements in the heap.
     *
     * @return number of elements
     */
    @Override
    public int getSize() {
        return arrayList.getSize();
    }

    /**
     * Returns a simple string representation of the heap.
     *
     * @return string with elements in array order
     */
    @Override
    public String toString() {
        return arrayList.toString();
    }

    /**
     * Removes all elements from the heap.
     */
    @Override
    public void clear() {
        arrayList.clear();
    }

    /**
     * Returns a GraphViz DOT representation of the heap (for visual debugging).
     * This one is excluded from coverage by {@link IgnoreCoverage}.
     *
     * @param name name of the graph
     * @return DOT representation as a string
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder("digraph ").append(name).append(" {\n");
        sb.append("  node [shape=circle, style=filled, color=lightgrey];\n");
        for (int i = 0; i < getSize(); i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < getSize()) {
                sb.append("  \"").append(arrayList.getElementAt(i)).append("\" -> \"")
                        .append(arrayList.getElementAt(left)).append("\";\n");
            }
            if (right < getSize()) {
                sb.append("  \"").append(arrayList.getElementAt(i)).append("\" -> \"")
                        .append(arrayList.getElementAt(right)).append("\";\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Adds a new element to the heap and restores the heap property
     * by percolating the element up.
     *
     * @param element element to insert
     */
    @Override
    public void push(T element) {
        arrayList.addElement(element);
        int index = getSize() - 1;
        percolateUp(index);
    }

    /**
     * Removes and returns the root element of the heap.
     *
     * @return the element with highest priority (root)
     * @throws EmptyCollectionException if the heap is empty
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (getSize() == 0) {
            throw new EmptyCollectionException();
        }

        T root = arrayList.getElementAt(0);
        T last = arrayList.getElementAt(getSize() - 1);

        // Moving last element to root and shrink
        arrayList.setElementAt(last, 0);
        arrayList.removeElementAt(getSize() - 1);

        // restoring heap order
        if (getSize() > 0) {
            percolateDown(0);
        }
        return root;
    }

    /**
     * Returns the root element without removing it.
     *
     * @return current root element
     * @throws EmptyCollectionException if heap is empty
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (getSize() == 0) {
            throw new EmptyCollectionException();
        }
        return arrayList.getElementAt(0);
    }


    /**
     * Iterator that traverses the heap elements in array order (not sorted).
     *
     * @return iterator over heap elements
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < getSize();
            }

            @Override
            public T next() {
                T value = arrayList.getElementAt(index);
                index++;
                return value;
            }
        };
    }

    /**
     * Moves an element upwards until heap order is restored.
     */
    private void percolateUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T current = arrayList.getElementAt(index);
            T parent = arrayList.getElementAt(parentIndex);

            if (comparator.compare(current, parent) < 0) {
                arrayList.setElementAt(parent, index);
                arrayList.setElementAt(current, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Moves an element downwards until heap order is restored.
     */
    private void percolateDown(int index) {
        int size = getSize();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && comparator.compare(arrayList.getElementAt(left), arrayList.getElementAt(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && comparator.compare(arrayList.getElementAt(right), arrayList.getElementAt(smallest)) < 0) {
                smallest = right;
            }

            if (smallest != index) {
                T tmp = arrayList.getElementAt(index);
                arrayList.setElementAt(arrayList.getElementAt(smallest), index);
                arrayList.setElementAt(tmp, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }
}
