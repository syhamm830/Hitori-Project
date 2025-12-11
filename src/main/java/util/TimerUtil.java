package util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Label;

public class TimerUtil {

    private Timeline timeline;
    private int secondsElapsed;
    private Label timerLabel;

    public TimerUtil(Label timerLabel) {
        this.timerLabel = timerLabel;
        this.secondsElapsed = 0;
    }

    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            updateLabel();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public void reset() {
        stop();
        secondsElapsed = 0;
        updateLabel();
    }

    private void updateLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
