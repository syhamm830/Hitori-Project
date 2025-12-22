package model;

import java.util.HashSet;
import java.util.Set;

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

    /**
     * Vérifie si la grille respecte toutes les règles Hitori
     */
    public boolean isValid() {
        return checkNoDuplicates() && checkNoAdjacentBlacks() && checkConnectedWhiteCells();
    }

    /**
     * Règle 1 : Aucune ligne ni colonne ne doit contenir de doublons 
     * parmi les chiffres visibles (cases blanches)
     */
    private boolean checkNoDuplicates() {
        // Vérifier les lignes
        for (int i = 0; i < size; i++) {
            Set<Integer> seenInRow = new HashSet<>();
            for (int j = 0; j < size; j++) {
                Cell cell = getCell(i, j);
                if (cell.isWhite()) {
                    if (seenInRow.contains(cell.getValue())) {
                        System.out.println("❌ Doublon trouvé en ligne " + i + " : " + cell.getValue());
                        return false;
                    }
                    seenInRow.add(cell.getValue());
                }
            }
        }

        // Vérifier les colonnes
        for (int j = 0; j < size; j++) {
            Set<Integer> seenInCol = new HashSet<>();
            for (int i = 0; i < size; i++) {
                Cell cell = getCell(i, j);
                if (cell.isWhite()) {
                    if (seenInCol.contains(cell.getValue())) {
                        System.out.println("❌ Doublon trouvé en colonne " + j + " : " + cell.getValue());
                        return false;
                    }
                    seenInCol.add(cell.getValue());
                }
            }
        }

        System.out.println("✅ Pas de doublons");
        return true;
    }

    /**
     * Règle 2 : Deux cases noircies ne peuvent pas être adjacentes 
     * horizontalement ou verticalement
     */
    private boolean checkNoAdjacentBlacks() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isBlack()) {
                    if (i > 0 && getCell(i - 1, j).isBlack()) {
                        System.out.println("❌ Cases noires adjacentes trouvées en (" + i + "," + j + ")");
                        return false;
                    }
                    if (i < size - 1 && getCell(i + 1, j).isBlack()) {
                        System.out.println("❌ Cases noires adjacentes trouvées en (" + i + "," + j + ")");
                        return false;
                    }
                    if (j > 0 && getCell(i, j - 1).isBlack()) {
                        System.out.println("❌ Cases noires adjacentes trouvées en (" + i + "," + j + ")");
                        return false;
                    }
                    if (j < size - 1 && getCell(i, j + 1).isBlack()) {
                        System.out.println("❌ Cases noires adjacentes trouvées en (" + i + "," + j + ")");
                        return false;
                    }
                }
            }
        }
        System.out.println("✅ Pas de cases noires adjacentes");
        return true;
    }

    /**
     * Règle 3 : Les cases blanches doivent former un seul bloc connexe
     */
    private boolean checkConnectedWhiteCells() {
        boolean[][] visited = new boolean[size][size];
        int startRow = -1, startCol = -1;

        // Trouver la première case blanche
        outerLoop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isWhite()) {
                    startRow = i;
                    startCol = j;
                    break outerLoop;
                }
            }
        }

        if (startRow == -1) {
            System.out.println("❌ Aucune case blanche trouvée");
            return false;
        }

        dfs(startRow, startCol, visited);

        int totalWhite = 0;
        int visitedWhite = 0;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isWhite()) {
                    totalWhite++;
                    if (visited[i][j]) {
                        visitedWhite++;
                    } else {
                        System.out.println("❌ Case blanche non connectée en (" + i + "," + j + ")");
                    }
                }
            }
        }

        System.out.println("📊 Cases blanches : " + visitedWhite + "/" + totalWhite + " connectées");
        return totalWhite == visitedWhite;
    }

    private void dfs(int row, int col, boolean[][] visited) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            return;
        }

        if (visited[row][col] || getCell(row, col).isBlack()) {
            return;
        }

        visited[row][col] = true;

        dfs(row - 1, col, visited);
        dfs(row + 1, col, visited);
        dfs(row, col - 1, visited);
        dfs(row, col + 1, visited);
    }

    /**
     * Vérifie si un coup est valide avant de le jouer
     */
    public boolean isMoveValid(int row, int col) {
        Cell cell = getCell(row, col);
        
        if (cell.isBlack()) {
            // Vérifier qu'elle n'est pas adjacente à une autre noire
            if (row > 0 && getCell(row - 1, col).isBlack()) return false;
            if (row < size - 1 && getCell(row + 1, col).isBlack()) return false;
            if (col > 0 && getCell(row, col - 1).isBlack()) return false;
            if (col < size - 1 && getCell(row, col + 1).isBlack()) return false;
        }

        return true;
    }

    /**
     * Compte le nombre de cases blanches
     */
    public int countWhiteCells() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isWhite()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Compte le nombre de cases noires
     */
    public int countBlackCells() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isBlack()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== État de la grille ===\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = getCell(i, j);
                if (cell.isBlack()) {
                    sb.append(" ● ");
                } else {
                    sb.append(" ").append(cell.getValue()).append(" ");
                }
            }
            sb.append("\n");
        }
        sb.append("========================\n");
        return sb.toString();
    }
}