package ui;

import controller.HitoriGame;
import exception.InvalidMoveException;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUI {

    private HitoriGame game;
    private GridPane gridPane;
    private Label timerLabel;
    private Label moveCountLabel;
    private Timeline timeline;
    private int seconds = 0;
    private int moveCount = 0;
    private Button[][] buttonGrid;
    private boolean darkMode = false;
    private VBox root;
    private Pane particlePane;

    public GameUI(HitoriGame game) {
        this.game = game;
    }

    public void show(Stage stage) {
        stage.setTitle("✨ Hitori - Puzzle Élégant");

        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("game-root");

        // En-tête avec titre et stats
        VBox header = createHeader();

        // Grille de jeu avec effet glassmorphism
        VBox gridContainer = createGridContainer();

        // Contrôles du jeu
        HBox controls = createControls(stage);

        // Pane pour les particules (overlay)
        particlePane = new Pane();
        particlePane.setMouseTransparent(true);

        StackPane mainContent = new StackPane();
        mainContent.getChildren().addAll(
            new VBox(20, header, gridContainer, controls),
            particlePane
        );

        root.getChildren().add(mainContent);

        Scene scene = new Scene(root, 800, 900);
        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();

        startTimer();
        updateTheme();
    }

    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        // Titre avec animation de pulsation
        Label title = new Label("✨ H I T O R I ✨");
        title.getStyleClass().add("game-title");
        
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        // Sous-titre avec niveau
        Label subtitle = new Label("Grille " + game.getGrid().getSize() + "×" + game.getGrid().getSize());
        subtitle.getStyleClass().add("game-subtitle");

        // Stats bar
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
        gridPane.setHgap(8);
        gridPane.setVgap(8);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getStyleClass().add("game-grid");

        drawGrid();

        container.getChildren().add(gridPane);
        return container;
    }

    private HBox createControls(Stage stage) {
        HBox controls = new HBox(20);
        controls.setAlignment(Pos.CENTER);
        controls.getStyleClass().add("controls-bar");

        Button checkButton = createStyledButton("✓ Vérifier", "check-button");
        Button hintButton = createStyledButton("💡 Indice", "hint-button");
        Button restartButton = createStyledButton("↻ Recommencer", "restart-button");
        Button themeButton = createStyledButton("🌙 Thème", "theme-button");
        Button backButton = createStyledButton("← Retour", "back-button");

        checkButton.setOnAction(e -> checkGameWithAnimation());
        hintButton.setOnAction(e -> showHint());
        restartButton.setOnAction(e -> restartGame());
        themeButton.setOnAction(e -> toggleTheme());
        backButton.setOnAction(e -> {
            timeline.stop();
            new ui.LevelSelectorUI(game).show(stage);
        });

        controls.getChildren().addAll(checkButton, hintButton, restartButton, themeButton, backButton);
        return controls;
    }

    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().addAll("game-button", styleClass);
        
        // Effet de hover animé
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
        int cellSize = Math.max(50, 70 - size * 5);
        btn.setPrefSize(cellSize, cellSize);
        btn.setMinSize(cellSize, cellSize);
        btn.setMaxSize(cellSize, cellSize);

        updateCellAppearance(btn, cell);

        final int r = row;
        final int c = col;

        if (cell.isPlayable()) {
            btn.setDisable(false);
            btn.setOnAction(e -> handleCellClick(btn, cell, r, c));
        } else {
            btn.setDisable(true);
            btn.getStyleClass().add("fixed-cell");
        }

        return btn;
    }

    private void handleCellClick(Button btn, Cell cell, int row, int col) {
        try {
            game.toggleCell(row, col);
            moveCount++;
            moveCountLabel.setText("🎯 " + moveCount + " coups");
            
            // Animation de flip
            animateCellFlip(btn, cell);
            
            // Vérifier les lignes/colonnes pour feedback visuel
            updateRowColumnFeedback();
            
        } catch (InvalidMoveException ex) {
            // Animation de shake pour erreur
            shakeButton(btn);
            showError(ex.getMessage());
        }
    }

    private void updateCellAppearance(Button btn, Cell cell) {
        btn.getStyleClass().removeAll("white-cell", "black-cell", "playable-cell", "fixed-cell");
        
        if (cell.isBlack()) {
            btn.getStyleClass().add("black-cell");
            btn.setText("●");
        } else {
            if (cell.isPlayable()) {
                btn.getStyleClass().add("playable-cell");
                btn.setText("");
            } else {
                btn.getStyleClass().add("white-cell");
                btn.setText(String.valueOf(cell.getValue()));
            }
        }

        // Effet de glow
        if (cell.isPlayable() && !cell.isBlack()) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.web("#6366f1"));
            glow.setRadius(10);
            btn.setEffect(glow);
        } else {
            btn.setEffect(null);
        }
    }

    private void animateCellFlip(Button btn, Cell cell) {
        RotateTransition rotate = new RotateTransition(Duration.millis(300), btn);
        rotate.setByAngle(180);
        rotate.setCycleCount(1);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), btn);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        
        ParallelTransition parallel = new ParallelTransition(rotate, scale);
        parallel.setOnFinished(e -> updateCellAppearance(btn, cell));
        parallel.play();
    }

    private void shakeButton(Button btn) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), btn);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void updateRowColumnFeedback() {
        int size = game.getGrid().getSize();
        
        for (int i = 0; i < size; i++) {
            boolean rowValid = checkRowValid(i);
            boolean colValid = checkColValid(i);
            
            for (int j = 0; j < size; j++) {
                Button btn = buttonGrid[i][j];
                if (btn != null && !game.getGrid().getCell(i, j).isBlack()) {
                    if (rowValid && colValid) {
                        btn.setStyle("-fx-border-color: #10b981; -fx-border-width: 2px;");
                    } else {
                        btn.setStyle("");
                    }
                }
            }
        }
    }

    private boolean checkRowValid(int row) {
        List<Integer> values = new ArrayList<>();
        for (int j = 0; j < game.getGrid().getSize(); j++) {
            Cell cell = game.getGrid().getCell(row, j);
            if (cell.isWhite() && !cell.isPlayable()) {
                if (values.contains(cell.getValue())) return false;
                values.add(cell.getValue());
            }
        }
        return true;
    }

    private boolean checkColValid(int col) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < game.getGrid().getSize(); i++) {
            Cell cell = game.getGrid().getCell(i, col);
            if (cell.isWhite() && !cell.isPlayable()) {
                if (values.contains(cell.getValue())) return false;
                values.add(cell.getValue());
            }
        }
        return true;
    }

    private void checkGameWithAnimation() {
        if (game.isGameWon()) {
            timeline.stop();
            showVictoryAnimation();
        } else {
            showError("La grille n'est pas encore correcte. Continuez ! 💪");
        }
    }

    private void showVictoryAnimation() {
        // Particules de célébration
        createConfetti();
        
        // Animation de toutes les cellules
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
        pause.setOnFinished(e -> showVictoryDialog());
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
        
        alert.showAndWait().ifPresent(response -> {
            // Afficher le tableau des scores avec le nouveau score
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
            particle.setLayoutX(400);
            particle.setLayoutY(300);
            
            particlePane.getChildren().add(particle);
            
            TranslateTransition tt = new TranslateTransition(Duration.seconds(2 + random.nextDouble()), particle);
            tt.setByX((random.nextDouble() - 0.5) * 600);
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
        showInfo("💡 Astuce : Cherchez les doublons dans les lignes et colonnes !");
    }

    private void restartGame() {
        game.resetGrid();
        seconds = 0;
        moveCount = 0;
        moveCountLabel.setText("🎯 0 coups");
        drawGrid();
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        updateTheme();
    }

    private void updateTheme() {
        if (darkMode) {
            root.getStyleClass().remove("light-mode");
            root.getStyleClass().add("dark-mode");
        } else {
            root.getStyleClass().remove("dark-mode");
            root.getStyleClass().add("light-mode");
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
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ℹ️ Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}