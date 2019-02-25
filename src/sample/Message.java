package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// Serializable means that objects of this class can be read/written over ObjectStreams
public class Message implements Serializable {
    // Message includes both sender ID and Image being sent
    private String sender;
    // Image is transient means that we have to provide our own code to read/write object
    private String data1;
    private transient Image data2;
    private transient MediaPlayer data3;

    // private transient Media data3;

    Message(String who, String text, Image image, MediaPlayer mediaPlayer) {
        sender = who;
        data1 = text;
        data2 = image;
        data3 = mediaPlayer;
    }

    String sender() {
        return sender;
    }

    String getData1(){return data1;}

    Image getData2() {
        return data2;
    }

    MediaPlayer getData3(){return data3;}



    public String toString() {
        return "\"" + data2 + "\" from: " + sender;
    }

}

