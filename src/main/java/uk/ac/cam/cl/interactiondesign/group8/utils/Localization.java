package uk.ac.cam.cl.interactiondesign.group8.utils;

import org.json.*;

import java.io.*;
import java.nio.file.*;

public class Localization {
    private static String language = "en_UK";
    private static JSONObject localizationData;

    public static void importLocalizationData(String filename) throws IOException {
        File dataFile = ResourceLoader.loadResource(filename);

        // Read file to single string
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        // Parse JSON string
        localizationData = new JSONObject(sb.toString());
    }

    // Set the language to be used
    public static void setLanguage(String lang) {
        language = lang;
    }

    public static String localize(String path) throws LocalizationException {
        String fullPath = language + "/" + path;
        String[] parts = fullPath.split("[\\\\\\/]");

        // Navigate within JSON
        Object cur = localizationData;
        boolean obj = true; // true - JSONObject, false - JSONArray

        try {
            for (int i = 0; i < parts.length - 1; ++i) {
                Object next;
                if (obj) {
                    next = ((JSONObject) cur).optJSONObject(parts[i]);
                    if (next == null) {
                        next = ((JSONObject) cur).optJSONArray(parts[i]);
                        obj = false;
                    }
                } else {
                    next = ((JSONArray) cur).optJSONArray(Integer.parseInt(parts[i]));
                    if (next == null) {
                        next = ((JSONArray) cur).optJSONObject(Integer.parseInt(parts[i]));
                        obj = true;
                    }
                }
                cur = next;
            }

            String result;
            if (obj)
                result = ((JSONObject) cur).getString(parts[parts.length - 1]);
            else
                result = ((JSONArray) cur).getString(Integer.parseInt(parts[parts.length - 1]));

            return result;
        } catch (NullPointerException e) {
            throw new LocalizationException("Unable to find path: '" + path + "'");
        }
    }

    public static class LocalizationException extends Exception {
        LocalizationException(Throwable cause) {
            super(cause);
        }

        public LocalizationException(String message) {
            super(message);
        }
    }
}