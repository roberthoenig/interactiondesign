package uk.ac.cam.cl.interactiondesign.group8;

import uk.ac.cam.cl.interactiondesign.group8.api.*;
import uk.ac.cam.cl.interactiondesign.group8.ui.*;
import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class WeatherApp extends JFrame {

    // UI
    private DateBar dateBar;
    private Scene scene;

    // API
    private LocationProvider locationProvider;
    private WeatherAPI weatherAPI;

    private Timer timer;

    private Date currentTime;

    // State
    private EWeather[] weatherState;
    private ETemperature temperatureState;

    public void setTime(Date time) {
        currentTime = time;

        try {
            scene.getTimeCarousel().setTime(time);
            // Get the weather from the API and update the UI
            WeatherAPI.WeatherData apiData = weatherAPI.getWeatherAtTime(time);

            scene.getTemperature().setTemperature((int) apiData.getCurrentTemperature());
            scene.setWeather(apiData.getCurrentWeather());

            temperatureState = apiData.getCurrentTemperatureState();
            weatherState = apiData.getCurrentWeather();
        } catch (WeatherAPI.UnableToGetWeatherException ignored) {}
    }

    public void generateMessage() {
        if (weatherState == null || temperatureState == null) {
            System.err.println("Unexpected lack of state when preparing message!");
            return;
        }
        scene.getCharacter().randomChat(weatherState, temperatureState, ETime.getTime(currentTime));
    }

    public WeatherApp() {
        super("Clever Weather");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(400, 300));

        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        JLayeredPane jlp = new JLayeredPane();

        // Create UI elements
        dateBar = new DateBar(this);
        jlp.add(dateBar, JLayeredPane.PALETTE_LAYER);
        scene = new Scene();
        jlp.add(scene, JLayeredPane.DEFAULT_LAYER);
        jlp.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                dateBar.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                scene.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
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
        }, 0, 1000);

        Runnable callGenerateMessage = this::generateMessage;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                callGenerateMessage.run();
            }
        }, 0, 15000);
    }
}
