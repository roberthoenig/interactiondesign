package uk.ac.cam.cl.interactiondesign.group8.utils;

import uk.ac.cam.cl.interactiondesign.group8.*;
import java.io.*;

public class ResourceLoader {
	// Load a file from the resources folder
    public static File loadResource(String filename) {
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
        } 
        catch (NullPointerException | IOException e) {
            System.err.println("Failed to load resource: '" + filename + "'");
        }

        return null;
    }
}