package uk.ac.cam.cl.interactiondesign.group8;

import uk.ac.cam.cl.interactiondesign.group8.api.DummyLocationProvider;
import uk.ac.cam.cl.interactiondesign.group8.api.OpenWeatherMapAPI;
import uk.ac.cam.cl.interactiondesign.group8.api.WeatherAPI;

import uk.ac.cam.cl.interactiondesign.group8.ui.*;

public class Main {

	private Scene scene;

	private void createScene() {
		scene = new Scene();
	}

    public static void main(String[] args) {
        WeatherAPI api = new OpenWeatherMapAPI(new DummyLocationProvider());

        System.out.println(api.getCurrentTemperature());
    }
}
