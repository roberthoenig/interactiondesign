package uk.ac.cam.cl.interactiondesign.group8.api;

import uk.ac.cam.cl.interactiondesign.group8.ui.ETemperature;
import uk.ac.cam.cl.interactiondesign.group8.ui.EWeather;

import java.util.Date;
import java.util.List;

public interface WeatherAPI {
    WeatherData getCurrentWeather() throws UnableToGetWeatherException;

    WeatherData getWeatherAtTime(Date date) throws UnableToGetWeatherException;

    default double interpolate(double x1, double y1, double x2, double y2, double x3) {
        return (x3 - x1) * (y2 - y1) / (x2 - x1) + y1;
    }

    class UnableToGetWeatherException extends Exception {
        UnableToGetWeatherException(Throwable cause) {
            super(cause);
        }

        UnableToGetWeatherException(String message) {
            super(message);
        }
    }

    class WeatherData {
        private final double currentTemperature;        // current temperature in kelvin
        private final EWeather[] currentWeather;        // current weather as an array of conditions
        private final double windSpeed;                 // current wind speed in m/s
        private final ETemperature currentWarmth;       // a current warmth enum

        WeatherData(double currentTemperature, List<EWeather> currentWeather, double windSpeed, ETemperature currentWarmth) {
            this.currentTemperature = currentTemperature;
            this.currentWeather = currentWeather.toArray(new EWeather[0]);
            this.windSpeed = windSpeed;
            this.currentWarmth = currentWarmth;
        }

        public double getCurrentTemperature() {
            return currentTemperature;
        }

        public EWeather[] getCurrentWeather() {
            return currentWeather.clone();
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public ETemperature getCurrentTemperatureState() {
            return currentWarmth;
        }
    }
}
