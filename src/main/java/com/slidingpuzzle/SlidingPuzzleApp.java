package com.slidingpuzzle;

import com.slidingpuzzle.view.PuzzleView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SlidingPuzzleApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a PuzzleView with no default configuration - it will show the selection screen
        PuzzleView puzzleView = new PuzzleView();
        Scene scene = new Scene(puzzleView, 650, 500);
        
        primaryStage.setTitle("Sliding Puzzle Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 