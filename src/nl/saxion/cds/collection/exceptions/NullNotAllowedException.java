package nl.saxion.cds.collection.exceptions;

public class NullNotAllowedException extends RuntimeException {
    public NullNotAllowedException() {
        super("a NULL is not allowed.");
    }
}
