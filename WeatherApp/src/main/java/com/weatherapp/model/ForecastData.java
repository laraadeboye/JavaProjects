package com.weatherapp.model;

import java.util.List;
import java.util.ArrayList;

/**
 * ForecastData.java
 * Model class that holds a list of ForecastEntry objects representing
 * a multi-period (e.g. 5-day / 3-hour) weather forecast.
 */
public class ForecastData {

    /** Represents a single forecast time-slot returned by the API. */
    public static class ForecastEntry {
        private long timestamp;              // Unix timestamp for this slot
        private double temperatureCelsius;   // temp stored in Celsius
        private String condition;            // short condition string
        private String conditionDescription; // longer description
        private String iconCode;             // OpenWeatherMap icon code
        private double windSpeedMetersPerSec;
        private int humidity;
        private double pressure;

        // --- Constructors ---

        /** Default no-arg constructor. */
        public ForecastEntry() {}

        // --- Derived helpers ---

        /** @return temperature converted to Fahrenheit */
        public double getTemperatureFahrenheit() {
            return (temperatureCelsius * 9.0 / 5.0) + 32.0;
        }

        /** @return wind speed converted from m/s to km/h */
        public double getWindSpeedKmh() {
            return windSpeedMetersPerSec * 3.6;
        }

        // --- Getters / Setters ---

        public long   getTimestamp()                       { return timestamp; }
        public void   setTimestamp(long ts)                { this.timestamp = ts; }

        public double getTemperatureCelsius()              { return temperatureCelsius; }
        public void   setTemperatureCelsius(double t)      { this.temperatureCelsius = t; }

        public String getCondition()                       { return condition; }
        public void   setCondition(String c)               { this.condition = c; }

        public String getConditionDescription()            { return conditionDescription; }
        public void   setConditionDescription(String d)    { this.conditionDescription = d; }

        public String getIconCode()                        { return iconCode; }
        public void   setIconCode(String ic)               { this.iconCode = ic; }

        public double getWindSpeedMetersPerSec()           { return windSpeedMetersPerSec; }
        public void   setWindSpeedMetersPerSec(double w)   { this.windSpeedMetersPerSec = w; }

        public int    getHumidity()                        { return humidity; }
        public void   setHumidity(int h)                   { this.humidity = h; }

        public double getPressure()                        { return pressure; }
        public void   setPressure(double p)                { this.pressure = p; }
    }

    // -----------------------------------------------------------------------
    // ForecastData fields
    // -----------------------------------------------------------------------

    private String cityName;
    private String countryCode;
    private List<ForecastEntry> entries;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public ForecastData() {
        this.entries = new ArrayList<>();
    }

    // -----------------------------------------------------------------------
    // Getters / Setters
    // -----------------------------------------------------------------------

    public String getCityName()                          { return cityName; }
    public void   setCityName(String cityName)           { this.cityName = cityName; }

    public String getCountryCode()                       { return countryCode; }
    public void   setCountryCode(String countryCode)     { this.countryCode = countryCode; }

    public List<ForecastEntry> getEntries()              { return entries; }
    public void setEntries(List<ForecastEntry> entries)  { this.entries = entries; }

    /** Convenience method to add a single entry. */
    public void addEntry(ForecastEntry entry)            { this.entries.add(entry); }
}
