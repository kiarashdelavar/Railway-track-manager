package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.EmptyCollectionException;

/**
 * An interface for implementing a collection of elements, which grows and shrinks
 * in a predetermined way.
 *
 * @param <T> the type to store
 */
public interface SaxOrderedCollection<T> extends SaxCollection<T> {
    /**
     * Add the element to list.
     *
     * @param element the element to push
     */
    void push(T element);

    /**
     * Remove the element from the list.
     *
     * @return the popped element
     */
    T pop() throws EmptyCollectionException;

    /**
     * Return the element of the list, without removing it.
     *
     * @return the element or null if the list is empty
     */
    T peek() throws EmptyCollectionException;
}
