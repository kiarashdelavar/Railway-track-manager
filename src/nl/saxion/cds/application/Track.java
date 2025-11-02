package nl.saxion.cds.application;

public record Track(String from, String to, double cost, double distance) {
    @Override
    public String toString() {
        return "{" + from + "=>" + to + "=" + distance + "}";
    }
}
