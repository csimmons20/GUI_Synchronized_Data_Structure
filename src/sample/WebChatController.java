package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaView;

import javax.swing.text.html.ImageView;
import javax.swing.text.html.ListView;
import java.util.ArrayList;

public class WebChatController {

    public ListView TheChat;
    public MediaView UserOneMedia;
    public MediaView UserTwoMedia;
    public ImageView UserOneImage;
    public ImageView UserTwoImage;
    public TextField UserOneText;
    public TextField UserTwoText;
    public Button UserOneSend;
    public Button UserTwoSend;
    public Button UserOneFile;
    public Button UserTwoFile;

    private SynchronizedQueue TheQueue;

    public void initialize(){
        TheQueue = new SynchronizedQueue();

        //Update conversation, using UpdateGUI to get information
        UserOneText.setText(TheQueue.getUserOneText());
        UserTwoText.setText(TheQueue.getUserTwoText());

        ArrayList TheChatTexts = TheQueue.getTheChatTexts();
        for (int i = 0; i < TheChatTexts.size(); i++) {
            TheChat.getItems().add(new Label((String)TheChatTexts.get(i)));
        }
    }


    public void OpenFile() {
        //create a way to make open file and put it into mediaview or imageview, depending if its a image, video, or audio
    }

    public void SendFile() {
        //send files
    }
    public void SendMessage() {
        System.out.println("Message Send: " + UserOneText.getText());
        System.out.println("Message Send: " + UserTwoText.getText());

        // Update the list view with the text from both users
        Label user1Message = new Label("User 1: " + UserOneText.getText());
        Label user2Message = new Label("User 2: " + UserTwoText.getText());

        //Make them try again until they succeed
        boolean putSucceeded = TheQueue.put(user1Message);
        while (!putSucceeded) {
            Thread.currentThread().yield();
            putSucceeded = TheQueue.put(user1Message);
        }
        boolean putSuccess = TheQueue.put(user2Message);
        while(!putSuccess){
            Thread.currentThread().yield();
            putSuccess = TheQueue.put(user2Message);
        }


        //Clear the bottom text field because it has been used.
        UserOneText.setText("");
        UserTwoText.setText("");

    }




}
