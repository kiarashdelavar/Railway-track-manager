# CDS Assignment

## Submission

A correct submission contains:

* The code of the developed program; create a zip export from GitLab, test this export (does it all still work?) and make sure it contains **no build artifacts** (such as an out folder) and no **.git folder**.
* The code works with Java 21, uses JUnit Jupiter 5 and contains no absolute path names.
* No libraries are used other than the aforementioned test framework, a GUI framework (SaxionApp) and the standard Java libraries, such as java.io and/or java.math, but **NOT java.util**! If in doubt, contact your lecturer about this in advance.
* The [documentation](#documentation) is in the doc folder as a **Markdown** file and contains name and student number.

**An incorrect submission will NOT be assessed.**

## Requirements

It is up to you to build a proof of concept for a railway track manager application with the following tasks:

* Reading (at the start) all Dutch stations from [stations.csv](../resources/stations.csv) (there are 397 of them) and the corresponding connections from [tracks.csv](../resources/tracks.csv) (there are 868 of them, total length 5117.6 kilometers, every connection is doubled; from A to B and from B to A).
* Display information of a station (code, name, type) based on (the beginning of) the name. Allow the user to choose a station if more stations start with the searched part of the name.
* Determine the shortest route between two stations, to be chosen by the user; show the route and **total length**.
* Determine the shortest round trip (so the first station is both start and end point) between three or more stations, to be chosen by the user; show the route and the **total length**.
* Determine the minimum number of rail connections needed so that you can get from an arbitrary station within the Netherlands to any other station within the Netherlands (minimum cost spanning tree, MCST). Show the tracks and the **total length**.
* Show the track network, route, round trip and MCST in a graphical representation. Use SaxionApp for this or **consult (and agree) in advance with your teacher which GUI framework** you want to use. An example map of the Netherlands that you can use is in [resources](../resources/Nederland.png); Use the following examples to scale the stations (more or less) correctly:
  * HDR - Den Helder approximately at (600, 400),
  * MT - Maastricht approximately at (1024, 1840) and
  * DZ - Delfzijl approximately at (1490, 150).

Create a (console) application with a menu to invoke all functionalities. Use the SaxionApp.GameLoop to refresh the graphics (when necessary). Making a full graphical version is also an option; so then no menu is needed, but e.g. buttons to execute the aforementioned functionalities.

Add JavaDoc comments to new classes and (public) methods and create appropriate unit tests (see also [Testing](#testing)).

## SaxCollection - the Saxion Collection Framework

The application must use the Saxion Collection Framework classes, which are largely to be implemented yet:

* A doubly linked list: [*SaxList*](../src/nl/saxion/cds/collection/SaxList.java)
* An array based list: [*SaxArrayList*](../src/nl/saxion/cds/collection/SaxArrayList.java)
* A hash map: [*SaxHashMap*](../src/nl/saxion/cds/collection/SaxHashMap.java).
* A binary search tree (BST), preferably implemented as an AVL tree:[*SaxBinarySearchTree*](../src/nl/saxion/cds/collection/SaxBinarySearchTree.java)
* A queue: [*SaxQueue*](../src/nl/saxion/cds/collection/SaxQueue.java)
* A stack: [*SaxStack*](../src/nl/saxion/cds/collection/SaxStack.java)
* A heap implementation **based on an array**: [*SaxHeap*](../src/nl/saxion/cds/collection/SaxHeap.java)
* A generic graaf: [*SaxGraph*](../src/nl/saxion/cds/collection/SaxGraph.java)

The use of the **Java collection classes and interfaces is NOT ALLOWED**, so use **nothing** from java.util, *except for Comparator, Iterator, Random and/or Scanner*.

## Documentation

Create technical documentation *about the application* in [Markdown](https://www.markdownguide.org/basic-syntax/) and also place it in this doc folder.

* Create a class diagram with a short description of the different classes of the application.
* Create a chapter for each application class and include a link to the relevant source code in it.
* Create a paragraph for each of the main methods of that application class   (and any other method that uses a SaxCollection class) with:
  * an outline how it functions and
  * of every SaxCollection class used in that method
    * explain why precisely this one is used (preferably using big-Oh qualifications) and
    * which other SaxCollection class(es) could also be used and explain why it was not preferred.

## Testing

At least 75% of the test cases for the SaxCollection classes must be successfully developed with JUnit 5 (coverage over 90% will result in extra points).

* **Coverage** refers to **the minimum coverage percentage** of class, method, line, and *branch* coverage.
* Make comments of the asserts, if necessary, so that they do not affect branch coverage.
* For parameters with restrictions, both good-weather and bad-weather tests must be provided.
* Getters and setters may be excluded from test coverage, provided they only consist of returning the corresponding variable or modifying the corresponding variable. Mark these methods accordingly (see the slides) so that they do not count towards the coverage percentages.
* **Ensure that the examples in the course material are included in the tests.**
* Exclude the *GraphViz* functions from the test coverage as well, but make sure *SaxGraph.GraphViz* is called in the tests of the course examples, so that it is easy to also visually verify on [GraphvizOnline](https://dreampuf.github.io/GraphvizOnline) whether it is correct. This can also be useful for debugging other tests.
* When an exception is possible, that situation must be tested; at minimum, test for the correct exception type.
