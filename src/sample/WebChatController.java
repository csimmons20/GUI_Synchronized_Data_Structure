package sample;

import javafx.event.ActionEvent;
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

import java.awt.event.KeyEvent;
import java.io.File;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class WebChatController {

    public TextField IPAddressText;
    public TextField portText;
    public Button startButton;
    public TextField statusText;
    public TextField yourNameText;

    public ListView TheChat;
    public ImageView UserOneImage;
    public ImageView UserTwoImage;
    public TextField UserOneText;
    public Button UserOneSend;
    public Button UserOneFile;
    public MediaView UserOneMedia;
    private SynchronizedQueue putQueue;
    private SynchronizedQueue getQueue;


    private Stage stage;
    private boolean serverMode;
    static boolean connected;

    public void initialize() {
        QueueFrom1to2 = new SynchronizedQueue();
        putQueue = new SynchronizedQueue();
        getQueue = new SynchronizedQueue();
        connected = false;

        // Create and start the GUI updater thread
        UpdateGUI updater = new UpdateGUI(putQueue, UserOneText, UserOneImage, TheChat, UserOneMedia);
        Thread updaterThread = new Thread(updater);
        updaterThread.start();
    }

    public void setStage(Stage theStage) {
        stage = theStage;
    }


    void setServerMode() {
        serverMode = true;
        startButton.setText("Start");
        try {
            // display the computer's IP address
            IPAddressText.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
            statusText.setText("Server start: getLocalHost failed. Exiting...");
        }
    }

    void setClientMode() {
        serverMode = false;
        startButton.setText("Connect");
        // display the IP address for the local computer
        IPAddressText.setText("127.0.0.1");
    }


    public void startButtonPressed() {
        // If connected, start button disabled;
        if (connected) {
            // don't do anything else
            return;
        }

        // We can't start network connection if Port number is unknown
        if (portText.getText().isEmpty()) {
            // user did not enter a Port number, so we can't connect.
            statusText.setText("Type a port number BEFORE connecting.");
            return;
        }

        // We're gonna start network connection!
        connected = true;
        startButton.setDisable(true);

        if (serverMode) {

            //Server: create a thread for listening for connecting clients
            ConnectToNewClients connectToNewClients = new ConnectToNewClients(Integer.parseInt(portText.getText()), putQueue, getQueue, statusText, yourNameText);
            Thread connectThread = new Thread(connectToNewClients);
            connectThread.start();

        } else {

            //Client: connect to a server
            try {
                Socket socketClientSide = new Socket(IPAddressText.getText(), Integer.parseInt(portText.getText()));
                statusText.setText("Connected to server at IP address " + IPAddressText.getText() + " on port " + portText.getText());


                //   Thread 1: handles communication TO server FROM client
                CommunicationOut communicationOut = new CommunicationOut(socketClientSide, new ObjectOutputStream(socketClientSide.getOutputStream()), getQueue, statusText);
                Thread communicationOutThread = new Thread(communicationOut);
                communicationOutThread.start();

                //   Thread 2: handles communication FROM server TO client
                CommunicationIn communicationIn = new CommunicationIn(socketClientSide, new ObjectInputStream(socketClientSide.getInputStream()), putQueue, null, statusText, yourNameText);
                Thread communicationInThread = new Thread(communicationIn);
                communicationInThread.start();

            } catch (Exception ex) {
                ex.printStackTrace();
                statusText.setText("Client start: networking failed. Exiting....");
            }

            // We connected!
        }
    }













    public void OpenFile() {
        //Open file and put it into either ImageView or MediaView
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {

            //Check to see if the file is an image
            if (file.getPath().endsWith(".png") || file.getPath().endsWith(".tiff") ||
                    file.getPath().endsWith(".jpeg") || file.getPath().endsWith(".gif") ||
                    file.getPath().endsWith(".jpg")) {

                Image newImage = new Image(file.toURI().toString());
                UserOneImage.setImage(newImage);
            }

            //Check to see if the file is a media
            if (file.getPath().endsWith(".avi") || file.getPath().endsWith(".flv") ||
                    file.getPath().endsWith(".wmv") || file.getPath().endsWith(".mov") ||
                    file.getPath().endsWith(".mp4")) {

                Media UOM = new Media(file.toURI().toString());
                MediaPlayer UOMP = new MediaPlayer(UOM);
                UOMP.setCycleCount(MediaPlayer.INDEFINITE);
                UOMP.setAutoPlay(true);
                UserOneMedia.setMediaPlayer(UOMP);
            }


        }

    }




    public void SendUserOne() {

        Message message = new Message(yourNameText.getText(), UserOneText.getText(), UserOneImage.getImage(), UserOneMedia.getMediaPlayer());

        boolean putSucceeded = getQueue.put(message);
        while (!putSucceeded){
        Thread.currentThread().yield();
        }
        Image userOneImg = UserOneImage.getImage();
        if (userOneImg != null) {
            while (!getQueue.put(userOneImg)) {
                Thread.currentThread().yield();
            }
        } else {
            String empty = "E.M.P.T.Y.";
            while (!getQueue.put(empty)) {
                Thread.currentThread().yield();
            }

        }
        System.out.println("SendMessage: PUT " + userOneImg);


        MediaPlayer userOneMed = UserOneMedia.getMediaPlayer();
        if (userOneMed != null) {
            while (!getQueue.put(userOneMed)) {
                Thread.currentThread().yield();
            }
        } else {
            String empty1 = "E.M.P.T.Y.";
            while (!getQueue.put(empty1)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneMed);


        String userOneTxt = yourNameText + ": " + UserOneText.getText();
        UserOneText.setText("");

        if (userOneTxt != null) {
            while (!getQueue.put(userOneTxt)) {
                Thread.currentThread().yield();
            }

        } else {
            String empty2 = "E.M.P.T.Y.";
            while (!getQueue.put(empty2)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneTxt);

    }

}
