# Project Explanation – Railway Track Manager (CDS Project)

This project was created for the **Complex Data Structures** course at Saxion University. The main goal was to build a working railway track manager system using only custom data structures from the Saxion Collection Framework. No classes from `java.util` were used (except `Comparator`, `Iterator`, `Scanner`, or `Random` as allowed by course rules).

---

##  Purpose

The project shows how different data structures and algorithms can be used in a realistic simulation. It works with real data from Dutch train stations and tracks, and it includes both a **console-based menu** and a **graphical interface** made with `SaxionApp`.

---

##  What I Have Done

### 1. Console and GUI System

- Created a console menu where users can choose actions.
- Used `SaxionApp.GameLoop` to draw the railway map and show paths and stations.
- Combined user interaction (console) with visual updates (canvas).

### 2. Data Import with `CsvLoader`

- Loaded data from two CSV files:
  - `stations.csv` – 397 train stations.
  - `tracks.csv` – 868 rail connections.
- Each station has a name, code, type, and coordinates.
- Each track includes the two station codes and the distance.

Data is stored using:
- `SaxList<Station>` for the station list.
- `SaxGraph<Station>` for the railway network graph.

### 3. Search Functionality

- Users can search for stations by name or partial name.
- Matching results are shown in the console.
- The map highlights selected stations in red or blue.

### 4. Shortest Route (Dijkstra)

- Implemented Dijkstra’s algorithm on the graph.
- Users can pick two stations to calculate the shortest route.
- The total distance is shown, and the path is drawn on the map in blue.

### 5. Round Trip Calculation

- Allows choosing 3 or more stations for a round trip.
- All possible permutations are calculated.
- The shortest round trip is shown and drawn on the map.

### 6. Minimum Cost Spanning Tree (MCST)

- Used Prim’s algorithm to calculate MCST.
- Shows the minimum number of rail connections needed to connect all stations.
- Tracks are highlighted in green.

### 7. Custom Data Structures

Implemented and used the following custom classes from the Saxion Collection Framework:

- `SaxList<T>` – Doubly linked list
- `SaxArrayList<T>` – Array list
- `SaxStack<T>` – Stack
- `SaxQueue<T>` – Queue
- `SaxHashMap<K, V>` – Hash map with chaining
- `SaxHeap<T>` – Min/Max heap (used in Dijkstra)
- `SaxBinarySearchTree<T>` – AVL tree (used optionally)
- `SaxGraph<V>` – Graph with support for edges, paths, MCST

### 8. Testing with JUnit 5

- Over 90% class, method, line, and branch coverage.
- Used `@Test`, `@BeforeEach`, `assertEquals`, `assertTrue`, `assertThrows`, etc.
- Included good-weather and bad-weather cases.
- GraphViz calls are included but excluded from coverage.

---

##  Learning Goals Covered

| Competence | Learning goal |
|------------|----------------|
| **SW/ANA/2** | You make estimates of the complexity of algorithms and, based on that, you compare the efficiency of similar algorithms. |
| **SW/REA/2** | You know the properties of linear data structures (queue, stack, map, hash table) and nonlinear data structures (tree, heap, count) and apply them in a given context. |
| **SW/ONT/2** | You make reasoned choices in appropriate data structures for internal storage and apply them in a given context. |
| **SW/REA/2** | You know some graph algorithms (depthfirst, breadthfirst, shortest paths, minimum cost spanning tree) and apply them in a given context. |
| **SW/REA/2** | You apply recursion and backtracking for appropriate problems. |
| **SW/ONT/2** | You apply assertions and write thorough unit tests, demonstrating that your algorithms and data structures are correct. |

---


##  Final Notes

This project helped me understand how to build real software using clean and efficient code. I practiced:

- Graph algorithms like Dijkstra and Prim
- Writing custom data structures
- Understanding Big-O complexity
- Using recursion for permutations
- Writing readable and well-tested Java code
- Drawing graphics with SaxionApp

I followed all course rules, avoided `java.util`, and explained every method and structure I used.
