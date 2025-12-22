package model;

public class Cell {

    public enum State { WHITE, BLACK }

    private final int value;
    private State state;

    public Cell(int value) {
        this.value = value;
        this.state = State.WHITE;
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
        state = (state == State.WHITE) ? State.BLACK : State.WHITE;
    }

    @Override
    public String toString() {
        return isBlack() ? "X" : String.valueOf(value);
    }
}