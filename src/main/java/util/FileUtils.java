package util;

import controller.HitoriGame;
import model.Cell;
import model.Grid;
import java.io.*;
import java.util.*;

public class FileUtils {

    private static final String SCORE_FILE = "src/main/resources/scores.txt";
    private static final String SAVE_FILE = "src/main/resources/savegame.dat";

    public static class GameState {
        public HitoriGame game;
        public int seconds;
        public int moveCount;
        public String level;

        public GameState(HitoriGame game, int seconds, int moveCount, String level) {
            this.game = game;
            this.seconds = seconds;
            this.moveCount = moveCount;
            this.level = level;
        }
    }

    /**
     * Charger une grille depuis un fichier
     */
    public static Grid loadGridFromFile(String level) throws IOException {
        String path = "src/main/resources/grids/grid_" + level.toLowerCase() + ".txt";
        File file = new File(path);

        if (!file.exists()) {
            throw new IOException("Fichier de grille introuvable : " + path);
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<int[]> rows = new ArrayList<>();
        String line;
        
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            int[] row = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                row[i] = Integer.parseInt(tokens[i]);
            }
            rows.add(row);
        }
        reader.close();

        Grid grid = new Grid(rows.size());
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).length; j++) {
                grid.setCell(i, j, rows.get(i)[j]);
            }
        }
        return grid;
    }

    /**
     * Sauvegarder l'état complet de la partie
     */
    public static void saveGameState(HitoriGame game, int seconds, int moveCount) throws IOException {
        File saveDir = new File("src/main/resources");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            Grid grid = game.getGrid();
            int size = grid.getSize();
            
            // Ligne 1 : Métadonnées
            writer.println(size + " " + seconds + " " + moveCount);
            
            // Lignes suivantes : État de chaque cellule
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Cell cell = grid.getCell(i, j);
                    String state = cell.isBlack() ? "B" : "W";
                    writer.print(cell.getValue() + " " + state);
                    if (j < size - 1) writer.print(" ");
                }
                writer.println();
            }
        }
    }

    /**
     * Charger l'état complet de la partie
     */
    public static GameState loadGameState() throws IOException {
        File saveFile = new File(SAVE_FILE);
        if (!saveFile.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            // Lire les métadonnées
            String[] metadata = reader.readLine().split(" ");
            int size = Integer.parseInt(metadata[0]);
            int seconds = Integer.parseInt(metadata[1]);
            int moveCount = Integer.parseInt(metadata[2]);

            Grid grid = new Grid(size);
            
            for (int i = 0; i < size; i++) {
                String line = reader.readLine();
                String[] tokens = line.split(" ");
                
                for (int j = 0; j < size; j++) {
                    int value = Integer.parseInt(tokens[j * 2]);
                    String state = tokens[j * 2 + 1];
                    
                    grid.setCell(i, j, value);
                    Cell cell = grid.getCell(i, j);
                    
                    if (state.equals("B")) {
                        cell.setState(Cell.State.BLACK);
                    }
                }
            }

            HitoriGame game = new HitoriGame();
            game.setGrid(grid);

            String level = size == 5 ? "easy" : size == 7 ? "medium" : "hard";

            return new GameState(game, seconds, moveCount, level);
        }
    }

    /**
     * Sauvegarder un score
     */
    public static void saveScore(int score) {
        List<Integer> scores = loadTopScores(10);
        scores.add(score);
        Collections.sort(scores);
        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }

        try {
            File scoreDir = new File("src/main/resources");
            if (!scoreDir.exists()) {
                scoreDir.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE));
            for (Integer s : scores) {
                writer.write(s.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charger les meilleurs scores
     */
    public static List<Integer> loadTopScores(int limit) {
        List<Integer> scores = new ArrayList<>();
        File file = new File(SCORE_FILE);
        if (!file.exists()) return scores;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    scores.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(scores);
        if (scores.size() > limit) {
            scores = scores.subList(0, limit);
        }
        return scores;
    }
}