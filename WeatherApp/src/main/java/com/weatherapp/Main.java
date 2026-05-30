package com.weatherapp;

import com.weatherapp.api.WeatherApiService;
import com.weatherapp.gui.WeatherAppFrame;
import com.weatherapp.util.AppConstants;

import javax.swing.*;

/**
 * Main.java
 * Application entry point for the Weather Information App.
 *
 * Usage:
 *   1. Replace AppConstants.API_KEY with your OpenWeatherMap API key.
 *   2. Ensure org.json is on the classpath (see README for build instructions).
 *   3. Run: java -cp ".:lib/json-20240303.jar" com.weatherapp.Main
 *
 * The Swing GUI is launched on the Event Dispatch Thread (EDT) as required
 * by the Swing threading model.
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Apply system look-and-feel for a native appearance where available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default cross-platform L&F if unavailable
            System.err.println("Could not load system look-and-feel: " + e.getMessage());
        }

        // Validate that the API key has been configured
        if ("YOUR_API_KEY_HERE".equals(AppConstants.API_KEY)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please replace YOUR_API_KEY_HERE in AppConstants.java\n" +
                    "with your OpenWeatherMap API key before running the app.\n\n" +
                    "Get a free key at: https://openweathermap.org/api",
                    "API Key Not Configured",
                    JOptionPane.WARNING_MESSAGE
            );
            // Allow the app to continue so the GUI layout can still be inspected
        }

        // Construct the API service layer
        WeatherApiService apiService;
        try {
            apiService = new WeatherApiService(
                    "YOUR_API_KEY_HERE".equals(AppConstants.API_KEY)
                            ? "DEMO_KEY_REPLACE_ME"   // placeholder so constructor doesn't throw
                            : AppConstants.API_KEY
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid API key: " + e.getMessage(),
                    "Configuration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Launch the GUI on the Event Dispatch Thread
        final WeatherApiService finalApiService = apiService;
        SwingUtilities.invokeLater(() -> new WeatherAppFrame(finalApiService));
    }
}
