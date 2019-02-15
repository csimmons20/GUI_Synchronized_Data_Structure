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

    private SynchronizedQueue originalQueue;
    private TextField GUIMessageView;
    private ImageView GUIimageView;
    private ListView TheChat;
    private MediaView GUIMediaView;
    private TextField yourNameText;


    UpdateGUI(SynchronizedQueue queue, TextField GUIMessage, ImageView imageView, ListView chat, MediaView media) {
        originalQueue = queue;
        GUIMessageView = GUIMessage;
        GUIimageView = imageView;
        TheChat = chat;
        GUIMediaView = media;
        yourNameText = name;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()) {
            // Ask queue for a image from user to display

            Object next = originalQueue.get();

            while (next == null) {
                Thread.currentThread().yield();
                next = originalQueue.get();
            }

            System.out.println("UpdateGUI GOT: " + next);

            if (!next.equals("E.M.P.T.Y.")) {
                Image nextImage = (Image)next;
                // get() has an image to work with
                GUIimageView.setImage(nextImage);
            }


            //Ask queue for a media from user to display
            Object forward = originalQueue.get();

            while (forward == null){
                Thread.currentThread().yield();
                forward = originalQueue.get();
            }
            System.out.println("UpdateGUI GOT: " + forward);

            if (!forward.equals("E.M.P.T.Y.")){
                MediaPlayer nextMedia = (MediaPlayer)forward;
                //get() have a media to work with
                GUIMediaView.setMediaPlayer(nextMedia);
            }



            // Ask queue for a message from user to add to chat
            Object message = originalQueue.get();

            while (message == null) {
                Thread.currentThread().yield();
                message = originalQueue.get();
            }

            Object finalMessage = message;
            if (!finalMessage.equals("E.M.P.T.Y")){
                // Update the list view with the text from the bottom text field
                Platform.runLater(() -> TheChat.getItems().add(new Label((String)finalMessage)));
            }



        }
    }
}
