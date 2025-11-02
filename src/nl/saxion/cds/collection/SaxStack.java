package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.EmptyCollectionException;

import java.util.Iterator;

/**
 * A simple stack implementation based on {@link SaxList}.
 * <p>
 * Follows the LIFO (Last In First Out) principle:
 * elements are added and removed from the top of the stack.
 *
 * @param <T> type of elements stored in the stack
 */
public class SaxStack<T> implements SaxOrderedCollection<T> {

    private final SaxList<T> list = new SaxList<>();

    /**
     * Pushes an element on top of the stack.
     *
     * @param value the element to add
     */
    @Override
    public void push(T value) {
        list.addElementAt(value, 0); // top is index 0
    }

    /**
     * Removes and returns the element at the top of the stack.
     *
     * @return the top element
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (getSize() == 0)
            throw new EmptyCollectionException();
        return list.removeElementAt(0);
    }

    /**
     * Returns (without removing) the element at the top of the stack.
     *
     * @return the top element
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (getSize() == 0)
            throw new EmptyCollectionException();
        return list.getElementAt(0);
    }

    /**
     * Returns the current number of elements in the stack.
     *
     * @return the stack size
     */
    @Override
    public int getSize() {
        return list.getSize();
    }

    /**
     * Removes all elements from the stack.
     */
    @Override
    public void clear() {
        list.clear();
    }

    /**
     * Returns a GraphViz DOT representation of this stack
     * (excluded from test coverage, for visual debugging only).
     *
     * @param name graph name
     * @return DOT string showing the stack as vertical nodes
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder("digraph ");
        sb.append(name).append(" {\n");
        sb.append("  node [shape=box, style=filled, color=lightblue];\n");

        for (int i = 0; i < getSize(); i++) {
            sb.append("  \"").append(list.getElementAt(i)).append("\";\n");
            if (i < getSize() - 1) {
                sb.append("  \"").append(list.getElementAt(i))
                        .append("\" -> \"").append(list.getElementAt(i + 1)).append("\";\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Returns a string representation of this stack (top to bottom).
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return list.toString();
    }

    /**
     * Returns an iterator that iterates from top to bottom.
     *
     * @return iterator over elements from top to bottom
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
                return list.getElementAt(index++);
            }
        };
    }
}
