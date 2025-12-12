package util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Label;

public class TimerUtil {

    private Timeline timeline;
    private int secondsElapsed;
    private Label timerLabel;

    // Constructeur avec Label
    public TimerUtil(Label timerLabel) {
        this.timerLabel = timerLabel;
        this.secondsElapsed = 0;
    }

    // Constructeur par défaut
    public TimerUtil() {
        this.timerLabel = null;
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

    public int getElapsedSeconds() {
        return secondsElapsed;
    }

    public void reset() {
        stop();
        secondsElapsed = 0;
        updateLabel();
    }

    private void updateLabel() {
        if (timerLabel != null) {
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }
}
