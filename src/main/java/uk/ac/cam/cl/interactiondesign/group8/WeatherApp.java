package uk.ac.cam.cl.interactiondesign.group8;

import uk.ac.cam.cl.interactiondesign.group8.api.*;
import uk.ac.cam.cl.interactiondesign.group8.ui.*;

import javax.swing.*;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherApp extends JFrame {

	// UI
	private DateSlider dateSlider;
	private Scene scene;

	// API
	private LocationProvider locationProvider;
	private WeatherAPI weatherAPI;

	private Timer timer;

	public void setTime(Date time) {
		try {
			// Get the weather from the API and update the UI
			scene.getTemperature().setTemperature(weatherAPI.getTemperatureAtTime(time));
		}
		catch (WeatherAPI.UnableToGetWeatherException e) {}
	}

	public WeatherApp() {
		// Create UI elements
		dateSlider = new DateSlider(this);
		scene = new Scene();

		// Initialise the API
		locationProvider = new DummyLocationProvider();
		weatherAPI = new OpenWeatherMapAPI(locationProvider);

		// Begin timer
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				dateSlider.updateTime();
			}
		}, 0, 180000);
	}
}