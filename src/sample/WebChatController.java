package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;

public class WebChatController {

    public ListView TheChat;
    public ImageView UserOneImage;
    public ImageView UserTwoImage;
    public TextField UserOneText;
    public TextField UserTwoText;
    public TextField yourNameText;
    public TextField IPAddressText;
    public TextField portText;
    public Button UserOneSend;
    public Button UserOneFile;
    public Button startButton;
    public MediaView UserOneMedia;
    public MediaView UserTwoMedia;

    private SynchronizedQueue Queue;
    private Stage stage;

    private boolean serverMode;
    static boolean connected;

    public void initialize(){
        Queue = new SynchronizedQueue();
        connected = false;

        // Create and start the GUI updater thread
        UpdateGUI updater = new UpdateGUI(Queue,UserOneText,UserOneImage,TheChat, yourNameText);
        Thread updaterThread = new Thread(updater);
        updaterThread.start();

        //GUI Updates text, image, and file to either people.
    }

    void setServerMode() {
        serverMode = true;
        startButton.setText("Start");
        try {
            // display the computer's IP address
            IPAddressText.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
            statusText.setText("Server start: getLocalHost failed. Exiting....");
        }
    }

    void setClientMode() {
        serverMode = false;
        startButton.setText("Connect");
        // display the IP address for the local computer
        IPAddressText.setText("127.0.0.1");
    }

    public void setStage(Stage theStage) {
        stage = theStage;
    }

    public void OpenFile() {
        //Open file and put it into imageview
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        // If user chose an image file via FileChooser
        if (file != null) {
            Image newImage = new Image(file.toURI().toString());
            UserOneImage.setImage(newImage);
        }

        //Open file and put it into mediaview
        final FileChooser fileChooser1 = new FileChooser();
        File file1 = fileChooser1.showOpenDialog(stage);

        //If user chose a media file via FileChooser
        if(file1 != null) {
            Media UOM = new Media(file1.toURI().toString());
            MediaPlayer UOMP = new MediaPlayer(UOM);
            UOMP.setAutoPlay(true);
            UserOneMedia.setMediaPlayer(UOMP);


        }


    }

    public void OpenFileTwo() {
        //create a way to make open file and put it into imageview
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        // If user chose an image file via FileChooser
        if (file != null) {
            Image newImage = new Image(file.toURI().toString());
            UserTwoImage.setImage(newImage);
        }

    }

    public void SendUserOne() {
        Image userOneImg = UserOneImage.getImage();

        if (userOneImg != null) {
            while (!Queue.put(userOneImg)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneImg);



        String userOneText = "User 1: " + UserOneText.getText();
        UserOneText.setText("");

        if (userOneText != null) {
            while (!Queue.put(userOneText)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneText);
    }


    public void SendUserTwo() {
        Image userTwoImg = UserTwoImage.getImage();

        if (userTwoImg != null) {
            while (!Queue.put(userTwoImg)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userTwoImg);



        String userTwoTxt = "User 2: " + UserTwoText.getText();
        UserTwoText.setText("");

        if (userTwoTxt != null) {
            while (!Queue.put(userTwoTxt)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userTwoTxt);
    }

}
