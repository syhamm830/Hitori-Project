package controller;

import model.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.FileUtils;

import static org.junit.jupiter.api.Assertions.*;

class HitoriGameTest {

    private HitoriGame game;

    @BeforeEach
    void setUp() throws Exception {
        game = new HitoriGame();
        game.loadGrid("src/main/resources/grids/grid_easy.txt");
    }

    @Test
    void testLoadGrid() {
        Grid grid = game.getGrid();
        assertNotNull(grid);
        assertEquals(5, grid.getSize());
    }

    @Test
    void testToggleCellValid() {
        try {
            game.toggleCell(0, 0);
        } catch (Exception e) {
            fail("Coup valide devrait passer sans exception");
        }
    }

    @Test
    void testGameWonInitiallyFalse() {
        assertFalse(game.isGameWon());
    }

    @Test
    void testElapsedTime() throws InterruptedException {
        Thread.sleep(1000);
        int time = game.getElapsedTime();
        assertTrue(time >= 1);
    }
}
