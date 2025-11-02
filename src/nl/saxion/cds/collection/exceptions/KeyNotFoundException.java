package nl.saxion.cds.collection.exceptions;

public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException(String key) {
        super("Key \"" + key + "\" is not found.");
    }
}
