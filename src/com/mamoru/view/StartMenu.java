package com.mamoru.view;

import com.mamoru.Main;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;

//Menu de inicio
public class StartMenu {
    private final Stage stage;
    private final Main main;

    public StartMenu(Stage stage, Main main) {
        this.stage = stage;
        this.main = main;
    }

    //para mostrar la interfaz
    public void show() {
        //fuente personalizada
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 18);

        //imagen del título
        ImageView titleImage = new ImageView(getClass().getResource("/images/mamoru_icon.png").toExternalForm());
        titleImage.setFitWidth(300);
        titleImage.setPreserveRatio(true);
        VBox titleBox = new VBox(titleImage);
        titleBox.setAlignment(Pos.CENTER);

        //botones
        Button newGameBtn = createStyledButton("New Mamoru", pixelFont);
        Button loadGameBtn = createStyledButton("Load Mamoru", pixelFont);
        Button exitBtn = createStyledButton("Exit", pixelFont);

        //acciones
        newGameBtn.setOnAction(e -> main.showSelectCreatureMenu(stage)); //pasa a la pantalla de seleccion
        loadGameBtn.setOnAction(e -> { //cargar partida guardada
            File saveFile = new File("data/mamoru_save.dat"); //abre el archivo
            if (saveFile.exists()) {
                new GameScreen(stage, main).show(); //usa el constructor de cargar partida
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Save Found");
                alert.setHeaderText(null);
                alert.setContentText("No saved Mamoru found.");
                alert.showAndWait();
            }
        });

        //boton para salir del juego
        exitBtn.setOnAction(e -> stage.close());

        //caja vertical para los botones
        VBox buttonBox = new VBox(10, newGameBtn, loadGameBtn, exitBtn);
        buttonBox.setAlignment(Pos.CENTER);

        //caja vertical de los botones y el titulo
        VBox centerContent = new VBox(30, titleBox, buttonBox);
        centerContent.setAlignment(Pos.CENTER);

        //imagen de la rata
        ImageView ratImage = new ImageView(getClass().getResource("/images/rat_accesories.png").toExternalForm());
        ratImage.setFitHeight(170);
        ratImage.setPreserveRatio(true);
        VBox ratBox = new VBox(ratImage);
        ratBox.setAlignment(Pos.CENTER);

        //espaciador invisible para equilibrar rata-centerContent
        Region rightSpacer = new Region();
        rightSpacer.setMinWidth(ratImage.getFitWidth());

        //distribución horizontal equilibrada
        HBox layoutRow = new HBox(40, ratBox, centerContent, rightSpacer);
        layoutRow.setAlignment(Pos.CENTER);

        //fondo con imagen
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/images/menu_background.png").toExternalForm(),
                        700, 500, false, true),
                BackgroundRepeat.NO_REPEAT, //no repetir la imagen horizontalmente
                BackgroundRepeat.NO_REPEAT, //no repetir la imagen verticalmente
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        layoutRow.setBackground(new Background(bgImage));
        layoutRow.setPrefSize(700, 500);

        //mostrar escena
        Scene scene = new Scene(layoutRow);
        stage.setScene(scene);
        stage.setTitle("Mamoru");
        stage.show();
    }

    //boton formateado y hover
    private Button createStyledButton(String text, Font font) {
        Button btn = new Button(text);
        btn.setFont(font);
        btn.setCursor(Cursor.HAND);

        String baseColor = "white";
        String normalStyle = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: " + baseColor + ";";
        String hoverStyle = "-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: " + baseColor + "; -fx-background-radius: 6;";

        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));

        return btn;
    }
}
