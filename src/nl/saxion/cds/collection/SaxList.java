package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.ElementNotFoundException;

import java.util.Comparator;
import java.util.Iterator;

/**
 * A doubly linked list implementation of SaxIndexedCollection.
 * Provides basic list operations with support for insertion, deletion, and sorting.
 *
 * @param <T> the type of elements held in this collection
 */
public class SaxList<T> implements SaxIndexedCollection<T> {
    private ListNode head = null;
    private ListNode tail = null;
    private int size = 0;

    /**
     * Returning the number of elements in the list.
     * @return size of the list
     */
    @Override
    public int getSize() {
        return size;
    }
    /** Clearing the list. */
    @Override
    public void clear() {
        tail = null;
        head = null;
        size = 0;
    }

    /**
     *  for testing in testsaxlist: Returns head node for internal testing.
     */
     public ListNode getHeadForTest() {
        return head;
    }

    /**
     * for testing in testsaxlist: Returns tail node for internal testing.
     */
     public ListNode getTailForTest() {
        return tail;
    }

    /**
     * FOR TESTING PURPOSES ONLY: Sets the head node manually.
     * Use this only in test cases to simulate broken structure.
     */
    public void setHeadForTest(ListNode node) {
        this.head = node;
    }



    /**
     * Returns a GraphViz string of the list structure anc excluded from the test coverage.
     * @param name the name of the graph
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder(" digraph " + name + " {\n");
        ListNode current = head;
        while (current != null && current.next != null) {
            sb.append("\"").append(current.value).append("\" -> \"").append(current.next.value).append("\";\n");
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return defaultToString();
    }

    /**
     * Add an element to the end of a list
     *
     * @param element the element to add to this collection.
     */
    @Override
    public void addElement(T element) {
        ListNode newNode = new ListNode(element);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }
    /**
     * Add (insert) an element at the given index
     *
     * @param element the element to add to this collection.
     * @param index   where the element is inserted.
     * @throws IndexOutOfBoundsException when the index references an invalid position
     */
    @Override
    public void addElementAt(T element, int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }

        ListNode newNode = new ListNode(element);
        if (index == 0) {
            newNode.next = head;
            if (head != null) head.prev = newNode;
            head = newNode;
            if (size == 0) tail = newNode;
        } else if (index == size) {
            addElement(element);
            return;
        } else {
            ListNode current = getNode(index);
            newNode.prev = current.prev;
            newNode.next = current;

            if (current.prev != null) {
                current.prev.next = newNode;
            } else {
                head = newNode;
            }

            current.prev = newNode;
        }
        size++;
    }

    private ListNode getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        if (head == null || tail == null) {
            throw new IllegalStateException("List is corrupted: head or tail is null while size = " + size);
        }

        ListNode current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                if (current == null || current.next == null && i < index - 1) {
                    throw new IllegalStateException("Broken link at index " + i);
                }
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                if (current == null || current.prev == null && i > index + 1) {
                    throw new IllegalStateException("Broken link at index " + i);
                }
                current = current.prev;
            }
        }

        return current;
    }
    /** Sets an element at a specific index. */
    @Override
    public void setElementAt(T element, int index) throws IndexOutOfBoundsException {
        getNode(index).value = element;
    }
    /** Gets an element at a specific index. */
    @Override
    public T getElementAt(int index) throws IndexOutOfBoundsException {
        return (T) getNode(index).value;
    }
    /** Gets the last element in the list. */
    @Override
    public T getLastElement() throws IndexOutOfBoundsException {
        if (tail == null) throw new IndexOutOfBoundsException();
        return (T) tail.value;
    }
    /** Removes an element at a specific index. */
    @Override
    public T removeElementAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }

        ListNode node = getNode(index);
        if (node == null) throw new IndexOutOfBoundsException(index); // for having extra safety

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        size--;
        return (T) node.value;
    }
    /** Checks if the list is sorted. */
    @Override
    public boolean isSorted() {
        return false;
    }

    /**
     * Sort the collection in place in ascending order,
     * using a simple O(N2) sorting algorithm: selection sort
     *
     * @param comparator sorting comparator
     */
    @Override
    public void sort(Comparator<T> comparator) {
        if (head == null || head.next == null || size <= 1)
            return;

        for (ListNode i = head; i != null; i = i.next) {
            ListNode min = i;
            for (ListNode j = i.next; j != null; j = j.next) {
                if (comparator.compare((T) j.value, (T) min.value) < 0) {
                    min = j;
                }
            }
            if (min != i) {
                T temp = (T) i.value;
                i.value = min.value;
                min.value = temp;
            }
        }
    }

    /**
     * Returns the index of the specified element in the list.
     * Performs a linear search starting from the head of the list.
     *
     * @param element the element to search for (can be null)
     * @return the index of the element if found
     * @throws ElementNotFoundException if the element is not in the list
     */
    @Override
    public int getIndexOfElement(T element) throws ElementNotFoundException {
        ListNode current = head;
        int index = 0;
        while (current != null) {
            if (element == null) {
                if (current.value == null) {
                    return index;
                }
            } else {
                if (element.equals(current.value)) {
                    return index;
                }
            }
            current = current.next;
            index++;
        }
        throw new ElementNotFoundException(element == null ? "null" : element.toString());
    }

    /**
     * Returns an iterator over elements of type {@code T} for this list.
     *
     * @return an {@link Iterator} that traverses the list from head to tail
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private ListNode current = head;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) throw new IndexOutOfBoundsException();
                T value = (T) current.value;
                current = current.next;
                return value;
            }
        };
    }
}
