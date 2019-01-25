package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebChat extends Application {

    private WebChatController webChatController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PictureViewer.fxml"));
        Parent root = loader.load();
        webChatController = loader.getController();
        webChatController.setStage(primaryStage);

        Thread.currentThread().setName("PictureView GUI Thread");
        // Display the scene
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
