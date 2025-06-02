package com.mamoru.view;

import com.mamoru.model.MamoruSaveData;
import com.mamoru.model.MamoruStatus;
import com.mamoru.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.Node;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

//clase del menu de seleccion de mascota
public class SelectCreatureMenu {
    private final Stage stage;
    private final Main main;

    public SelectCreatureMenu(Stage stage, Main main) {
        this.stage = stage;
        this.main = main;
    }

    //clase para mostrar la interfaz
    public void show() {
        //fuente y tamaño
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 16);

        //carga de imagen de logo
        ImageView logo = new ImageView(new Image(getClass().getResource("/images/mamoru_icon.png").toExternalForm()));
        logo.setFitWidth(250);
        logo.setPreserveRatio(true);

        //imagenes y formateo de animales
        ImageView ratIcon = createSelectableIcon("/images/rat_icon.png", "Rat");
        ImageView bunnyIcon = createSelectableIcon("/images/bunny_icon.png", "Bunny");
        ImageView parrotIcon = createSelectableIcon("/images/parrot_icon.png", "Parrot");
        ImageView ferretIcon = createSelectableIcon("/images/ferret_icon.png", "Ferret");

        //caja horizontal para las imagentes de los animales, posicionada en el centro de la ventana
        HBox creatureBox = new HBox(20, ratIcon, bunnyIcon, parrotIcon, ferretIcon);
        creatureBox.setAlignment(Pos.CENTER);

        //creacion de cuadro de texto y formateo
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your pet's name");
        nameField.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 13.5));
        nameField.setMaxWidth(300);

        //creacion del boton de inicio
        Button startBtn = new Button("Start");
        startBtn.setOnMouseEntered(e -> startBtn.setCursor(Cursor.HAND));
        startBtn.setFont(pixelFont);
        startBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        startBtn.setOnAction(e -> {
            String selected = (String) stage.getProperties().get("selectedCreature"); //obtiene la mascota elegida
            String name = nameField.getText().trim(); //obtiene el nombre de la mascota
            if (selected == null || name.isEmpty()) { //si alguno de los dos es nulo tira una alerta
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select a creature and enter a name.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            MamoruStatus status = new MamoruStatus(); //instancia del status del mamoru
            MamoruSaveData saveData = new MamoruSaveData(name, selected, status); //instancia de los datos guardados

            //control de excepciones
            try {
                FileOutputStream fileOut = new FileOutputStream("data/mamoru_save.dat"); //entra al archivo
                ObjectOutputStream out = new ObjectOutputStream(fileOut); //conversion para escribir objetos serializados
                out.writeObject(saveData); //guarda el estado del mamoru
                out.close();
                fileOut.close();//cierra el archivo
            } catch (IOException ex) {
                ex.printStackTrace(); //detalle de erores
            }

            //abre el gameScreen
            new GameScreen(stage, name, selected, status, main).show();
        });

        //boton para regresar al menu principal
        Button backBtn = new Button("Back");
        backBtn.setOnMouseEntered(e -> backBtn.setCursor(Cursor.HAND));
        backBtn.setFont(pixelFont);
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        backBtn.setOnAction(e -> main.showStartMenu(stage));

        //caja horizontal para ambos botones
        HBox buttonBox = new HBox(15, backBtn, startBtn);
        buttonBox.setAlignment(Pos.CENTER);

        //caja vertical para alinear logo, animales, caja y botones
        VBox content = new VBox(30, logo, creatureBox, nameField, buttonBox);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40));

        //imagen de fondo
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/images/selector_background.png").toExternalForm(),
                        700, 500, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );

        //root del layout
        StackPane root = new StackPane();
        root.setBackground(new Background(bgImage));
        root.getChildren().add(content);

        //instancia de la nueva escena
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Select Your Mamoru");
        stage.show();
    }

    //Creacion de los iconos seleccionables
    private ImageView createSelectableIcon(String path, String creatureName) {
        ImageView icon = new ImageView(new Image(getClass().getResource(path).toExternalForm())); //busqueda de imagen
        icon.setOnMouseEntered(e -> icon.setCursor(Cursor.HAND)); //cursor a mano
        icon.setFitHeight(67); //altura comun de las imagenes
        icon.setPreserveRatio(true); //preservacion de ratio
        icon.setStyle("-fx-effect: dropshadow(gaussian, transparent, 0, 0, 0, 0);"); //no deberia tener sombras visuales

        //pequeña animacion cuando aparecen los personajes
        ScaleTransition st = new ScaleTransition(Duration.millis(200), icon);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();

        //efecto de brillo
        icon.setOnMouseClicked(e -> {
            stage.getProperties().put("selectedCreature", creatureName);
            for (Node node : ((HBox) icon.getParent()).getChildren()) {
                if (node instanceof ImageView) {
                    node.setStyle("-fx-effect: dropshadow(gaussian, transparent, 0, 0, 0, 0);");
                }
            }
            icon.setStyle("-fx-effect: dropshadow(gaussian, white, 15, 0.5, 0, 0);");
        });

        return icon;
    }
}
