package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URI;

// Serializable means that objects of this class can be read/written over ObjectStreams
public class Message implements Serializable {
    // Message includes both sender ID and Image being sent
    private String sender;
    // Image is transient means that we have to provide our own code to read/write object
    private String data1;
    private transient Image data2;
    private byte[] data3;
    private File mediaFile;

    Message(String who, String text, Image image, File file) {
        sender = who;
        data1 = text;
        data2 = image;
        mediaFile = file;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            long byteLength = file.length(); // byte count of the file-content
            data3 = new byte[(int) byteLength];
            fileInputStream.read(data3, 0, (int) byteLength);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    String sender() {
        return sender;
    }

    String getData1(){return data1;}

    Image getData2() {
        return data2;
    }

    byte[] getData3(){return data3;}

    File getMediaFile() {
        return mediaFile;
    }

    public String toString() {
        return "\"" + data1 + "\" image:" + data2 + " video: " + data3 + " from: " + sender;
    }

    private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        // this reads sender String with default code
        inStream.defaultReadObject();
        // this reads data Image using this custom code
        data2 = SwingFXUtils.toFXImage(ImageIO.read(inStream), null);

        DataInputStream dIn = new DataInputStream(inStream);
        int length = dIn.readInt();                    // read length of incoming file
        if (length>0) {
            data3 = new byte[length];
            dIn.readFully(data3, 0, data3.length); // read the file
        }
        // write the byte[] to temporary file
        mediaFile = File.createTempFile("TempMedia", "csal", null);
        FileOutputStream fos = new FileOutputStream(mediaFile);
        fos.write(data3);
        fos.close();
    }


    private void writeObject(ObjectOutputStream outStream) throws IOException {
        // this writes sender String with default code
        outStream.defaultWriteObject();
        // this writes data Image using this custom code
        ImageIO.write(SwingFXUtils.fromFXImage(data2, null), "png", outStream);
        // this writes File bytes using custom code
        DataOutputStream dOut = new DataOutputStream(outStream);
        dOut.writeInt(data3.length); // write length of the file
        dOut.write(data3);           // write the file bytes
        dOut.close();
    }

}

