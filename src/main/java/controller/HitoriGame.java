package controller;

import exception.InvalidMoveException;
import model.Cell;
import model.Grid;
import util.FileUtils;

public class HitoriGame {

    private Grid grid;

    public HitoriGame() {}

    public void loadGrid(String level) throws Exception {
        this.grid = FileUtils.loadGridFromFile(level);
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    /**
     * Noircir ou blanchir une case
     */
    public void toggleCell(int row, int col) throws InvalidMoveException {
        Cell cell = grid.getCell(row, col);
        cell.toggleState();
        
        // Vérifier si le coup crée des noires adjacentes
        if (!grid.isMoveValid(row, col)) {
            cell.toggleState(); // Annuler
            throw new InvalidMoveException("Coup invalide : deux cases noires ne peuvent pas être adjacentes !");
        }
    }

    /**
     * Vérifie si le joueur a gagné
     */
    public boolean isGameWon() {
        return grid.isValid();
    }

    /**
     * Réinitialiser la grille (toutes les cases deviennent blanches)
     */
    public void resetGrid() {
        if (grid != null) {
            for (int i = 0; i < grid.getSize(); i++) {
                for (int j = 0; j < grid.getSize(); j++) {
                    grid.getCell(i, j).setState(Cell.State.WHITE);
                }
            }
        }
    }
}