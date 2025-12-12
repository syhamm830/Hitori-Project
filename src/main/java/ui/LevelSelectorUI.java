package ui;

import controller.HitoriGame;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelSelectorUI {

    private HitoriGame game;

    public LevelSelectorUI(HitoriGame game) {
        this.game = game;
    }

    public void show(Stage stage) {
        stage.setTitle("✨ Hitori - Sélection du Niveau");

        VBox root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("selector-root");

        // Titre principal avec animation
        Label title = new Label("✨ H I T O R I ✨");
        title.getStyleClass().add("selector-title");
        
        // Animation de pulsation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.08);
        pulse.setToY(1.08);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        // Sous-titre
        Label subtitle = new Label("Choisissez votre défi");
        subtitle.getStyleClass().add("selector-subtitle");

        // Conteneur des boutons de niveau
        VBox levelButtons = createLevelButtons(stage);

        // Footer avec info
        Label footer = new Label("Un jeu de logique et de réflexion");
        footer.getStyleClass().add("selector-footer");

        root.getChildren().addAll(title, subtitle, levelButtons, footer);

        Scene scene = new Scene(root, 700, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLevelButtons(Stage stage) {
        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("level-container");

        // Bouton Facile
        Button easyButton = createLevelButton(
            "🌱 FACILE",
            "Grille 5×5 - Parfait pour débuter",
            "#10b981",
            "#059669"
        );
        easyButton.setOnAction(e -> loadLevel(stage, "easy"));

        // Bouton Moyen
        Button mediumButton = createLevelButton(
            "⚡ MOYEN",
            "Grille 7×7 - Pour les joueurs expérimentés",
            "#f59e0b",
            "#d97706"
        );
        mediumButton.setOnAction(e -> loadLevel(stage, "medium"));

        // Bouton Difficile
        Button hardButton = createLevelButton(
            "🔥 DIFFICILE",
            "Grille 9×9 - Le challenge ultime",
            "#ef4444",
            "#dc2626"
        );
        hardButton.setOnAction(e -> loadLevel(stage, "hard"));

        // Bouton Scores
        Button scoresButton = createSecondaryButton("🏆 Meilleurs Scores");
        scoresButton.setOnAction(e -> showScoreboard(stage));

        // Bouton Règles
        Button rulesButton = createSecondaryButton("📖 Règles du Jeu");
        rulesButton.setOnAction(e -> showRules());

        // Bouton Quitter
        Button exitButton = createSecondaryButton("❌ Quitter");
        exitButton.setOnAction(e -> stage.close());

        container.getChildren().addAll(
            easyButton, 
            mediumButton, 
            hardButton,
            new Region(), // Spacer
            scoresButton,
            rulesButton,
            exitButton
        );

        return container;
    }

    private Button createLevelButton(String text, String description, String color1, String color2) {
        VBox buttonContent = new VBox(8);
        buttonContent.setAlignment(Pos.CENTER);

        Label mainText = new Label(text);
        mainText.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: white;");

        Label descText = new Label(description);
        descText.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: rgba(255,255,255,0.9);");

        buttonContent.getChildren().addAll(mainText, descText);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setPrefWidth(500);
        button.setPrefHeight(100);
        button.setStyle(String.format(
            "-fx-background-color: linear-gradient(145deg, %s, %s); " +
            "-fx-background-radius: 20px; " +
            "-fx-border-radius: 20px; " +
            "-fx-border-color: rgba(255,255,255,0.3); " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);",
            color1, color2
        ));

        // Animation de hover
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            button.setStyle(String.format(
                "-fx-background-color: linear-gradient(145deg, %s, %s); " +
                "-fx-background-radius: 20px; " +
                "-fx-border-radius: 20px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 3px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 8);",
                color2, color1
            ));
        });

        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            button.setStyle(String.format(
                "-fx-background-color: linear-gradient(145deg, %s, %s); " +
                "-fx-background-radius: 20px; " +
                "-fx-border-radius: 20px; " +
                "-fx-border-color: rgba(255,255,255,0.3); " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);",
                color1, color2
            ));
        });

        return button;
    }

    private Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(400);
        button.setPrefHeight(60);
        button.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-font-weight: 700; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: rgba(255,255,255,0.15); " +
            "-fx-background-radius: 15px; " +
            "-fx-border-radius: 15px; " +
            "-fx-border-color: rgba(255,255,255,0.3); " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: rgba(255,255,255,0.25); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: rgba(255,255,255,0.15); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: rgba(255,255,255,0.3); " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);"
            );
        });

        return button;
    }

    private void loadLevel(Stage stage, String level) {
        try {
            game.loadGrid(level);
            GameUI gameUI = new GameUI(game);
            
            // Transition avec fondu
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> gameUI.show(stage));
            fadeOut.play();
            
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("❌ Erreur");
            alert.setHeaderText("Impossible de charger le niveau");
            alert.setContentText("Fichier de grille introuvable : " + level);
            alert.showAndWait();
        }
    }

    private void showScoreboard(Stage stage) {
        ScoreBoardUI scoreBoardUI = new ScoreBoardUI(game);
        scoreBoardUI.show(stage);
    }

    private void showRules() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📖 Règles du Hitori");
        alert.setHeaderText("Comment jouer ?");
        alert.setContentText(
            "🎯 OBJECTIF :\n" +
            "Noircir certaines cases pour que la grille respecte 3 règles.\n\n" +
            
            "📋 LES 3 RÈGLES :\n\n" +
            
            "1️⃣ PAS DE DOUBLONS\n" +
            "Aucune ligne ni colonne ne doit contenir de chiffres identiques parmi les cases blanches.\n\n" +
            
            "2️⃣ PAS DE NOIRES ADJACENTES\n" +
            "Deux cases noircies ne peuvent jamais être côte à côte (horizontalement ou verticalement).\n\n" +
            
            "3️⃣ CONNEXITÉ DES BLANCHES\n" +
            "Toutes les cases blanches doivent former un seul bloc connecté.\n\n" +
            
            "💡 ASTUCE :\n" +
            "Commencez par identifier les doublons évidents, puis noircissez stratégiquement !\n\n" +
            
            "🎮 CONTRÔLES :\n" +
            "Cliquez sur une case vide pour la noircir ou la rendre blanche."
        );
        alert.showAndWait();
    }
}