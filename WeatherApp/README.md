# Weather Information App

A Java Swing desktop application that delivers real-time weather data and
5-day forecasts via the OpenWeatherMap API.

---

## Features

| Feature | Details |
|---|---|
| **API Integration** | OpenWeatherMap `/weather` and `/forecast` endpoints |
| **GUI Framework** | Java Swing with a custom gradient `GradientPanel` |
| **Location input** | City name *or* latitude/longitude coordinates |
| **Current weather** | Temperature, feels-like, humidity, wind, pressure, visibility, sunrise/sunset |
| **Weather icons** | Unicode emoji icons representing each condition group |
| **Forecast display** | 5-day summary derived from the 3-hourly forecast feed |
| **Unit conversion** | Temperature: °C / °F. Wind speed: m/s / km/h / mph |
| **Error handling** | Validates user input before the API call; maps HTTP error codes to friendly messages |
| **Search history** | Up to 20 recent searches with timestamps; double-click to repeat a search |
| **Dynamic backgrounds** | Background gradient changes based on weather condition and time of day |

---

## Prerequisites

| Requirement | Version |
|---|---|
| Java Development Kit (JDK) | 17 or newer |
| OpenWeatherMap API key | Free tier is sufficient |
| `org.json` library | `json-20240303.jar` (included in `lib/`) |

---

## Getting an API Key

1. Visit <https://openweathermap.org/api> and create a free account.
2. Navigate to **My API Keys** and copy your default key.
3. New keys may take up to 2 hours to activate.

---

## Configuration

Open `src/main/java/com/weatherapp/util/AppConstants.java` and replace the
placeholder with your real key:

```java
public static final String API_KEY = "YOUR_API_KEY_HERE";
//  replace with  ^^^^^^^^^^^^^^^^^^^  your key
```

---

## Project Structure

```
WeatherApp/
├── lib/
│   └── json-20240303.jar          # org.json dependency
├── src/main/java/com/weatherapp/
│   ├── Main.java                  # Entry point
│   ├── api/
│   │   ├── WeatherApiService.java # All HTTP / JSON logic
│   │   └── WeatherApiException.java
│   ├── gui/
│   │   └── WeatherAppFrame.java   # Main Swing window
│   ├── model/
│   │   ├── WeatherData.java       # Current weather POJO
│   │   ├── ForecastData.java      # Forecast POJO + ForecastEntry
│   │   └── SearchHistory.java     # History entry POJO
│   └── util/
│       ├── AppConstants.java      # Colors, fonts, API key
│       ├── InputValidator.java    # City name & coordinate validation
│       └── TimeOfDayHelper.java   # Dynamic background helper
└── README.md
```

---

## Building and Running

### Option A: Command line (Linux / macOS)

```bash
# 1. From the project root, compile all source files
find src -name "*.java" > sources.txt
javac -cp "lib/json-20240303.jar" -d out @sources.txt

# 2. Run
java -cp "out:lib/json-20240303.jar" com.weatherapp.Main
```

### Option B: Command line (Windows)

```cmd
REM 1. Compile
dir /s /b src\*.java > sources.txt
javac -cp "lib\json-20240303.jar" -d out @sources.txt

REM 2. Run
java -cp "out;lib\json-20240303.jar" com.weatherapp.Main
```

### Option C: IntelliJ IDEA

1. Open the project root as a new project.
2. Go to **File > Project Structure > Libraries**, click **+**, and add
   `lib/json-20240303.jar`.
3. Set the SDK to JDK 17+.
4. Run `Main.java`.

### Option D: Eclipse

1. **File > Import > Existing Projects into Workspace**, select the project root.
2. Right-click the project > **Build Path > Add External Archives**, add
   `lib/json-20240303.jar`.
3. Run `Main.java` as a Java application.

---

## How to Use the App

### Searching by city name
1. Type a city name in the search bar (e.g. `London`, `Lagos,NG`, `New York`).
2. Press **Enter** or click **Search City**.

### Searching by coordinates
1. Type coordinates in the format `latitude,longitude` (e.g. `51.5,-0.12`).
2. Click **Search Coords**.

### Switching units
- Click **°C** or **°F** to toggle the temperature unit.
- Click **m/s**, **km/h**, or **mph** to change the wind speed unit.
- The display updates immediately without a new API call.

### Search history
- Every successful search is logged in the right-hand panel with a timestamp.
- **Double-click** any history entry to repeat that search.
- Click **Clear History** to remove all entries.

### Dynamic background
- The gradient background changes automatically based on the current weather
  condition (clear, cloudy, rain, snow, thunderstorm) and the time of day
  (dawn, morning, afternoon, evening, night).

---

## Error Messages

| Message | Likely Cause |
|---|---|
| City name cannot be empty | The search field was blank |
| Location not found | Typo in city name, or the city is not in OWM's database |
| Invalid API key | The key in `AppConstants.java` is wrong or not yet active |
| API rate limit exceeded | Too many requests; wait a moment and retry |
| Network error | No internet connection or firewall blocking the request |

---

## Dependencies

- **org.json** (BSD licence) - lightweight JSON parser
  - Maven: `org.json:json:20240303`
  - Download: <https://mvnrepository.com/artifact/org.json/json>

---

## Implementation Notes

- All network calls run on a **SwingWorker** background thread so the GUI
  remains responsive during API requests.
- Temperature is stored internally in **Celsius** and converted on display.
- Wind speed is stored internally in **m/s** and converted on display.
- The `TimeOfDayHelper` blends weather-based and time-based colours for a
  natural-looking dynamic background.
- History is capped at **20 entries** (in-memory only; not persisted to disk).
