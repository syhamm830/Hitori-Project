package model;

import java.util.*;

public class Grid {

    private final int size;
    private final Cell[][] cells;

    public Grid(int size) {
        this.size = size;
        cells = new Cell[size][size];
    }

    public int getSize() {
        return size;
    }

    public void setCell(int row, int col, int value) {
        cells[row][col] = new Cell(value);
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public void toggleCellState(int row, int col) {
        cells[row][col].toggleState();
    }

    // Vérifie toutes les règles
    public boolean isValid() {
        return checkNoDuplicates() && checkNoAdjacentBlacks() && checkConnectedWhiteCells();
    }

    private boolean checkNoDuplicates() {
        for (int i = 0; i < size; i++) {
            boolean[] seenRow = new boolean[size + 1];
            boolean[] seenCol = new boolean[size + 1];
            for (int j = 0; j < size; j++) {
                Cell rowCell = getCell(i, j);
                Cell colCell = getCell(j, i);
                if (rowCell.isWhite()) {
                    if (seenRow[rowCell.getValue()]) return false;
                    seenRow[rowCell.getValue()] = true;
                }
                if (colCell.isWhite()) {
                    if (seenCol[colCell.getValue()]) return false;
                    seenCol[colCell.getValue()] = true;
                }
            }
        }
        return true;
    }

    private boolean checkNoAdjacentBlacks() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isBlack()) {
                    if (i > 0 && getCell(i - 1, j).isBlack()) return false;
                    if (i < size - 1 && getCell(i + 1, j).isBlack()) return false;
                    if (j > 0 && getCell(i, j - 1).isBlack()) return false;
                    if (j < size - 1 && getCell(i, j + 1).isBlack()) return false;
                }
            }
        }
        return true;
    }

    private boolean checkConnectedWhiteCells() {
        boolean[][] visited = new boolean[size][size];
        int startRow = -1, startCol = -1;

        outer:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isWhite()) {
                    startRow = i;
                    startCol = j;
                    break outer;
                }
            }
        }
        if (startRow == -1) return true;

        dfs(startRow, startCol, visited);

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (getCell(i, j).isWhite() && !visited[i][j]) return false;

        return true;
    }

    private void dfs(int row, int col, boolean[][] visited) {
        if (row < 0 || col < 0 || row >= size || col >= size) return;
        if (visited[row][col] || getCell(row, col).isBlack()) return;

        visited[row][col] = true;
        dfs(row - 1, col, visited);
        dfs(row + 1, col, visited);
        dfs(row, col - 1, visited);
        dfs(row, col + 1, visited);
    }
}
