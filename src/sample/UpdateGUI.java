package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class UpdateGUI implements Runnable {

    private SynchronizedQueue Queue;
    private TextField GUIMessageView;
    private ImageView GUIimageView;
    private MediaView GUIMediaView;
    private ListView TheChat;
    private TextField yourNameText;


    UpdateGUI(SynchronizedQueue queue, TextField GUIMessage, ImageView imageView, MediaView media, ListView chat, TextField name) {
        Queue = queue;
        GUIMessageView = GUIMessage;
        GUIimageView = imageView;
        GUIMediaView = media;
        TheChat = chat;
        yourNameText = name;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()) {
            // Ask queue for a image from user 1 to display
            Object next = Queue.get();

            while (next == null) {
                Thread.currentThread().yield();
                next = Queue.get();
            }

            System.out.println("UpdateGUI GOT: " + next);

            if (!next.equals("E.M.P.T.Y")) {
                Image nextImage = (Image) next;
                // get() has to get image to work with
                GUIimageView.setImage(nextImage);
            }

            // Ask queue for a media from user to display
            Object forward = Queue.get();

            while (forward == null) {
                Thread.currentThread().yield();
                forward = Queue.get();
            }
            System.out.println("UpdatedGUI GOT: " + forward);

            if (!forward.equals("E.M.P.T.Y")) {
                MediaPlayer nextMedia = (MediaPlayer) forward;
                // get() has to get media to work with
                GUIMediaView.setMediaPlayer(nextMedia);
            }

            // Ask queue for a message from user 1 to add to chat
            Object message = Queue.get();

            while (message == null) {
                Thread.currentThread().yield();
                message = Queue.get();
            }

            Object finalMessage = message;
            if (!finalMessage.equals("E.M.P.T.Y")) {
                // Update the list view with the text from the bottom text field
                Platform.runLater(() -> TheChat.getItems().add(new Label((String)finalMessage)));
            }
        }
    }
}
