package nl.saxion.cds.collection;

import java.util.Comparator;
import java.util.Iterator;

/**
 * A directed, weighted graph data structure with nodes and edges. The graph CAN be disconnected.
 *
 * @param <V> type of the nodes in the graph
 */
public class SaxGraph<V> implements SaxCollection<V> {
    // main list holding all graph nodes
    private SaxIndexedCollection<SaxGraph<V>.Node> nodes = new SaxArrayList<>();

    /**
     * Adds a new node with the given value to the graph.
     *
     * @param value the value of the node to be added
     * @throws IllegalArgumentException if value is null or already exists
     */
    public void addNode(V value) throws IllegalArgumentException {
        if (value == null) throw new IllegalArgumentException("Node value cannot be null.");
        if (contains(value)) throw new IllegalArgumentException("Node already exists: " + value);
        nodes.addElement(new Node(value));
    }

    /**
     * Returns a collection of all node values (V) in the graph.
     */
    public SaxIndexedCollection<V> getNodes() {
        SaxIndexedCollection<V> values = new SaxArrayList<>();
        for (int i = 0; i < nodes.getSize(); i++) {
            values.addElement(nodes.getElementAt(i).value);
        }
        return values;
    }

    /**
     * Checks whether a node with the given value exists in the graph.
     *
     * @param value the value to look for
     * @return true if the node exists, false otherwise
     */
    public boolean contains(V value) {
        for (int i = 0; i < nodes.getSize(); i++) {
            if (nodes.getElementAt(i).value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return defaultToString();
    }

    /**
     * Inner class representing a graph node with a value and edges.
     */
    private class Node {
        V value;
        SaxList<DirectedEdge> edges = new SaxList<>();
        Node(V value) {
            this.value = value;
        }
    }


    /**
     * Returns the number of nodes in the graph.
     * @return number of nodes
     */
    @Override
    public int getSize() {
        return nodes.getSize();
    }

    /**
     * Clears all nodes and edges in the graph.
     */
    @Override
    public void clear() {
        nodes = new SaxList<>();
    }

    /**
     * Returns GraphViz dot syntax for visualizing the graph.
     * @param name graph name
     * @return dot format string
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder("digraph ").append(name).append(" {\n");
        for (int i = 0; i < nodes.getSize(); i++) {
            Node node = nodes.getElementAt(i);
            for (int j = 0; j < node.edges.getSize(); j++) {
                DirectedEdge e = node.edges.getElementAt(j);
                sb.append("  \"").append(e.from()).append("\" -> \"")
                        .append(e.to()).append("\" [label=").append(e.weight()).append("]\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }



    /**
     * Gets a list of edges from the given node.
     *
     * @param element the element of the node the edges originate from
     * @return a list of edges which originate from the node with the given element
     */
    public SaxList<DirectedEdge> getEdges(V element) {
        for (int i = 0; i < nodes.getSize(); i++) {
            Node node = nodes.getElementAt(i);
            if (node.value.equals(element)) {
                return node.edges;
            }
        }
        return new SaxList<>(); // return empty if not found
    }

    /**
     * Iterator over node values.
     * @return iterator of node values (V)
     */
    @Override
    public Iterator<V> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodes.getSize();
            }

            @Override
            public V next() {
                if (!hasNext()) throw new IndexOutOfBoundsException();
                return nodes.getElementAt(index++).value;
            }
        };
    }


    /**
     * Adds a directed edge from one node to another.
     */
    public void addEdge(V fromValue, V toValue, double weight) throws IllegalArgumentException {
        if (fromValue == null || toValue == null) throw new IllegalArgumentException("Node value cannot be null");

        Node fromNode = null;
        Node toNode = null;

        for (int i = 0; i < nodes.getSize(); i++) {
            Node n = nodes.getElementAt(i);
            if (n.value.equals(fromValue)) fromNode = n;
            if (n.value.equals(toValue)) toNode = n;
        }

        if (fromNode == null) {
            fromNode = new Node(fromValue);
            nodes.addElement(fromNode);
        }
        if (toNode == null) {
            toNode = new Node(toValue);
            nodes.addElement(toNode);
        }

        // add directed edge from -> to
        fromNode.edges.addElement(new DirectedEdge(fromValue, toValue, weight));
    }

    /**
     * Adds two directed edges: from→to and to→from.
     */
    public void addEdgeBidirectional(V fromValue, V toValue, double weight) throws IllegalArgumentException {
        addEdge(fromValue, toValue, weight);
        addEdge(toValue, fromValue, weight);
    }

    /**
     * Returns total weight of all edges in the graph.
     */
    public double getTotalWeight() {
        double total = 0;
        for (int i = 0; i < nodes.getSize(); i++) {
            Node node = nodes.getElementAt(i);
            for (int j = 0; j < node.edges.getSize(); j++) {
                total += node.edges.getElementAt(j).weight();
            }
        }
        return total;
    }


    /**
     * Execute the Dijkstra algorithm; find shortest paths to all other nodes.
     *
     * @param startNode the node to start from
     * @return new graph containing shortest paths only
     * @throws IllegalArgumentException if startNode is null
     */
    public SaxGraph<V> shortestPathsDijkstra(V startNode) throws IllegalArgumentException {
        if (startNode == null) throw new IllegalArgumentException("Start node cannot be null");

        SaxHashMap<V, Double> dist = new SaxHashMap<>();
        SaxHashMap<V, DirectedEdge> prev = new SaxHashMap<>();
        SaxHashMap<V, Boolean> visited = new SaxHashMap<>();
        SaxHeap<DijkstraNode> queue = new SaxHeap<>(DijkstraNode::compareTo);

        dist.put(startNode, 0.0);
        queue.push(new DijkstraNode(startNode, 0.0));

        while (queue.getSize() > 0) {
            DijkstraNode current = queue.pop();
            if (visited.containsKey(current.node)) continue;
            visited.put(current.node, true);

            SaxList<DirectedEdge> edges = getEdges(current.node);
            for (int i = 0; i < edges.getSize(); i++) {
                DirectedEdge e = edges.getElementAt(i);
                double newDist = dist.get(current.node) + e.weight();

                if (!dist.containsKey(e.to()) || newDist < dist.get(e.to())) {
                    if (dist.containsKey(e.to())) dist.remove(e.to());
                    if (prev.containsKey(e.to())) prev.remove(e.to());

                    dist.put(e.to(), newDist);
                    prev.put(e.to(), e);
                    queue.push(new DijkstraNode(e.to(), newDist));
                }

            }
        }

        SaxGraph<V> result = new SaxGraph<>();
        
        for (int i = 0; i < nodes.getSize(); i++) {
            V value = nodes.getElementAt(i).value;
            if (!result.contains(value)) {
                result.addNode(value);
            }
        }

        SaxList<V> keys = dist.getKeys();
        for (int i = 0; i < keys.getSize(); i++) {
            V node = keys.getElementAt(i);

            if (prev.containsKey(node)) {
                DirectedEdge e = prev.get(node);
                if (!result.contains(e.from())) result.addNode(e.from());
                if (!result.contains(e.to())) result.addNode(e.to());
                result.addEdge(e.from(), e.to(), e.weight());
            } else if (node.equals(startNode)) {
                result.addEdge(node, node, 0.0);
            }
        }

        return result;
    }


    // Inner helper class
    private class DijkstraNode implements Comparable<DijkstraNode> {
        V node;
        double dist;

        DijkstraNode(V node, double dist) {
            this.node = node;
            this.dist = dist;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            return Double.compare(this.dist, other.dist);
        }
    }

    /**
     * Do a depth first traversal through the graph.
     * @param startNode node to start searching
     * @return the list of edges
     * @throws IllegalArgumentException if startNode is null
     */
    public SaxList<DirectedEdge> depthFirstTraversal(V startNode) throws IllegalArgumentException {
        if (startNode == null) throw new IllegalArgumentException("Start node is null");

        SaxList<DirectedEdge> result = new SaxList<>();
        SaxSet<V> visited = new SaxHashSet<>();
        dfsVisit(startNode, visited, result);
        return result;
    }

    // Helper method
    private void dfsVisit(V current, SaxSet<V> visited, SaxList<DirectedEdge> result) {
        visited.add(current);

        SaxList<DirectedEdge> edges = getEdges(current);
        for (int i = 0; i < edges.getSize(); i++) {
            DirectedEdge edge = edges.getElementAt(i);
            V next = edge.to();
            if (!visited.contains(next)) {
                result.addElement(edge);
                dfsVisit(next, visited, result);
            }
        }
    }


    /**
     * Do a depth first search through the graph.
     * @param startNode node to start searching
     * @param endNode node to stop searching or null if all nodes must be returned
     * @return the list of edges
     * @throws IllegalArgumentException if startNode is null or not in graph
     */
    public SaxList<DirectedEdge> depthFirstSearch(V startNode, V endNode) {
        if (startNode == null || endNode == null)
            throw new IllegalArgumentException("Start or end node cannot be null");

        SaxList<DirectedEdge> path = new SaxList<>();
        SaxSet<V> visited = new SaxHashSet<>();
        boolean found = dfsPath(startNode, endNode, visited, path);
        return found ? path : new SaxList<>();
    }

    // Helper with backtracking
    private boolean dfsPath(V current, V end, SaxSet<V> visited, SaxList<DirectedEdge> path) {
        visited.add(current);
        if (end != null && current.equals(end)) return true;

        SaxList<DirectedEdge> edges = getEdges(current);
        for (int i = 0; i < edges.getSize(); i++) {
            DirectedEdge edge = edges.getElementAt(i);
            V next = edge.to();
            if (!visited.contains(next)) {
                path.addElement(edge);
                if (dfsPath(next, end, visited, path)) return true;
                path.removeElementAt(path.getSize() - 1); // backtrack
            }
        }
        return end == null; // return true if end == null (traversal mode)
    }


    /**
     * Return a list of all directed edges in the graph.
     * @return all edges in this graph
     */
    public SaxList<DirectedEdge> getEdges() {
        SaxList<DirectedEdge> all = new SaxList<>();
        for (int i = 0; i < nodes.getSize(); i++) {
            Node node = nodes.getElementAt(i);
            for (int j = 0; j < node.edges.getSize(); j++) {
                all.addElement(node.edges.getElementAt(j));
            }
        }
        return all;
    }


    /**
     * Determine the minimal cost (total weight) of edges which are necessary to connect all nodes.
     * A disconnected graph will still be disconnected, but all edges will be examined;
     * the algorithm must therefore be run on each sub graph.
     *
     * @return the MCST graph (a copy)
     */
    public SaxGraph<V> minimumCostSpanningTree() {
        SaxGraph<V> result = new SaxGraph<>();
        SaxSet<V> visited = new SaxHashSet<>();

        for (int i = 0; i < nodes.getSize(); i++) {
            V start = nodes.getElementAt(i).value;
            if (visited.contains(start)) continue;

            // Prim's Algorithm using a Heap
            Comparator<DirectedEdge> comp = Comparator.comparingDouble(DirectedEdge::weight);
            SaxHeap<DirectedEdge> edgesHeap = new SaxHeap<>(comp);

            visited.add(start);
            result.addNode(start);
            for (int j = 0; j < getEdges(start).getSize(); j++) {
                edgesHeap.push(getEdges(start).getElementAt(j));
            }

            while (edgesHeap.getSize() > 0) {
                DirectedEdge edge = edgesHeap.pop();
                V to = edge.to();

                if (visited.contains(to)) continue;

                visited.add(to);
                result.addNode(to);
                result.addEdge(edge.from(), edge.to(), edge.weight());

                SaxList<DirectedEdge> toEdges = getEdges(to);
                for (int j = 0; j < toEdges.getSize(); j++) {
                    DirectedEdge nextEdge = toEdges.getElementAt(j);
                    if (!visited.contains(nextEdge.to())) {
                        edgesHeap.push(nextEdge);
                    }
                }
            }
        }

        return result;
    }


    /**
     * Determine the minimal trip length of a tour of all nodes.
     *
     * @param nodes nodes to visit (in order)
     * @return a queue of edge-lists (one route per leg of trip) which form the full tour
     */
    public SaxQueue<SaxList<DirectedEdge>> roundTrip(SaxList<V> nodes) {
        SaxQueue<SaxList<DirectedEdge>> trip = new SaxQueue<>();

        for (int i = 0; i < nodes.getSize() - 1; i++) {
            V from = nodes.getElementAt(i);
            V to = nodes.getElementAt(i + 1);

            // Run Dijkstra and build 'prev' map
            SaxHashMap<V, DirectedEdge> prev = new SaxHashMap<>();
            SaxHashMap<V, Double> dist = new SaxHashMap<>();
            SaxHeap<DijkstraNode> heap = new SaxHeap<>(DijkstraNode::compareTo);
            SaxSet<V> visited = new SaxHashSet<>();

            dist.put(from, 0.0);
            heap.push(new DijkstraNode(from, 0.0));

            while (heap.getSize() > 0) {
                DijkstraNode current = heap.pop();
                if (visited.contains(current.node)) continue;
                visited.add(current.node);

                SaxList<DirectedEdge> edges = getEdges(current.node);
                for (int j = 0; j < edges.getSize(); j++) {
                    DirectedEdge e = edges.getElementAt(j);
                    double newDist = dist.get(current.node) + e.weight();
                    if (!dist.containsKey(e.to()) || newDist < dist.get(e.to())) {
                        dist.put(e.to(), newDist);
                        prev.put(e.to(), e);
                        heap.push(new DijkstraNode(e.to(), newDist));
                    }
                }
            }

            // Now backtrack using prev
            SaxList<DirectedEdge> leg = new SaxList<>();
            V current = to;

            while (!current.equals(from)) {
                if (!prev.containsKey(current)) {
                    System.out.println(" No path found from " + from + " to " + to + " — aborting leg.");
                    break;
                }
                DirectedEdge edge = prev.get(current);
                leg.addElementAt(edge, 0);
                current = edge.from();
            }

            trip.push(leg);
        }

        return trip;
    }


    /**
     * Execute the A* algorithm to determine the shortest path from startNode to endNode.
     *
     * @param startNode the node to start searching
     * @param endNode   the target node
     * @param estimator a (handler) function to estimate the distance (weight) between two nodes or null if no estimator
     * @return a list of edges (from start to end) which comprise the shortest path from startNode to endNode. An empty list will be returned if no path can be found
     * @throws IllegalArgumentException if any parameter is null
     */
    public SaxList<SaxGraph<V>.DirectedEdge> shortestPathAStar(V startNode, V endNode, Estimator<V> estimator) throws IllegalArgumentException {
        if (startNode == null || endNode == null || estimator == null)
            throw new IllegalArgumentException("Start, end, or estimator is null");

        SaxHashMap<V, Double> gScore = new SaxHashMap<>();
        SaxHashMap<V, Double> fScore = new SaxHashMap<>();
        SaxHashMap<V, DirectedEdge> cameFrom = new SaxHashMap<>();
        SaxSet<V> visited = new SaxHashSet<>();

        Comparator<V> comp = Comparator.comparingDouble(fScore::get);
        SaxHeap<V> openSet = new SaxHeap<>(comp);

        gScore.put(startNode, 0.0);
        fScore.put(startNode, estimator.estimate(startNode, endNode));
        openSet.push(startNode);

        while (openSet.getSize() > 0) {
            V current = openSet.pop();

            if (current.equals(endNode)) {
                // Reconstruct path
                SaxList<DirectedEdge> path = new SaxList<>();
                V node = endNode;
                while (!node.equals(startNode)) {
                    DirectedEdge edge = cameFrom.get(node);
                    path.addElementAt(edge, 0); // prepend
                    node = edge.from();
                }
                return path;
            }

            visited.add(current);

            SaxList<DirectedEdge> edges = getEdges(current);
            for (int i = 0; i < edges.getSize(); i++) {
                DirectedEdge edge = edges.getElementAt(i);
                V neighbor = edge.to();
                if (visited.contains(neighbor)) continue;

                double tentativeG = gScore.get(current) + edge.weight();
                if (!gScore.containsKey(neighbor) || tentativeG < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, edge);
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + estimator.estimate(neighbor, endNode));

                    if (!openSet.contains(neighbor)) {
                        openSet.push(neighbor);
                    }
                }
            }
        }
        return new SaxList<>();
    }


    @FunctionalInterface
    public interface Estimator<T> {
        double estimate(T current, T target);
    }

    /**
     * A directed edge in the graph.
     */
    public final class DirectedEdge {
        private final V from;
        private final V to;
        private final double weight;
        /**
         * @param from   from node
         * @param to     to node
         * @param weight weight
         */
        public DirectedEdge(V from, V to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public V from() {
            return from;
        }

        public V to() {
            return to;
        }

        public double weight() {
            return weight;
        }

        public int compare(DirectedEdge o2) {
            return Double.compare(this.weight, o2.weight);
        }

        @Override
        public String toString() {
            return from + " → " + to + " (" + weight + ")";
        }
    }
}
