package nl.saxion.cds.collection;

// Node class used by SaxQueue.
public class SaxQueueNode<T> {
    T value;
    SaxQueueNode<T> next;

    public SaxQueueNode(T value) {
        this.value = value;
    }
}
