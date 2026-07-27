package com.bureauintelligent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Application JavaFX principale du Bureau Intelligent.
 *
 * <p>Cette classe ne contient volontairement aucune logique métier :
 * elle sera enrichie au fil des branches {@code feature/*} (dashboard,
 * gestion des tâches, sessions de travail, etc.). Pour l'instant, elle
 * sert uniquement à valider que la configuration Maven/JavaFX est
 * fonctionnelle (branche {@code chore/setup}).</p>
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Bureau Intelligent — configuration initiale OK");
        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 640, 400);
        stage.setTitle("Bureau Intelligent");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
