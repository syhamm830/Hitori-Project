package model;

public class Cell {

    public enum State { WHITE, BLACK }

    private final int value;
    private State state;
    private boolean playable; // nouvelle propriété

    public Cell(int value) {
        this.value = value;
        this.state = State.WHITE;
        this.playable = (value == 0); // case 0 = jouable
    }

    public int getValue() {
        return value;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isWhite() {
        return state == State.WHITE;
    }

    public boolean isBlack() {
        return state == State.BLACK;
    }

    public void toggleState() {
        if (playable) {
            state = (state == State.WHITE) ? State.BLACK : State.WHITE;
        }
    }

    public boolean isPlayable() {
        return playable;
    }

    @Override
    public String toString() {
        return isBlack() ? "X" : (playable ? " " : String.valueOf(value));
    }
}
