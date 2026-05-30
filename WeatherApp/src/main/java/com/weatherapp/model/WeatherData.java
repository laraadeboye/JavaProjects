package com.weatherapp.model;

/**
 * WeatherData.java
 * Model class that holds all weather information retrieved from the API.
 * Stores current conditions as well as metadata about the location.
 */
public class WeatherData {

    // Location details
    private String cityName;
    private String countryCode;
    private double latitude;
    private double longitude;

    // Current weather conditions
    private double temperatureCelsius;    // stored in Celsius; converted on display
    private double feelsLikeCelsius;
    private int humidity;                 // percentage (0-100)
    private double windSpeedMetersPerSec; // stored in m/s; converted on display
    private int windDegrees;             // meteorological wind direction
    private String condition;            // short description e.g. "Clear", "Rain"
    private String conditionDescription; // longer description e.g. "clear sky"
    private String iconCode;             // OpenWeatherMap icon code e.g. "01d"
    private int cloudiness;              // percentage (0-100)
    private long sunrise;                // Unix timestamp
    private long sunset;                 // Unix timestamp
    private long timestamp;              // data timestamp (Unix)

    // Atmospheric data
    private double pressure;             // hPa
    private int visibility;              // metres

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /** Default no-arg constructor. */
    public WeatherData() {}

    // -----------------------------------------------------------------------
    // Derived / convenience methods
    // -----------------------------------------------------------------------

    /**
     * Converts stored Celsius temperature to Fahrenheit.
     * @return temperature in Fahrenheit
     */
    public double getTemperatureFahrenheit() {
        return (temperatureCelsius * 9.0 / 5.0) + 32.0;
    }

    /**
     * Converts stored feels-like Celsius temperature to Fahrenheit.
     * @return feels-like temperature in Fahrenheit
     */
    public double getFeelsLikeFahrenheit() {
        return (feelsLikeCelsius * 9.0 / 5.0) + 32.0;
    }

    /**
     * Converts stored wind speed (m/s) to km/h.
     * @return wind speed in km/h
     */
    public double getWindSpeedKmh() {
        return windSpeedMetersPerSec * 3.6;
    }

    /**
     * Converts stored wind speed (m/s) to mph.
     * @return wind speed in mph
     */
    public double getWindSpeedMph() {
        return windSpeedMetersPerSec * 2.23694;
    }

    /**
     * Converts meteorological wind degrees to a compass direction string.
     * @return compass direction e.g. "N", "NE", "SW"
     */
    public String getWindDirection() {
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                               "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(windDegrees / 22.5) % 16;
        return directions[index];
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public String getCityName()                        { return cityName; }
    public void   setCityName(String cityName)         { this.cityName = cityName; }

    public String getCountryCode()                     { return countryCode; }
    public void   setCountryCode(String countryCode)   { this.countryCode = countryCode; }

    public double getLatitude()                        { return latitude; }
    public void   setLatitude(double latitude)         { this.latitude = latitude; }

    public double getLongitude()                       { return longitude; }
    public void   setLongitude(double longitude)       { this.longitude = longitude; }

    public double getTemperatureCelsius()              { return temperatureCelsius; }
    public void   setTemperatureCelsius(double t)      { this.temperatureCelsius = t; }

    public double getFeelsLikeCelsius()                { return feelsLikeCelsius; }
    public void   setFeelsLikeCelsius(double t)        { this.feelsLikeCelsius = t; }

    public int    getHumidity()                        { return humidity; }
    public void   setHumidity(int humidity)            { this.humidity = humidity; }

    public double getWindSpeedMetersPerSec()           { return windSpeedMetersPerSec; }
    public void   setWindSpeedMetersPerSec(double w)   { this.windSpeedMetersPerSec = w; }

    public int    getWindDegrees()                     { return windDegrees; }
    public void   setWindDegrees(int windDegrees)      { this.windDegrees = windDegrees; }

    public String getCondition()                       { return condition; }
    public void   setCondition(String condition)       { this.condition = condition; }

    public String getConditionDescription()            { return conditionDescription; }
    public void   setConditionDescription(String d)    { this.conditionDescription = d; }

    public String getIconCode()                        { return iconCode; }
    public void   setIconCode(String iconCode)         { this.iconCode = iconCode; }

    public int    getCloudiness()                      { return cloudiness; }
    public void   setCloudiness(int cloudiness)        { this.cloudiness = cloudiness; }

    public long   getSunrise()                         { return sunrise; }
    public void   setSunrise(long sunrise)             { this.sunrise = sunrise; }

    public long   getSunset()                          { return sunset; }
    public void   setSunset(long sunset)               { this.sunset = sunset; }

    public long   getTimestamp()                       { return timestamp; }
    public void   setTimestamp(long timestamp)         { this.timestamp = timestamp; }

    public double getPressure()                        { return pressure; }
    public void   setPressure(double pressure)         { this.pressure = pressure; }

    public int    getVisibility()                      { return visibility; }
    public void   setVisibility(int visibility)        { this.visibility = visibility; }

    @Override
    public String toString() {
        return String.format("WeatherData{city='%s', country='%s', temp=%.1f°C, condition='%s'}",
                cityName, countryCode, temperatureCelsius, condition);
    }
}
