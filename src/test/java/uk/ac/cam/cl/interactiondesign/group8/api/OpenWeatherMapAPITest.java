package uk.ac.cam.cl.interactiondesign.group8.api;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class OpenWeatherMapAPITest {
    private void testCurrentWeather(String location) {
        LocationProvider lp = new DummyLocationProvider(location);
        WeatherAPI api = new OpenWeatherMapAPI(lp);

        try {
            System.out.println("Current temperature in " + location + ":   " + api.getCurrentTemperature());
        } catch (WeatherAPI.UnableToGetWeatherException e) {
            fail("Failed to get weather!");
        }
    }

    private void testFutureWeather(String location) {
        LocationProvider lp = new DummyLocationProvider(location);
        WeatherAPI api = new OpenWeatherMapAPI(lp);

        Calendar cal = Calendar.getInstance();

        // Attempt to find the weather now
        Date now = new Date();
        cal.setTime(now);
        try {
            System.out.println("The temperature now in " + location + " is:   " + api.getTemperatureAtTime(cal.getTime()));
        } catch (WeatherAPI.UnableToGetWeatherException e) {
            fail("This should be able to return the current weather.");
        }

        // Attempt to find the weather in five seconds
        now = new Date();
        cal.setTime(now);
        cal.add(Calendar.SECOND, 5);
        try {
            System.out.println("The temperature in five seconds in " + location + " is:   " + api.getTemperatureAtTime(cal.getTime()));
        } catch (WeatherAPI.UnableToGetWeatherException e) {
            fail("This should be able to return the future weather.");
        }

        // Attempt to find the area in an hour
        now = new Date();
        cal.setTime(now);
        cal.add(Calendar.HOUR, 1);
        try {
            System.out.println("The temperature in one hour in " + location + " is:   " + api.getTemperatureAtTime(cal.getTime()));
        } catch (WeatherAPI.UnableToGetWeatherException e) {
            fail("This should be able to return the future weather.");
        }

        // Attempt to find the area in two days
        now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DATE, 2);
        try {
            System.out.println("The temperature in two days in " + location + " is:   " + api.getTemperatureAtTime(cal.getTime()));
        } catch (WeatherAPI.UnableToGetWeatherException e) {
            fail("This should be able to return the future weather.");
        }

        // Attempt to find the area in 7 days (this should fail)
        now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DATE, 7);

        boolean caught = false;
        try {
            System.out.println("The temperature in one hour in " + location + " is:   " + api.getTemperatureAtTime(cal.getTime()));
            fail("This should not be able to return weather this far in advance.");
        } catch (WeatherAPI.UnableToGetWeatherException ignored) {}

        // Attempt to find the weather two minutes ago (to check if latency can break it)
        now = new Date();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, -2);

        try {
            System.out.println("The temperature two minutes ago in " + location + " is:   " + api.getTemperatureAtTime(cal.getTime()));
        } catch (WeatherAPI.UnableToGetWeatherException e) {
            fail("Fails to get weather of times in the past!");
        }
    }

    @Test
    public void testCurrentWeatherCambridge() {
        testCurrentWeather("Cambridge,UK");
    }

    @Test
    public void testCurrentWeatherEssex() {
        testCurrentWeather("Basildon,UK");
    }

    @Test
    public void testCurrentWeatherBristol() {
        testCurrentWeather("Bristol,UK");
    }

    @Test
    public void futureWeatherCambridge() {
        testFutureWeather("Cambridge,UK");
    }

    @Test
    public void futureWeatherEssex() {
        testFutureWeather("Basildon,UK");
    }

    @Test
    public void futureWeatherBristol() {
        testFutureWeather("Bristol,UK");
    }
}