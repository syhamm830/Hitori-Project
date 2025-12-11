package util;

import model.Cell;
import model.Grid;
import model.ScoreEntry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static Grid loadGridFromFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
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

    private static final String SCORE_FILE = "src/main/resources/scores.txt";

    public static List<ScoreEntry> loadScores() throws IOException {
        List<ScoreEntry> scores = new ArrayList<>();
        File file = new File(SCORE_FILE);
        if (!file.exists()) return scores;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                scores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
            }
        }
        reader.close();
        return scores;
    }

    public static void saveScore(ScoreEntry entry) throws IOException {
        List<ScoreEntry> scores = loadScores();
        scores.add(entry);
        scores.sort((a, b) -> Integer.compare(a.getScore(), b.getScore()));
        if (scores.size() > 10) scores = scores.subList(0, 10);

        BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE));
        for (ScoreEntry s : scores) {
            writer.write(s.getPlayerName() + "," + s.getScore());
            writer.newLine();
        }
        writer.close();
    }
}
