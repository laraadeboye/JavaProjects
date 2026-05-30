package com.weatherapp.api;

/**
 * WeatherApiException.java
 * Custom checked exception thrown by WeatherApiService when an API request
 * fails, returns a non-200 HTTP status code, or encounters a network error.
 *
 * Callers can inspect the HTTP status code via {@link #getStatusCode()} to
 * distinguish between different failure modes (e.g. 404 city not found vs.
 * 401 invalid API key vs. -1 network error).
 */
public class WeatherApiException extends Exception {

    /** The HTTP status code returned by the API, or -1 for network errors. */
    private final int statusCode;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Creates a new WeatherApiException with a message and HTTP status code.
     *
     * @param message    human-readable description of the error
     * @param statusCode HTTP status code (-1 if not applicable)
     */
    public WeatherApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Creates a new WeatherApiException wrapping another throwable.
     *
     * @param message    human-readable description of the error
     * @param statusCode HTTP status code (-1 if not applicable)
     * @param cause      the underlying exception
     */
    public WeatherApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    // -----------------------------------------------------------------------
    // Getter
    // -----------------------------------------------------------------------

    /**
     * Returns the HTTP status code associated with this exception.
     *
     * @return HTTP status code, or -1 for network/parse errors
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Convenience method that returns a user-friendly explanation based on
     * the HTTP status code.
     *
     * @return readable reason string
     */
    public String getUserFriendlyReason() {
        return switch (statusCode) {
            case 401 -> "Invalid API key. Please check your OpenWeatherMap API key.";
            case 404 -> "Location not found. Please check the city name and try again.";
            case 429 -> "API rate limit exceeded. Please wait a moment and try again.";
            case 500, 502, 503 -> "The weather service is temporarily unavailable. Please try later.";
            case -1  -> "Network error. Please check your internet connection.";
            default  -> getMessage();
        };
    }
}
