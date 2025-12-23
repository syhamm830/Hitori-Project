package ui;

import controller.HitoriGame;
import exception.InvalidMoveException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cell;

import java.util.Random;

public class GameUI {

    private boolean victoryShown = false;
    private HitoriGame game;
    private GridPane gridPane;
    private Label timerLabel;
    private Label moveCountLabel;
    private Timeline timeline;
    private int seconds = 0;
    private int moveCount = 0;
    private Button[][] buttonGrid;
    private VBox root;
    private Pane particlePane;

    public GameUI(HitoriGame game) {
        this.game = game;
    }

    public GameUI(HitoriGame game, int savedSeconds, int savedMoves) {
        this.game = game;
        this.seconds = savedSeconds;
        this.moveCount = savedMoves;
    }

    public void show(Stage stage) {
        stage.setTitle("🎯 Hitori - Puzzle Logique");

        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("game-root");

        VBox header = createHeader();
        VBox gridContainer = createGridContainer();
        HBox controls = createControls(stage);

        particlePane = new Pane();
        particlePane.setMouseTransparent(true);

        StackPane mainContent = new StackPane();
        mainContent.getChildren().addAll(
            new VBox(20, header, gridContainer, controls),
            particlePane
        );

        root.getChildren().add(mainContent);

        Scene scene = new Scene(root);
        
        try {
            String css = getClass().getResource("/styles/game.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("❌ CSS non trouvé !");
            e.printStackTrace();
        }
        
        stage.setScene(scene);
        stage.show();

        startTimer();
    }

    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label title = new Label("🎯 H I T O R I");
        title.getStyleClass().add("game-title");
        
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        Label subtitle = new Label("Grille " + game.getGrid().getSize() + "×" + game.getGrid().getSize());
        subtitle.getStyleClass().add("game-subtitle");

        HBox statsBar = new HBox(40);
        statsBar.setAlignment(Pos.CENTER);
        statsBar.getStyleClass().add("stats-bar");

        timerLabel = new Label("⏱ 00:00");
        timerLabel.getStyleClass().add("stat-label");

        moveCountLabel = new Label("🎯 0 coups");
        moveCountLabel.getStyleClass().add("stat-label");

        statsBar.getChildren().addAll(timerLabel, moveCountLabel);

        header.getChildren().addAll(title, subtitle, statsBar);
        return header;
    }

    private VBox createGridContainer() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("grid-container");

        gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.setAlignment(Pos.CENTER);

        drawGrid();

        container.getChildren().add(gridPane);
        return container;
    }

    private HBox createControls(Stage stage) {
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER);
        controls.getStyleClass().add("controls-bar");

        Button checkButton = createStyledButton("✓ Vérifier", "check-button");
        Button hintButton = createStyledButton("💡 Indice", "hint-button");
        Button saveButton = createStyledButton("💾 Sauvegarder", "save-button");
        Button restartButton = createStyledButton("↻ Recommencer", "restart-button");
        Button backButton = createStyledButton("← Retour", "back-button");

        checkButton.setOnAction(e -> checkGameWithAnimation());
        hintButton.setOnAction(e -> showHint());
        saveButton.setOnAction(e -> saveGame());
        restartButton.setOnAction(e -> restartGame());
        backButton.setOnAction(e -> {
            timeline.stop();
            new ui.LevelSelectorUI(game).show(stage);
        });

        controls.getChildren().addAll(checkButton, hintButton, saveButton, restartButton, backButton);
        return controls;
    }

    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().addAll("game-button", styleClass);
        
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        return button;
    }

    private void drawGrid() {
        gridPane.getChildren().clear();
        int size = game.getGrid().getSize();
        buttonGrid = new Button[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = game.getGrid().getCell(i, j);
                Button btn = createCellButton(cell, i, j);
                buttonGrid[i][j] = btn;
                gridPane.add(btn, j, i);
            }
        }
    }

    private Button createCellButton(Cell cell, int row, int col) {
        Button btn = new Button();
        int size = game.getGrid().getSize();
        int cellSize = Math.max(55, 75 - size * 5);
        btn.setPrefSize(cellSize, cellSize);
        btn.setMinSize(cellSize, cellSize);
        btn.setMaxSize(cellSize, cellSize);

        updateCellAppearance(btn, cell);

        final int r = row;
        final int c = col;

        btn.setOnAction(e -> handleCellClick(btn, cell, r, c));

        return btn;
    }

    private void handleCellClick(Button btn, Cell cell, int row, int col) {
        try {
            game.toggleCell(row, col);
            moveCount++;
            moveCountLabel.setText("🎯 " + moveCount + " coups");

            updateCellAppearance(btn, cell);
            animateCellFlip(btn);

        } catch (InvalidMoveException ex) {
            shakeButton(btn);
            showError(ex.getMessage());
        }
    }

    private void updateCellAppearance(Button btn, Cell cell) {
        btn.getStyleClass().removeAll("white-cell", "black-cell");
        
        if (cell.isBlack()) {
            btn.getStyleClass().add("black-cell");
            btn.setText("●");
        } else {
            btn.getStyleClass().add("white-cell");
            btn.setText(String.valueOf(cell.getValue()));
        }
    }

    private void animateCellFlip(Button btn) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), btn);
        scale.setToX(1.15);
        scale.setToY(1.15);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void shakeButton(Button btn) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), btn);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void checkGameWithAnimation() {
        System.out.println(game.getGrid().toString());
        System.out.println("\n🔍 Vérification de la grille...");

        if (!game.isGameWon()) {
            showError(
                "❌ La grille n'est pas encore complète ou contient des erreurs.\n\n" +
                "Vérifiez :\n" +
                "• Les doublons dans les lignes/colonnes\n" +
                "• Les cases noires adjacentes\n" +
                "• La connexité des cases blanches"
            );
            return;
        }

        if (game.isGameWon() && !victoryShown) {
            victoryShown = true;
            System.out.println("🎉 VICTOIRE !");
            timeline.stop();
            showVictoryAnimation();
        }
    }

    private void showVictoryAnimation() {
        createConfetti();
        
        int size = game.getGrid().getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button btn = buttonGrid[i][j];
                if (btn != null) {
                    ScaleTransition st = new ScaleTransition(Duration.millis(500 + i * 50 + j * 50), btn);
                    st.setFromX(1.0);
                    st.setFromY(1.0);
                    st.setToX(1.3);
                    st.setToY(1.3);
                    st.setAutoReverse(true);
                    st.setCycleCount(2);
                    st.play();
                }
            }
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> Platform.runLater(this::showVictoryDialog));
        pause.play();
    }

    private void showVictoryDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🎉 Victoire !");
        alert.setHeaderText("Félicitations !");
        alert.setContentText(String.format(
            "✨ Vous avez résolu la grille !\n\n" +
            "⏱ Temps : %d:%02d\n" +
            "🎯 Coups : %d\n\n" +
            "Votre score a été enregistré !",
            seconds / 60, seconds % 60, moveCount
        ));
        
        alert.show();

        alert.setOnHidden(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            ScoreBoardUI scoreBoardUI = new ScoreBoardUI(seconds);
            scoreBoardUI.show(stage);
        });
    }

    private void createConfetti() {
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            Circle particle = new Circle(5);
            particle.setFill(Color.hsb(random.nextDouble() * 360, 0.8, 1.0));
            particle.setLayoutX(450);
            particle.setLayoutY(300);
            
            particlePane.getChildren().add(particle);
            
            TranslateTransition tt = new TranslateTransition(Duration.seconds(2 + random.nextDouble()), particle);
            tt.setByX((random.nextDouble() - 0.5) * 700);
            tt.setByY(random.nextDouble() * 400 + 200);
            
            FadeTransition ft = new FadeTransition(Duration.seconds(2), particle);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            
            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.setOnFinished(e -> particlePane.getChildren().remove(particle));
            pt.play();
        }
    }

    private void showHint() {
        showInfo("💡 Astuce : Cherchez les doublons dans les lignes et colonnes !\nNoircissez une des cases dupliquées.");
    }

    private void restartGame() {
        game.resetGrid();
        seconds = 0;
        moveCount = 0;
        victoryShown = false;
        moveCountLabel.setText("🎯 0 coups");
        drawGrid();
    }

    private void saveGame() {
        try {
            util.FileUtils.saveGameState(game, seconds, moveCount);
            showInfo("💾 Partie sauvegardée avec succès !");
        } catch (Exception ex) {
            showError("Erreur lors de la sauvegarde : " + ex.getMessage());
        }
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            timerLabel.setText(String.format("⏱ %02d:%02d", seconds / 60, seconds % 60));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ℹ️ Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}