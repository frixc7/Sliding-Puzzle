package com.slidingpuzzle.view;

import com.slidingpuzzle.controller.PuzzleController;
import com.slidingpuzzle.model.PuzzleBoard;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PuzzleView extends BorderPane {
    private PuzzleController controller;
    private GridPane puzzleGrid;
    private Label movesLabel;
    private Label scoreLabel;
    private Label bestScoreLabel;
    private Label timerLabel; // Label to display the timer
    private Button[][] tileButtons; // Store references to tile buttons
    private boolean animationInProgress = false; // Flag to prevent multiple animations at once
    
    public PuzzleView() {
        this.controller = new PuzzleController();
        initializeUI();
    }
    
    public PuzzleView(String configuration) {
        this.controller = new PuzzleController(configuration);
        initializeUI();
    }
    
    private void initializeUI() {
        setupPuzzleGrid();
        setupControlPanel();
        setupInfoPanel();
        startTimer();
    }
    
    private void setupPuzzleGrid() {
        puzzleGrid = new GridPane();
        puzzleGrid.setHgap(5);
        puzzleGrid.setVgap(5);
        puzzleGrid.setPadding(new Insets(10));
        
        // Initialize the tile buttons array
        int size = controller.getBoard().getSize();
        tileButtons = new Button[size][size];
        
        updatePuzzleGrid();
        
        setCenter(puzzleGrid);
    }
    
    private void updatePuzzleGrid() {
        puzzleGrid.getChildren().clear();
        PuzzleBoard board = controller.getBoard();
        
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                int value = board.getValue(row, col);
                
                if (value != 0) { // Don't create a button for the empty space
                    Button tileButton = createTileButton(value);
                    final int r = row;
                    final int c = col;
                    tileButton.setOnAction(e -> handleTileClick(r, c));
                    puzzleGrid.add(tileButton, col, row);
                    tileButtons[row][col] = tileButton;
                } else {
                    tileButtons[row][col] = null; // Empty space
                }
            }
        }
    }
    
    private void handleTileClick(int row, int col) {
        if (animationInProgress) {
            return; // Prevent clicks during animation
        }
        
        if (controller.isMoveValid(row, col)) {
            // Find the empty cell position before the move
            int emptyRow = controller.getBoard().getEmptyRow();
            int emptyCol = controller.getBoard().getEmptyCol();
            
            // Make the move in the controller
            if (controller.makeMove(row, col)) {
                // Animate the tile movement
                animateTileMovement(row, col, emptyRow, emptyCol);
                
                // Update info labels
                updateInfoLabels();
                
                if (controller.isGameWon()) {
                    showWinMessage();
                    // Timer is already stopped in controller
                }
            }
        }
    }
    
    private void updateInfoLabels() {
        movesLabel.setText("Moves: " + controller.getMoves());
        scoreLabel.setText("Score: " + controller.getScore());
        bestScoreLabel.setText("Best Score: " + controller.getBestScore());
    }
    
    private void updateTimerLabel(long elapsedTimeMillis) {
        long minutes = (elapsedTimeMillis / 1000) / 60;
        long seconds = (elapsedTimeMillis / 1000) % 60;
        long tenths = (elapsedTimeMillis % 1000) / 100;
        
        String timeString = String.format("%02d:%02d.%d", minutes, seconds, tenths);
        timerLabel.setText("Time: " + timeString);
    }
    
    private void startTimer() {
        controller.startTimer();
    }
    
    private void showWinMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Won");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("You solved the puzzle with a score of " + controller.getScore() + "!");
        alert.showAndWait();
    }
    
    private Button createTileButton(int value) {
        Button button = new Button(String.valueOf(value));
        button.setPrefSize(80, 80);
        button.setFont(Font.font(20));
        // Add a nice visual style to the buttons
        button.setStyle("-fx-background-radius: 5; -fx-background-color: #f0f0f0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);");
        return button;
    }
    
    /**
     * Animates the tile movement from the source position to the empty position
     */
    private void animateTileMovement(int fromRow, int fromCol, int toRow, int toCol) {
        Button tileButton = tileButtons[fromRow][fromCol];
        
        // Calculate the direction and distance to move
        double moveX = (toCol - fromCol) * (tileButton.getWidth() + puzzleGrid.getHgap());
        double moveY = (toRow - fromRow) * (tileButton.getHeight() + puzzleGrid.getVgap());
        
        // Create the animation
        TranslateTransition transition = new TranslateTransition(Duration.millis(150), tileButton);
        transition.setByX(moveX);
        transition.setByY(moveY);
        
        // Set animation flag
        animationInProgress = true;
        
        // When animation is done, update the grid
        transition.setOnFinished(e -> {
            // Reset the translation for the next animation
            tileButton.setTranslateX(0);
            tileButton.setTranslateY(0);
            
            // Update the grid after animation
            updatePuzzleGrid();
            
            // Clear animation flag
            animationInProgress = false;
        });
        
        // Start the animation
        transition.play();
    }
    
    private void setupControlPanel() {
        HBox controlPanel = new HBox(10);
        controlPanel.setPadding(new Insets(10));
        
        Button rearrangeButton = new Button("Rearrange");
        rearrangeButton.setOnAction(e -> {
            controller.resetToCurrent();
            updatePuzzleGrid();
            updateInfoLabels();
        });
        
        Button shuffleButton = new Button("Shuffle");
        shuffleButton.setOnAction(e -> {
            // Randomly select one configuration from the preset list
            String[] configurations = {"073214568", "124857063", "204153876", "624801753", "670132584", "781635240", "280163547"};
            int randomIndex = (int) (Math.random() * configurations.length);
            controller.reset(configurations[randomIndex]);
            updatePuzzleGrid();
            updateInfoLabels();
        });
        
        controlPanel.getChildren().addAll(rearrangeButton, shuffleButton);
        setBottom(controlPanel);
    }
    
    private void setupInfoPanel() {
        Label titleLabel = new Label("Sliding Puzzle");
        titleLabel.setFont(Font.font(24));
        
        movesLabel = new Label("Moves: " + controller.getMoves());
        scoreLabel = new Label("Score: " + controller.getScore());
        bestScoreLabel = new Label("Best Score: " + controller.getBestScore());
        timerLabel = new Label("Time: 00:00.0");
        
        // Set up timer callback
        controller.setTimerCallback(elapsedTimeMillis -> {
            Platform.runLater(() -> {
                updateTimerLabel(elapsedTimeMillis);
            });
        });
        
        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.getChildren().addAll(titleLabel, movesLabel, scoreLabel, bestScoreLabel, timerLabel);
        
        setRight(infoPanel);
    }
} 