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
    private  transient MediaPlayer data3;
    public// private transient Media data3;

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
        return "\"" + data1 + "\" image:" + data2 + " video: " + data3 + " from: " + sender;
    }

    private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        // this reads sender String with default code
        inStream.defaultReadObject();
        // this reads data Image using this custom code
        data2 = SwingFXUtils.toFXImage(ImageIO.read(inStream), null);
    }

    private void writeObject(ObjectOutputStream outStream) throws IOException {
        // this writes sender String with default code
        outStream.defaultWriteObject();
        // this writes data Image using this custom code
        ImageIO.write(SwingFXUtils.fromFXImage(data2, null), "png" +  "tiff" + "jpeg" + "gif" + "bmp", outStream);
    }

}

