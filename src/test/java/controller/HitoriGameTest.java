package controller;

import exception.InvalidMoveException;
import model.Cell;
import model.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HitoriGameTest {

    private HitoriGame game;

    @BeforeEach
    void setUp() throws Exception {
        game = new HitoriGame();
        game.loadGrid("easy");
    }

    @Test
    void testLoadGrid() {
        Grid grid = game.getGrid();
        assertNotNull(grid, "La grille ne devrait pas être nulle");
        assertEquals(5, grid.getSize(), "La grille 'easy' devrait être 5x5");
    }

    @Test
    void testToggleCellValid() {
        try {
            // Noircir une case blanche (coup valide)
            game.toggleCell(0, 0);
            assertTrue(game.getGrid().getCell(0, 0).isBlack(), 
                "La case devrait être noire après toggle");
            
            // Re-toggle pour la rendre blanche
            game.toggleCell(0, 0);
            assertTrue(game.getGrid().getCell(0, 0).isWhite(), 
                "La case devrait être blanche après second toggle");
        } catch (InvalidMoveException e) {
            fail("Coup valide devrait passer sans exception : " + e.getMessage());
        }
    }

    @Test
    void testToggleCellInvalidAdjacentBlacks() {
        try {
            // Noircir deux cases adjacentes devrait échouer
            game.toggleCell(0, 0);  // Noircir (0,0)
            
            assertThrows(InvalidMoveException.class, () -> {
                game.toggleCell(0, 1);  // Tenter de noircir case adjacente
            }, "Noircir deux cases adjacentes devrait lancer InvalidMoveException");
            
        } catch (InvalidMoveException e) {
            fail("La première case devrait être noircie sans problème");
        }
    }

    @Test
    void testGameWonInitiallyFalse() {
        assertFalse(game.isGameWon(), 
            "Le jeu ne devrait pas être gagné au début");
    }

    @Test
    void testResetGrid() throws InvalidMoveException {
        // Noircir quelques cases
        game.toggleCell(0, 0);
        game.toggleCell(1, 1);
        
        assertTrue(game.getGrid().getCell(0, 0).isBlack(), 
            "La case (0,0) devrait être noire");
        
        // Réinitialiser
        game.resetGrid();
        
        assertTrue(game.getGrid().getCell(0, 0).isWhite(), 
            "La case (0,0) devrait être blanche après reset");
        assertTrue(game.getGrid().getCell(1, 1).isWhite(), 
            "La case (1,1) devrait être blanche après reset");
    }

    @Test
    void testSetAndGetGrid() {
        Grid newGrid = new Grid(7);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                newGrid.setCell(i, j, i + j);
            }
        }
        
        game.setGrid(newGrid);
        
        assertEquals(7, game.getGrid().getSize(), 
            "La nouvelle grille devrait être 7x7");
        assertEquals(newGrid, game.getGrid(), 
            "La grille devrait être celle qu'on a définie");
    }

    @Test
    void testGridValidation() {
        Grid grid = game.getGrid();
        
        // Au départ, la grille n'est pas valide (contient des doublons)
        assertFalse(grid.isValid(), 
            "La grille de départ devrait contenir des doublons");
    }

    @Test
    void testCellStates() {
        Cell cell = game.getGrid().getCell(0, 0);
        
        // Vérifier l'état initial
        assertTrue(cell.isWhite(), "La case devrait être blanche au départ");
        assertFalse(cell.isBlack(), "La case ne devrait pas être noire au départ");
        
        // Changer l'état
        cell.setState(Cell.State.BLACK);
        
        assertTrue(cell.isBlack(), "La case devrait être noire");
        assertFalse(cell.isWhite(), "La case ne devrait plus être blanche");
    }

    @Test
    void testLoadDifferentLevels() throws Exception {
        // Test niveau facile
        game.loadGrid("easy");
        assertEquals(5, game.getGrid().getSize(), "Easy devrait être 5x5");
        
        // Test niveau moyen
        game.loadGrid("medium");
        assertEquals(7, game.getGrid().getSize(), "Medium devrait être 7x7");
        
        // Test niveau difficile
        game.loadGrid("hard");
        assertEquals(9, game.getGrid().getSize(), "Hard devrait être 9x9");
    }

    @Test
    void testInvalidLevel() {
        assertThrows(Exception.class, () -> {
            game.loadGrid("invalid_level");
        }, "Charger un niveau invalide devrait lancer une exception");
    }

    @Test
    void testCountCells() throws InvalidMoveException {
        // Compter les cases au départ
        int initialWhite = game.getGrid().countWhiteCells();
        int initialBlack = game.getGrid().countBlackCells();
        
        assertEquals(25, initialWhite, "Toutes les cases devraient être blanches au départ");
        assertEquals(0, initialBlack, "Aucune case ne devrait être noire au départ");
        
        // Noircir quelques cases
        game.toggleCell(0, 0);
        game.toggleCell(2, 2);
        
        assertEquals(23, game.getGrid().countWhiteCells(), "Il devrait y avoir 23 cases blanches");
        assertEquals(2, game.getGrid().countBlackCells(), "Il devrait y avoir 2 cases noires");
    }

    @Test
    void testCellToggle() {
        Cell cell = game.getGrid().getCell(0, 0);
        
        // État initial
        assertTrue(cell.isWhite());
        
        // Premier toggle
        cell.toggleState();
        assertTrue(cell.isBlack());
        
        // Deuxième toggle
        cell.toggleState();
        assertTrue(cell.isWhite());
    }
}