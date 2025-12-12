package util;

import model.Grid;
import java.io.*;
import java.util.*;

public class FileUtils {

    private static final String SCORE_FILE = "src/main/resources/scores.txt";

    // Charger ou générer une grille selon le niveau
    public static Grid loadGridFromFile(String level) throws IOException {
        String path = "src/main/resources/grids/grid_" + level.toLowerCase() + ".txt";
        File file = new File(path);

        if (!file.exists()) {
            generateGridFile(level, path);
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

    // Génération automatique d'une grille jouable
    private static void generateGridFile(String level, String path) throws IOException {
        int size;
        switch (level.toLowerCase()) {
            case "easy": size = 5; break;
            case "medium": size = 7; break;
            case "hard": size = 9; break;
            default: size = 5;
        }

        int[][] grid = new int[size][size];
        Random rand = new Random();

        // Génération d'une grille initiale sans doublons par ligne et colonne
        for (int i = 0; i < size; i++) {
            List<Integer> numbers = new ArrayList<>();
            for (int n = 1; n <= size; n++) numbers.add(n);
            Collections.shuffle(numbers, rand);
            for (int j = 0; j < size; j++) {
                grid[i][j] = numbers.get(j);
            }
        }

        // Introduire des cases jouables (0) selon difficulté
        int zeroCount;
        switch (level.toLowerCase()) {
            case "easy": zeroCount = size; break;
            case "medium": zeroCount = size * 2; break;
            case "hard": zeroCount = size * 3; break;
            default: zeroCount = size;
        }

        // Répartition des zéros pour éviter plusieurs dans la même ligne/colonne
        Set<String> usedPositions = new HashSet<>();
        for (int z = 0; z < zeroCount; z++) {
            int i, j;
            String key;
            do {
                i = rand.nextInt(size);
                j = rand.nextInt(size);
                key = i + "-" + j;
            } while (grid[i][j] == 0 || usedPositions.contains(key));
            grid[i][j] = 0;
            usedPositions.add(key);
        }

        // Écriture dans le fichier
        File dir = new File("src/main/resources/grids");
        if (!dir.exists()) dir.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    writer.write(grid[i][j] + (j < size - 1 ? " " : ""));
                }
                writer.newLine();
            }
        }
    }

    // Sauvegarder un score (en secondes)
    public static void saveScore(int score) {
        List<Integer> scores = loadTopScores(10);
        scores.add(score);
        Collections.sort(scores);
        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (Integer s : scores) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Charger les meilleurs scores
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
