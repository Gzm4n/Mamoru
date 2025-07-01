package com.mamoru.view;

import com.mamoru.Main;
import com.mamoru.model.MamoruStatus;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SleepOverlay {

    public static void showSleepScreen(Stage stage, MamoruStatus status, Main main) {
        Font pixelFont = Font.loadFont(SleepOverlay.class.getResourceAsStream("/fonts/pixel.ttf"), 16);
        Font smallFont = Font.loadFont(SleepOverlay.class.getResourceAsStream("/fonts/pixel.ttf"), 10);

        VBox overlayRoot = new VBox(20);
        overlayRoot.setAlignment(Pos.CENTER);

        BackgroundImage bgImage = new BackgroundImage(
                new Image(SleepOverlay.class.getResource("/images/sleep_bg.png").toExternalForm(), 700, 500, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );

        overlayRoot.setBackground(new Background(bgImage));

        long minutesLeft = status.getMinutesLeftToWakeUp();
        Text title = new Text("💤 Mamoru is sleeping");
        title.setFont(Font.font(pixelFont.getFamily(), 20));
        title.setStyle("-fx-fill: white;");

        Text countdown = new Text("Time left: " + minutesLeft + " minutes");
        countdown.setFont(Font.font(smallFont.getFamily(), 16));
        countdown.setStyle("-fx-fill: white;");

        Button returnButton = new Button("Go back");
        returnButton.setOnAction(e -> main.showStartMenu(stage));

        overlayRoot.getChildren().addAll(title, countdown, returnButton);

        Scene overlayScene = new Scene(overlayRoot, 600, 400);
        stage.setScene(overlayScene);
        stage.show();
    }
}
