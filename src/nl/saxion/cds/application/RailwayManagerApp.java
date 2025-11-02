package nl.saxion.cds.application;

import nl.saxion.app.SaxionApp;


/**
 * Entry point for the Railway Track Manager application.
 * Now using SaxionApp.startGameLoop() for graphics.
 */
public class RailwayManagerApp {
    public static void main(String[] args) {
        SaxionApp.startGameLoop(new RailwayManager(), 768, 1024, 5);
    }
}
