package uk.ac.cam.cl.interactiondesign.group8.api;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.cam.cl.interactiondesign.group8.ui.EWeather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OpenWeatherMapAPI implements WeatherAPI {
    private static final String baseURL = "https://api.openweathermap.org/data/2.5/";
    private static final String apiKey = "7e1c24937d661ef7edcf15d8059110b4";

    private long lastCurrentWeatherTime = 0L;
    private WeatherData lastCurrentWeather;

    private long lastFutureWeatherTime = 0L;
    private JsonNode lastFutureWeatherJSON;

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

    private WeatherData getCurrentWeatherHelper() throws UnableToGetWeatherException {
        try {
            JsonNode currentWeather = getCurrentWeather(locationProvider.getCurrentLocation());

            double kelvinTemp = currentWeather.getObject().getJSONObject("main").getDouble("temp");
            double windSpeed = currentWeather.getObject().getJSONObject("wind").getDouble("speed");

            JSONArray weatherDataArray = currentWeather.getObject().getJSONArray("weather");
            List<EWeather> weatherList = generateWeatherList(weatherDataArray);

            return new WeatherData(kelvinTemp, weatherList, windSpeed);
        } catch (UnirestException e) {
            throw new UnableToGetWeatherException(e);
        }
    }

    @Override
    public WeatherData getCurrentWeather() throws UnableToGetWeatherException {
        if (System.currentTimeMillis() - lastCurrentWeatherTime < 1000L) {
            return lastCurrentWeather;
        } else {
            lastCurrentWeatherTime = System.currentTimeMillis();
            return lastCurrentWeather = getCurrentWeatherHelper();
        }
    }

    private JsonNode getWeatherForecast(String location) throws UnirestException {
        if (System.currentTimeMillis() - lastFutureWeatherTime < 1000L) {
            return lastFutureWeatherJSON;
        } else {
            lastFutureWeatherTime = System.currentTimeMillis();
            return lastFutureWeatherJSON = makeAuthenticatedRequest("forecast", Map.of("q", location));
        }
    }

    @Override
    public WeatherData getWeatherAtTime(Date date) throws UnableToGetWeatherException {
        try {
            JsonNode forecast = getWeatherForecast(locationProvider.getCurrentLocation());

            Date lastDateTime = null;
            double lastTemperature = 0;
            double lastWindSpeed = 0;
            JSONArray lastWeather = null;

            JSONArray array = forecast.getObject().getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int datetime = obj.getInt("dt");

                Date thisDateTime = new Date(datetime * 1000L);
                double thisTemperature = obj.getJSONObject("main").getDouble("temp");
                double thisWindSpeed = obj.getJSONObject("wind").getDouble("speed");
                JSONArray thisWeather = obj.getJSONArray("weather");

                if (date.before(thisDateTime)) {
                    // The date given is before the time we have
                    if (lastDateTime == null) {
                        return getCurrentWeather();
                    } else {
                        double kelvinTemp = interpolate(
                                lastDateTime.toInstant().getEpochSecond(),
                                lastTemperature,
                                thisDateTime.toInstant().getEpochSecond(),
                                thisTemperature,
                                date.toInstant().getEpochSecond());

                        double windSpeed = interpolate(
                                lastDateTime.toInstant().getEpochSecond(),
                                lastWindSpeed,
                                thisDateTime.toInstant().getEpochSecond(),
                                thisWindSpeed,
                                date.toInstant().getEpochSecond());

                        List<EWeather> weatherList;
                        if (
                                thisDateTime.toInstant().getEpochSecond() - date.toInstant().getEpochSecond() <
                                        date.toInstant().getEpochSecond() - lastDateTime.toInstant().getEpochSecond()
                                        || lastWeather == null

                        ) {
                            // Smaller means closer to the future
                            weatherList = generateWeatherList(thisWeather);
                        } else {
                            // Larger means closer to the past
                            weatherList = generateWeatherList(lastWeather);
                        }

                        return new WeatherData(kelvinTemp, weatherList, windSpeed);
                    }
                }

                lastDateTime = thisDateTime;
                lastTemperature = thisTemperature;
                lastWindSpeed = thisWindSpeed;
                lastWeather = thisWeather;
            }

            throw new UnableToGetWeatherException("OpenWeatherMap does not include this date in their forecast!");
        } catch (UnirestException e) {
            throw new UnableToGetWeatherException(e);
        }
    }

    private List<EWeather> generateWeatherList(JSONArray weatherDataArray) {
        List<EWeather> weatherList = new ArrayList<>();

        for (int i = 0; i < weatherDataArray.length(); i++) {
            JSONObject weatherData = weatherDataArray.getJSONObject(i);

            int id = (int) weatherData.get("id");
            if (id < 300) {
                weatherList.add(EWeather.THUNDERSTORM);
            } else if (id < 400) {
                weatherList.add(EWeather.DRIZZLE);
            } else if (id < 600) {
                weatherList.add(EWeather.RAIN);
            } else if (id < 700) {
                weatherList.add(EWeather.SNOW);
            } else if (id == 741) {
                weatherList.add(EWeather.FOG);
            } else if (id == 804) {
                weatherList.add(EWeather.OVERCAST);
            } else {
                System.out.println("ID '" + id + "' should be clear.");
                weatherList.add(EWeather.CLEAR);
            }
        }

        return weatherList;
    }

    public void setLocationProvider(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }
}
