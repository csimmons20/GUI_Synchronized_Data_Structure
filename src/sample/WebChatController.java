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

    public ListView TheChat;
    public ImageView UserOneImage;
    public ImageView UserTwoImage;
    public TextField UserOneText;
    public TextField UserTwoText;
    public TextField yourNameText;
    public TextField IPAddressText;
    public TextField portText;
    public TextField statusText;
    public Button UserOneSend;
    public Button UserOneFile;
    public Button startButton;
    public MediaView UserOneMedia;
    public MediaView UserTwoMedia;

    private SynchronizedQueue QueueFrom1to2;
    private SynchronizedQueue QueueFrom2to1;
    private SynchronizedQueue inQueue;
    private SynchronizedQueue outQueue;

    private SynchronizedQueue Queue;
    private Stage stage;

    public void initialize() {
        QueueFrom1to2 = new SynchronizedQueue();
        QueueFrom2to1 = new SynchronizedQueue();

        // Create and start the GUI updater thread
        UpdateGUI updater = new UpdateGUI(QueueFrom1to2, UserTwoText, UserTwoImage, TheChat, UserTwoMedia);
        UpdateGUI updater2 = new UpdateGUI(QueueFrom2to1, UserOneText, UserOneImage, TheChat, UserOneMedia);
        Thread updaterThread = new Thread(updater);
        Thread updaterThread2 = new Thread(updater2);
        updaterThread.start();
        updaterThread2.start();

        //GUI Updates text, image, and file to either people.
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
            statusText.setText("Server start: getLocalHost failed. Exiting....");
        }
    }

    void setClientMode() {
        serverMode = false;
        startButton.setText("Connect");
        // display the IP address for the local computer
        IPAddressText.setText("127.0.0.1");
    }

    public void startButtonPressed() {
        // If we're already connected, start button should be disabled
        if (connected) {
            // don't do anything else; the threads will stop and everything will be cleaned up by them.
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

            // We're a server: create a thread for listening for connecting clients
            ConnectToNewClients connectToNewClients = new ConnectToNewClients(Integer.parseInt(portText.getText()), inQueue, outQueue, statusText, yourNameText);
            Thread connectThread = new Thread(connectToNewClients);
            connectThread.start();

        } else {

            // We're a client: connect to a server
            try {
                Socket socketClientSide = new Socket(IPAddressText.getText(), Integer.parseInt(portText.getText()));
                statusText.setText("Connected to server at IP address " + IPAddressText.getText() + " on port " + portText.getText());

                // The socketClientSide provides 2 separate streams for 2-way communication
                //   the InputStream is for communication FROM server TO client
                //   the OutputStream is for communication TO server FROM client
                // Create data reader and writer from those stream (NOTE: ObjectOutputStream MUST be created FIRST)

                // Every client prepares for communication with its server by creating 2 new threads:
                //   Thread 1: handles communication TO server FROM client
                CommunicationOut communicationOut = new CommunicationOut(socketClientSide, new ObjectOutputStream(socketClientSide.getOutputStream()), outQueue, statusText);
                Thread communicationOutThread = new Thread(communicationOut);
                communicationOutThread.start();

                //   Thread 2: handles communication FROM server TO client
                CommunicationIn communicationIn = new CommunicationIn(socketClientSide, new ObjectInputStream(socketClientSide.getInputStream()), inQueue, null, statusText, yourNameText);
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

            //Check to see if file is an image
            if (file.getPath().endsWith(".png") || file.getPath().endsWith(".tiff") ||
                    file.getPath().endsWith(".jpeg") || file.getPath().endsWith(".gif") ||
                    file.getPath().endsWith(".jpg")) {

                Image newImage = new Image(file.toURI().toString());
                UserOneImage.setImage(newImage);
            }

            //Check to see if file is a media
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


   /* public void OpenFileTwo() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {

            //Check to see if file is an image
            if (file.getPath().endsWith(".png") || file.getPath().endsWith(".tiff") ||
                    file.getPath().endsWith(".jpeg") || file.getPath().endsWith(".gif")) {

                Image newImage = new Image(file.toURI().toString());
                UserTwoImage.setImage(newImage);
            }

            //Check to see if file is a media
            if (file.getPath().endsWith(".avi") || file.getPath().endsWith(".flv") ||
                    file.getPath().endsWith(".wmv") || file.getPath().endsWith(".mov") ||
                    file.getPath().endsWith(".mp4")) {

                Media UOM = new Media(file.toURI().toString());
                MediaPlayer UOMP = new MediaPlayer(UOM);
                UOMP.setAutoPlay(true);
                UserTwoMedia.setMediaPlayer(UOMP);
            }


        }
    }*/


    public void SendUserOne() {
        Image userOneImg = UserOneImage.getImage();
        if (userOneImg != null) {
            while (!QueueFrom1to2.put(userOneImg)) {
                Thread.currentThread().yield();
            }
        } else {
            String empty = "E.M.P.T.Y.";
            while (!QueueFrom1to2.put(empty)) {
                Thread.currentThread().yield();
            }

        }
        System.out.println("SendMessage: PUT " + userOneImg);


        MediaPlayer userOneMed = UserOneMedia.getMediaPlayer();
        if (userOneMed != null) {
            while (!QueueFrom1to2.put(userOneMed)) {
                Thread.currentThread().yield();
            }
        } else {
            String empty1 = "E.M.P.T.Y.";
            while (!QueueFrom1to2.put(empty1)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneMed);


        String userOneTxt = "User 1: " + UserOneText.getText();
        UserOneText.setText("");

        if (userOneTxt != null) {
            while (!QueueFrom1to2.put(userOneTxt)) {
                Thread.currentThread().yield();
            }

        } else {
            String empty2 = "E.M.P.T.Y.";
            while (!QueueFrom1to2.put(empty2)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneTxt);

    }

    /* public void SendUserTwo() {
        Image userTwoImg = UserTwoImage.getImage();
        if (userTwoImg != null) {
            while (!QueueFrom2to1.put(userTwoImg)) {
                Thread.currentThread().yield();
            }
        } else {
            String empty = "E.M.P.T.Y.";
            while (!QueueFrom2to1.put(empty)) {
                Thread.currentThread().yield();
            }

        }
        System.out.println("SendMessage: PUT " + userTwoImg);


        MediaPlayer userTwoMed = UserTwoMedia.getMediaPlayer();
        if (userTwoMed != null) {
            while (!QueueFrom2to1.put(userTwoMed)) {
                Thread.currentThread().yield();
            }
        } else {
            String empty1 = "E.M.P.T.Y.";
            while (!QueueFrom2to1.put(empty1)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userTwoMed);


        String userTwoTxt = "User 2: " + UserTwoText.getText();
        UserTwoText.setText("");

        if (userTwoTxt != null) {
            while (!QueueFrom2to1.put(userTwoTxt)) {
                Thread.currentThread().yield();
            }

        } else {
            String empty2 = "E.M.P.T.Y.";
            while (!QueueFrom2to1.put(empty2)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userTwoTxt);


    } */
}
