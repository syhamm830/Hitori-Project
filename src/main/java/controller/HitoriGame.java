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

    public void toggleCell(int row, int col) throws InvalidMoveException {
        Cell cell = grid.getCell(row, col);
        // Seules les cases jouables (0 dans le fichier) peuvent être modifiées
        if (cell.getValue() == 0) {
            cell.toggleState();
            if (!grid.isMoveValid(row, col)) {
                cell.toggleState(); // annuler le coup
                throw new InvalidMoveException("Coup invalide : règles Hitori violées !");
            }
        } else {
            throw new InvalidMoveException("Cette case est fixe et ne peut pas être modifiée !");
        }
    }

    public boolean isGameWon() {
        return grid.isValid() && grid.areAllWhiteCellsConnected();
    }

    public void resetGrid() {
        if (grid != null) {
            for (int i = 0; i < grid.getSize(); i++)
                for (int j = 0; j < grid.getSize(); j++) {
                    Cell cell = grid.getCell(i, j);
                    if (cell.getValue() == 0) {
                        cell.setState(Cell.State.WHITE);
                    }
                }
        }
    }
}
