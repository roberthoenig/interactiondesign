package uk.ac.cam.cl.interactiondesign.group8.api;

public interface WeatherAPI {
    int getCurrentTemperature() throws UnableToGetWeatherException;

    default double kelvinToCelsius(double input) {
        return input - 273.15d;
    }

    class UnableToGetWeatherException extends Exception {
        public UnableToGetWeatherException(Throwable cause) {
            super(cause);
        }
    }
}
