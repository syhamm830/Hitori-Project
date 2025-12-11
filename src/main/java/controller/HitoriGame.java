package controller;

import exception.InvalidMoveException;
import model.Cell;
import model.Grid;
import util.FileUtils;
import util.TimerUtil;

public class HitoriGame {

    private Grid grid;
    private TimerUtil timer;

    public HitoriGame() {
        timer = new TimerUtil();
    }

    public void loadGrid(String path) throws Exception {
        this.grid = FileUtils.loadGridFromFile(path);
        timer.start();
    }

    public Grid getGrid() {
        return grid;
    }

    public void toggleCell(int row, int col) throws InvalidMoveException {
        Cell cell = grid.getCell(row, col);
        cell.toggle();
        validateMove(row, col);
    }

    private void validateMove(int row, int col) throws InvalidMoveException {
        if (!grid.isMoveValid(row, col)) {
            throw new InvalidMoveException("Coup invalide : règles Hitori violées !");
        }
    }

    public boolean isGameWon() {
        return grid.isValid() && grid.areAllWhiteCellsConnected();
    }

    public void stopTimer() {
        timer.stop();
    }

    public int getElapsedTime() {
        return timer.getElapsedSeconds();
    }
}
