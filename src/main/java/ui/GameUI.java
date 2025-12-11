package ui;

import controller.HitoriGame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cell;
import util.TimerUtil;

public class GameUI {

    private HitoriGame game;
    private GridPane gridPane;
    private TimerUtil timer;
    private Label timerLabel;

    public GameUI(HitoriGame game) {
        this.game = game;
    }

    public void show(Stage stage) {
        stage.setTitle("Hitori - Jeu");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #a0d8ef); -fx-padding: 20px;");

        Label title = new Label("Hitori - Niveau " + game.getGrid().getSize() + "x" + game.getGrid().getSize());
        title.getStyleClass().add("label-title");

        timerLabel = new Label("00:00");
        timerLabel.getStyleClass().add("timer-label");
        timer = new TimerUtil(timerLabel);
        timer.start();

        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);

        drawGrid();

        Button checkButton = new Button("Vérifier");
        Button restartButton = new Button("Recommencer");

        checkButton.setOnAction(e -> checkGame(stage));
        restartButton.setOnAction(e -> {
            game.resetGrid();
            timer.reset();
            drawGrid();
        });

        HBox buttons = new HBox(20, checkButton, restartButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, timerLabel, gridPane, buttons);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void drawGrid() {
        gridPane.getChildren().clear();

        int size = game.getGrid().getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = game.getGrid().getCell(i, j);
                Button btn = new Button(String.valueOf(cell.getValue()));
                btn.setPrefSize(50, 50);

                updateButtonStyle(btn, cell);

                final int row = i;
                final int col = j;
                btn.setOnAction(e -> {
                    game.toggleCell(row, col);
                    updateButtonStyle(btn, cell);
                });

                gridPane.add(btn, j, i);
            }
        }
    }

    private void updateButtonStyle(Button btn, Cell cell) {
        // Supprime les anciennes classes
        btn.getStyleClass().removeAll("white-cell", "black-cell");

        // Ajoute la classe CSS correspondante
        if (cell.isBlack()) {
            btn.getStyleClass().add("black-cell");
        } else {
            btn.getStyleClass().add("white-cell");
        }
    }

    private void checkGame(Stage stage) {
        if (game.isGameWon()) {
            timer.stop();
            int scoreTime = timer.getSecondsElapsed();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Félicitations !");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez terminé la grille en " + formatTime(scoreTime) + " !");
            alert.showAndWait();

            ScoreBoardUI scoreUI = new ScoreBoardUI(scoreTime);
            scoreUI.show(stage);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Encore du travail !");
            alert.setHeaderText(null);
            alert.setContentText("La grille n'est pas encore correcte.");
            alert.showAndWait();
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", minutes, sec);
    }
}
