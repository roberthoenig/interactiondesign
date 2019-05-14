package uk.ac.cam.cl.interactiondesign.group8;

import uk.ac.cam.cl.interactiondesign.group8.api.*;
import uk.ac.cam.cl.interactiondesign.group8.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherApp extends JFrame {

	// UI
	private DateBar dateBar;
	private Scene scene;

	// API
	private LocationProvider locationProvider;
	private WeatherAPI weatherAPI;

	private Timer timer;

	public void setTime(Date time) {
		try {
			scene.getTimeCarousel().setTime(time);
			// Get the weather from the API and update the UI
			scene.getTemperature().setTemperature(weatherAPI.getTemperatureAtTime(time));
		}
		catch (WeatherAPI.UnableToGetWeatherException e) {}
	}

	public WeatherApp() {
		super("WeatherApp");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(400, 300));

		JPanel contentPane = new JPanel(new BorderLayout(0,0));
		JLayeredPane jlp = new JLayeredPane();

		// Create UI elements
		dateBar = new DateBar(this);
		jlp.add(dateBar, JLayeredPane.PALETTE_LAYER);
		scene = new Scene();
		jlp.add(scene, JLayeredPane.DEFAULT_LAYER);
		jlp.addComponentListener(new ComponentAdapter() {
	        public void componentResized(ComponentEvent e) {
	            dateBar.setBounds(0,0,e.getComponent().getWidth(),e.getComponent().getHeight());
	            scene.setBounds(0,0,e.getComponent().getWidth(),e.getComponent().getHeight());
	        }
		});
		contentPane.add(jlp, BorderLayout.CENTER);
		setContentPane(contentPane);

		// Initialise the API
		locationProvider = new DummyLocationProvider();
		weatherAPI = new OpenWeatherMapAPI(locationProvider);

		// Begin timer
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				dateBar.updateTime();
			}
		}, 0, 180000);
	}
}