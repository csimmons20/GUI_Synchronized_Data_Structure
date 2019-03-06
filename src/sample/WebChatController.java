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
    public ImageView ImagetoSend;
    public ImageView ImagetoRecieve;
    public TextField UserOneText;
    public Button UserOneSend;
    public Button UserOneFile;
    public MediaView MediatoSend;
    public MediaView MediatoRecieve;

    private SynchronizedQueue inQueue;
    private SynchronizedQueue outQueue;

    private File fileToSend;

    private Stage stage;
    private boolean serverMode;
    static boolean connected;

    public void initialize() {
        inQueue = new SynchronizedQueue();
        outQueue = new SynchronizedQueue();
        connected = false;

        // Create and start the GUI updater thread
        UpdateGUI updater = new UpdateGUI(inQueue, ImagetoRecieve, TheChat, MediatoRecieve, yourNameText);
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
            ConnectToNewClients connectToNewClients = new ConnectToNewClients(Integer.parseInt(portText.getText()), inQueue, outQueue, statusText, yourNameText);
            Thread connectThread = new Thread(connectToNewClients);
            connectThread.start();

        } else {

            //Client: connect to a server
            try {
                Socket socketClientSide = new Socket(IPAddressText.getText(), Integer.parseInt(portText.getText()));
                statusText.setText("Connected to server at IP address " + IPAddressText.getText() + " on port " + portText.getText());


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

            //Check to see if the file is an image
            if (file.getPath().endsWith(".png") || file.getPath().endsWith(".tiff") ||
                    file.getPath().endsWith(".jpeg") || file.getPath().endsWith(".gif") ||
                    file.getPath().endsWith(".jpg")) {

                Image newImage = new Image(file.toURI().toString());
                ImagetoSend.setImage(newImage);
            }

            //Check to see if the file is a media
            if (file.getPath().endsWith(".avi") || file.getPath().endsWith(".flv") ||
                    file.getPath().endsWith(".wmv") || file.getPath().endsWith(".mov") ||
                    file.getPath().endsWith(".mp4")) {

                fileToSend = file;
                Media UOM = new Media(file.toURI().toString());
                MediaPlayer UOMP = new MediaPlayer(UOM);
                UOMP.setCycleCount(MediaPlayer.INDEFINITE);
                UOMP.setAutoPlay(true);
                MediatoSend.setMediaPlayer(UOMP);
            }


        }

    }




    public void SendUserOne() {


        Message message = new Message(yourNameText.getText(), UserOneText.getText(), ImagetoSend.getImage(), fileToSend);

            boolean putSucceeded = outQueue.put(message);
            while (!putSucceeded) {
                Thread.currentThread().yield();
                putSucceeded = outQueue.put(message);
            }


        //Clear text after being sent
        UserOneText.setText("");

    }

}
