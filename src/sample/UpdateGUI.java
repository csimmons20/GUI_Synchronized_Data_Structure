package sample;


import javafx.scene.control.TextField;

public class UpdateGUI implements Runnable {

    int allData;
    private SynchronizedQueue originalQueue;
    private TextField GUIMessageView;

    UpdateGUI(SynchronizedQueue queue, TextField GUIMessage) {
        originalQueue = queue;
        GUIMessageView = GUIMessage;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()) {
            // Ask queue for a message to open
            TextField next = (TextField)originalQueue.get();
            while (next == null) {
                Thread.currentThread().yield();
                next = (TextField) originalQueue.get();
            }

            // FINALLY I have a file to do something with
            GUIMessageView.setTextField(next);
        }
    }
}
