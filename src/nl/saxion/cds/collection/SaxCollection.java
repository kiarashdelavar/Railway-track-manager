package nl.saxion.cds.collection;

/**
 * The general collection type
 *
 * @param <T> type to store elements of
 */
public interface SaxCollection<T> extends Iterable<T> {
    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    int getSize();

    /**
     * Removes all elements from this collection.
     */
    void clear();

    /**
     * Create a String representation of the data in GraphViz (see <a href="https://graphviz.org">GraphViz</a>)
     * format, which you can print-copy-paste on the site see <a href="https://dreampuf.github.io/GraphvizOnline">GraphViz online</a>.
     *
     * @return a GraphViz string representation of this collection
     */
    default String graphViz() {
        return graphViz(getClass().getSimpleName());
    }

    /**
     * Create a String representation of the data in GraphViz (see <a href="https://graphviz.org">GraphViz</a>)
     * format, which you can print-copy-paste on the site see <a href="https://dreampuf.github.io/GraphvizOnline">GraphViz online</a>.
     *
     * @param name name of the produced graph
     * @return a GraphViz string representation of this collection
     */
    String graphViz(String name);

    /**
     * Create a default toString function to display a SaxCollection instance.
     * Call this function in the overridden toString of a SaxCollection subclass,
     * but make sure you first implement the Iterator of the subclass.
     *
     * @return all T elements (toString-ed) between square brackets and separated by a comma and a space
     */
    default String defaultToString() {
        var builder = new StringBuilder();
        builder.append('[');
        for (var element : this) {
            builder.append((element == null) ? "NULL" : element.toString());
            builder.append(", ");
        }
        if (builder.length() > 2) {
            builder.delete(builder.length() - 2, builder.length()); // remove last ", "
        }
        builder.append("]");
        return builder.toString();
    }
}
