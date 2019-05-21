package uk.ac.cam.cl.interactiondesign.group8.ui;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.cam.cl.interactiondesign.group8.utils.ResourceLoader;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ChatSelector {
    private final String jsonFile;
    private final LanguageCode languageCode;

    private Random random;

    private JSONObject jsonObject;

    public ChatSelector(String jsonFile, LanguageCode languageCode) {
        this.jsonFile = jsonFile;
        this.languageCode = languageCode;

        try {
            jsonObject = ResourceLoader.loadJsonObject(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load JSON file '" + jsonFile + "'!", e);
        }

        random = new Random();
    }

    public Result getChatMessage(IChatInterface... options) {
        double totalProbability = Arrays.stream(options).mapToDouble(IChatInterface::getProbability).sum();

        double choice = random.nextDouble() * totalProbability;
        IChatInterface decision = null;
        for (IChatInterface option : options) {
            choice -= option.getProbability();
            if (choice <= 0) {
                decision = option;
            }
        }

        assert decision != null;    // it's not, I promise
        JSONArray phrases = jsonObject.getJSONObject(languageCode.toString())
                .getJSONObject(decision.getHeader())
                .getJSONArray(decision.toString());

        return new Result((String) phrases.get(random.nextInt(phrases.length())), null, null);
    }

    // The JSON files must have a fixed format

    enum LanguageCode {
        en_GB
    }

    class Result {
        private final String message;
        private final AudioInputStream audio;
        private final EClothing clothing;

        Result(String message, AudioInputStream audio, EClothing clothing) {
            this.message = message;
            this.audio = audio;
            this.clothing = clothing;
        }

        public String getMessage() {
            return message;
        }

        public AudioInputStream getAudio() {
            return audio;
        }

        public EClothing getClothing() {
            return clothing;
        }
    }
}
