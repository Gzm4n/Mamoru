package com.mamoru;

import com.mamoru.view.SelectCreatureMenu;
import com.mamoru.view.StartMenu;
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

    //ejecucion
    public static void main(String[] args) {
        launch(args);
    }
}
