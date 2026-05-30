package com.weatherapp.api;

import com.weatherapp.model.ForecastData;
import com.weatherapp.model.WeatherData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * WeatherApiService.java
 * Handles all communication with the OpenWeatherMap REST API.
 *
 * Endpoints used:
 *   - Current weather:  api.openweathermap.org/data/2.5/weather
 *   - 5-day forecast:   api.openweathermap.org/data/2.5/forecast
 *
 * The class is intentionally stateless: every public method is independent
 * and can be called from any thread (the GUI calls them from a SwingWorker).
 */
public class WeatherApiService {

    // -----------------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------------

    /** Base URL for the OpenWeatherMap API v2.5. */
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";

    /** Connection and read timeout in milliseconds. */
    private static final int TIMEOUT_MS = 10_000;

    // -----------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------

    /** API key obtained from https://openweathermap.org/api */
    private final String apiKey;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Creates a new WeatherApiService with the given API key.
     *
     * @param apiKey valid OpenWeatherMap API key
     */
    public WeatherApiService(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key must not be null or blank.");
        }
        this.apiKey = apiKey;
    }

    // -----------------------------------------------------------------------
    // Public API methods
    // -----------------------------------------------------------------------

    /**
     * Fetches current weather for a city name.
     *
     * @param cityName  city to look up (e.g. "London" or "Lagos,NG")
     * @return populated WeatherData object
     * @throws WeatherApiException if the request fails or the city is not found
     */
    public WeatherData getCurrentWeatherByCity(String cityName) throws WeatherApiException {
        String encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8);
        String urlStr = BASE_URL + "/weather?q=" + encodedCity
                + "&appid=" + apiKey + "&units=metric";
        String rawJson = fetchJson(urlStr);
        return parseWeatherData(new JSONObject(rawJson));
    }

    /**
     * Fetches current weather for geographic coordinates.
     *
     * @param latitude  decimal latitude
     * @param longitude decimal longitude
     * @return populated WeatherData object
     * @throws WeatherApiException if the request fails
     */
    public WeatherData getCurrentWeatherByCoords(double latitude, double longitude)
            throws WeatherApiException {
        String urlStr = BASE_URL + "/weather?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + apiKey + "&units=metric";
        String rawJson = fetchJson(urlStr);
        return parseWeatherData(new JSONObject(rawJson));
    }

    /**
     * Fetches a 5-day / 3-hour forecast for a city name.
     *
     * @param cityName city to look up
     * @return populated ForecastData object
     * @throws WeatherApiException if the request fails or the city is not found
     */
    public ForecastData getForecastByCity(String cityName) throws WeatherApiException {
        String encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8);
        String urlStr = BASE_URL + "/forecast?q=" + encodedCity
                + "&appid=" + apiKey + "&units=metric&cnt=40";
        String rawJson = fetchJson(urlStr);
        return parseForecastData(new JSONObject(rawJson));
    }

    /**
     * Fetches a 5-day / 3-hour forecast for geographic coordinates.
     *
     * @param latitude  decimal latitude
     * @param longitude decimal longitude
     * @return populated ForecastData object
     * @throws WeatherApiException if the request fails
     */
    public ForecastData getForecastByCoords(double latitude, double longitude)
            throws WeatherApiException {
        String urlStr = BASE_URL + "/forecast?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + apiKey + "&units=metric&cnt=40";
        String rawJson = fetchJson(urlStr);
        return parseForecastData(new JSONObject(rawJson));
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Makes an HTTP GET request to the given URL and returns the response body
     * as a String.  Throws WeatherApiException for any non-200 status code.
     *
     * @param urlStr fully-formed URL with query parameters
     * @return raw JSON response body
     * @throws WeatherApiException on network error or non-200 HTTP status
     */
    private String fetchJson(String urlStr) throws WeatherApiException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            connection.setRequestProperty("Accept", "application/json");

            int statusCode = connection.getResponseCode();

            // Read the appropriate stream (error stream for non-200 responses)
            BufferedReader reader;
            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            String responseBody = sb.toString();

            // Handle API-level errors returned in the JSON body
            if (statusCode != HttpURLConnection.HTTP_OK) {
                String message = extractApiErrorMessage(responseBody, statusCode);
                throw new WeatherApiException(message, statusCode);
            }

            return responseBody;

        } catch (WeatherApiException e) {
            throw e; // re-throw our own exception type
        } catch (Exception e) {
            throw new WeatherApiException(
                    "Network error: " + e.getMessage(), -1);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Attempts to extract a human-readable error message from the API's JSON
     * error response body.  Falls back to a generic message if parsing fails.
     *
     * @param body       raw JSON error body
     * @param statusCode HTTP status code
     * @return user-friendly error message
     */
    private String extractApiErrorMessage(String body, int statusCode) {
        try {
            JSONObject json = new JSONObject(body);
            String msg = json.optString("message", "Unknown error");
            return "API Error " + statusCode + ": " + msg;
        } catch (Exception e) {
            return "HTTP Error " + statusCode;
        }
    }

    /**
     * Parses a current-weather JSON object into a WeatherData instance.
     *
     * @param json parsed JSON from the /weather endpoint
     * @return populated WeatherData
     */
    private WeatherData parseWeatherData(JSONObject json) {
        WeatherData data = new WeatherData();

        // --- Location ---
        data.setCityName(json.optString("name", "Unknown"));
        JSONObject sys = json.optJSONObject("sys");
        if (sys != null) {
            data.setCountryCode(sys.optString("country", ""));
            data.setSunrise(sys.optLong("sunrise", 0));
            data.setSunset(sys.optLong("sunset", 0));
        }
        JSONObject coord = json.optJSONObject("coord");
        if (coord != null) {
            data.setLatitude(coord.optDouble("lat", 0));
            data.setLongitude(coord.optDouble("lon", 0));
        }

        // --- Temperature & atmosphere ---
        JSONObject main = json.optJSONObject("main");
        if (main != null) {
            data.setTemperatureCelsius(main.optDouble("temp", 0));
            data.setFeelsLikeCelsius(main.optDouble("feels_like", 0));
            data.setHumidity(main.optInt("humidity", 0));
            data.setPressure(main.optDouble("pressure", 0));
        }

        // --- Wind ---
        JSONObject wind = json.optJSONObject("wind");
        if (wind != null) {
            data.setWindSpeedMetersPerSec(wind.optDouble("speed", 0));
            data.setWindDegrees(wind.optInt("deg", 0));
        }

        // --- Weather condition ---
        JSONArray weatherArray = json.optJSONArray("weather");
        if (weatherArray != null && !weatherArray.isEmpty()) {
            JSONObject w = weatherArray.getJSONObject(0);
            data.setCondition(w.optString("main", "Unknown"));
            data.setConditionDescription(w.optString("description", ""));
            data.setIconCode(w.optString("icon", "01d"));
        }

        // --- Clouds & visibility ---
        JSONObject clouds = json.optJSONObject("clouds");
        if (clouds != null) {
            data.setCloudiness(clouds.optInt("all", 0));
        }
        data.setVisibility(json.optInt("visibility", 0));
        data.setTimestamp(json.optLong("dt", System.currentTimeMillis() / 1000));

        return data;
    }

    /**
     * Parses a forecast JSON object (from the /forecast endpoint) into a
     * ForecastData instance containing multiple ForecastEntry objects.
     *
     * @param json parsed JSON from the /forecast endpoint
     * @return populated ForecastData
     */
    private ForecastData parseForecastData(JSONObject json) {
        ForecastData forecastData = new ForecastData();

        // City information
        JSONObject city = json.optJSONObject("city");
        if (city != null) {
            forecastData.setCityName(city.optString("name", "Unknown"));
            JSONObject country = city.optJSONObject("country");
            // "country" is a string value, not an object in this endpoint
            forecastData.setCountryCode(city.optString("country", ""));
        }

        // Parse each 3-hour forecast slot
        JSONArray list = json.optJSONArray("list");
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                JSONObject slot = list.getJSONObject(i);
                ForecastData.ForecastEntry entry = new ForecastData.ForecastEntry();

                entry.setTimestamp(slot.optLong("dt", 0));

                JSONObject main = slot.optJSONObject("main");
                if (main != null) {
                    entry.setTemperatureCelsius(main.optDouble("temp", 0));
                    entry.setHumidity(main.optInt("humidity", 0));
                    entry.setPressure(main.optDouble("pressure", 0));
                }

                JSONObject wind = slot.optJSONObject("wind");
                if (wind != null) {
                    entry.setWindSpeedMetersPerSec(wind.optDouble("speed", 0));
                }

                JSONArray weatherArray = slot.optJSONArray("weather");
                if (weatherArray != null && !weatherArray.isEmpty()) {
                    JSONObject w = weatherArray.getJSONObject(0);
                    entry.setCondition(w.optString("main", "Unknown"));
                    entry.setConditionDescription(w.optString("description", ""));
                    entry.setIconCode(w.optString("icon", "01d"));
                }

                forecastData.addEntry(entry);
            }
        }

        return forecastData;
    }
}
