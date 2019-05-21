package uk.ac.cam.cl.interactiondesign.group8.api;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.cam.cl.interactiondesign.group8.ui.ETemperature;
import uk.ac.cam.cl.interactiondesign.group8.ui.EWeather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OpenWeatherMapAPI implements WeatherAPI {
    private static final String baseURL = "https://api.openweathermap.org/data/2.5/";
    private static final String apiKey = "7e1c24937d661ef7edcf15d8059110b4";

    // These are there to cache the last weather.
    // This is such that the API is not hit with many requests
    // as the slider is used.
    private long lastCurrentWeatherTime = 0L;
    private WeatherData lastCurrentWeather;

    // Caching the future weather
    private long lastFutureWeatherTime = 0L;
    private JsonNode lastFutureWeatherJSON;

    // Store a reference to the LocationProvider for this
    private LocationProvider locationProvider;

    public OpenWeatherMapAPI(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    private JsonNode makeAuthenticatedRequest(String endpoint, Map<String, Object> params) throws UnirestException {
        // Make a get request to the API
        return Unirest.get(baseURL + endpoint)
                .queryString("APPID", apiKey)   // this adds the API key as a get parameter
                .queryString(params)                  // this adds the parameters of the request as get parameters
                .asJson()                             // converts the request to JSON
                .getBody();                           // returns the result of the request as JSON
    }

    private JsonNode getCurrentWeather(String location) throws UnirestException {
        // make an authenticated to the specific endpoint for the current weather in the location
        return makeAuthenticatedRequest("weather", Map.of("q", location));
    }

    private WeatherData getCurrentWeatherHelper() throws UnableToGetWeatherException {
        try {
            // This function uses the API to gather info and cache it for use elsewhere, after this one
            JsonNode currentWeather = getCurrentWeather(locationProvider.getCurrentLocation());

            // Gets the temperature and wind speed from the JSON structure
            double kelvinTemp = currentWeather.getObject().getJSONObject("main").getDouble("temp");
            double windSpeed = currentWeather.getObject().getJSONObject("wind").getDouble("speed");

            // Gets th weather data (the list of current weathers) from the JSON structure
            JSONArray weatherDataArray = currentWeather.getObject().getJSONArray("weather");
            List<EWeather> weatherList = generateWeatherList(weatherDataArray); // convert these to an enum list

            // Return this, packed into the immutable result object
            return new WeatherData(kelvinTemp, weatherList, windSpeed, ETemperature.getTempFromKelvin(kelvinTemp));
        } catch (UnirestException e) {
            throw new UnableToGetWeatherException(e);
        }
    }

    @Override
    public WeatherData getCurrentWeather() throws UnableToGetWeatherException {
        if (System.currentTimeMillis() - lastCurrentWeatherTime < 1000L) {
            // If it was recently cached, use that existing value
            return lastCurrentWeather;
        } else {
            // If it was last cached a while ago, download it again and reset the time
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
            // Get the current weather forecast, accepting caching
            JsonNode forecast = getWeatherForecast(locationProvider.getCurrentLocation());

            // Set basics for iterating through the loop while keeping the last value
            Date lastDateTime = null;
            double lastTemperature = 0;
            double lastWindSpeed = 0;
            JSONArray lastWeather = null;

            // Get the list of future weathers from the JSON object
            JSONArray array = forecast.getObject().getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                // Iterate through the array until you find a future weather
                JSONObject obj = array.getJSONObject(i);
                int datetime = obj.getInt("dt");

                Date thisDateTime = new Date(datetime * 1000L);
                double thisTemperature = obj.getJSONObject("main").getDouble("temp");
                double thisWindSpeed = obj.getJSONObject("wind").getDouble("speed");
                JSONArray thisWeather = obj.getJSONArray("weather");

                if (date.before(thisDateTime)) {
                    // The date given is before the time we have
                    if (lastDateTime == null) {
                        // Give the current weather, as this is likely some form of floating point error
                        return getCurrentWeather();
                    } else {
                        // Interpolate the kelvin temp with the last temperature
                        // This provides a slightly closer approximation than discrete
                        // As the weather is only provided in 3 hour intervals
                        // But these could be wider/slimmer based on the API implemented
                        double kelvinTemp = interpolate(
                                lastDateTime.toInstant().getEpochSecond(),
                                lastTemperature,
                                thisDateTime.toInstant().getEpochSecond(),
                                thisTemperature,
                                date.toInstant().getEpochSecond());

                        // And the wind speed
                        double windSpeed = interpolate(
                                lastDateTime.toInstant().getEpochSecond(),
                                lastWindSpeed,
                                thisDateTime.toInstant().getEpochSecond(),
                                thisWindSpeed,
                                date.toInstant().getEpochSecond());

                        // Gain the list of weathers
                        List<EWeather> weatherList;

                        // Choose the list of weathers based on which is closer to the time hoped for
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

                        // Return the immutable result object
                        return new WeatherData(kelvinTemp, weatherList, windSpeed, ETemperature.getTempFromKelvin(kelvinTemp));
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
        // Convert the API's many weather result numbers into our enums
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
//                System.out.println("ID '" + id + "' should be clear.");
                weatherList.add(EWeather.CLEAR);
            }
        }

        return weatherList;
    }

    public void setLocationProvider(LocationProvider locationProvider) {
        // Allows the location provider to be swapped out
        this.locationProvider = locationProvider;
    }
}
