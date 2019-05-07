package uk.ac.cam.cl.interactiondesign.group8.api;

public interface WeatherAPI {
    int getCurrentTemperature();

    default double kelvinToCelsius(double input) {
        return input - 273.15d;
    }
}
