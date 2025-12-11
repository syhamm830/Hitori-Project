package model;

public class ScoreEntry {
    private final String playerName;
    private final int score; // par exemple temps en secondes ou points

    public ScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return playerName + " - " + score;
    }
}
