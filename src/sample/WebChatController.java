package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    private Stage stage;

    public void initialize(){
        TheQueue = new SynchronizedQueue();

        // Create and start the GUI updater thread
        UpdateGUI updater = new UpdateGUI(TheQueue, UserTwoText, UserTwoImage, TheChat);
        Thread updaterThread = new Thread(updater);
        updaterThread.start();

        //GUI Updates text, image, and file to either people.
    }

    public void setStage(Stage theStage) {
        stage = theStage;
    }

    public void OpenFile() {
        //create a way to make open file and put it into mediaview or imageview, depending if its a image, video, or audio
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        // If user chose a file via FileChooser
        if (file != null) {
            Image newImage = new Image(file.toURI().toString());
            UserOneImage.setImage(newImage);
        }
    }

    public void Send() {
        Image userOneImg = UserOneImage.getImage();

        if (userOneImg != null) {
            while (!TheQueue.put(userOneImg)) {
                Thread.currentThread().yield();
            }
        }
        System.out.println("SendMessage: PUT " + userOneImg);


        String userOneText = UserOneText.getText();
        UserOneText.setText("");

        if (userOneText != null) {
            while (!TheQueue.put(userOneText)) {
                Thread.currentThread().yield();
            }
        }

        System.out.println("SendMessage: PUT " + userOneText);
    }

    public void TexttheText(){
        //Be allowed to text

    }

    public void SendMessage() {
        //send message to show in listview
        System.out.println("SendMessage: " + UserOneText.getText());

        // Update the list view with the text from the bottom text field
        TheChat.getItems().add(new Label(UserOneText.getText()));
        // Clear the bottom text field because it has been used.
        UserOneText.setText("");
    }
}
