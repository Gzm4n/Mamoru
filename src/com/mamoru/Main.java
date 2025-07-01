package com.mamoru;

import com.mamoru.model.MamoruStatus;
import com.mamoru.view.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application { //clase de JavaFX
    //mostrar el menu principal
    @Override
    public void start(Stage primaryStage) {
        showStartMenu(primaryStage);
    }

    //metodo especifico para crear una instancia del menu principal y mostrarlo
    public void showStartMenu(Stage stage) {
        StartMenu startMenu = new StartMenu(stage, this); //this se refiere a "este es el main"
        startMenu.show();
    }

    //lo mismo, pero con el menu de seleccion de mascota
    public void showSelectCreatureMenu(Stage stage) {
        SelectCreatureMenu creatureMenu = new SelectCreatureMenu(stage, this);
        creatureMenu.show();
    }

    public void showFeedScreen(Stage stage, String creatureName, String creatureType, MamoruStatus status) {
        FeedScreen feedScreen = new FeedScreen(stage, creatureName, creatureType, status, () -> {
            // Acción al regresar (opcional)
            showGameScreen(stage, creatureName, creatureType, status);
        });
        feedScreen.show();
    }

    public void showCleanScreen(Stage stage, String creatureName, String creatureType, MamoruStatus status) {
        CleanScreen cleanScreen = new CleanScreen(stage, creatureName, creatureType, status, () -> {
            // Acción al regresar (opcional)
            showGameScreen(stage, creatureName, creatureType, status);
        });
        cleanScreen.show();
    }

    public void showGameScreen(Stage stage, String creatureName, String creatureType, MamoruStatus status) {
        GameScreen gameScreen = new GameScreen(stage, creatureName, creatureType, status, this);
        gameScreen.show();
    }


    //ejecucion
    public static void main(String[] args) {
        launch(args);
    }
}
