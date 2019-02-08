package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebChatClient extends Application {

    private WebChatController webChatController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WebChatDisplay.fxml"));
        Parent root = loader.load();
        webChatController = loader.getController();

        Thread.currentThread().setName("PictureView GUI Thread");
        // Display the scene
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 944, 567));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
