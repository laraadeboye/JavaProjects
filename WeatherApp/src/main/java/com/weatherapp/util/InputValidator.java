package com.weatherapp.util;

/**
 * InputValidator.java
 * Provides static validation methods for user input in the Weather App.
 *
 * Validates:
 *   - City name strings (non-blank, reasonable length, safe characters)
 *   - Latitude / longitude coordinate strings
 */
public final class InputValidator {

    // Maximum length we accept for a city name input string
    private static final int MAX_CITY_NAME_LENGTH = 100;

    // Valid coordinate ranges
    private static final double MIN_LAT = -90.0;
    private static final double MAX_LAT =  90.0;
    private static final double MIN_LON = -180.0;
    private static final double MAX_LON =  180.0;

    // Prevent instantiation
    private InputValidator() {}

    // -----------------------------------------------------------------------
    // City name validation
    // -----------------------------------------------------------------------

    /**
     * Validates a city name entered by the user.
     *
     * Rules:
     *   1. Must not be null or blank.
     *   2. Must not exceed MAX_CITY_NAME_LENGTH characters.
     *   3. Must contain at least one letter.
     *   4. Must not contain shell-injection or script characters.
     *
     * @param cityName raw string from the text field
     * @return ValidationResult containing success flag and error message
     */
    public static ValidationResult validateCityName(String cityName) {
        if (cityName == null || cityName.isBlank()) {
            return ValidationResult.fail("City name cannot be empty.");
        }

        String trimmed = cityName.trim();

        if (trimmed.length() > MAX_CITY_NAME_LENGTH) {
            return ValidationResult.fail(
                    "City name is too long (max " + MAX_CITY_NAME_LENGTH + " characters).");
        }

        // Must contain at least one alphabetic character
        if (!trimmed.matches(".*[a-zA-Z].*")) {
            return ValidationResult.fail("City name must contain at least one letter.");
        }

        // Reject characters that are clearly not part of a city name
        if (trimmed.matches(".*[<>\"';&|`$!{}\\[\\]\\\\].*")) {
            return ValidationResult.fail("City name contains invalid characters.");
        }

        return ValidationResult.ok(trimmed);
    }

    // -----------------------------------------------------------------------
    // Coordinate validation
    // -----------------------------------------------------------------------

    /**
     * Validates a coordinate pair entered in the format "latitude,longitude".
     *
     * @param coordInput raw string from the text field e.g. "51.5,-0.12"
     * @return ValidationResult with success flag, message, and parsed double[]
     */
    public static ValidationResult validateCoordinates(String coordInput) {
        if (coordInput == null || coordInput.isBlank()) {
            return ValidationResult.fail("Coordinates cannot be empty.");
        }

        String[] parts = coordInput.trim().split(",");
        if (parts.length != 2) {
            return ValidationResult.fail(
                    "Please enter coordinates as \"latitude,longitude\" (e.g. 51.5,-0.12).");
        }

        double lat, lon;
        try {
            lat = Double.parseDouble(parts[0].trim());
        } catch (NumberFormatException e) {
            return ValidationResult.fail("Latitude is not a valid number.");
        }
        try {
            lon = Double.parseDouble(parts[1].trim());
        } catch (NumberFormatException e) {
            return ValidationResult.fail("Longitude is not a valid number.");
        }

        if (lat < MIN_LAT || lat > MAX_LAT) {
            return ValidationResult.fail(
                    "Latitude must be between " + MIN_LAT + " and " + MAX_LAT + ".");
        }
        if (lon < MIN_LON || lon > MAX_LON) {
            return ValidationResult.fail(
                    "Longitude must be between " + MIN_LON + " and " + MAX_LON + ".");
        }

        return new ValidationResult(true, null, new double[]{lat, lon});
    }

    // -----------------------------------------------------------------------
    // ValidationResult inner class
    // -----------------------------------------------------------------------

    /**
     * Carries the result of a validation check.
     * On success: {@link #isValid()} is true and {@link #getSanitizedInput()}
     * contains the cleaned input string.
     * On failure: {@link #isValid()} is false and {@link #getErrorMessage()}
     * explains the problem.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String  errorMessage;
        private final String  sanitizedInput; // city name after trim
        private final double[] coords;         // parsed [lat, lon] or null

        // Private constructor used by factory methods
        private ValidationResult(boolean valid, String errorMessage,
                                 String sanitizedInput, double[] coords) {
            this.valid          = valid;
            this.errorMessage   = errorMessage;
            this.sanitizedInput = sanitizedInput;
            this.coords         = coords;
        }

        /** Constructor for coordinate results. */
        private ValidationResult(boolean valid, String errorMessage, double[] coords) {
            this(valid, errorMessage, null, coords);
        }

        /** Factory: validation passed, store sanitized city name. */
        static ValidationResult ok(String sanitized) {
            return new ValidationResult(true, null, sanitized, null);
        }

        /** Factory: validation failed with a reason. */
        static ValidationResult fail(String message) {
            return new ValidationResult(false, message, null, null);
        }

        public boolean  isValid()           { return valid; }
        public String   getErrorMessage()   { return errorMessage; }
        public String   getSanitizedInput() { return sanitizedInput; }
        public double[] getCoords()         { return coords; }
    }
}
