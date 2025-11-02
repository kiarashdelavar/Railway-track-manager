package nl.saxion.cds.collection;

public interface SaxSet<V> extends SaxCollection<V> {
    void add(V value);
    boolean contains(V value);
}