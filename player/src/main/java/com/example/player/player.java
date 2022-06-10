package com.example.player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class player extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // loads the main screen of the player
        Parent root = FXMLLoader.load(getClass().getResource("playerWindow.fxml"));
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("REP Media Player");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}