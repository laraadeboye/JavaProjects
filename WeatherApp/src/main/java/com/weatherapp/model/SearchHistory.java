package com.weatherapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SearchHistory.java
 * Represents a single entry in the user's search history.
 * Stores the query string, the resolved city name, and the timestamp
 * at which the search was performed.
 */
public class SearchHistory {

    // Date-time formatter for display in the history panel
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  HH:mm:ss");

    // -----------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------

    /** The raw search term the user typed (city name or "lat,lon"). */
    private String query;

    /** The resolved city name returned by the API (may differ from query). */
    private String resolvedCity;

    /** Country code returned by the API (e.g. "US", "GB"). */
    private String countryCode;

    /** Temperature recorded at search time, stored in Celsius. */
    private double temperatureCelsius;

    /** Weather condition string at search time (e.g. "Clear", "Rain"). */
    private String condition;

    /** Timestamp when this search was executed. */
    private LocalDateTime searchTime;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Builds a SearchHistory entry from a completed weather lookup.
     *
     * @param query            original user input
     * @param resolvedCity     city name from API response
     * @param countryCode      country code from API response
     * @param temperatureCelsius temperature at the time of the search
     * @param condition        weather condition at the time of the search
     */
    public SearchHistory(String query,
                         String resolvedCity,
                         String countryCode,
                         double temperatureCelsius,
                         String condition) {
        this.query             = query;
        this.resolvedCity      = resolvedCity;
        this.countryCode       = countryCode;
        this.temperatureCelsius = temperatureCelsius;
        this.condition         = condition;
        this.searchTime        = LocalDateTime.now(); // capture current time
    }

    // -----------------------------------------------------------------------
    // Derived helpers
    // -----------------------------------------------------------------------

    /**
     * Returns a human-readable label for the history list.
     * Example: "London, GB - Clear  (Mar 15, 2024  14:30:00)"
     */
    public String getDisplayLabel() {
        return String.format("%s, %s - %s  (%s)",
                resolvedCity, countryCode, condition,
                searchTime.format(DISPLAY_FORMATTER));
    }

    /**
     * Converts the stored Celsius value to Fahrenheit.
     * @return temperature in Fahrenheit
     */
    public double getTemperatureFahrenheit() {
        return (temperatureCelsius * 9.0 / 5.0) + 32.0;
    }

    // -----------------------------------------------------------------------
    // Getters / Setters
    // -----------------------------------------------------------------------

    public String         getQuery()              { return query; }
    public void           setQuery(String query)  { this.query = query; }

    public String         getResolvedCity()       { return resolvedCity; }
    public void           setResolvedCity(String c){ this.resolvedCity = c; }

    public String         getCountryCode()        { return countryCode; }
    public void           setCountryCode(String c){ this.countryCode = c; }

    public double         getTemperatureCelsius() { return temperatureCelsius; }
    public void           setTemperatureCelsius(double t){ this.temperatureCelsius = t; }

    public String         getCondition()          { return condition; }
    public void           setCondition(String c)  { this.condition = c; }

    public LocalDateTime  getSearchTime()         { return searchTime; }
    public void           setSearchTime(LocalDateTime t){ this.searchTime = t; }

    @Override
    public String toString() {
        return getDisplayLabel();
    }
}
