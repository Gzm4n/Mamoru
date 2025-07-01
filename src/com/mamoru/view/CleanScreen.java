package com.mamoru.view;

import javafx.scene.shape.Rectangle;
import com.mamoru.model.MamoruStatus;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CleanScreen {
    private final Stage stage;
    private final String creatureName;
    private final String creatureType;
    private final MamoruStatus status;
    private final Runnable onReturn;

    public CleanScreen(Stage stage, String creatureName, String creatureType, MamoruStatus status, Runnable onReturn) {
        this.stage = stage;
        this.creatureName = creatureName;
        this.creatureType = creatureType;
        this.status = status;
        this.onReturn = onReturn;
    }

    public void show() {
        // Fondo personalizado
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/images/game_background.png").toExternalForm(), 700, 500, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );

        Pane root = new Pane();
        root.setPrefSize(700, 500);
        root.setBackground(new Background(bgImage));

        // Imagen del Mamoru
        String imagePath = "/images/" + creatureType.toLowerCase() + ".png";
        Image creatureImg = new Image(getClass().getResourceAsStream(imagePath));
        ImageView creatureView = new ImageView(creatureImg);
        creatureView.setFitWidth(100);
        creatureView.setPreserveRatio(true);
        creatureView.setLayoutX(300);
        creatureView.setLayoutY(360);

        Image soapImg = new Image(getClass().getResourceAsStream("/images/soap.png"));
        ImageView soapView = new ImageView(soapImg);
        soapView.setFitHeight(80);
        soapView.setPreserveRatio(true);
        soapView.setLayoutX(100);
        soapView.setLayoutY(400);

        // Barra de hunger
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 14);
        Text hungerLabel = new Text("HYGIENE");
        hungerLabel.setFont(pixelFont);
        hungerLabel.setFill(Color.WHITE);
        hungerLabel.setLayoutX(20);
        hungerLabel.setLayoutY(40);

        Rectangle hygieneBar = new Rectangle();
        hygieneBar.setHeight(15);
        hygieneBar.setWidth(status.getHunger() * 2);
        hygieneBar.setFill(Color.GREEN);
        hygieneBar.setLayoutX(118);
        hygieneBar.setLayoutY(25);

        // Drag and drop
        soapView.setOnMousePressed(MouseEvent::consume);

        soapView.setOnMouseDragged(e -> {
            soapView.setLayoutX(e.getSceneX() - 25);
            soapView.setLayoutY(e.getSceneY() - 25);
        });

        soapView.setOnMouseReleased(e -> {
            Bounds soapBounds = soapView.localToScene(soapView.getBoundsInLocal());
            Bounds mamoruBounds = creatureView.localToScene(creatureView.getBoundsInLocal());

            if (soapBounds.intersects(mamoruBounds)) {
                status.clean();
                hygieneBar.setWidth(status.getHygiene() * 2);
                if (status.getHygiene() >=100){
                    saveStatus();
                    onReturn.run();
                }
            }

            // Volver a posición original
            soapView.setLayoutX(100);
            soapView.setLayoutY(400);
        });

        root.getChildren().addAll(hungerLabel, hygieneBar, creatureView, soapView);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void saveStatus() {
        new java.io.File("data").mkdirs();
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(
                new java.io.FileOutputStream("data/mamoru_save.dat"))) {
            oos.writeObject(status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
