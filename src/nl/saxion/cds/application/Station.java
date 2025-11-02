package nl.saxion.cds.application;

import nl.saxion.cds.collection.SaxGraph;

public record Station(String code, String name, String type, Coordinate coordinate) {
    public static final SaxGraph.Estimator<Station> HAVERSINE_ESTIMATOR = (o1, o2) -> o1.coordinate().haversineDistance(o2.coordinate());

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Station) {
            return code.equals(((Station) obj).code());
        }
        return false;
    }

    @Override
    public String toString() {
        return name + '(' + code + ')';
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
