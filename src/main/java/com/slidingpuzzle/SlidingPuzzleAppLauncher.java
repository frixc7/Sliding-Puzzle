package com.slidingpuzzle;

/**
 * Launcher class for the Sliding Puzzle application.
 * This class serves as the main entry point when running from a shaded JAR.
 * It properly initializes JavaFX runtime when running from a packaged JAR file.
 */
public class SlidingPuzzleAppLauncher {
    
    /**
     * Main method that serves as the entry point for the application.
     * It sets up JavaFX modules and then starts the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Pass the command-line arguments to the JavaFX application
        SlidingPuzzleApp.main(args);
    }
}
