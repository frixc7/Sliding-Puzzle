package com.slidingpuzzle;

import com.slidingpuzzle.view.PuzzleView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SlidingPuzzleApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        PuzzleView puzzleView = new PuzzleView("204153876"); // Using default configuration
        Scene scene = new Scene(puzzleView, 600, 400);
        
        primaryStage.setTitle("Sliding Puzzle Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 