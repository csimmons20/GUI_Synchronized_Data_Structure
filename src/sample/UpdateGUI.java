package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UpdateGUI implements Runnable {

    private SynchronizedQueue inQueue;
    private ImageView GUIimageView;
    private ListView TheChat;
    private MediaView GUIMediaView;
    private TextField yourNameText;
    public MediaPlayer mp;
    private LocalTime time;

    UpdateGUI(SynchronizedQueue queue, ImageView imageView, ListView chat, MediaView media, TextField name) {
        inQueue = queue;
        GUIimageView = imageView;
        TheChat = chat;
        GUIMediaView = media;
        yourNameText = name;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()) {

            // Try to create time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            time = LocalTime.parse(LocalTime.now().format(dtf));

            //Format formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            //time = LocalTime.now();



            // Try to get a Message from the inputQueue
            Message message = (Message) inQueue.get();
            while (message == null) {
                Thread.currentThread().yield();
                message = (Message) inQueue.get();
            }
            Message finalMessage = message; // needed for Platform.runLater()

            // Got a message from another client... prepend the chat with it.
            // Write text
            if (!(finalMessage.getData1()).equals("")) {
                System.out.println("DATA 1 got");
                Platform.runLater(() -> TheChat.getItems().add(0, new Label("[" + time + "] " + finalMessage.sender() + " says \"" + finalMessage.getData1() + "\"")));
            }

            if (!finalMessage.sender().equals(yourNameText.getText())) {
                // Update picture
                if (finalMessage.getData2() != null) {
                    System.out.println("DATA 2 got");
                    Image nextImage = finalMessage.getData2();
                    Platform.runLater(() -> GUIimageView.setImage(nextImage));
                    Platform.runLater(() -> TheChat.getItems().add(0, new Label("[" + time + "] " + "Messenger: " + finalMessage.sender() + " sent a picture.")));
                }
                // Update Media
                if (finalMessage.getData3() != null) {
                    System.out.println("DATA 3 got" + finalMessage.getData3());
                    Media nextMedia = new Media(finalMessage.getMediaFile().toURI().toString());
                    mp = new MediaPlayer(nextMedia);
                    mp.setCycleCount(MediaPlayer.INDEFINITE);
                    //mp.setAutoPlay(true);
                    Platform.runLater(() -> GUIMediaView.setMediaPlayer(mp));
                    Platform.runLater(() -> TheChat.getItems().add(0, new Label("[" + time + "] " + "Messenger: " + finalMessage.sender() + " sent a video.")));

                }
            }
        }
    }
}