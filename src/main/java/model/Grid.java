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
     * parmi les chiffres visibles (cases blanches non jouables)
     */
    private boolean checkNoDuplicates() {
        // Vérifier les lignes
        for (int i = 0; i < size; i++) {
            Set<Integer> seenInRow = new HashSet<>();
            for (int j = 0; j < size; j++) {
                Cell cell = getCell(i, j);
                // On compte uniquement les cellules blanches avec valeur fixe
                if (cell.isWhite() && !cell.isPlayable()) {
                    int value = cell.getValue();
                    if (value > 0) { // Ignorer les 0
                        if (seenInRow.contains(value)) {
                            return false; // Doublon trouvé
                        }
                        seenInRow.add(value);
                    }
                }
            }
        }

        // Vérifier les colonnes
        for (int j = 0; j < size; j++) {
            Set<Integer> seenInCol = new HashSet<>();
            for (int i = 0; i < size; i++) {
                Cell cell = getCell(i, j);
                // On compte uniquement les cellules blanches avec valeur fixe
                if (cell.isWhite() && !cell.isPlayable()) {
                    int value = cell.getValue();
                    if (value > 0) { // Ignorer les 0
                        if (seenInCol.contains(value)) {
                            return false; // Doublon trouvé
                        }
                        seenInCol.add(value);
                    }
                }
            }
        }

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
                    // Vérifier en haut
                    if (i > 0 && getCell(i - 1, j).isBlack()) {
                        return false;
                    }
                    // Vérifier en bas
                    if (i < size - 1 && getCell(i + 1, j).isBlack()) {
                        return false;
                    }
                    // Vérifier à gauche
                    if (j > 0 && getCell(i, j - 1).isBlack()) {
                        return false;
                    }
                    // Vérifier à droite
                    if (j < size - 1 && getCell(i, j + 1).isBlack()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Règle 3 : Les cases blanches restantes doivent former 
     * un seul bloc connexe
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

        // Si aucune case blanche n'existe (peu probable), c'est valide
        if (startRow == -1) {
            return true;
        }

        // Parcours en profondeur depuis la première case blanche
        dfs(startRow, startCol, visited);

        // Vérifier que toutes les cases blanches ont été visitées
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).isWhite() && !visited[i][j]) {
                    return false; // Une case blanche n'est pas connectée
                }
            }
        }

        return true;
    }

    /**
     * Parcours en profondeur (DFS) pour vérifier la connexité
     */
    private void dfs(int row, int col, boolean[][] visited) {
        // Vérifier les limites
        if (row < 0 || col < 0 || row >= size || col >= size) {
            return;
        }

        // Si déjà visité ou case noire, arrêter
        if (visited[row][col] || getCell(row, col).isBlack()) {
            return;
        }

        // Marquer comme visité
        visited[row][col] = true;

        // Explorer les 4 directions
        dfs(row - 1, col, visited); // Haut
        dfs(row + 1, col, visited); // Bas
        dfs(row, col - 1, visited); // Gauche
        dfs(row, col + 1, visited); // Droite
    }

    /**
     * Vérifie si un coup est valide (utilisé pour empêcher les coups invalides)
     */
    public boolean isMoveValid(int row, int col) {
        Cell cell = getCell(row, col);
        
        // La cellule doit être jouable
        if (!cell.isPlayable()) {
            return false;
        }

        // Si la cellule est noire, vérifier qu'elle n'est pas adjacente à une autre noire
        if (cell.isBlack()) {
            // Vérifier les 4 directions
            if (row > 0 && getCell(row - 1, col).isBlack()) return false;
            if (row < size - 1 && getCell(row + 1, col).isBlack()) return false;
            if (col > 0 && getCell(row, col - 1).isBlack()) return false;
            if (col < size - 1 && getCell(row, col + 1).isBlack()) return false;
        }

        // Vérifier que les cases blanches restent connectées
        // (Optionnel : cette vérification peut être trop stricte pendant le jeu)
        // On peut la désactiver et ne vérifier qu'à la fin
        
        return true;
    }

    /**
     * Méthode publique pour vérifier la connexité des cases blanches
     */
    public boolean areAllWhiteCellsConnected() {
        return checkConnectedWhiteCells();
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

    /**
     * Affiche la grille en mode console (utile pour debug)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = getCell(i, j);
                if (cell.isBlack()) {
                    sb.append(" X ");
                } else if (cell.isPlayable()) {
                    sb.append(" . ");
                } else {
                    sb.append(" ").append(cell.getValue()).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}