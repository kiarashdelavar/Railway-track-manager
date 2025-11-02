package nl.saxion.cds.collection.exceptions;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String value) {
        super("Value \"" + value + "\" is not found.");
    }
}
