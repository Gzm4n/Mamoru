package com.mamoru.view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mamoru.model.MamoruStatus;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PlayScreen {
    private Pane root;
    private ImageView mamoru;
    private ArrayList<ImageView> obstacles;
    private int hits = 0;
    private int energySpent = 0;
    private Label energyLabel;
    private MamoruStatus status;
    private Runnable onFinish;
    private String creatureType;
    private Rectangle energyBar;
    private boolean showAlert = false;
    private int lives = 3;
    private Label livesLabel;

    public PlayScreen(Stage stage, MamoruStatus status, Runnable onFinish, String creatureType){
        this.status = status;
        this.onFinish = onFinish;
        this.creatureType = creatureType;

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 10);

        // Panel principal
        root = new Pane();
        root.setPrefSize(700, 500);

        //Background
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/images/minigame_bg.png").toExternalForm(), 700, 500, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );
        root.setBackground(new Background(bgImage));

        // Imagen de Mamoru
        Image mamoruImg = new Image(getClass().getResource("/images/" +creatureType+ ".png").toExternalForm());
        mamoru = new ImageView(mamoruImg);
        mamoru.setFitHeight(80);
        mamoru.setPreserveRatio(true);
        mamoru.setTranslateX((700 - 40) / 2.0);
        mamoru.setTranslateY(400);

        // Obstáculos
        obstacles = new ArrayList<>();

        VBox energyBarBox = createLabeledBar(status.getEnergy(), Color.WHITE);

        livesLabel = new Label("VIDAS: " + lives);
        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setTranslateX(10);
        livesLabel.setTranslateY(50);
        livesLabel.setFont(pixelFont);

        root.getChildren().addAll(energyBarBox, livesLabel, mamoru);

        // Crear escena
        Scene scene = new Scene(root);
        stage.setScene(scene);

        int mamoruWidth = 40;
        int panelWidth = 700;

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    mamoru.setTranslateX(Math.max(mamoru.getTranslateX() - 20, 0));
                    break;
                case RIGHT:
                    mamoru.setTranslateX(Math.min(mamoru.getTranslateX() + 20, panelWidth - mamoruWidth));
                    break;
            }
        });

        // Animación principal
        AnimationTimer timer = new AnimationTimer() {
            long lastTime = 0;
            long lastEnergyTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                if (lastEnergyTime == 0) lastEnergyTime = now;

                double elapsedSeconds = (now - lastTime) / 1_000_000_000.0;
                double energyElapsed = (now - lastEnergyTime) / 1_000_000_000.0;

                if (energyElapsed >= 3.0) {
                    energySpent += 2;
                    double actualEnergy = Math.max(0, status.getEnergy() - energySpent);
                    energyBar.setWidth(120 * actualEnergy / 100.0);

                    if (actualEnergy <= 15 && !showAlert) {
                        showAlert = true;
                        Platform.runLater(() -> {
                            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                            alerta.setTitle("¡Advertencia!");
                            alerta.setHeaderText(null);
                            alerta.setContentText("¡Tu Mamoru está agotado! La energía está muy baja.");
                            alerta.showAndWait();
                        });
                    }

                    lastEnergyTime = now;
                }

                if (elapsedSeconds >= 1.5) {
                    generateObstacle();
                    lastTime = now;
                }

                updateObstacles();

                if (lives <= 0) {
                    stop();
                    endGame(stage);
                }
            }
        };


        timer.start();
        stage.show();
    }


    private VBox createLabeledBar(int value, Color color) {
        // Cargar la fuente personalizada
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 10);

        javafx.scene.text.Text label = new javafx.scene.text.Text("ENERGY");
        label.setFont(pixelFont);
        label.setFill(Color.WHITE);

        // Barra visual
        Rectangle barBg = new Rectangle(120, 10, Color.GRAY);
        Rectangle barFill = new Rectangle(120 * value / 100.0, 10, color);
        energyBar = barFill;

        StackPane barStack = new StackPane(barBg, barFill);
        barStack.setAlignment(Pos.CENTER_LEFT);
        barStack.setMaxWidth(120);

        VBox box = new VBox(2, label, barStack);
        box.setTranslateX(10);
        box.setTranslateY(10);
        return box;
    }


    private void generateObstacle() {
        Image brickImage = new Image(getClass().getResource("/images/brick.png").toExternalForm());
        ImageView obs = new ImageView(brickImage);
        obs.setFitWidth(70);
        obs.setPreserveRatio(true);
        obs.setTranslateX(new Random().nextInt(500));
        obs.setTranslateY(0);
        obstacles.add(obs);
        root.getChildren().add(obs);
    }

    private void updateObstacles() {
        Iterator<ImageView> it = obstacles.iterator();
        while (it.hasNext()) {
            ImageView obs = it.next();
            obs.setTranslateY(obs.getTranslateY() + 5);

            if (obs.getBoundsInParent().intersects(mamoru.getBoundsInParent())) {
                lives--;
                livesLabel.setText("VIDAS: " + lives);
                root.getChildren().remove(obs);
                it.remove();
            }
            else if (obs.getTranslateY() > 500) {
                root.getChildren().remove(obs);
                it.remove();
            }
        }
    }

    private void endGame(Stage stage) {
        Platform.runLater(() -> {
            status.setEnergy(Math.max(0, status.getEnergy() - energySpent));
            onFinish.run();
        });
    }
}