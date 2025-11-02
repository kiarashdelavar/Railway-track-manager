package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.ElementNotFoundException;

import java.util.Comparator;

/**
 * An interface for implementing a collection of elements which are indexed
 *
 * @param <T> the element type to store
 */
public interface SaxIndexedCollection<T> extends SaxCollection<T> {
    /**
     * Adds the provided element to the end of this collection.
     *
     * @param element the element to add.
     */
    void addElement(T element);

    /**
     * Adds the provided element to this collection.
     *
     * @param element the element to add to this collection.
     * @param index   where the element is inserted.
     */
    void addElementAt(T element, int index) throws IndexOutOfBoundsException;

    /**
     * Replace the element at the given index with the new provided element.
     *
     * @param index   the index at which to replace an element.
     * @param element the element to replace the original element with.
     */
    void setElementAt(T element, int index) throws IndexOutOfBoundsException;

    /**
     * Retrieve the element at the provided index in the collection.
     *
     * @param index the index from which to retrieve the element.
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if an invalid index is provided.
     */
    T getElementAt(int index) throws IndexOutOfBoundsException;

    /**
     * Retrieve the last element,
     *
     * @return the last element
     * @throws IndexOutOfBoundsException if the collection is empty
     */
    T getLastElement() throws IndexOutOfBoundsException;

    /**
     * Remove the element at the provided index in the collection.
     *
     * @param index the index from which to retrieve the element.
     * @return the element that was removed from the given index.
     * @throws IndexOutOfBoundsException if an invalid index is provided.
     */
    T removeElementAt(int index) throws IndexOutOfBoundsException;

    /**
     * Determine if this collection is sorted or not.
     *
     * @return if it is sorted
     */
    boolean isSorted();

    /**
     * Sort the collection in place in ascending order, according to the given comparator.
     *
     * @param comparator sorting comparator.
     */
    void sort(Comparator<T> comparator);

    default boolean contains(T element) {
        try {
            return getIndexOfElement(element) >= 0;
        } catch (ElementNotFoundException e) {
            return false;
        }
    }

    /**
     * Find the index at which an instance of the provided element is located.
     *
     * @param element the element to search for.
     * @return the index at which the element can be retrieved from this collection.
     * @throws ElementNotFoundException if the element is not part of this collection.
     */
    int getIndexOfElement(T element) throws ElementNotFoundException;
}
