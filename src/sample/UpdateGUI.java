package sample;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UpdateGUI implements Runnable {

    int allData;

    private SynchronizedQueue originalQueue;
    private TextField GUIMessageView;
    private ImageView GUIimageView;

    UpdateGUI(SynchronizedQueue queue, TextField GUIMessage, ImageView imageView) {
        originalQueue = queue;
        GUIMessageView = GUIMessage;
        GUIimageView = imageView;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()) {
            // Ask queue for a message to open
            Image next = (Image)originalQueue.get();

            while (next == null) {
                Thread.currentThread().yield();
                next = (Image) originalQueue.get();
            }

            // FINALLY I have a file to do something with
            GUIimageView.setImage(next);


        }
    }
}
