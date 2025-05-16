package com.slidingpuzzle.model;

import java.util.Arrays;

public class PuzzleBoard {
    private int[][] board;
    private int emptyRow;
    private int emptyCol;
    private final int size = 3; // 3x3 puzzle
    private final int[][] goalState;
    
    public PuzzleBoard() {
        board = new int[size][size];
        goalState = initializeGoalState();
        initializeBoard("204153876"); // Default configuration
    }
    
    public PuzzleBoard(String configuration) {
        board = new int[size][size];
        goalState = initializeGoalState();
        initializeBoard(configuration);
    }
    
    private int[][] initializeGoalState() {
        int[][] goal = new int[size][size];
        int value = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                goal[i][j] = value % (size * size);
                value++;
            }
        }
        return goal;
    }
    
    private void initializeBoard(String configuration) {
        if (configuration.length() != size * size) {
            throw new IllegalArgumentException("Configuration must be " + (size * size) + " characters long");
        }
        
        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = Character.getNumericValue(configuration.charAt(index++));
                board[row][col] = value;
                
                if (value == 0) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }
    }
    
    public boolean moveTile(int row, int col) {
        // Check if the selected tile is adjacent to the empty space
        if (Math.abs(row - emptyRow) + Math.abs(col - emptyCol) != 1) {
            return false;
        }
        
        // Swap the selected tile with the empty space
        board[emptyRow][emptyCol] = board[row][col];
        board[row][col] = 0;
        
        // Update the empty space position
        emptyRow = row;
        emptyCol = col;
        
        return true;
    }
    
    public boolean isGoalState() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != goalState[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public int getValue(int row, int col) {
        return board[row][col];
    }
    
    public int getSize() {
        return size;
    }
    
    public int getEmptyRow() {
        return emptyRow;
    }
    
    public int getEmptyCol() {
        return emptyCol;
    }
} 