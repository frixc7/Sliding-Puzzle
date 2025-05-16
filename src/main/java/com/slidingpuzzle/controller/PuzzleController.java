package com.slidingpuzzle.controller;

import com.slidingpuzzle.model.PuzzleBoard;

import java.util.Timer;
import java.util.TimerTask;

public class PuzzleController {
    private PuzzleBoard board;
    private int moves;
    private int score;
    private int bestScore;
    private boolean gameWon = false;
    private String currentConfiguration; // Stores the current configuration
    
    // Timer related fields
    private long startTime;
    private long elapsedTime;
    private boolean timerRunning;
    private Timer timer;
    private TimerTask timerTask;
    private TimerUpdateCallback timerCallback;
    
    // Interface for timer updates
    public interface TimerUpdateCallback {
        void onTimerUpdate(long elapsedTimeMillis);
    }
    
    public PuzzleController() {
        this.currentConfiguration = "204153876"; // Default configuration
        this.board = new PuzzleBoard(currentConfiguration);
        this.moves = 100; // Starting number of moves
        this.score = 0;
        this.bestScore = 0;
        initializeTimer();
    }
    
    public PuzzleController(String configuration) {
        this.currentConfiguration = configuration;
        this.board = new PuzzleBoard(configuration);
        this.moves = 100; // Starting number of moves
        this.score = 0;
        this.bestScore = 0;
        initializeTimer();
    }
    
    public boolean makeMove(int row, int col) {
        if (gameWon || moves <= 0) {
            return false;
        }
        
        if (board.moveTile(row, col)) {
            moves--;
            score += 10; // Increase score for each move
            
            // Check if the game is won after the move
            if (board.isGoalState()) {
                gameWon = true;
                stopTimer(); // Stop the timer when game is won
                if (score > bestScore) {
                    bestScore = score;
                }
            }
            
            return true;
        }
        
        return false;
    }
    
    public boolean isMoveValid(int row, int col) {
        int emptyRow = board.getEmptyRow();
        int emptyCol = board.getEmptyCol();
        
        // Check if the tile is adjacent to the empty space
        return (Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1);
    }
    
    /**
     * Resets the board to a specific configuration
     * @param configuration The puzzle configuration string
     */
    public void resetToConfiguration(String configuration) {
        this.currentConfiguration = configuration;
        this.board = new PuzzleBoard(configuration);
        this.moves = 100;
        this.score = 0;
        this.gameWon = false;
        resetTimer(); // Reset the timer when board is reset
    }
    
    /**
     * Resets the board to the original shuffled configuration
     */
    public void resetToCurrent() {
        // Reset using the stored configuration
        resetToConfiguration(currentConfiguration);
    }
    
    /**
     * Backward compatibility method for reset
     * @param configuration The puzzle configuration string
     */
    public void reset(String configuration) {
        resetToConfiguration(configuration);
    }
    
    public boolean isGameWon() {
        return gameWon;
    }
    
    public PuzzleBoard getBoard() {
        return board;
    }
    
    public int getMoves() {
        return moves;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getBestScore() {
        return bestScore;
    }
    
    /* Timer related methods */
    
    private void initializeTimer() {
        elapsedTime = 0;
        timerRunning = false;
    }
    
    public void startTimer() {
        if (timerRunning) return;
        
        timerRunning = true;
        startTime = System.currentTimeMillis() - elapsedTime;
        
        if (timer != null) {
            timer.cancel();
        }
        
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (timerRunning) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    if (timerCallback != null) {
                        timerCallback.onTimerUpdate(elapsedTime);
                    }
                }
            }
        };
        
        // Update every 100ms
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }
    
    public void stopTimer() {
        timerRunning = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    
    public void resetTimer() {
        stopTimer();
        elapsedTime = 0;
        if (timerCallback != null) {
            timerCallback.onTimerUpdate(elapsedTime);
        }
        startTimer();
    }
    
    public void setTimerCallback(TimerUpdateCallback callback) {
        this.timerCallback = callback;
    }
    
    public long getElapsedTime() {
        return elapsedTime;
    }
} 