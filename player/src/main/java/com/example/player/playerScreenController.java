package com.example.player;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.concurrent.Callable;

public class playerScreenController {
    @FXML
    private MediaView mediaView;

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private ImageView fullShrinkScreenImg;

    @FXML
    private ImageView volMuteBtn;
    @FXML
    private HBox buttonControls;

    @FXML
    private Label timePassed;

    @FXML
    private Label totalTime;

    @FXML
    private Slider volSlider;

    @FXML
    private Slider timeSlider;

    private MediaPlayer mediaPlayer;

    private Stage stage;
    private Duration mediaDuration;

    public void addMedia(File file,String mediaType){
        // function to add media file to the player
        // uploads file and then plays it

        // sets up the media player
        String path=file.toURI().toString();
        Media media=new Media(path);
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView.setMediaPlayer(mediaPlayer);
        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();

        // if media selected is video, then proper ratio of height and width of view port is maintained
        if(mediaType=="video"){
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));
        }

        mediaPlayer.setOnReady(new Runnable() {
            // when media player is ready
            @Override
            public void run() {
                // gets the duration of media
                mediaDuration= media.getDuration();
            }
        });

        // event listeners and methods for synchronizing slider and the playing time of media
        mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                timeSlider.setMax(newTime.toSeconds());
                totalTime.setText(convertTime(newTime));
            }
        });
        timeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean isChanging) {
                bindCurrentTimeLabel();
                if(!isChanging){
                    mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }
            }
        });
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                bindCurrentTimeLabel();
                double currentTime=mediaPlayer.getCurrentTime().toSeconds();
                if(Math.abs(currentTime-newVal.doubleValue())>0.5){
                    mediaPlayer.seek(Duration.seconds(newVal.doubleValue()));
                }
            }
        });
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                bindCurrentTimeLabel();
                if(!timeSlider.isValueChanging()){
                    timeSlider.setValue(newTime.toSeconds());
                }
            }
        });
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if(!timePassed.textProperty().equals(totalTime.textProperty())){
                    timePassed.textProperty().unbind();
                    timePassed.setText(convertTime(mediaPlayer.getTotalDuration()));
                }
            }
        });
    }

    private String convertTime(Duration duration){
        // function to convert time in desired output
        // return time in string format

        int totalTime= (int) duration.toSeconds();
        int seconds=totalTime%60;
        int hours=totalTime/60;
        int minutes=hours%60;
        hours=hours/60;
        if (hours > 0)
            return String.format("%d:%02d:%02d",hours,minutes,seconds);

        else
            return String.format("%02d:%02d",minutes,seconds);
    }
    public void bindCurrentTimeLabel() {
        // Binds the text of the current time label to the current time of the video.
        // This will allow the timer to update along with the video.
        timePassed.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return convertTime(mediaPlayer.getCurrentTime()) + " / ";
            }
        }, mediaPlayer.currentTimeProperty()));
    }
    @FXML
    public void playPauseVideo(){
        // play-pause button on the player
        if(mediaPlayer.getStatus().toString()=="PLAYING") {
            playPauseBtn.setImage(new Image(getClass().getResourceAsStream("images/playBtn.png")));
            mediaPlayer.pause();
        }
        else{
            playPauseBtn.setImage(new Image(getClass().getResourceAsStream("images/pauseBtn.png")));
            mediaPlayer.play();
        }
    }
    @FXML
    public void fastForward(){
        // fast forwards the video by 10 secs
        Duration newTime= new Duration(mediaPlayer.getCurrentTime().toMillis()+10000);
        mediaPlayer.seek(newTime);
    }

    @FXML
    public void backForward(){
        // goes back by 10 secs
        Duration newTime=new Duration(mediaPlayer.getCurrentTime().toMillis()-10000);
        mediaPlayer.seek(newTime);
    }

    @FXML
    public void fullScreen(ActionEvent event){
        // enters full screen
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        if(stage.isFullScreen()){
            fullShrinkScreenImg.setImage(new Image(getClass().getResourceAsStream("images/fullScreen.png")));
            stage.setFullScreen(false);
            buttonControls.setSpacing(50);
        }
        else {
            fullShrinkScreenImg.setImage(new Image(getClass().getResourceAsStream("images/shrinkScreen.png")));
            stage.setFullScreen(true);
            buttonControls.setSpacing(200);
        }
    }

    @FXML
    public void muteAudio(){
        // mutes the audio
        if(mediaPlayer.isMute()){
            volMuteBtn.setImage(new Image(getClass().getResourceAsStream("images/volumeBtn.png")));
            mediaPlayer.setMute(false);
        }
        else{
            volMuteBtn.setImage(new Image(getClass().getResourceAsStream("images/muteBtn.png")));
            mediaPlayer.setMute(true);
        }
    }

    @FXML
    public void onVolSlide(){
        // volume control
        double volToBe=volSlider.getValue()/100;
        mediaPlayer.setVolume(volToBe);
        if(volToBe==0.0){
            volMuteBtn.setImage(new Image(getClass().getResourceAsStream("images/muteBtn.png")));
        }
        volMuteBtn.setImage(new Image(getClass().getResourceAsStream("images/volumeBtn.png")));
    }

}
