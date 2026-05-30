package com.weatherapp.gui;

import com.weatherapp.api.WeatherApiException;
import com.weatherapp.api.WeatherApiService;
import com.weatherapp.model.ForecastData;
import com.weatherapp.model.SearchHistory;
import com.weatherapp.model.WeatherData;
import com.weatherapp.util.AppConstants;
import com.weatherapp.util.InputValidator;
import com.weatherapp.util.TimeOfDayHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * WeatherAppFrame.java
 * Main application window for the Weather Information App.
 *
 * Features implemented:
 *   - City name and coordinate-based search
 *   - Current weather display with weather condition icon (emoji)
 *   - Temperature unit toggle (Celsius / Fahrenheit)
 *   - Wind speed unit toggle (m/s / km/h / mph)
 *   - 5-day forecast panel with daily summaries
 *   - Search history list with timestamps
 *   - Dynamic background colour based on weather condition and time of day
 *   - Comprehensive error handling with user-friendly messages
 *
 * GUI Framework: Java Swing
 */
public class WeatherAppFrame extends JFrame {

    // -----------------------------------------------------------------------
    // Instance variables
    // -----------------------------------------------------------------------

    /** Service layer responsible for all API communication. */
    private final WeatherApiService apiService;

    /** Holds the most recently fetched current weather data. */
    private WeatherData currentWeatherData;

    /** Holds the most recently fetched forecast data. */
    private ForecastData currentForecastData;

    /** History of completed searches, newest first. */
    private final List<SearchHistory> searchHistoryList = new ArrayList<>();

    /** Whether the user has selected Fahrenheit (false = Celsius). */
    private boolean useFahrenheit = false;

    /** Current wind speed unit: 0=m/s, 1=km/h, 2=mph */
    private int windUnitIndex = 1; // default km/h

    // -----------------------------------------------------------------------
    // Top-level panels
    // -----------------------------------------------------------------------

    /** Gradient background panel that fills the entire frame. */
    private GradientPanel backgroundPanel;

    // -----------------------------------------------------------------------
    // Search bar components
    // -----------------------------------------------------------------------
    private JTextField searchField;
    private JButton    searchButton;
    private JButton    coordSearchButton;
    private JLabel     errorLabel;

    // -----------------------------------------------------------------------
    // Current weather display components
    // -----------------------------------------------------------------------
    private JLabel cityLabel;
    private JLabel conditionIconLabel;  // large emoji icon
    private JLabel temperatureLabel;
    private JLabel conditionLabel;
    private JLabel feelsLikeLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;
    private JLabel pressureLabel;
    private JLabel visibilityLabel;
    private JLabel sunriseLabel;
    private JLabel sunsetLabel;
    private JLabel timestampLabel;

    // -----------------------------------------------------------------------
    // Unit toggle buttons
    // -----------------------------------------------------------------------
    private JToggleButton celsiusButton;
    private JToggleButton fahrenheitButton;
    private JToggleButton windMsButton;
    private JToggleButton windKmhButton;
    private JToggleButton windMphButton;

    // -----------------------------------------------------------------------
    // Forecast panel
    // -----------------------------------------------------------------------
    private JPanel forecastPanel;

    // -----------------------------------------------------------------------
    // History panel
    // -----------------------------------------------------------------------
    private DefaultListModel<String> historyListModel;
    private JList<String>            historyList;

    // -----------------------------------------------------------------------
    // Formatters
    // -----------------------------------------------------------------------
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("EEE, MMM dd  HH:mm");

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Initialises the application window with the provided API service.
     *
     * @param apiService configured WeatherApiService instance
     */
    public WeatherAppFrame(WeatherApiService apiService) {
        this.apiService = apiService;
        initFrame();
        buildUI();
        applyDynamicBackground(null, null); // default background for current time
        setVisible(true);
    }

    // -----------------------------------------------------------------------
    // Frame setup
    // -----------------------------------------------------------------------

    /** Configures the JFrame properties (title, size, close behaviour). */
    private void initFrame() {
        setTitle("Weather Information App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null); // centre on screen
    }

    // -----------------------------------------------------------------------
    // UI Construction
    // -----------------------------------------------------------------------

    /** Assembles all UI panels and adds them to the frame's content pane. */
    private void buildUI() {
        backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        backgroundPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(backgroundPanel);

        // --- Top: search bar ---
        backgroundPanel.add(buildSearchPanel(), BorderLayout.NORTH);

        // --- Centre: current weather + forecast (left) and history (right) ---
        JPanel centrePanel = new JPanel(new BorderLayout(10, 0));
        centrePanel.setOpaque(false);

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(buildWeatherDisplayPanel(), BorderLayout.CENTER);
        leftPanel.add(buildForecastPanel(), BorderLayout.SOUTH);

        centrePanel.add(leftPanel, BorderLayout.CENTER);
        centrePanel.add(buildHistoryPanel(), BorderLayout.EAST);

        backgroundPanel.add(centrePanel, BorderLayout.CENTER);

        // --- Bottom: unit toggles + status ---
        backgroundPanel.add(buildBottomPanel(), BorderLayout.SOUTH);
    }

    // -----------------------------------------------------------------------
    // Search panel
    // -----------------------------------------------------------------------

    /** Builds the top search bar panel. */
    private JPanel buildSearchPanel() {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(8, 0));

        // Title label
        JLabel titleLabel = new JLabel("  Weather Information App");
        titleLabel.setFont(AppConstants.FONT_HEADING);
        titleLabel.setForeground(AppConstants.COLOR_TEXT_PRIMARY);

        // Search text field
        searchField = new JTextField(30);
        searchField.setFont(AppConstants.FONT_BODY);
        searchField.setToolTipText(
                "Enter city name (e.g. London) or coordinates (e.g. 51.5,-0.12)");
        searchField.addActionListener(e -> performCitySearch()); // Enter key triggers search

        // City search button
        searchButton = new JButton("Search City");
        styleButton(searchButton, AppConstants.COLOR_ACCENT);
        searchButton.addActionListener(e -> performCitySearch());

        // Coordinate search button
        coordSearchButton = new JButton("Search Coords");
        styleButton(coordSearchButton, new Color(0x2980B9));
        coordSearchButton.addActionListener(e -> performCoordSearch());

        // Error label (hidden by default)
        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppConstants.FONT_LABEL);
        errorLabel.setForeground(AppConstants.COLOR_ERROR);

        // Layout
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        searchRow.setOpaque(false);
        searchRow.add(new JLabel("Location:"));
        searchRow.add(searchField);
        searchRow.add(searchButton);
        searchRow.add(coordSearchButton);
        searchRow.add(errorLabel);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(searchRow, BorderLayout.CENTER);

        return panel;
    }

    // -----------------------------------------------------------------------
    // Current weather display panel
    // -----------------------------------------------------------------------

    /** Builds the main current-weather information card. */
    private JPanel buildWeatherDisplayPanel() {
        JPanel card = createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 10, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: City name + timestamp
        cityLabel = new JLabel("-- Search for a city --");
        cityLabel.setFont(AppConstants.FONT_HEADING);
        cityLabel.setForeground(AppConstants.COLOR_TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        card.add(cityLabel, gbc);

        timestampLabel = new JLabel("");
        timestampLabel.setFont(AppConstants.FONT_LABEL);
        timestampLabel.setForeground(AppConstants.COLOR_TEXT_SECONDARY);
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(timestampLabel, gbc);

        // Row 1: Big icon + temperature
        conditionIconLabel = new JLabel("🌡️", SwingConstants.CENTER);
        conditionIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(conditionIconLabel, gbc);

        temperatureLabel = new JLabel("--°", SwingConstants.CENTER);
        temperatureLabel.setFont(AppConstants.FONT_TEMP_LARGE);
        temperatureLabel.setForeground(AppConstants.COLOR_TEXT_PRIMARY);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        card.add(temperatureLabel, gbc);

        // Row 2: Condition description + feels like
        conditionLabel = new JLabel("  ");
        conditionLabel.setFont(AppConstants.FONT_SUBHEADING);
        conditionLabel.setForeground(AppConstants.COLOR_TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(conditionLabel, gbc);

        feelsLikeLabel = new JLabel("  ");
        feelsLikeLabel.setFont(AppConstants.FONT_BODY);
        feelsLikeLabel.setForeground(AppConstants.COLOR_TEXT_SECONDARY);
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        card.add(feelsLikeLabel, gbc);

        // Row 3-5: Detail grid (humidity, wind, pressure, visibility, sunrise, sunset)
        JSeparator sep = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(sep, gbc);
        gbc.fill = GridBagConstraints.NONE;

        humidityLabel   = createDetailLabel("💧 Humidity: --");
        windLabel       = createDetailLabel("💨 Wind: --");
        pressureLabel   = createDetailLabel("🌡 Pressure: --");
        visibilityLabel = createDetailLabel("👁 Visibility: --");
        sunriseLabel    = createDetailLabel("🌅 Sunrise: --");
        sunsetLabel     = createDetailLabel("🌇 Sunset: --");

        addDetailRow(card, gbc, 4, humidityLabel,   windLabel);
        addDetailRow(card, gbc, 5, pressureLabel,   visibilityLabel);
        addDetailRow(card, gbc, 6, sunriseLabel,    sunsetLabel);

        return card;
    }

    /** Adds a two-column detail row to the weather card. */
    private void addDetailRow(JPanel card, GridBagConstraints gbc,
                              int row, JLabel left, JLabel right) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(left, gbc);
        gbc.gridx = 2;
        card.add(right, gbc);
    }

    // -----------------------------------------------------------------------
    // Forecast panel
    // -----------------------------------------------------------------------

    /** Builds the short-term forecast section below the current weather. */
    private JPanel buildForecastPanel() {
        JPanel wrapper = createCard();
        wrapper.setLayout(new BorderLayout(0, 6));

        JLabel title = new JLabel("5-Day Forecast");
        title.setFont(AppConstants.FONT_SUBHEADING);
        title.setForeground(AppConstants.COLOR_TEXT_PRIMARY);
        wrapper.add(title, BorderLayout.NORTH);

        forecastPanel = new JPanel(new GridLayout(1, 5, 8, 0));
        forecastPanel.setOpaque(false);

        // Placeholder cards until data arrives
        for (int i = 0; i < 5; i++) {
            forecastPanel.add(buildEmptyForecastCard());
        }

        wrapper.add(forecastPanel, BorderLayout.CENTER);
        return wrapper;
    }

    /** Creates one empty forecast day card (placeholder). */
    private JPanel buildEmptyForecastCard() {
        JPanel card = createSmallCard();
        card.setLayout(new GridLayout(4, 1, 2, 2));
        card.add(new JLabel("--", SwingConstants.CENTER));
        card.add(new JLabel("?", SwingConstants.CENTER));
        card.add(new JLabel("--°", SwingConstants.CENTER));
        card.add(new JLabel("--", SwingConstants.CENTER));
        return card;
    }

    /** Refreshes the forecast panel with data from the most recent API call. */
    private void updateForecastPanel() {
        if (currentForecastData == null) return;

        forecastPanel.removeAll();

        // Group entries by calendar day and take the noon-ish slot
        List<ForecastData.ForecastEntry> dailySummaries = extractDailySummaries(
                currentForecastData.getEntries());

        int count = Math.min(5, dailySummaries.size());
        for (int i = 0; i < count; i++) {
            forecastPanel.add(buildForecastDayCard(dailySummaries.get(i)));
        }

        // Fill remaining slots if fewer than 5 days returned
        for (int i = count; i < 5; i++) {
            forecastPanel.add(buildEmptyForecastCard());
        }

        forecastPanel.revalidate();
        forecastPanel.repaint();
    }

    /**
     * From the list of 3-hourly entries, picks one representative entry per
     * calendar day (preferring the slot closest to noon).
     *
     * @param entries all forecast entries from the API
     * @return list of one entry per day
     */
    private List<ForecastData.ForecastEntry> extractDailySummaries(
            List<ForecastData.ForecastEntry> entries) {

        List<ForecastData.ForecastEntry> summaries = new ArrayList<>();
        String lastDay = "";

        for (ForecastData.ForecastEntry entry : entries) {
            LocalDateTime dt = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(entry.getTimestamp()), ZoneId.systemDefault());
            String dayKey = dt.toLocalDate().toString();

            if (!dayKey.equals(lastDay)) {
                summaries.add(entry); // first entry of a new day
                lastDay = dayKey;
            }
        }
        return summaries;
    }

    /** Builds one forecast day card with icon, day name, and temperature. */
    private JPanel buildForecastDayCard(ForecastData.ForecastEntry entry) {
        JPanel card = createSmallCard();
        card.setLayout(new GridLayout(4, 1, 2, 2));

        LocalDateTime dt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(entry.getTimestamp()), ZoneId.systemDefault());

        // Day of week
        JLabel dayLabel = new JLabel(
                dt.format(DateTimeFormatter.ofPattern("EEE")), SwingConstants.CENTER);
        dayLabel.setFont(AppConstants.FONT_LABEL);
        dayLabel.setForeground(AppConstants.COLOR_TEXT_SECONDARY);

        // Weather emoji icon
        JLabel iconLabel = new JLabel(
                AppConstants.getWeatherEmoji(entry.getIconCode()), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        // Temperature
        String tempStr = useFahrenheit
                ? String.format("%.0f°F", entry.getTemperatureFahrenheit())
                : String.format("%.0f°C", entry.getTemperatureCelsius());
        JLabel tempLabel = new JLabel(tempStr, SwingConstants.CENTER);
        tempLabel.setFont(AppConstants.FONT_VALUE);
        tempLabel.setForeground(AppConstants.COLOR_TEXT_PRIMARY);

        // Condition
        JLabel condLabel = new JLabel(entry.getCondition(), SwingConstants.CENTER);
        condLabel.setFont(AppConstants.FONT_LABEL);
        condLabel.setForeground(AppConstants.COLOR_TEXT_SECONDARY);

        card.add(dayLabel);
        card.add(iconLabel);
        card.add(tempLabel);
        card.add(condLabel);

        return card;
    }

    // -----------------------------------------------------------------------
    // History panel
    // -----------------------------------------------------------------------

    /** Builds the search history sidebar. */
    private JPanel buildHistoryPanel() {
        JPanel card = createCard();
        card.setPreferredSize(new Dimension(280, 0));
        card.setLayout(new BorderLayout(0, 8));

        JLabel title = new JLabel("Search History");
        title.setFont(AppConstants.FONT_SUBHEADING);
        title.setForeground(AppConstants.COLOR_TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        historyList.setFont(AppConstants.FONT_LABEL);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.setBackground(new Color(255, 255, 255, 80));

        // Double-click on history item re-runs that search
        historyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = historyList.getSelectedIndex();
                    if (idx >= 0 && idx < searchHistoryList.size()) {
                        SearchHistory entry = searchHistoryList.get(idx);
                        searchField.setText(entry.getQuery());
                        performCitySearch();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(260, 400));
        card.add(scrollPane, BorderLayout.CENTER);

        // Clear history button
        JButton clearBtn = new JButton("Clear History");
        styleButton(clearBtn, new Color(0x888888));
        clearBtn.addActionListener(e -> clearHistory());
        card.add(clearBtn, BorderLayout.SOUTH);

        return card;
    }

    // -----------------------------------------------------------------------
    // Bottom panel (unit toggles)
    // -----------------------------------------------------------------------

    /** Builds the bottom bar with unit toggle buttons. */
    private JPanel buildBottomPanel() {
        JPanel panel = createCard();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 4));

        // Temperature unit toggle
        panel.add(new JLabel("Temperature:"));
        ButtonGroup tempGroup = new ButtonGroup();

        celsiusButton = new JToggleButton("°C");
        fahrenheitButton = new JToggleButton("°F");
        celsiusButton.setSelected(true);

        celsiusButton.addActionListener(e -> {
            useFahrenheit = false;
            refreshWeatherDisplay();
        });
        fahrenheitButton.addActionListener(e -> {
            useFahrenheit = true;
            refreshWeatherDisplay();
        });

        tempGroup.add(celsiusButton);
        tempGroup.add(fahrenheitButton);
        panel.add(celsiusButton);
        panel.add(fahrenheitButton);

        // Separator
        panel.add(new JSeparator(SwingConstants.VERTICAL));

        // Wind speed unit toggle
        panel.add(new JLabel("  Wind Speed:"));
        ButtonGroup windGroup = new ButtonGroup();

        windMsButton  = new JToggleButton("m/s");
        windKmhButton = new JToggleButton("km/h");
        windMphButton = new JToggleButton("mph");
        windKmhButton.setSelected(true);

        windMsButton.addActionListener(e  -> { windUnitIndex = 0; refreshWeatherDisplay(); });
        windKmhButton.addActionListener(e -> { windUnitIndex = 1; refreshWeatherDisplay(); });
        windMphButton.addActionListener(e -> { windUnitIndex = 2; refreshWeatherDisplay(); });

        windGroup.add(windMsButton);
        windGroup.add(windKmhButton);
        windGroup.add(windMphButton);
        panel.add(windMsButton);
        panel.add(windKmhButton);
        panel.add(windMphButton);

        return panel;
    }

    // -----------------------------------------------------------------------
    // Search logic
    // -----------------------------------------------------------------------

    /**
     * Reads the search field as a city name, validates it, and triggers
     * an asynchronous API request on a SwingWorker background thread.
     */
    private void performCitySearch() {
        String raw = searchField.getText();
        InputValidator.ValidationResult result = InputValidator.validateCityName(raw);

        if (!result.isValid()) {
            showError(result.getErrorMessage());
            return;
        }

        clearError();
        String cityName = result.getSanitizedInput();
        setSearching(true);

        // Background thread so the UI stays responsive
        SwingWorker<WeatherData[], Void> worker = new SwingWorker<>() {
            @Override
            protected WeatherData[] doInBackground() throws Exception {
                WeatherData weather  = apiService.getCurrentWeatherByCity(cityName);
                // Store forecast data for the forecast panel
                currentForecastData  = apiService.getForecastByCity(cityName);
                return new WeatherData[]{ weather };
            }

            @Override
            protected void done() {
                setSearching(false);
                try {
                    WeatherData[] results = get();
                    currentWeatherData = results[0];
                    refreshWeatherDisplay();
                    addToHistory(raw, currentWeatherData);
                } catch (Exception ex) {
                    handleApiError(ex);
                }
            }
        };
        worker.execute();
    }

    /**
     * Reads the search field as "lat,lon" coordinates, validates them, and
     * triggers an asynchronous API request.
     */
    private void performCoordSearch() {
        String raw = searchField.getText();
        InputValidator.ValidationResult result = InputValidator.validateCoordinates(raw);

        if (!result.isValid()) {
            showError(result.getErrorMessage());
            return;
        }

        clearError();
        double[] coords = result.getCoords();
        double lat = coords[0];
        double lon = coords[1];
        setSearching(true);

        SwingWorker<WeatherData[], Void> worker = new SwingWorker<>() {
            @Override
            protected WeatherData[] doInBackground() throws Exception {
                WeatherData weather = apiService.getCurrentWeatherByCoords(lat, lon);
                currentForecastData = apiService.getForecastByCoords(lat, lon);
                return new WeatherData[]{ weather };
            }

            @Override
            protected void done() {
                setSearching(false);
                try {
                    WeatherData[] results = get();
                    currentWeatherData = results[0];
                    refreshWeatherDisplay();
                    addToHistory(raw, currentWeatherData);
                } catch (Exception ex) {
                    handleApiError(ex);
                }
            }
        };
        worker.execute();
    }

    // -----------------------------------------------------------------------
    // Display update methods
    // -----------------------------------------------------------------------

    /**
     * Updates all current-weather labels and the forecast panel based on
     * {@code currentWeatherData} and the selected unit preferences.
     * Safe to call multiple times (e.g., after unit toggle).
     */
    private void refreshWeatherDisplay() {
        if (currentWeatherData == null) return;

        WeatherData d = currentWeatherData;

        // City + country
        cityLabel.setText(d.getCityName() + ", " + d.getCountryCode());

        // Icon
        conditionIconLabel.setText(AppConstants.getWeatherEmoji(d.getIconCode()));

        // Temperature
        if (useFahrenheit) {
            temperatureLabel.setText(String.format("%.1f°F", d.getTemperatureFahrenheit()));
            feelsLikeLabel.setText(String.format("Feels like %.1f°F", d.getFeelsLikeFahrenheit()));
        } else {
            temperatureLabel.setText(String.format("%.1f°C", d.getTemperatureCelsius()));
            feelsLikeLabel.setText(String.format("Feels like %.1f°C", d.getFeelsLikeCelsius()));
        }

        // Condition
        conditionLabel.setText(capitalise(d.getConditionDescription()));

        // Humidity
        humidityLabel.setText(String.format("💧 Humidity: %d%%", d.getHumidity()));

        // Wind speed
        String windStr = switch (windUnitIndex) {
            case 0 -> String.format("💨 Wind: %.1f m/s %s", d.getWindSpeedMetersPerSec(), d.getWindDirection());
            case 2 -> String.format("💨 Wind: %.1f mph %s", d.getWindSpeedMph(), d.getWindDirection());
            default -> String.format("💨 Wind: %.1f km/h %s", d.getWindSpeedKmh(), d.getWindDirection());
        };
        windLabel.setText(windStr);

        // Pressure
        pressureLabel.setText(String.format("🌡 Pressure: %.0f hPa", d.getPressure()));

        // Visibility
        visibilityLabel.setText(String.format("👁 Visibility: %.1f km", d.getVisibility() / 1000.0));

        // Sunrise / Sunset
        sunriseLabel.setText("🌅 Sunrise: " + epochToTime(d.getSunrise()));
        sunsetLabel.setText("🌇 Sunset: " + epochToTime(d.getSunset()));

        // Timestamp
        timestampLabel.setText("Updated: " + epochToDateTime(d.getTimestamp()));

        // Dynamic background
        applyDynamicBackground(d.getCondition(), d.getIconCode());

        // Refresh forecast panel
        updateForecastPanel();
    }

    /**
     * Applies a dynamic background gradient based on the weather condition
     * and icon code (which encodes day/night as suffix 'd' or 'n').
     * Falls back to time-of-day colour if no weather data is available.
     *
     * @param condition weather condition string or null
     * @param iconCode  OWM icon code or null
     */
    private void applyDynamicBackground(String condition, String iconCode) {
        Color bg;
        if (condition == null) {
            bg = TimeOfDayHelper.getBackgroundColorForCurrentTime();
        } else {
            bg = AppConstants.getBackgroundColor(condition, iconCode);
            // Blend with time-of-day color for a more natural effect
            Color timeBg = TimeOfDayHelper.getBackgroundColorForCurrentTime();
            bg = TimeOfDayHelper.blend(bg, timeBg, 0.65);
        }
        backgroundPanel.setTopColor(bg);
        backgroundPanel.setBottomColor(bg.darker());
        backgroundPanel.repaint();
    }

    // -----------------------------------------------------------------------
    // History management
    // -----------------------------------------------------------------------

    /**
     * Adds a completed search to the history list and updates the sidebar.
     *
     * @param query          the raw user input
     * @param weatherData    the resulting weather data
     */
    private void addToHistory(String query, WeatherData weatherData) {
        SearchHistory entry = new SearchHistory(
                query,
                weatherData.getCityName(),
                weatherData.getCountryCode(),
                weatherData.getTemperatureCelsius(),
                weatherData.getCondition()
        );
        searchHistoryList.add(0, entry);  // newest first
        historyListModel.add(0, entry.getDisplayLabel());

        // Cap history at 20 entries
        if (searchHistoryList.size() > 20) {
            searchHistoryList.remove(searchHistoryList.size() - 1);
            historyListModel.remove(historyListModel.size() - 1);
        }
    }

    /** Removes all entries from the history list. */
    private void clearHistory() {
        searchHistoryList.clear();
        historyListModel.clear();
    }

    // -----------------------------------------------------------------------
    // Error handling
    // -----------------------------------------------------------------------

    /**
     * Inspects the exception from a SwingWorker and displays an appropriate
     * user-friendly error message.
     *
     * @param ex exception caught from SwingWorker.get()
     */
    private void handleApiError(Exception ex) {
        Throwable cause = ex.getCause();
        String message;
        if (cause instanceof WeatherApiException wae) {
            message = wae.getUserFriendlyReason();
        } else if (cause != null) {
            message = "Unexpected error: " + cause.getMessage();
        } else {
            message = "Unexpected error: " + ex.getMessage();
        }
        showError(message);
    }

    /** Displays an error message in the error label. */
    private void showError(String message) {
        errorLabel.setText("⚠ " + message);
        errorLabel.setVisible(true);
    }

    /** Hides the error label. */
    private void clearError() {
        errorLabel.setText(" ");
        errorLabel.setVisible(false);
    }

    // -----------------------------------------------------------------------
    // Helper / factory methods
    // -----------------------------------------------------------------------

    /** Enables or disables the search buttons and shows a loading cursor. */
    private void setSearching(boolean searching) {
        searchButton.setEnabled(!searching);
        coordSearchButton.setEnabled(!searching);
        setCursor(Cursor.getPredefinedCursor(
                searching ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
        if (searching) {
            temperatureLabel.setText("Loading...");
        }
    }

    /** Creates a semi-transparent rounded card panel. */
    private JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255, 80));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 120), 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        return panel;
    }

    /** Creates a smaller card for forecast day cells. */
    private JPanel createSmallCard() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255, 70));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 120), 1, true),
                new EmptyBorder(6, 6, 6, 6)));
        return panel;
    }

    /** Creates a detail label with the standard label font and colour. */
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppConstants.FONT_BODY);
        label.setForeground(AppConstants.COLOR_TEXT_PRIMARY);
        return label;
    }

    /** Applies consistent styling to an action button. */
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(AppConstants.FONT_BODY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Capitalises the first letter of a string. */
    private String capitalise(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Converts a Unix epoch timestamp to a local time string (HH:mm).
     *
     * @param epoch Unix timestamp in seconds
     * @return formatted time string
     */
    private String epochToTime(long epoch) {
        if (epoch == 0) return "--:--";
        LocalDateTime dt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
        return dt.format(TIME_FMT);
    }

    /**
     * Converts a Unix epoch timestamp to a full date-time string.
     *
     * @param epoch Unix timestamp in seconds
     * @return formatted date-time string
     */
    private String epochToDateTime(long epoch) {
        if (epoch == 0) return "--";
        LocalDateTime dt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
        return dt.format(DT_FMT);
    }

    // -----------------------------------------------------------------------
    // Inner class: GradientPanel
    // -----------------------------------------------------------------------

    /**
     * GradientPanel.java (inner class)
     * A JPanel that paints a vertical linear gradient background.
     * The top and bottom colours are updated by
     * {@link WeatherAppFrame#applyDynamicBackground} to implement the
     * dynamic background feature.
     */
    private static class GradientPanel extends JPanel {

        private Color topColor    = new Color(0x4A90D9);
        private Color bottomColor = new Color(0x1A2340);

        void setTopColor(Color c)    { this.topColor    = c; }
        void setBottomColor(Color c) { this.bottomColor = c; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint gp = new GradientPaint(
                    0, 0, topColor,
                    0, getHeight(), bottomColor);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}
