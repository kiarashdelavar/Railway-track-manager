package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.EmptyCollectionException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * SaxQueue implements a simple First-In-First-Out (FIFO) queue using a singly linked list.
 * @param <T> the type of elements in this queue
 */
public class SaxQueue<T> implements SaxOrderedCollection<T> {
    private SaxQueueNode<T> head = null;
    private SaxQueueNode<T> tail = null;
    private int size = 0;
    /**
     * Adds a new element to the back of the queue.
     *
     * @param value the element to add
     */
    @Override
    public void push(T value) {
        SaxQueueNode<T> node = new SaxQueueNode<>(value);
        if (tail != null) {
            tail.next = node;
        } else {
            head = node;
        }
        tail = node;
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the front element
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (head == null) {
            throw new EmptyCollectionException();
        }
        T value = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    /**
     * Returns the element at the front of the queue without removing it.
     *
     * @return the front element
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (head == null) {
            throw new EmptyCollectionException();
        }
        return head.value;
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return the current size of the queue
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Removes all elements from the queue.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns a GraphViz representation of the queue.
     * (Used for visualizing in SaxionApp — not included in test coverage)
     *
     * @param name the name of the GraphViz graph
     * @return a string in GraphViz DOT format
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder("digraph " + name + " {\n");
        SaxQueueNode<T> current = head;
        while (current != null && current.next != null) {
            sb.append("\"").append(current.value).append("\" -> \"")
                    .append(current.next.value).append("\";\n");
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Returns a string representation of the queue using the defaultToString().
     */
    @Override
    public String toString() {
        return defaultToString();
    }

    /**
     * Returns an iterator over elements in the queue from front to back.
     *
     * @return an iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private SaxQueueNode<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }
            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T value = current.value;
                current = current.next;
                return value;
            }
        };
    }
}
