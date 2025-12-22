package ui;

import controller.HitoriGame;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelSelectorUI {

    private HitoriGame game;

    public LevelSelectorUI(HitoriGame game) {
        this.game = game;
    }

    public void show(Stage stage) {
        stage.setTitle("🎯 Hitori - Sélection du Niveau");

        VBox root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("selector-root");

        Label title = new Label("🎯 H I T O R I");
        title.getStyleClass().add("selector-title");
        
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.08);
        pulse.setToY(1.08);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        Label subtitle = new Label("Choisissez votre défi");
        subtitle.getStyleClass().add("selector-subtitle");

        VBox levelButtons = createLevelButtons(stage);

        Label footer = new Label("Un jeu de logique et de réflexion");
        footer.getStyleClass().add("selector-footer");

        root.getChildren().addAll(title, subtitle, levelButtons, footer);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLevelButtons(Stage stage) {
        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("level-container");

        Button easyButton = createLevelButton(
            "🌱 FACILE",
            "Grille 5×5 - Parfait pour débuter",
            "#10b981",
            "#059669"
        );
        easyButton.setOnAction(e -> loadLevel(stage, "easy"));

        Button mediumButton = createLevelButton(
            "⚡ MOYEN",
            "Grille 7×7 - Pour les joueurs expérimentés",
            "#f59e0b",
            "#d97706"
        );
        mediumButton.setOnAction(e -> loadLevel(stage, "medium"));

        Button hardButton = createLevelButton(
            "🔥 DIFFICILE",
            "Grille 9×9 - Le challenge ultime",
            "#ef4444",
            "#dc2626"
        );
        hardButton.setOnAction(e -> loadLevel(stage, "hard"));

        Button scoresButton = createSecondaryButton("🏆 Meilleurs Scores");
        scoresButton.setOnAction(e -> showScoreboard(stage));

        Button loadButton = createSecondaryButton("📂 Charger Partie");
        loadButton.setOnAction(e -> loadSavedGame(stage));

        Button rulesButton = createSecondaryButton("📖 Règles du Jeu");
        rulesButton.setOnAction(e -> showRules());

        Button exitButton = createSecondaryButton("❌ Quitter");
        exitButton.setOnAction(e -> stage.close());

        container.getChildren().addAll(
            easyButton, 
            mediumButton, 
            hardButton,
            new Region(),
            scoresButton,
            loadButton,
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
        descText.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: rgba(255,255,255,0.95);");

        buttonContent.getChildren().addAll(mainText, descText);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setPrefWidth(500);
        button.setPrefHeight(100);
        button.setStyle(String.format(
            "-fx-background-color: linear-gradient(145deg, %s, %s); " +
            "-fx-background-radius: 20px; " +
            "-fx-border-radius: 20px; " +
            "-fx-border-color: rgba(255,255,255,0.4); " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);",
            color1, color2
        ));

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
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 8);",
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
                "-fx-border-color: rgba(255,255,255,0.4); " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);",
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
            "-fx-text-fill: #1e293b; " +
            "-fx-background-color: rgba(255,255,255,0.9); " +
            "-fx-background-radius: 15px; " +
            "-fx-border-radius: 15px; " +
            "-fx-border-color: #cbd5e1; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: #4338ca; " +
                "-fx-background-color: white; " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: #4338ca; " +
                "-fx-border-width: 3px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(67,56,202,0.3), 15, 0, 0, 5);"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: #1e293b; " +
                "-fx-background-color: rgba(255,255,255,0.9); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: #cbd5e1; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);"
            );
        });

        return button;
    }

    private void loadLevel(Stage stage, String level) {
        try {
            game.loadGrid(level);
            GameUI gameUI = new GameUI(game);
            
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

    private void loadSavedGame(Stage stage) {
        try {
            util.FileUtils.GameState savedState = util.FileUtils.loadGameState();
            if (savedState != null) {
                GameUI gameUI = new GameUI(savedState.game, savedState.seconds, savedState.moveCount);
                
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> gameUI.show(stage));
                fadeOut.play();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("⚠️ Aucune sauvegarde");
                alert.setHeaderText(null);
                alert.setContentText("Aucune partie sauvegardée trouvée.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("❌ Erreur");
            alert.setHeaderText("Impossible de charger la partie");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void showRules() {
        Stage rulesStage = new Stage();
        rulesStage.setTitle("📖 Règles du Hitori");
        
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #f0f4f8, #e0e7ff);");
        
        Label title = new Label("📖 RÈGLES DU HITORI");
        title.setStyle(
            "-fx-font-size: 32px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #4338ca; " +
            "-fx-effect: dropshadow(gaussian, rgba(67,56,202,0.3), 10, 0.5, 0, 4);"
        );
        
        Label subtitle = new Label("Maîtrisez les 3 règles fondamentales");
        subtitle.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-text-fill: #64748b; " +
            "-fx-padding: 0 0 10 0;"
        );
        
        VBox rulesContainer = new VBox(25);
        rulesContainer.setAlignment(Pos.CENTER_LEFT);
        rulesContainer.setPadding(new Insets(20));
        rulesContainer.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-border-color: #cbd5e1; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 20px;"
        );
        
        VBox rule1 = createRuleBox(
            "1️⃣",
            "PAS DE DOUBLONS",
            "Aucune ligne ni colonne ne doit contenir de chiffres identiques parmi les cases BLANCHES (non noircies).",
            "Exemple : Si une ligne contient [1 2 3 2 5], vous devez noircir l'un des deux « 2 ».",
            "#10b981"
        );
        
        VBox rule2 = createRuleBox(
            "2️⃣",
            "PAS DE NOIRES ADJACENTES",
            "Deux cases noircies ne peuvent JAMAIS être côte à côte horizontalement ou verticalement.",
            "Exemple : Si vous noircissez une case, les 4 cases autour (haut, bas, gauche, droite) doivent rester blanches.",
            "#f59e0b"
        );
        
        VBox rule3 = createRuleBox(
            "3️⃣",
            "CONNEXITÉ DES BLANCHES",
            "Toutes les cases blanches doivent former UN SEUL bloc connecté. Il doit être possible de passer d'une case blanche à n'importe quelle autre en ne traversant que des cases blanches.",
            "Exemple : Vous ne pouvez pas créer d'îlots de cases blanches isolés les uns des autres.",
            "#8b5cf6"
        );
        
        rulesContainer.getChildren().addAll(rule1, rule2, rule3);
        
        VBox tipsBox = new VBox(10);
        tipsBox.setAlignment(Pos.CENTER_LEFT);
        tipsBox.setPadding(new Insets(15, 20, 15, 20));
        tipsBox.setStyle(
            "-fx-background-color: rgba(139, 92, 246, 0.1); " +
            "-fx-background-radius: 15px; " +
            "-fx-border-color: #8b5cf6; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 15px;"
        );
        
        Label tipsTitle = new Label("💡 ASTUCES DE PRO");
        tipsTitle.setStyle(
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #8b5cf6;"
        );
        
        Label tip1 = new Label("• Commencez par repérer les doublons évidents dans les lignes et colonnes");
        tip1.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 14px;");
        tip1.setWrapText(true);
        tip1.setMaxWidth(500);
        
        Label tip2 = new Label("• Attention aux cases qui créeraient des noires adjacentes !");
        tip2.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 14px;");
        tip2.setWrapText(true);
        tip2.setMaxWidth(500);
        
        Label tip3 = new Label("• Vérifiez régulièrement que toutes les cases blanches restent connectées");
        tip3.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 14px;");
        tip3.setWrapText(true);
        tip3.setMaxWidth(500);
        
        tipsBox.getChildren().addAll(tipsTitle, tip1, tip2, tip3);
        
        VBox controlsBox = new VBox(10);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.setPadding(new Insets(15, 20, 15, 20));
        controlsBox.setStyle(
            "-fx-background-color: rgba(67, 56, 202, 0.1); " +
            "-fx-background-radius: 15px; " +
            "-fx-border-color: #4338ca; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 15px;"
        );
        
        Label controlsTitle = new Label("🎮 CONTRÔLES");
        controlsTitle.setStyle(
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #4338ca;"
        );
        
        Label control1 = new Label("• Clic sur case blanche → Noircir la case");
        control1.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 14px;");
        
        Label control2 = new Label("• Clic sur case noire → Rendre la case blanche");
        control2.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 14px;");
        
        Label control3 = new Label("• Toutes les cases peuvent être modifiées - cliquez librement !");
        control3.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 14px;");
        control3.setWrapText(true);
        control3.setMaxWidth(500);
        
        controlsBox.getChildren().addAll(controlsTitle, control1, control2, control3);
        
        Button closeButton = new Button("✓ J'ai compris !");
        closeButton.setPrefWidth(250);
        closeButton.setPrefHeight(50);
        closeButton.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: linear-gradient(to bottom, #10b981, #059669); " +
            "-fx-background-radius: 15px; " +
            "-fx-border-radius: 15px; " +
            "-fx-border-color: #047857; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 12, 0, 0, 5);"
        );
        
        closeButton.setOnMouseEntered(e -> {
            closeButton.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: linear-gradient(to bottom, #059669, #047857); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: #10b981; " +
                "-fx-border-width: 3px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.5), 15, 0, 0, 6);"
            );
        });
        
        closeButton.setOnMouseExited(e -> {
            closeButton.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: linear-gradient(to bottom, #10b981, #059669); " +
                "-fx-background-radius: 15px; " +
                "-fx-border-radius: 15px; " +
                "-fx-border-color: #047857; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 12, 0, 0, 5);"
            );
        });
        
        closeButton.setOnAction(e -> rulesStage.close());
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(new VBox(15, rulesContainer, tipsBox, controlsBox));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(500);
        
        root.getChildren().addAll(title, subtitle, scrollPane, closeButton);
        
        Scene scene = new Scene(root, 650, 750);
        rulesStage.setScene(scene);
        rulesStage.show();
    }
    
    private VBox createRuleBox(String emoji, String ruleTitle, String description, String example, String accentColor) {
        VBox ruleBox = new VBox(8);
        ruleBox.setPadding(new Insets(15));
        ruleBox.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: " + accentColor + "; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 12px;"
        );
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 28px;");
        
        Label titleLabel = new Label(ruleTitle);
        titleLabel.setStyle(
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + accentColor + ";"
        );
        
        header.getChildren().addAll(emojiLabel, titleLabel);
        
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(550);
        descLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #1e293b; " +
            "-fx-padding: 5 0 0 0;"
        );
        
        Label exampleLabel = new Label("💡 " + example);
        exampleLabel.setWrapText(true);
        exampleLabel.setMaxWidth(550);
        exampleLabel.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-style: italic; " +
            "-fx-padding: 5 0 0 0;"
        );
        
        ruleBox.getChildren().addAll(header, descLabel, exampleLabel);
        return ruleBox;
    }
}