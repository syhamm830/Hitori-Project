package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.FileUtils;

import java.util.List;

public class ScoreBoardUI {

    private int lastScore;

    public ScoreBoardUI(int lastScore) {
        this.lastScore = lastScore;
        FileUtils.saveScore(lastScore); // Sauvegarde dans fichier scores.txt
    }

    public void show(Stage stage) {
        stage.setTitle("Hitori - Meilleurs Scores");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #fafafa; -fx-padding: 20px;");

        Label title = new Label("Meilleurs Scores");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        List<Integer> scores = FileUtils.loadTopScores(10);

        for (int i = 0; i < scores.size(); i++) {
            Label scoreLabel = new Label((i + 1) + ". " + scores.get(i) + " sec");
            root.getChildren().add(scoreLabel);
        }

        Button backButton = new Button("Retour au menu");
        backButton.setOnAction(e -> {
            HitoriGame game = new HitoriGame();
            LevelSelectorUI selector = new LevelSelectorUI(game);
            selector.show(stage);
        });

        root.getChildren().addAll(backButton);

        Scene scene = new Scene(root, 300, 400);
        stage.setScene(scene);
        stage.show();
    }
}
