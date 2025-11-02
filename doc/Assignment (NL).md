# CDS Opdracht

## Inlevering

Een juiste inlevering bevat:

* De code van het ontwikkelde programma; maak een zip export vanuit GitLab, test deze export (werkt het allemaal nog?) en zorg dat er **geen build artefacten** (zoals een out map) en geen **.git map** in zit.
* De code werkt met Java 21, gebruikt JUnit Jupiter 5 en bevat geen absolute pad-namen.
* Er worden geen andere libraries gebruikt dan het genoemde test framework, een goedgekeurd GUI framework (SaxionApp) en de standaard Java libraries, zoals java.io en/of java.math, maar **GEEN java.util**! Neem in geval van twijfel vooraf contact met je docent hierover op.
* De [documentatie](#documentatie) staat in de doc map als **Markdown**-bestand en bevat naam en studentnummer.

**Een onjuiste inlevering wordt NIET beoordeeld.**

## Requirements

Aan jou de taak om een proof of concept voor een spoormanager-applicatie te bouwen met de volgende taken:

* Inlezen (bij de start) van alle Nederlandse stations uit [stations.csv](../resources/stations.csv) (dit zijn er 397) en de bijbehorende verbindingen uit [tracks.csv](../resources/tracks.csv) (dit zijn er 868, totale lengte 5117,6 kilometer, iedere verbinding zit er dubbel in; van A naar B en van B naar A).
* Toon informatie van een station (code, naam, type) op basis van (het begin van) de naam. Laat de gebruiker een station kiezen indien er meer stations beginnen met het gezochte deel van de naam.
* Bepaal de kortste route tussen twee te kiezen stations; toon de route en de **totale lengte**.
* Bepaal de kortste rondgang (het eerste station is dus zowel start als eindpunt) tussen drie of meer te kiezen stations; toon de route en de **totale lengte**.
* Bepaal welke spoorverbindingen er minimaal nodig zijn, zodat je vanuit een willekeurig station binnen Nederland naar ieder ander station binnen Nederland kunt komen (=minimum cost spanning tree, MCST). Toon de tracks en de **totale lengte**.
* Toon het spoornetwerk, de route, de rondgang en de MCST in een grafische weergave. Gebruik hiervoor SaxionApp of **stem vooraf met je docent af welk GUI framework** je wilt gaan gebruiken. Een voorbeeldkaart van Nederland die je hiervoor kunt gebruiken staat in [resources](../resources/Nederland.png); Gebruik de volgende voorbeelden om de stations (min of meer) juist te schalen:
  * HDR - Den Helder ongeveer op (600, 400),
  * MT - Maastricht ongeveer op (1024, 1840) en
  * DZ - Delfzijl ongeveer op (1490, 150).

Maak een (console) applicatie met een menu om alle functionaliteiten aan te roepen. Gebruik de SaxionApp.GameLoop om de grafische weergave (wanneer noodzakelijk) te verversen. Ook het maken van een volledige grafische versie is een optie; dan is er dus geen menu nodig, maar bijv. knoppen om genoemde functionaliteiten uit te kunnen voeren.

Voeg JavaDoc comments aan nieuwe klassen en (public) methodes toe en maak de juiste unit-testen aan (zie ook [Testen](#testen)).

## SaxCollection - het Saxion Collection Framework

De applicatie moet gebruik maken van de Saxion Collection Framework klassen,
dat de volgende klassen bevat, die grotendeels nog geïmplementeerd moeten worden:

* Een dubbel gelinkte list: [*SaxList*](../src/nl/saxion/cds/collection/SaxList.java)
* Een lijst op basis van een array: [*SaxArrayList*](../src/nl/saxion/cds/collection/SaxArrayList.java)
* Een hash map: [*SaxHashMap*](../src/nl/saxion/cds/collection/SaxHashMap.java).
* Een binary search tree (BST), bij voorkeur uitgewerkt als AVL-tree: [*SaxBinarySearchTree*](../src/nl/saxion/cds/collection/SaxBinarySearchTree.java)
* Een queue: [*SaxQueue*](../src/nl/saxion/cds/collection/SaxQueue.java)
* Een stack: [*SaxStack*](../src/nl/saxion/cds/collection/SaxStack.java)
* Een heap implementatie **op basis van een array**: [*SaxHeap*](../src/nl/saxion/cds/collection/SaxHeap.java)
* Een generieke graaf: [*SaxGraph*](../src/nl/saxion/cds/collection/SaxGraph.java)

Het gebruik van de **Java collection classes en interfaces is NIET TOEGESTAAN**, dus **NIETS** gebruiken uit java.util, *behalve Comparator, Iterator, Random en/of Scanner*.

## Documentatie

Maak technische documentatie *over de applicatie* in [Markdown](https://www.markdownguide.org/basic-syntax/) en plaats deze ook in deze doc-folder.

* Start de documentatie met je volledige naam en studentnummer en een link naar jouw GitLab repository.
* Maak een klassendiagram van de applicatie met een korte beschrijving van de verschillende klassen.
* Maak een hoofdstuk per applicatie klasse en neem een link naar de betreffende broncode hierin op.
* Maak voor de belangrijkste methodes van die applicatieklasse (en iedere andere methode die een SaxCollection klasse gebruikt) een paragraaf en beschrijf daarin:
  * op hoofdlijnen hoe deze functioneert en
  * van iedere gebruikte SaxCollection klasse in die methode
    * onderbouw waarom juist deze wordt gebruikt (gebruik hiervoor bij voorkeur big-Oh kwalificaties) en
    * welke andere SaxCollection klasse(n) ook zou kunnen worden gebruikt en onderbouw waarom die dus niet de voorkeur had.

## Testen

Er moeten voor tenminste 75% (>90% geeft extra punten) dekkende geslaagde testcases zijn uitgewerkt voor de SaxCollection klassen met JUnit 5.

* Met **dekkende** wordt bedoeld **het minimum dekkingspercentage** van class, method, line en *branch* coverage.
* Maak eventueel comments van de asserts, zodat ze de branch coverage niet beïnvloeden.
* Voor parameters met een restrictie moeten er good-weather en bad-weather tests gemaakt zijn.
* Getters en setters mogen worden uitgesloten van de test coverage, mits deze alleen bestaan uit het retourneren van de bijbehorende variabele respectievelijk het rechtstreeks wijzigen van de bijbehorende variabele. Markeer deze methodes dan als zodanig (zie de sheets), zodat ze niet meetellen in de dekkingspercentages.
* **Zorg ervoor dat je voorbeelden die in het lesmateriaal staan meeneemt in de testen**.
* Sluit de *GraphViz* functies ook uit van de test coverage, maar zorg dat *SaxGraph.GraphViz* bij de testen van de voorbeelden uit het lesmateriaal wordt aangeroepen, zodat het eenvoudig ook visueel te controleren is op [GraphvizOnline](https://dreampuf.github.io/GraphvizOnline) of het klopt. Ook voor het debuggen van andere testen kan dit handig zijn.
* Wanneer een Exception mogelijk is, moet die situatie getest worden; test minimaal het juiste exceptie type.
