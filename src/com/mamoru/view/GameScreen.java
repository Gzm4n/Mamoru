package com.mamoru.view;

import com.mamoru.Main;
import com.mamoru.model.MamoruStatus;
import com.mamoru.model.MamoruSaveData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.*;

public class GameScreen {
    private final Stage stage;
    private String creatureName;
    private String creatureType;
    private MamoruStatus status;
    private Rectangle hungerBar;
    private Rectangle energyBar;
    private Rectangle hygieneBar;
    private Timeline timeline;
    private final Main main;

    //directorio donde se guarda el progreso del juego
    private static final String SAVE_FILE = "data/mamoru_save.dat";

    //constructor utilizado al iniciar una nueva partida
    public GameScreen(Stage stage, String creatureName, String creatureType, MamoruStatus status, Main main) {
        this.stage = stage;
        this.creatureName = creatureName;
        this.creatureType = creatureType;
        this.status = status;
        this.main = main;
        saveStatus();
        this.status.applyTimeDecay();
    }

    //constructor para cargar partida
    public GameScreen(Stage stage, Main main) {
        this.stage = stage;
        this.main = main;
        loadSaveOrDefault();
        this.status.applyTimeDecay();
    }

    //metodo correspondiente a la interfaz
    public void show() {
        //fuente personalizada y sus tamaños
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 16);
        Font smallFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 10);

        //formateo de la imagen del fondo
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/images/game_background.png").toExternalForm(), 700, 500, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );

        //codigo de color del fondo de los botones
        String brownColor = "#8C4B2D";

        //caja de elementos verticales, barras de estado (layout)
        VBox statusBox = new VBox(8,
                createLabeledBar("HUNGER", status.getHunger(), smallFont, Color.valueOf(brownColor), "hunger"),
                createLabeledBar("ENERGY", status.getEnergy(), smallFont, Color.valueOf(brownColor), "energy"),
                createLabeledBar("HYGIENE", status.getHygiene(), smallFont, Color.valueOf(brownColor), "hygiene")
        );
        //formateo y posicion
        statusBox.setAlignment(Pos.BOTTOM_LEFT);
        statusBox.setPadding(new Insets(20, 10, 10, 20));

        //creacion de botones
        Button feedBtn = createStyledButton("Feed", pixelFont, brownColor);
        Button playBtn = createStyledButton("Play", pixelFont, brownColor);
        Button sleepBtn = createStyledButton("Sleep", pixelFont, brownColor);
        Button cleanBtn = createStyledButton("Clean", pixelFont, brownColor);

        //funcionamiento de botones setOnAction
        feedBtn.setOnAction(e -> {
            status.feed(); //actualiza el valor del estado
            updateBars(); //actualiza visualmente la barra
            saveStatus(); //guarda el estado
        });
        playBtn.setOnAction(e -> {
            status.play();
            updateBars();
            saveStatus();
        });
        sleepBtn.setOnAction(e -> {
            status.sleep();
            updateBars();
            saveStatus();
        });
        cleanBtn.setOnAction(e -> {
            status.clean();
            updateBars();
            saveStatus();
        });

        //caja horizontal de botones en la parte superior
        HBox buttonRow = new HBox(15, feedBtn, playBtn, sleepBtn, cleanBtn);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(10)); //relleno interno

        //nombre de la mascota formateo
        Text nameTitle = new Text(creatureName);
        nameTitle.setFont(Font.font(pixelFont.getFamily(), 22));
        nameTitle.setFill(Color.WHITE);

        //caja para mostrar el nombre de la mascota en el centro
        VBox nameBox = new VBox(nameTitle);
        nameBox.setAlignment(Pos.TOP_CENTER);
        nameBox.setPadding(new Insets(5));

        //cargar la imagen de la mascota desde el path correspondiente
        String imagePath = "/images/" + creatureType.toLowerCase() + ".png";
        Image image;
        //manejar excepciones
        try {
            image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (Exception e) {
            System.out.println("No se encontró la imagen del Mamoru: " + imagePath); //aviso de errores por consola
            image = new Image(getClass().getResource("/images/rat.png").toExternalForm()); //imagen de default
        }

        //mostrar la imagen cargada y su formato en pantalla
        ImageView creatureImage = new ImageView(image);
        creatureImage.setFitHeight(140);
        creatureImage.setPreserveRatio(true);

        //caja vertical para alinear a la imagen en el centro inferior
        VBox bottomCreature = new VBox(creatureImage);
        bottomCreature.setAlignment(Pos.BOTTOM_CENTER);
        bottomCreature.setPadding(new Insets(20));

        //layout de la interfaz completa con borderpane
        BorderPane layout = new BorderPane();
        layout.setTop(buttonRow);
        layout.setCenter(bottomCreature);
        layout.setLeft(statusBox);
        layout.setBottom(nameBox);
        layout.setBackground(new Background(bgImage));

        //creacion de boton de salida con una imagen del directorio
        Image exitIcon = new Image(getClass().getResource("/images/exit.png").toExternalForm());
        ImageView exitView = new ImageView(exitIcon);
        exitView.setFitWidth(32);
        exitView.setPreserveRatio(true);

        Button exitButton = new Button();
        exitButton.setGraphic(exitView);
        exitButton.setStyle("-fx-background-color: transparent;");
        exitButton.setCursor(javafx.scene.Cursor.HAND); //hacer que el cursor cambia a una mano
        exitButton.setOnAction(e -> {
            saveStatus();
            if (main != null) {
                main.showStartMenu(stage);
            } else {
                stage.close();
            }
        });

        //anadir una capa de opacidad al layout y colocal el boton de exit a la esquina inferior derecha
        Rectangle overlay = new Rectangle(700, 500, Color.rgb(0, 0, 0, 0.1));
        StackPane root = new StackPane();
        root.getChildren().addAll(overlay, layout);
        StackPane.setAlignment(exitButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(exitButton, new Insets(10));
        root.getChildren().add(exitButton);

        //creacion de la ventana
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Mamoru - Game");
        stage.setOnCloseRequest(e -> saveStatus()); //guardar al salir
        stage.show();

        //empieza a contar el tiempo para afectar al estado del mamoru
        startDecayTimer();
    }

    //cargar un mamoru desde un archivo o usar caracteres default
    private void loadSaveOrDefault() {
        //manejo de excepciones en caso de fallar la carga del archivo
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) { //leer el archivo
            MamoruSaveData data = (MamoruSaveData) ois.readObject(); //convertir el archivo al objeto original y castear
            this.creatureName = data.getName();
            this.creatureType = data.getType();
            this.status = data.getStatus();
            System.out.println("Cargado: " + creatureName + ", " + creatureType);
        } catch (Exception e) {
            System.out.println("Fallo al cargar. Usando valores por defecto");
            this.creatureName = "Your Mamoru";
            this.creatureType = "rat";
            this.status = new MamoruStatus();
        }
    }

    //guardar estado del mamoru
    private void saveStatus() {
        try {
            new File("data").mkdirs(); //crea la carpeta de data en caso de que no exista
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) { //abre el archivo
                oos.writeObject(new MamoruSaveData(creatureName, creatureType, status)); //sobreescribe el archivo
                System.out.println("Guardado: " + creatureName + ", " + creatureType);
            }
        } catch (IOException e) {
            System.out.println("Error saving Mamoru: " + e.getMessage());
        }
    }

    //formateo de los botones superiores
    private Button createStyledButton(String text, Font font, String bgColor) {
        Button btn = new Button(text);
        btn.setFont(font);

        String normalStyle = "-fx-background-color: " + bgColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 6 12;";

        String hoverStyle = "-fx-background-color: derive(" + bgColor + ", -20%);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 6 12;";

        //hover, se ilumina con el click encima
        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
        btn.setCursor(javafx.scene.Cursor.HAND);

        return btn;
    }

    //formateo de las barras de estado
    private VBox createLabeledBar(String labelText, int value, Font font, Color color, String type) {
        Text label = new Text(labelText);
        label.setFont(font);
        label.setFill(Color.WHITE);

        Rectangle barBg = new Rectangle(120, 10, Color.GRAY);
        Rectangle barFill = new Rectangle(120 * value / 100.0, 10, color);

        switch (type) {
            case "hunger": hungerBar = barFill; break;
            case "energy": energyBar = barFill; break;
            case "hygiene": hygieneBar = barFill; break;
        }

        StackPane barStack = new StackPane(barBg, barFill);
        barStack.setAlignment(Pos.CENTER_LEFT);
        barStack.setMaxWidth(120);

        VBox box = new VBox(2, label, barStack);
        return box;
    }

    //actualizar valores de las barras para la interfaz
    private void updateBars() {
        hungerBar.setWidth(120 * status.getHunger() / 100.0);
        energyBar.setWidth(120 * status.getEnergy() / 100.0);
        hygieneBar.setWidth(120 * status.getHygiene() / 100.0);
    }

    //timer para bajar los valores de las barras segun el tiempo
    private void startDecayTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> { //valores se actualizan cada 30 minutos
            status.decay();
            updateBars();
            saveStatus();

            //logica en caso de que las barras lleguen a 0
            if (status.getHunger() <= 0 || status.getEnergy() <= 0 || status.getHygiene() <= 0) {
                timeline.stop();
                Alert alert = new Alert(Alert.AlertType.INFORMATION); //ventana de alerta
                alert.setTitle("Mamoru ha muerto");
                alert.setHeaderText("Lo siento");
                alert.setContentText("Tu Mamoru ha muerto por descuido.");
                alert.showAndWait();
                stage.close();
            }
        }));

        //ciclos infinitos
        timeline.setCycleCount(Timeline.INDEFINITE);
        //inicia el contador
        timeline.play();
    }
}
