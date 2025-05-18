# Sliding Puzzle Game

A JavaFX implementation of the classic Sliding Puzzle game.

## Features

- Puzzle Selection
- Move tracking
- Scoring system
- Game controls

## Tech Stack

- Java
- JavaFX
- PlantUML (for UML diagrams)

## How to Run

### Option 1: From Source
1. Clone the repository
2. Build the project: `mvn clean package`
3. Run the application: `mvn javafx:run`

### Option 2: Executable JAR
1. Download the `sliding-puzzle-1.0-SNAPSHOT.jar` file
2. Double-click the JAR file to run it
   - Alternatively, run via command line: `java -jar sliding-puzzle-1.0-SNAPSHOT.jar`

### Requirements for Running JAR File
- Java 17 or higher must be installed
- The current JAR is built for Linux and contains platform-specific JavaFX libraries
- For Windows/macOS, build a platform-specific JAR or run via command line
- Ensure your system is configured to open JAR files with Java

## Game Instructions

- Click on a tile adjacent to the empty space to move it
- Rearrange the tiles to get the following configuration: 123456780

## UML Diagrams

- All UML diagrams are stored in the `umls` directory.
- All exported versions of the UML diagrams are stored in the `out/umls` directory.
