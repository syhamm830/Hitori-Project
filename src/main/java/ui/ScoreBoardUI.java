package ui;

import controller.HitoriGame;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.FileUtils;

import java.util.List;

public class ScoreBoardUI {

    private HitoriGame game;
    private Integer lastScore;

    public ScoreBoardUI(int lastScore) {
        this.lastScore = lastScore;
        this.game = new HitoriGame();
        FileUtils.saveScore(lastScore);
    }

    public ScoreBoardUI(HitoriGame game) {
        this.game = game;
        this.lastScore = null;
    }

    public void show(Stage stage) {
        stage.setTitle("🏆 Hitori - Meilleurs Scores");

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getStyleClass().add("scoreboard-root");

        VBox header = createHeader();
        VBox scoresContainer = createScoresContainer();
        Button backButton = createBackButton(stage);

        root.getChildren().addAll(header, scoresContainer, backButton);

        Scene scene = new Scene(root, 700, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label trophy = new Label("🏆");
        trophy.setStyle("-fx-font-size: 80px;");
        
        RotateTransition rotate = new RotateTransition(Duration.seconds(3), trophy);
        rotate.setFromAngle(-10);
        rotate.setToAngle(10);
        rotate.setCycleCount(Timeline.INDEFINITE);
        rotate.setAutoReverse(true);
        rotate.play();

        Label title = new Label("MEILLEURS SCORES");
        title.getStyleClass().add("scoreboard-title");

        Label subtitle = new Label(lastScore != null ? 
            "Votre temps : " + formatTime(lastScore) : 
            "Top 10 des meilleurs temps");
        subtitle.getStyleClass().add("scoreboard-subtitle");

        header.getChildren().addAll(trophy, title, subtitle);
        return header;
    }

    private VBox createScoresContainer() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("scores-container");

        List<Integer> scores = FileUtils.loadTopScores(10);

        if (scores.isEmpty()) {
            Label emptyLabel = new Label("Aucun score enregistré pour le moment");
            emptyLabel.getStyleClass().add("empty-scores-label");
            container.getChildren().add(emptyLabel);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                HBox scoreRow = createScoreRow(i + 1, scores.get(i), scores.get(i).equals(lastScore));
                container.getChildren().add(scoreRow);
                
                FadeTransition fade = new FadeTransition(Duration.millis(300 + i * 100), scoreRow);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.play();
                
                TranslateTransition slide = new TranslateTransition(Duration.millis(300 + i * 100), scoreRow);
                slide.setFromX(-50);
                slide.setToX(0);
                slide.play();
            }
        }

        return container;
    }

    private HBox createScoreRow(int rank, int score, boolean isCurrentScore) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefWidth(550);
        row.setPrefHeight(70);
        row.getStyleClass().add("score-row");
        
        if (isCurrentScore) {
            row.getStyleClass().add("current-score-row");
            
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(1), row);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.03);
            pulse.setToY(1.03);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
        }

        Circle badge = new Circle(25);
        String badgeColor = getRankColor(rank);
        badge.setFill(Color.web(badgeColor));
        badge.setEffect(new DropShadow(10, Color.web(badgeColor, 0.4)));

        Label rankLabel = new Label(String.valueOf(rank));
        rankLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: white;");
        StackPane badgeStack = new StackPane(badge, rankLabel);

        String medal = "";
        if (rank == 1) medal = "🥇";
        else if (rank == 2) medal = "🥈";
        else if (rank == 3) medal = "🥉";

        Label medalLabel = new Label(medal);
        medalLabel.setStyle("-fx-font-size: 30px;");

        Label timeLabel = new Label(formatTime(score));
        timeLabel.getStyleClass().add("score-time-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(badgeStack, medalLabel, timeLabel, spacer);

        if (isCurrentScore) {
            Label newLabel = new Label("✨ NOUVEAU");
            newLabel.getStyleClass().add("new-score-label");
            row.getChildren().add(newLabel);
        }

        return row;
    }

    private String getRankColor(int rank) {
        switch (rank) {
            case 1: return "#f59e0b";
            case 2: return "#94a3b8";
            case 3: return "#cd7f32";
            default: return "#4338ca";
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private Button createBackButton(Stage stage) {
        Button button = new Button("← Retour au Menu");
        button.setPrefWidth(400);
        button.setPrefHeight(60);
        button.setStyle(
            "-fx-font-size: 18px; " +
            "-fx-font-weight: 700; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: linear-gradient(145deg, #4338ca, #3730a3); " +
            "-fx-background-radius: 15px; " +
            "-fx-border-radius: 15px; " +
            "-fx-border-color: #312e81; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(67,56,202,0.3), 15, 0, 0, 5);"
        );

        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            button.setStyle(
                "-fx-font-size: 18px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: linear-gradient(145deg, #3730a3, #312e81); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 3px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(67,56,202,0.5), 20, 0, 0, 8);"
            );
        });

        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            button.setStyle(
                "-fx-font-size: 18px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: linear-gradient(145deg, #4338ca, #3730a3); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: #312e81; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(67,56,202,0.3), 15, 0, 0, 5);"
            );
        });

        button.setOnAction(e -> {
            LevelSelectorUI selector = new LevelSelectorUI(game);
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> selector.show(stage));
            fadeOut.play();
        });

        return button;
    }
}