package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaView;

import javax.swing.text.html.ImageView;
import javax.swing.text.html.ListView;

public class WebChatController {

    public ListView UserOneChat;
    public ListView UserTwoChat;
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

    }

    public void OpenFile() {
        //create a way to make open file and put it into mediaview or imageview, depending if its a image, video, or audio
    }

    public void SendFile() {
        //send files
    }

    public void TexttheText(){
        //be alloed to text
    }

    public void SendMessage() {
        //send message to show in listview

    }



}
