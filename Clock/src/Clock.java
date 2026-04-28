import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clock class responsible for managing and displaying the current time and date.
 * Uses two threads:
 *   - A background updater thread (lower priority) to fetch the current time.
 *   - A display thread (higher priority) to print the time to the console.
 */
public class Clock {

    // Shared variable holding the latest formatted time string
    private volatile String currentTime;

    // Formatter for readable output: e.g., "14:05:37 27-04-2026"
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    // Controls whether threads keep running
    private volatile boolean running;

    /**
     * Updates currentTime with the latest system time.
     * Called repeatedly by the updater thread.
     */
    private void updateTime() {
        currentTime = LocalDateTime.now().format(FORMATTER);
    }

    /**
     * Prints the current time to the console.
     * Called repeatedly by the display thread.
     */
    private void displayTime() {
        if (currentTime != null) {
            // "\r" keeps output on the same line for a live-clock effect
            System.out.print("\rCurrent Time: " + currentTime);
        }
    }

    /**
     * Starts the clock by launching the updater and display threads.
     * The display thread is assigned a higher priority for timekeeping precision.
     */
    public void start() {
        running = true;

        // Background thread: fetches and updates the current time
        Thread updaterThread = new Thread(() -> {
            while (running) {
                updateTime();
                try {
                    Thread.sleep(100); // Update every 100ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Updater thread interrupted: " + e.getMessage());
                }
            }
        }, "TimeUpdaterThread");

        // Display thread: prints the current time to the console
        Thread displayThread = new Thread(() -> {
            while (running) {
                displayTime();
                try {
                    Thread.sleep(1000); // Refresh display every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Display thread interrupted: " + e.getMessage());
                }
            }
        }, "TimeDisplayThread");

        // Assign thread priorities
        // Display thread gets MAX priority for precise, timely output
        displayThread.setPriority(Thread.MAX_PRIORITY);   // Priority 10
        // Updater thread gets normal (default) priority
        updaterThread.setPriority(Thread.NORM_PRIORITY);  // Priority 5

        System.out.println("Clock started.");
        System.out.printf("  %-20s priority: %d%n", updaterThread.getName(), updaterThread.getPriority());
        System.out.printf("  %-20s priority: %d%n%n", displayThread.getName(), displayThread.getPriority());

        // Start both threads
        updaterThread.start();
        displayThread.start();
    }

    /**
     * Main entry point. Creates and starts the Clock, running for 10 seconds.
     */
    public static void main(String[] args) {
        Clock clock = new Clock();
        clock.start();

        // Let the clock run for 10 seconds then stop
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted: " + e.getMessage());
        }

        clock.running = false;
        System.out.println("\n\nClock stopped.");
    }
}
