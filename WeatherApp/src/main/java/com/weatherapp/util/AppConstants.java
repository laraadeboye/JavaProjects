package com.weatherapp.util;

import java.awt.Color;
import java.awt.Font;

/**
 * AppConstants.java
 * Application-wide constants: colors, fonts, dimensions, and other
 * configuration values used throughout the Weather Information App.
 *
 * All fields are public static final; this class should never be
 * instantiated.
 */
public final class AppConstants {

    // Prevent instantiation
    private AppConstants() {}

    // -----------------------------------------------------------------------
    // API Configuration
    // -----------------------------------------------------------------------

    /**
     * Your OpenWeatherMap API key.
     * Sign up at https://openweathermap.org/api to get a free key.
     * Replace the placeholder value below with your real key before running.
     */
    public static final String API_KEY = "YOUR_API_KEY_HERE";

    // -----------------------------------------------------------------------
    // Unit labels
    // -----------------------------------------------------------------------

    public static final String UNIT_CELSIUS    = "Celsius (°C)";
    public static final String UNIT_FAHRENHEIT = "Fahrenheit (°F)";
    public static final String UNIT_KMH        = "km/h";
    public static final String UNIT_MPH        = "mph";
    public static final String UNIT_MS         = "m/s";

    // -----------------------------------------------------------------------
    // Color palette
    // -----------------------------------------------------------------------

    /** Main background color used during the day (clear blue). */
    public static final Color COLOR_DAY_CLEAR      = new Color(0x4A90D9);

    /** Background color for cloudy daytime conditions. */
    public static final Color COLOR_DAY_CLOUDY     = new Color(0x7B9DB0);

    /** Background color for daytime rain. */
    public static final Color COLOR_DAY_RAIN       = new Color(0x506070);

    /** Background color for evening / sunset period. */
    public static final Color COLOR_EVENING        = new Color(0xE8845A);

    /** Background color for nighttime. */
    public static final Color COLOR_NIGHT          = new Color(0x1A2340);

    /** Background color for snow conditions. */
    public static final Color COLOR_SNOW           = new Color(0xC8DCE8);

    /** Background for thunderstorm conditions. */
    public static final Color COLOR_THUNDERSTORM   = new Color(0x2E3545);

    /** Panel / card background (semi-transparent white). */
    public static final Color COLOR_PANEL_BG       = new Color(255, 255, 255, 60);

    /** Primary text color (dark, for light backgrounds). */
    public static final Color COLOR_TEXT_PRIMARY   = new Color(0x1A2340);

    /** Secondary / muted text color. */
    public static final Color COLOR_TEXT_SECONDARY = new Color(0x4A5568);

    /** White used on dark backgrounds. */
    public static final Color COLOR_TEXT_WHITE     = Color.WHITE;

    /** Accent / highlight color. */
    public static final Color COLOR_ACCENT         = new Color(0xFF6B35);

    /** Error message color. */
    public static final Color COLOR_ERROR          = new Color(0xD32F2F);

    // -----------------------------------------------------------------------
    // Fonts
    // -----------------------------------------------------------------------

    /** Large temperature display font. */
    public static final Font FONT_TEMP_LARGE    = new Font("SansSerif", Font.BOLD,  72);

    /** Medium heading font. */
    public static final Font FONT_HEADING       = new Font("SansSerif", Font.BOLD,  24);

    /** Sub-heading font. */
    public static final Font FONT_SUBHEADING    = new Font("SansSerif", Font.BOLD,  16);

    /** Normal body text font. */
    public static final Font FONT_BODY          = new Font("SansSerif", Font.PLAIN, 14);

    /** Small label font. */
    public static final Font FONT_LABEL         = new Font("SansSerif", Font.PLAIN, 12);

    /** Small bold font for data values. */
    public static final Font FONT_VALUE         = new Font("SansSerif", Font.BOLD,  14);

    // -----------------------------------------------------------------------
    // Window dimensions
    // -----------------------------------------------------------------------

    public static final int WINDOW_WIDTH  = 1100;
    public static final int WINDOW_HEIGHT = 750;

    // -----------------------------------------------------------------------
    // Weather icon emoji map (fallback when image icons are unavailable)
    // -----------------------------------------------------------------------

    /**
     * Maps an OpenWeatherMap icon code prefix (first two chars) to a Unicode
     * weather emoji used when image assets are not loaded.
     *
     * @param iconCode OpenWeatherMap icon code e.g. "01d", "09n"
     * @return corresponding emoji character(s)
     */
    public static String getWeatherEmoji(String iconCode) {
        if (iconCode == null || iconCode.length() < 2) return "🌡";
        return switch (iconCode.substring(0, 2)) {
            case "01" -> "☀️";   // clear sky
            case "02" -> "⛅";  // few clouds
            case "03" -> "🌥️";  // scattered clouds
            case "04" -> "☁️";   // broken / overcast clouds
            case "09" -> "🌧️";  // shower rain
            case "10" -> "🌦️";  // rain
            case "11" -> "⛈️";  // thunderstorm
            case "13" -> "❄️";   // snow
            case "50" -> "🌫️";  // mist / fog
            default   -> "🌡️";
        };
    }

    /**
     * Maps a condition string to a color appropriate for the background,
     * taking into account the time-of-day suffix of the icon code.
     *
     * @param condition weather condition (e.g. "Clear", "Rain")
     * @param iconCode  OpenWeatherMap icon code (e.g. "01d", "01n")
     * @return Color to use for the dynamic background
     */
    public static Color getBackgroundColor(String condition, String iconCode) {
        if (condition == null) return COLOR_DAY_CLEAR;

        // Night time: always use night palette regardless of condition
        boolean isNight = iconCode != null && iconCode.endsWith("n");
        if (isNight) return COLOR_NIGHT;

        return switch (condition) {
            case "Clear"         -> COLOR_DAY_CLEAR;
            case "Clouds"        -> COLOR_DAY_CLOUDY;
            case "Rain",
                 "Drizzle"       -> COLOR_DAY_RAIN;
            case "Thunderstorm"  -> COLOR_THUNDERSTORM;
            case "Snow"          -> COLOR_SNOW;
            case "Mist", "Fog",
                 "Haze", "Smoke",
                 "Dust", "Sand",
                 "Ash", "Squall",
                 "Tornado"       -> COLOR_DAY_CLOUDY;
            default              -> COLOR_DAY_CLEAR;
        };
    }
}
