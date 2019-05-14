package uk.ac.cam.cl.interactiondesign.group8.api;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
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

            return (int) Math.floor(kelvinToCelsius(kelvinTemp));
        } catch (UnirestException e) {
            throw new UnableToGetWeatherException(e);
        }
    }

    private JsonNode getWeatherForecast(String location) throws UnirestException {
        return makeAuthenticatedRequest("forecast", Map.of("q", location));
    }

    @Override
    public int getTemperatureAtTime(Date date) throws UnableToGetWeatherException {
        try {
            JsonNode forecast = getWeatherForecast(locationProvider.getCurrentLocation());

            Date lastDateTime = null;
            double lastTemperature = 0;

            JSONArray array = forecast.getObject().getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int datetime = obj.getInt("dt");

                Date thisDateTime = new Date(datetime * 1000L);
                double thisTemperature = obj.getJSONObject("main").getDouble("temp");

                if (date.before(thisDateTime)) {
                    // The date given is before the time we have
                    if (lastDateTime == null) {
                        return getCurrentTemperature();
                    } else {
                        return (int) Math.round(interpolate((double) lastDateTime.getTime(),
                                lastTemperature,
                                (double) thisDateTime.getTime(),
                                thisTemperature,
                                (double) date.getTime()
                                ));
                    }
                    // // Interpolate the two datapoints
                    // else {
                    //     double kelvinTemp = interpolate(
                    //         lastDateTime.toInstant().getEpochSecond(),
                    //         lastTemperature,
                    //         thisDateTime.toInstant().getEpochSecond(),
                    //         thisTemperature,
                    //         date.toInstant().getEpochSecond());
                    //     return (int) Math.floor(kelvinToCelsius(kelvinTemp));
                    // }
                }

                lastDateTime = thisDateTime;
                lastTemperature = thisTemperature;
            }

            throw new UnableToGetWeatherException("Got section does not include this date!");
        } catch (UnirestException e) {
            throw new UnableToGetWeatherException(e);
        }
    }
}
