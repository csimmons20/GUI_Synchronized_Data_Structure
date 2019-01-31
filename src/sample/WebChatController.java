package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import java.util.ArrayList;

public class WebChatController {

    public ListView TheChat;
    public ImageView UserOneImage;
    public ImageView UserTwoImage;
    public TextField UserOneText;
    public TextField UserTwoText;
    public Button UserOneSend;
    public Button UserTwoSend;
    public Button UserOneFile;
    public Button UserTwoFile;
    public MediaView UserOneMedia;
    public MediaView UserTwoMedia;

    private SynchronizedQueue TheQueue;

    public void initialize(){
        TheQueue = new SynchronizedQueue();


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
