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

        // Now that model has been initialized from a file, update View with saved values from Model
        UserOneText.setText(TheQueue.getUserOneText());
        UserTwoText.setText(TheQueue.getUserTwoText());

        ArrayList BottomListViewTexts = TheQueue.getBottomListViewTexts();
        for (int i = 0; i < BottomListViewTexts.size(); i++) {
            TheChat.getItems().add(new Label((String) BottomListViewTexts.get(i)));
        }

        //GUI Updates text, image, and file to either people.
    }

    public void OpenFile() {
        //create a way to make open file and put it into mediaview or imageview, depending if its a image, video, or audio
    }

    public void SendFile() {
        //send files
    }

    public void TexttheText(){
        //Be allowed to text

    }

    public void SendMessage() {
        //send message to show in listview

    }



}
