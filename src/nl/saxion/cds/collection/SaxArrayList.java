package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.ElementNotFoundException;

import java.util.Comparator;
import java.util.Iterator;
/**
 * A dynamic array-based implementation of the SaxIndexedCollection interface.
 * Stores elements in a resizable array and supports insertion, deletion, searching, and sorting.
 *
 * @param <T> the type of elements held in this collection
 */
public class SaxArrayList<T> implements SaxIndexedCollection<T> {
    // Minimal size of the internal array for default constructor
    private static final int MINIMUM_SIZE = 16;
    // Extending means doubling in size, until the size is bigger than this maximum extension size
    private static final int MAXIMUM_EXTENSION = 256;
    private Comparator<T> lastComparator = null; // Comparator used by last sorting

    // Java prohibits creating an array with a generic type, so we use Object
    private Object[] elements;

    // Number of elements in use
    private int size = 0;

    public SaxArrayList() {
        this(MINIMUM_SIZE);
    }

    public SaxArrayList(int capacity) {
        elements = new Object[capacity];
    }

    // SaxCollection methods

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // to clear the reference to the element, so it can be garbage collected if no more references exist
        }
        size = 0;
    }

    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type T in an Object array
    @SuppressWarnings("unchecked")
    // Ignore this for coverage
    @IgnoreCoverage
    public String graphViz(String name) {
        var builder = new StringBuilder();
        builder.append("digraph ");
        builder.append(name);
        builder.append(" {\n");
        for (int i = 0; i < size - 1; ++i) {
            T from = (T) elements[i];
            T to = (T) elements[i + 1];
            builder.append(String.format("\"%s\" -> \"%s\"\n", (from == null ? "NULL_" + i : from.toString()), (to == null ? "NULL_" + (i + 1) : to.toString())));
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toString() {
        return defaultToString();
    }

    // SaxIndexedCollection methods

    @Override
    public void addElement(T element) throws IndexOutOfBoundsException {
        int index = checkAndExtendSize(size);
        elements[index] = element;
        resetSorted();
    }

    @Override
    public void addElementAt(T element, int index) throws IndexOutOfBoundsException {
        elements[checkAndExtendSize(index)] = element;
        resetSorted();
    }

    @Override
    public void setElementAt(T element, int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Integer.toString(index));
        elements[index] = element;
        resetSorted();
    }

    // Do no type checking; a Java hack, because we store objects of a generic type T in an Object array
    @SuppressWarnings("unchecked")
    @Override
    public T getElementAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Integer.toString(index));
        return (T) elements[index];
    }

    // Do no type checking; a Java hack, because we store objects of a generic type T in an Object array
    @SuppressWarnings("unchecked")
    @Override
    public T getLastElement() throws IndexOutOfBoundsException {
        if (size == 0)
            throw new IndexOutOfBoundsException("empty collection");
        return (T) elements[size - 1];
    }

    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type T in an Object array
    @SuppressWarnings("unchecked")
    public T removeElementAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Integer.toString(index));
        T value = (T) elements[index];
        if (index < --size) {
            // shift all element one to the left (removing the element to delete)
            System.arraycopy(elements, index + 1, elements, index, size - index);
        }
        elements[size] = null; // this element no longer contains valid info
        resetSorted();
        return value;
    }

    @Override
    public boolean isSorted() {
        return lastComparator != null;
    }

    /**
     * Recursively do a quick sort (in place) on the elements in ascending order.
     */
    @Override
    public void sort(Comparator<T> comparator) {
        this.lastComparator = comparator;
        quickSort(0, size - 1, comparator);
    }

    private void quickSort(int low, int high, Comparator<T> comp) {
        if (low < high) {
            int pivotIndex = partition(low, high, comp);
            quickSort(low, pivotIndex - 1, comp);
            quickSort(pivotIndex + 1, high, comp);
        }
    }


    private int partition(int low, int high, Comparator<T> comp) {
        T pivot = (T) elements[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comp.compare((T) elements[j], pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        Object temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    @Override
    public int getIndexOfElement(T element) throws ElementNotFoundException {
        if (isSorted() && lastComparator != null) {
            int low = 0, high = size - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                T midVal = (T) elements[mid];
                int cmp = lastComparator.compare(midVal, element);
                if (cmp == 0) return mid;
                else if (cmp < 0) low = mid + 1;
                else high = mid - 1;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element == null && elements[i] == null) return i;
                if (element != null && element.equals(elements[i])) return i;
            }
        }
        throw new ElementNotFoundException(element == null ? "null" : element.toString());
    }
    /**
     * Check if the array of elements can hold another element and if not extend the array.
     * Make room on position index and adjust size.
     *
     * @param index position where to make room for a new element, valid from 0 up to size (size == add at end)
     * @return index
     * @throws IndexOutOfBoundsException index < 0 or > size
     */
    private int checkAndExtendSize(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException(Integer.toString(index));
        if (elements.length < size + 1) {
            // extend array by doubling in size if size is smaller than the maximum extension
            int capacity = elements.length < MAXIMUM_EXTENSION ? elements.length << 1 : elements.length + MAXIMUM_EXTENSION;
            var newElements = new Object[capacity];
            // copy existing elements
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        if (index < size) {
            // Make room for the new element
            System.arraycopy(elements, index, elements, index + 1, size - index);
        }
        ++size;
        return index;
    }

    /**
     * Mark list as not sorted.
     */
    private void resetSorted() {
        lastComparator = null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            // Do no type checking; a Java hack, because we store objects of a generic type T in an Object array
            @SuppressWarnings("unchecked")
            public T next() {
                return (T) elements[currentIndex++];
            }

            // Remove is not necessary; just for demonstration purposes / as a challenge
            @Override
            public void remove() {
                SaxArrayList.this.removeElementAt(currentIndex - 1);
            }
        };
    }
}
