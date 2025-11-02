package nl.saxion.cds.application;

public record Coordinate(String label, double latitude, double longitude) {
    /**
     * Calculate the distance in kilometers between this and another coordinate using the Haversine formula.
     * Code adopted from <a href="https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/">Geeks for Geeks</a>
     *
     * @param to calculating distance to this coordinate
     * @return distance in kilometers
     */
    public double haversineDistance(Coordinate to) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(to.latitude - this.latitude);
        double dLon = Math.toRadians(to.longitude - this.longitude);

        // convert to radians
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(to.latitude);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                           Math.pow(Math.sin(dLon / 2), 2) *
                                   Math.cos(lat1) *
                                   Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return Math.round(rad * c * 10.0) / 10.0; // rounding to hectometers is specific enough for station distances
    }

    /**
     * converting the station's longitude to a pixel X position on the 768px-wide map.
     * method scales the longitude value between minLon and maxLon to fit on screen.
     */
    public double getX() {
        double minLon = 3.15;   // more room on the left
        double maxLon = 7.23;   // more room on the right
        return (this.longitude - minLon) / (maxLon - minLon) * 768;
    }


    /**
     * Converts the station's latitude to a pixel Y position on the 1024px-high map.
     * method flips and scales the latitude between minLat and maxLat so that
     * the top of the map is the north and the bottom is the south.
     */
    public double getY() {
        double minLat = 50.70;  // more room at the bottom
        double maxLat = 53.60;  // more room at the top
        return (1 - (this.latitude - minLat) / (maxLat - minLat)) * 1024;
    }

    @Override
    public String toString() {
        return label;
    }
}
