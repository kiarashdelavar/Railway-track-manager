package nl.saxion.cds.application;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import nl.saxion.cds.collection.SaxArrayList;
import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;
import java.awt.Color;
import java.awt.*;
import java.util.Scanner;

/**
 * RailwayManager with graphical rendering using SaxionApp GameLoop.
 */
public class RailwayManager implements GameLoop {
    private final SaxGraph<Station> graph = new SaxGraph<>();
    private final CsvLoader loader = new CsvLoader();
    private final SaxList<Station> stationList = new SaxList<>();
    private SaxList<SaxGraph<Station>.DirectedEdge> highlightedPath = null;
    private Color highlightedColor = null;

    private void menuLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nRailway Track Manager - MENU:");
            System.out.println("1. Search station by name");
            System.out.println("2. Find shortest route");
            System.out.println("3. Find shortest round trip");
            System.out.println("4. Show MCST");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> searchStationByName(scanner);
                case 2 -> findShortestRoute(scanner);
                case 3 -> findRoundTrip(scanner);
                case 4 -> showMCST();
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid option");
            }
        }
    }

    @Override
    public void init() {
        loader.loadStations("stations.csv", stationList, graph);
        loader.loadTracks("tracks.csv", graph);
        SaxionApp.drawImage("resources/Nederland.png", 0, 0, 768, 1024);
        new Thread(this::menuLoop).start();
    }


    @Override
    public void loop() {
        drawTracks();
        drawStations();
        drawHighlightedPath();
    }

    /**
     * Draws the station code slightly above the station's coordinate.
     *
     * @param station the station to label
     */
    private void drawStationLabel(Station station, boolean showLabel) {
        int x = (int) station.coordinate().getX();
        int y = (int) station.coordinate().getY();

        if (showLabel) {
            SaxionApp.setTextDrawingColor(Color.YELLOW);
            SaxionApp.drawText(station.code(), x - 10, y - 30, 14);
        }
    }


    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
    }

    /**
     * Draw red circle for each station
     */
    private void drawStations() {
        SaxionApp.setFill(Color.RED);
        for (int i = 0; i < stationList.getSize(); i++) {
            Station station = stationList.getElementAt(i);
            Coordinate c = station.coordinate();
            SaxionApp.drawCircle((int) c.getX(), (int) c.getY(), 6);
            drawStationLabel(station, false);
        }
    }

    /**
     * Lets the user search for a station by typing part of its name.
     * Shows all matching stations in the console and highlights the chosen one on the map.
     */
    private void searchStationByName(Scanner scanner) {
        System.out.print("Enter station name (or beginning): ");
        String input = scanner.nextLine().toLowerCase();

        SaxList<Station> matches = new SaxList<>();
        for (Station s : stationList) {
            if (s.name().toLowerCase().startsWith(input)) {
                matches.addElement(s);
            }
        }

        SaxionApp.clear();
        SaxionApp.drawImage("resources/Nederland.png", 0, 0, 768, 1024);
        drawTracks();
        drawStations();


        if (matches.getSize() == 0) {
            System.out.println("No stations found.");
        } else {
            System.out.println("Matches:");
            for (int i = 0; i < matches.getSize(); i++) {
                Station s = matches.getElementAt(i);
                System.out.println(i + ". " + s.code() + " - " + s.name() + " (" + s.type() + ")");
            }

            if (matches.getSize() > 1) {
                System.out.print("Choose station index to view: ");
                int index = scanner.nextInt();
                scanner.nextLine();
                if (index >= 0 && index < matches.getSize()) {
                    Station selected = matches.getElementAt(index);
                    System.out.println("Selected: " + selected.name());
                    SaxionApp.setFill(Color.BLUE);
                    SaxionApp.drawCircle((int) selected.coordinate().getX(), (int) selected.coordinate().getY(), 10);
                    drawStationLabel(selected, true);
                }
            }
        }
    }


    /**
     * Helps the user choose one specific station when there are multiple matches.
     * Returns the selected station or null if none is found.
     * @param partName part of the station name typed by the user
     * @param scanner  used to read user input
     * @return the chosen Station, or null if not found
     */
    private Station chooseStationFromSearch(String partName, Scanner scanner) {
        SaxList<Station> matches = new SaxList<>();
        for (Station s : stationList) {
            if (s.name().toLowerCase().startsWith(partName.toLowerCase())) {
                matches.addElement(s);
            }
        }

        SaxionApp.clear();
        SaxionApp.drawImage("resources/Nederland.png", 0, 0, 768, 1024);
        drawTracks();
        drawStations();

        if (matches.getSize() == 0) {
            System.out.println(" No stations found.");
            return null;
        }

        if (matches.getSize() == 1) {
            return matches.getElementAt(0);
        }

        System.out.println("Multiple matches found:");
        for (int i = 0; i < matches.getSize(); i++) {
            Station s = matches.getElementAt(i);
            System.out.println(i + ". " + s.code() + " - " + s.name());
        }

        System.out.print("Choose station index: ");
        int index = scanner.nextInt();
        scanner.nextLine();
        if (index < 0 || index >= matches.getSize()) {
            System.out.println(" Invalid index.");
            return null;
        }

        return matches.getElementAt(index);
    }


    /**
     * Reconstructs the shortest path from Dijkstra result graph by tracing backward from end to start.
     * It uses the fact that each node in the Dijkstra result has exactly one incoming edge (from → to).
     *
     * @param dijkstraTree the result of shortestPathsDijkstra(start)
     * @param start        the start station
     * @param end          the end station
     * @return list of directed edges forming the path, or null if no path exists
     */
    private SaxList<SaxGraph<Station>.DirectedEdge> buildPath(SaxGraph<Station> dijkstraTree, Station start, Station end) {
        SaxList<SaxGraph<Station>.DirectedEdge> path = new SaxList<>();
        Station current = end;

        while (!current.equals(start)) {
            SaxGraph<Station>.DirectedEdge found = null;

            // finding the edge where current is the destination
            for (Station node : dijkstraTree) {
                for (SaxGraph<Station>.DirectedEdge edge : dijkstraTree.getEdges(node)) {
                    if (edge.to().equals(current)) {
                        found = edge;
                        break;
                    }
                }
                if (found != null) break;
            }

            if (found == null) {
                return null;
            }

            path.addElementAt(found, 0);
            current = found.from();
        }

        return path;
    }

    /**
     * Finds and shows the shortest route between two stations using Dijkstra.
     * The route is printed in the console and drawn in blue on the map
     * with station names shown above each stop.
     *
     * @param scanner used for user input
     */
    private void findShortestRoute(Scanner scanner) {
        System.out.print("Enter START station name: ");
        String startName = scanner.nextLine();
        Station start = chooseStationFromSearch(startName, scanner);
        if (start == null) return;

        System.out.print("Enter END station name: ");
        String endName = scanner.nextLine();
        Station end = chooseStationFromSearch(endName, scanner);
        if (end == null) return;

        SaxGraph<Station> dijkstraResult = graph.shortestPathsDijkstra(start);
        SaxList<SaxGraph<Station>.DirectedEdge> path = buildPath(dijkstraResult, start, end);

        if (path == null || path.getSize() == 0) {
            System.out.println("No path found between " + start.name() + " and " + end.name());
            return;
        }

        // Print to console
        double totalLength = 0;
        System.out.println("Shortest route:");
        for (SaxGraph<Station>.DirectedEdge edge : path) {
            System.out.println(edge.from().name() + " → " + edge.to().name() + " (" + edge.weight() + " km)");
            totalLength += edge.weight();
        }
        System.out.printf("Total distance: %.1f km%n", totalLength);

        // clearing and redrawing map
        SaxionApp.clear();
        SaxionApp.drawImage("resources/Nederland.png", 0, 0, 768, 1024);
        drawTracks();
        drawStations();
        this.highlightedPath = path;
        this.highlightedColor = Color.BLUE;
    }

    /**
     * Draws the highlighted route on the map.
     * Ensures each station code is printed only once.
     */
    private void drawHighlightedPath() {
        if (highlightedPath == null || highlightedPath.getSize() == 0) return;

        SaxionApp.setTextDrawingColor(highlightedColor);
        for (int i = 0; i < highlightedPath.getSize(); i++) {
            var edge = highlightedPath.getElementAt(i);
            Coordinate from = edge.from().coordinate();
            Coordinate to = edge.to().coordinate();

            SaxionApp.drawLine((int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
        }

        // Draw station codes once only
        SaxionApp.setTextDrawingColor(Color.YELLOW);
        SaxArrayList<String> drawnCodes = new SaxArrayList<>();

        for (int i = 0; i < highlightedPath.getSize(); i++) {
            Station station = highlightedPath.getElementAt(i).from();
            if (!isAlreadyDrawn(drawnCodes, station.code())) {
                drawnCodes.addElement(station.code());
                Coordinate coord = station.coordinate();
                SaxionApp.drawText(station.code(), (int) coord.getX() + 5, (int) coord.getY() - 10, 14);
            }
        }

        // Last station (to)
        Station last = highlightedPath.getElementAt(highlightedPath.getSize() - 1).to();
        if (!isAlreadyDrawn(drawnCodes, last.code())) {
            Coordinate coord = last.coordinate();
            SaxionApp.drawText(last.code(), (int) coord.getX() + 5, (int) coord.getY() - 10, 14);
        }
    }

    /**
     * Helper method to manually check if code is already in list (avoids broken contains)
     */
    private boolean isAlreadyDrawn(SaxArrayList<String> list, String code) {
        for (int i = 0; i < list.getSize(); i++) {
            if (list.getElementAt(i).equals(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the shortest round trip through 3 or more user-selected stations.
     * Uses Dijkstra for pairwise shortest paths and brute-force all permutations.
     * Shows the route and draws it in purple on the map.
     *
     * @param scanner The Scanner for reading user input.
     */
    private void findRoundTrip(Scanner scanner) {
        System.out.print("Enter number of stations for round trip (min 3): ");
        int count = scanner.nextInt();
        scanner.nextLine();

        SaxionApp.clear();
        SaxionApp.drawImage("resources/Nederland.png", 0, 0, 768, 1024);
        drawTracks();
        drawStations();

        if (count < 3) {
            System.out.println("You must enter at least 3 stations.");
            return;
        }

        SaxList<Station> inputStations = new SaxList<>();
        for (int i = 0; i < count; i++) {
            System.out.print("Enter station name " + (i + 1) + ": ");
            String name = scanner.nextLine();
            Station station = chooseStationFromSearch(name, scanner);
            if (station != null) {
                inputStations.addElement(station);
            } else {
                System.out.println("Invalid station, skipping...");
                return;
            }
        }

        Station start = inputStations.getElementAt(0);
        SaxList<Station> toPermute = new SaxList<>();
        for (int i = 1; i < inputStations.getSize(); i++) {
            toPermute.addElement(inputStations.getElementAt(i));
        }

        SaxList<SaxList<Station>> permutations = new SaxList<>();
        generatePermutations(toPermute, new SaxList<>(), permutations);

        double bestDistance = Double.MAX_VALUE;
        SaxList<SaxGraph<Station>.DirectedEdge> bestPath = null;

        for (int i = 0; i < permutations.getSize(); i++) {
            SaxList<Station> order = permutations.getElementAt(i);

            SaxList<SaxGraph<Station>.DirectedEdge> totalPath = new SaxList<>();
            double totalDistance = 0;
            Station current = start;

            // Go through each station in order
            for (int j = 0; j < order.getSize(); j++) {
                Station next = order.getElementAt(j);
                SaxGraph<Station> dijkstra = graph.shortestPathsDijkstra(current);
                SaxList<SaxGraph<Station>.DirectedEdge> segment = buildPath(dijkstra, current, next);
                if (segment == null) {
                    totalDistance = Double.MAX_VALUE;
                    break;
                }
                for (SaxGraph<Station>.DirectedEdge e : segment) totalPath.addElement(e);
                for (int s = 0; s < segment.getSize(); s++) totalDistance += segment.getElementAt(s).weight();
                current = next;
            }

            // Return to start
            if (totalDistance != Double.MAX_VALUE) {
                SaxGraph<Station> dijkstra = graph.shortestPathsDijkstra(current);
                SaxList<SaxGraph<Station>.DirectedEdge> returnSegment = buildPath(dijkstra, current, start);
                if (returnSegment != null) {
                    for (SaxGraph<Station>.DirectedEdge e : returnSegment) totalPath.addElement(e);
                    for (int s = 0; s < returnSegment.getSize(); s++) totalDistance += returnSegment.getElementAt(s).weight();

                    if (totalDistance < bestDistance) {
                        bestDistance = totalDistance;
                        bestPath = totalPath;
                    }
                }
            }
        }

        if (bestPath == null) {
            System.out.println("No valid round trip could be found!");
            return;
        }

        // Output and drawing result
        System.out.println("Best round trip route:");
        for (SaxGraph<Station>.DirectedEdge edge : bestPath) {
            System.out.println(edge.from().name() + " → " + edge.to().name() + " (" + edge.weight() + " km)");
        }
        System.out.printf("Total round trip distance: %.1f km%n", bestDistance);

        // storing path and color for loop() drawing
        this.highlightedPath = bestPath;
        this.highlightedColor = Color.BLUE;
    }


    /**
     * Recursively generates all permutations of the station list.
     */
    private void generatePermutations(SaxList<Station> input, SaxList<Station> current, SaxList<SaxList<Station>> result) {
        if (input.getSize() == 0) {
            SaxList<Station> copy = new SaxList<>();
            for (int i = 0; i < current.getSize(); i++) copy.addElement(current.getElementAt(i));
            result.addElement(copy);
            return;
        }

        for (int i = 0; i < input.getSize(); i++) {
            Station selected = input.getElementAt(i);
            input.removeElementAt(i);
            current.addElement(selected);

            generatePermutations(input, current, result);

            current.removeElementAt(current.getSize() - 1);
            input.addElementAt(selected, i);
        }
    }

    /**
     * Visualizes the Minimum Cost Spanning Tree (MCST) of the current graph
     * using SaxionApp on a map of the Netherlands and prints the total length.
     */
    private void showMCST() {
        SaxGraph<Station> mcst = graph.minimumCostSpanningTree();
        SaxionApp.clear();

        SaxionApp.drawImage("resources/Nederland.png", 0, 0, 768, 1024);

        double totalLength = 0.0;

        for (Station from : mcst.getNodes()) {
            Coordinate fromCoord = from.coordinate();
            int x1 = (int) fromCoord.getX();
            int y1 = (int) fromCoord.getY();

            SaxionApp.setFill(Color.RED);
            SaxionApp.drawCircle(x1, y1, 5);

            SaxionApp.setFill(Color.WHITE);
            SaxionApp.drawText(from.code(), x1 + 5, y1 - 10, 14);


            SaxionApp.setFill(Color.CYAN);
            for (SaxGraph.DirectedEdge edge : mcst.getEdges(from)) {
                Station to = (Station) edge.to();
                if (from.code().compareTo(to.code()) < 0) {
                    Coordinate toCoord = to.coordinate();
                    int x2 = (int) toCoord.getX();
                    int y2 = (int) toCoord.getY();

                    double len = edge.weight();
                    totalLength += len;

                    SaxionApp.drawLine(x1, y1, x2, y2);
                }
            }
        }
        SaxionApp.setFill(Color.GREEN);
        SaxionApp.drawText("Total MCST length: " + String.format("%.2f", totalLength) + " km", 50, 50, 14);

        System.out.println("Minimum Cost Spanning Tree length: " + totalLength + " km");
        this.highlightedColor = Color.BLUE;
    }


    /**
     * Draw gray lines for each track connection
     */
    private void drawTracks() {
        SaxionApp.setBorderColor(Color.GRAY);
        for (Station from : graph) {
            for (SaxGraph<Station>.DirectedEdge edge : graph.getEdges(from)) {
                Station to = edge.to();
                SaxionApp.drawLine(
                        (int) from.coordinate().getX(),
                        (int) from.coordinate().getY(),
                        (int) to.coordinate().getX(),
                        (int) to.coordinate().getY()
                );
            }
        }
    }
}
