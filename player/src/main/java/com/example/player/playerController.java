package com.example.player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class playerController {
    /*
        controller class for main screen of player
        sets up the functionality of the buttons, present in the main screen
    */
    public File file;
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    private HBox btnParent;

    @FXML
    public void onClickAudio(ActionEvent event) throws IOException {
        // when audio button is clicker

        // chooses the audio file from the computer
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio","*.mp3","*.m4a","*.flac")
        );

        // gets the audio file
        file=chooser.showOpenDialog(btnParent.getScene().getWindow());
        if(file!=null){
            // loads up the new screen to play audio
            FXMLLoader loader=new FXMLLoader(getClass().getResource("playerScreen.fxml"));
            root=loader.load();

            // loads another controller file
            playerScreenController screenController=loader.getController();
            screenController.addMedia(file,"audio");

            // sets up the stage
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene= new Scene(root, 900, 400);
            stage.setScene(scene);
            stage.setTitle("REP Media Player - "+ file.getName());
            stage.show();
        }
        else{
            // if no audio file was selected
            System.out.println("No file selected");
        }
    }

    @FXML
    public void onClickVideo(ActionEvent event) throws IOException{
        // when the video button is clicked

        // chooses the video file from the computer
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video", "*.mp4","*.mov","*.avi","*.wmv","*.mkv")
        );

        // gets the video file
        file=chooser.showOpenDialog(btnParent.getScene().getWindow());
        if(file!=null){
            // laods up new screen to play video
            FXMLLoader loader=new FXMLLoader(getClass().getResource("playerScreen.fxml"));
            root=loader.load();

            // loads another controller file
            playerScreenController screenController=loader.getController();
            screenController.addMedia(file,"video");

            // sets up the stage
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene= new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.setTitle("REP Media Player - "+ file.getName());
            stage.show();
        }
        else{
            // if no video file was selected
            System.out.println("No file selected");
        }
    }
    @FXML
    public void enterFullScreenMenuItem(){
        // sets full screen
        stage.setFullScreen(true);
    }

    @FXML
    public void closePlayerMenuItem(){
        // closes media app
        System.exit(0);
    }
}
