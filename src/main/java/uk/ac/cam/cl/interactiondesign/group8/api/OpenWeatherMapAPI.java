package uk.ac.cam.cl.interactiondesign.group8.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

public class OpenWeatherMapAPI implements WeatherAPI {
    private static final String baseURL = "https://api.openweathermap.org/data/2.5/";
    private static final String apiKey = "950f7dfa2f93b16e52fd6f2d01562ef9";

    private LocationProvider locationProvider;

    public OpenWeatherMapAPI(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    private JsonNode makeAuthenticatedRequest(String endpoint, Map<String, Object> params) throws UnirestException {
        return Unirest.get(baseURL + endpoint)
                .queryString("APPID", apiKey)
                .queryString(params)
                .asJson()
                .getBody();
    }

    private JsonNode getCurrentWeather(String location) throws UnirestException {
        return makeAuthenticatedRequest("weather", Map.of("q", location));
    }

    @Override
    public int getCurrentTemperature() throws UnableToGetWeatherException {
        try {
            JsonNode currentWeather = getCurrentWeather(locationProvider.getCurrentLocation());

            double kelvinTemp = currentWeather.getObject().getJSONObject("main").getDouble("temp");

            previousTemperatureTimestamp = System.currentTimeMillis();
            return previousTemperature = (int) Math.floor(kelvinToCelsius(kelvinTemp));
        } catch (UnirestException e) {
            throw new UnableToGetWeatherException(e);
        }
    }
}
