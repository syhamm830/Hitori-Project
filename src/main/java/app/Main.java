package app;

import controller.HitoriGame;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.LevelSelectorUI;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        HitoriGame game = new HitoriGame();
        LevelSelectorUI selectorUI = new LevelSelectorUI(game);
        selectorUI.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
