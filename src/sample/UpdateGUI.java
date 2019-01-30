package sample;


import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UpdateGUI implements Runnable {

    private SynchronizedQueue originalQueue;
    private TextField UOneM;
    private TextField UTwoM;
    private ListView Chatbox;

    UpdateGUI(SynchronizedQueue queue, TextField UOM, TextField UTM, ListView CB) {
        originalQueue = queue;
        UOneM = UOM;
        UTwoM = UTM;
        Chatbox = CB;
    }

    public void run() {
        Thread.currentThread().setName("GUIUpdater Thread");

        while (!Thread.interrupted()){
            //Update Text
            TheChat.getItems().add(new Label("User 1: " + UserOneText.getText()));
            TheChat.getItems().add(new Label("User 2: " + UserTwoText.getText()));
        }



        }
    }

