package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.HitoriGame;

public class LevelSelectorUI {

    private HitoriGame game;

    public LevelSelectorUI(HitoriGame game) {
        this.game = game;
    }

    public void show(Stage stage) {
        stage.setTitle("Hitori - Choix du Niveau");

        Button easyButton = new Button("Facile");
        Button mediumButton = new Button("Moyen");
        Button hardButton = new Button("Difficile");

        easyButton.setOnAction(e -> startGame("easy", stage));
        mediumButton.setOnAction(e -> startGame("medium", stage));
        hardButton.setOnAction(e -> startGame("hard", stage));

        VBox vbox = new VBox(20, easyButton, mediumButton, hardButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 50px;");

        Scene scene = new Scene(vbox, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void startGame(String level, Stage stage) {
        try {
            switch (level) {
                case "easy":
                    game.loadGrid("src/main/resources/grids/grid_easy.txt");
                    break;
                case "medium":
                    game.loadGrid("src/main/resources/grids/grid_medium.txt");
                    break;
                case "hard":
                    game.loadGrid("src/main/resources/grids/grid_hard.txt");
                    break;
            }

            GameUI gameUI = new GameUI(game);
            gameUI.show(stage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
