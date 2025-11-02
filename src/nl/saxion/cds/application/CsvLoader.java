package nl.saxion.cds.application;

import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;

import java.io.*;


public class CsvLoader {

    public void loadStations(String resourceName, SaxList<Station> stationList, SaxGraph<Station> graph) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourceName);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;

                    String code = parts[0];
                    String name = parts[1];
                    String type = parts[2];
                    double lat = Double.parseDouble(parts[3]);
                    double lon = Double.parseDouble(parts[4]);

                    Coordinate coord = new Coordinate(code, lat, lon);
                    Station station = new Station(code, name, type, coord);
                    stationList.addElement(station);
                    graph.addNode(station);
                }
                System.out.println("Loaded " + stationList.getSize() + " stations.");
            }
        } catch (IOException e) {
            System.err.println("Error reading stations.csv: " + e.getMessage());
        }
    }

    public void loadTracks(String resourceName, SaxGraph<Station> graph) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourceName);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 4) continue;

                    String fromCode = parts[0];
                    String toCode = parts[1];
                    double distance = Double.parseDouble(parts[3]);

                    Station from = findStationByCode(graph, fromCode);
                    Station to = findStationByCode(graph, toCode);
                    if (from != null && to != null) {
                        graph.addEdge(from, to, distance);
                    }
                }
                System.out.println("Loaded track connections.");
            }
        } catch (IOException e) {
            System.err.println("Error reading tracks.csv: " + e.getMessage());
        }
    }


    private Station findStationByCode(SaxGraph<Station> graph, String code) {
        for (Station station : graph) {
            if (station.code().equals(code)) return station;
        }
        return null;
    }
}
