package uk.ac.cam.cl.interactiondesign.group8.utils;

import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;
import org.json.JSONTokener;
import uk.ac.cam.cl.interactiondesign.group8.*;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class ResourceLoader {
    // Load a file from the resources folder
    public static File loadResource(String filename) throws IOException {
        try {
            InputStream in = Main.class.getClassLoader().getResourceAsStream(filename);

            // Copy the file to a readable location
            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            for (int bytes = 0; (bytes = in.read(buffer)) != -1; out.write(buffer, 0, bytes)) ;

            return tempFile;
        } catch (NullPointerException | IOException e) {
            throw new IOException("Failed to load resource: '" + filename + "'");
        }
    }

    // As before but converts to a BufferedImage
    public static BufferedImage loadImage(String filename) throws IOException {
        File tempFile = loadResource(filename);

        try {
            return ImageIO.read(tempFile);
        } catch (IOException e) {
            throw new IOException("Failed to load image: '" + filename + "'");
        }
    }

    public static JSONObject loadJsonObject(String filename) throws IOException {
        File tempFile = loadResource(filename);

        try (FileReader fr = new FileReader(tempFile)) {
            return new org.json.JSONObject(new JSONTokener(fr));
        } catch (IOException e) {
            throw new IOException("Failed to load file contents: '" + filename + "'");
        }
    }
}