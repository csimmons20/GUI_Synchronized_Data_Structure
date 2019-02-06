package sample;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UpdateGUI implements Runnable {

    private SynchronizedQueue originalQueue;
    private TextField GUIMessageView;
    private ImageView GUIimageView;
    private ListView TheChat;


    UpdateGUI(SynchronizedQueue queue, TextField GUIMessage, ImageView imageView, ListView chat ) {
        originalQueue = queue;
        GUIMessageView = GUIMessage;
        GUIimageView = imageView;
        TheChat = chat;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()) {
            // Ask queue for a image to display
            Image next = (Image)originalQueue.get();

            while (next == null) {
                Thread.currentThread().yield();
                next = (Image) originalQueue.get();
            }

            System.out.println("UpdateGUI GOT: " +next);
            // FINALLY I have a file to do something with
            GUIimageView.setImage(next);

            // Ask queue for a message to add to chat
            String message = (String)originalQueue.get();

            while (message == null) {
                Thread.currentThread().yield();
                message = (String) originalQueue.get();
            }

            System.out.println("UpdateGUI GOT: " +message);
            // FINALLY I have a file to do something with

            // Update the list view with the text from the bottom text field
            TheChat.getItems().add(new Label(message));
            // Clear the bottom text field because it has been used.


        }
    }
}
