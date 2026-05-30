package com.weatherapp.util;

import java.awt.Color;
import java.time.LocalTime;

/**
 * TimeOfDayHelper.java
 * Utility class that categorises the current local time into periods of day
 * and provides corresponding background colours for the dynamic background
 * feature of the Weather Information App.
 *
 * Time periods:
 *   Dawn      05:00 - 06:59
 *   Morning   07:00 - 11:59
 *   Afternoon 12:00 - 16:59
 *   Evening   17:00 - 19:59
 *   Night     20:00 - 04:59
 */
public final class TimeOfDayHelper {

    // Prevent instantiation
    private TimeOfDayHelper() {}

    // -----------------------------------------------------------------------
    // Period enum
    // -----------------------------------------------------------------------

    /** Represents a broad period of the day. */
    public enum Period {
        DAWN, MORNING, AFTERNOON, EVENING, NIGHT
    }

    // -----------------------------------------------------------------------
    // Colours per period (when no weather condition overrides)
    // -----------------------------------------------------------------------

    private static final Color COLOR_DAWN      = new Color(0xFFB347); // soft orange
    private static final Color COLOR_MORNING   = new Color(0x87CEEB); // sky blue
    private static final Color COLOR_AFTERNOON = new Color(0x4A90D9); // bright blue
    private static final Color COLOR_EVENING   = new Color(0xE8845A); // sunset orange-red
    private static final Color COLOR_NIGHT     = new Color(0x1A2340); // deep navy

    // -----------------------------------------------------------------------
    // Public methods
    // -----------------------------------------------------------------------

    /**
     * Returns the current {@link Period} based on the system clock.
     *
     * @return current period of the day
     */
    public static Period getCurrentPeriod() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 5  && hour < 7)  return Period.DAWN;
        if (hour >= 7  && hour < 12) return Period.MORNING;
        if (hour >= 12 && hour < 17) return Period.AFTERNOON;
        if (hour >= 17 && hour < 20) return Period.EVENING;
        return Period.NIGHT;
    }

    /**
     * Returns a background {@link Color} for the current time of day.
     * This is the baseline colour; {@link AppConstants#getBackgroundColor}
     * can further refine it based on weather condition.
     *
     * @return background color appropriate to the current time
     */
    public static Color getBackgroundColorForCurrentTime() {
        return switch (getCurrentPeriod()) {
            case DAWN      -> COLOR_DAWN;
            case MORNING   -> COLOR_MORNING;
            case AFTERNOON -> COLOR_AFTERNOON;
            case EVENING   -> COLOR_EVENING;
            case NIGHT     -> COLOR_NIGHT;
        };
    }

    /**
     * Returns a label suitable for display that describes the current period.
     *
     * @return human-readable period name
     */
    public static String getCurrentPeriodLabel() {
        return switch (getCurrentPeriod()) {
            case DAWN      -> "Dawn";
            case MORNING   -> "Morning";
            case AFTERNOON -> "Afternoon";
            case EVENING   -> "Evening";
            case NIGHT     -> "Night";
        };
    }

    /**
     * Blends two colours together.
     * Useful for smooth background transitions between conditions.
     *
     * @param c1    first colour
     * @param c2    second colour
     * @param ratio how much of c1 to use (0.0 = all c2, 1.0 = all c1)
     * @return blended color
     */
    public static Color blend(Color c1, Color c2, double ratio) {
        double inv = 1.0 - ratio;
        int r = (int) (c1.getRed()   * ratio + c2.getRed()   * inv);
        int g = (int) (c1.getGreen() * ratio + c2.getGreen() * inv);
        int b = (int) (c1.getBlue()  * ratio + c2.getBlue()  * inv);
        return new Color(
                Math.min(255, Math.max(0, r)),
                Math.min(255, Math.max(0, g)),
                Math.min(255, Math.max(0, b))
        );
    }
}
