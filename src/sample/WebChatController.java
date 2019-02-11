package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class WebChatController {

    public ListView TheChat;
    public ImageView UserOneImage;
    public ImageView UserTwoImage;
    public TextField UserOneText;
    public TextField UserTwoText;
    public Button UserOneSend;
    public Button UserTwoSend;
    public Button UserOneFile;
    public Button UserTwoFile;
    public MediaView UserOneMedia;
    public MediaView UserTwoMedia;

    private SynchronizedQueue QueueFrom1to2;
    private SynchronizedQueue QueueFrom2to1;
    private Stage stage;

    public void initialize(){
        QueueFrom1to2 = new SynchronizedQueue();
        QueueFrom2to1 = new SynchronizedQueue();

        // Create and start the GUI updater thread
        UpdateGUI updater = new UpdateGUI(QueueFrom1to2, UserTwoText, UserTwoImage, TheChat);
        UpdateGUI updater2 = new UpdateGUI(QueueFrom2to1,UserOneText,UserOneImage,TheChat);
        Thread updaterThread = new Thread(updater);
        Thread updaterThread2 = new Thread(updater2);
        updaterThread.start();
        updaterThread2.start();

        //GUI Updates text, image, and file to either people.
    }

    public void setStage(Stage theStage) {
        stage = theStage;
    }

    public void OpenFile() {
        //create a way to make open file and put it into mediaview or imageview, depending if its a image, video, or audio
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        // If user 1 chose a file via FileChooser
        if (file != null) {
            Image newImage = new Image(file.toURI().toString());
            UserOneImage.setImage(newImage);
        }

    }

    public void SendUserOne() {
        Image userOneImg = UserOneImage.getImage();
        //Image userTwoImg = UserTwoImage.getImage();

        if (userOneImg != null) {
            while (!QueueFrom1to2.put(userOneImg)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneImg);



        String userOneText = "User 1: " + UserOneText.getText();
        UserOneText.setText("");

        if (userOneText != null) {
            while (!QueueFrom1to2.put(userOneText)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneText);
    }


    public void SendUserTwo() {
        Image userTwoImg = UserTwoImage.getImage();

        if (userTwoImg != null) {
            while (!QueueFrom2to1.put(userTwoImg)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userTwoImg);



        String userTwoTxt = "User 2: " + UserTwoText.getText();
        UserTwoText.setText("");

        if (userTwoTxt != null) {
            while (!QueueFrom2to1.put(userTwoTxt)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userTwoTxt);
    }

}
