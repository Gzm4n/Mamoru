package com.mamoru.view;

import com.mamoru.Main;
import javafx.scene.control.Button;
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

public class FeedScreen {
    private final Stage stage;
    private final String creatureName;
    private final String creatureType;
    private final MamoruStatus status;
    private final Runnable onReturn;

    public FeedScreen(Stage stage, String creatureName, String creatureType, MamoruStatus status, Runnable onReturn) {
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

        // Imagen de la galleta
        Image cookieImg = new Image(getClass().getResourceAsStream("/images/cookie.png"));
        ImageView cookieView = new ImageView(cookieImg);
        cookieView.setFitWidth(50);
        cookieView.setPreserveRatio(true);
        cookieView.setLayoutX(100);
        cookieView.setLayoutY(400);

        // Barra de hunger
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 14);
        Text hungerLabel = new Text("HUNGER");
        hungerLabel.setFont(pixelFont);
        hungerLabel.setFill(Color.WHITE);
        hungerLabel.setLayoutX(20);
        hungerLabel.setLayoutY(40);

        Rectangle hungerBar = new Rectangle();
        hungerBar.setHeight(15);
        hungerBar.setWidth(status.getHunger() * 2);
        hungerBar.setFill(Color.GREEN);
        hungerBar.setLayoutX(108);
        hungerBar.setLayoutY(25);

        // Drag and drop
        cookieView.setOnMousePressed(MouseEvent::consume);

        cookieView.setOnMouseDragged(e -> {
            cookieView.setLayoutX(e.getSceneX() - 25);
            cookieView.setLayoutY(e.getSceneY() - 25);
        });

        cookieView.setOnMouseReleased(e -> {
            Bounds cookieBounds = cookieView.localToScene(cookieView.getBoundsInLocal());
            Bounds mamoruBounds = creatureView.localToScene(creatureView.getBoundsInLocal());

            if (cookieBounds.intersects(mamoruBounds)) {
                status.feed();
                hungerBar.setWidth(status.getHunger() * 2);

                if (status.getHunger() >= 100){
                    saveStatus();
                    onReturn.run();
                }
            }

            // Volver a posición original
            cookieView.setLayoutX(100);
            cookieView.setLayoutY(400);
        });

        root.getChildren().addAll(hungerLabel, hungerBar, creatureView, cookieView);

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
