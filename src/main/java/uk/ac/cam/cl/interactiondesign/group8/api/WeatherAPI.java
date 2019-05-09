package uk.ac.cam.cl.interactiondesign.group8.api;

import java.util.Date;

public interface WeatherAPI {
    int getCurrentTemperature() throws UnableToGetWeatherException;
    int getTemperatureAtTime(Date date) throws UnableToGetWeatherException;

    default double kelvinToCelsius(double input) {
        return input - 273.15d;
    }

    default double interpolate(double x1, double y1, double x2, double y2, double x3) {
        return (x3 - x1) * (y2 - y1) / (x2 - x1) + y1;
    }

    class UnableToGetWeatherException extends Exception {
        UnableToGetWeatherException(Throwable cause) {
            super(cause);
        }

        public UnableToGetWeatherException(String message) {
            super(message);
        }
    }
}
