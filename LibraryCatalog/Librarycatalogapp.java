import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * LibraryCatalogApp.java
 * Command-line interface for interacting with the generic library catalog.
 * Supports adding, removing, retrieving, and displaying library items.
 */
public class LibraryCatalogApp {

    // Scanner for reading user input from the console
    private static final Scanner scanner = new Scanner(System.in);

    // The main catalog that stores LibraryItems with Integer IDs
    private static final Catalog<Integer> catalog = new Catalog<>("City Library Catalog");

    // ----------------------------------------------------------------
    //  ENTRY POINT
    // ----------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Welcome to the Generic Library Catalog   ");
        System.out.println("============================================");

        boolean running = true;

        while (running) {
            printMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> retrieveItem();
                case 4 -> catalog.displayAllItems();
                case 5 -> {
                    System.out.println("\nThank you for using the Library Catalog. Goodbye!");
                    running = false;
                }
                default -> System.out.println("[ERROR] Invalid choice. Please enter a number between 1 and 5.");
            }
        }

        scanner.close();
    }

    // ----------------------------------------------------------------
    //  MENU
    // ----------------------------------------------------------------

    /** Prints the main menu options to the console. */
    private static void printMenu() {
        System.out.println("\n-------- Main Menu --------");
        System.out.println("1. Add a new library item");
        System.out.println("2. Remove a library item");
        System.out.println("3. Retrieve item details");
        System.out.println("4. View all catalog items");
        System.out.println("5. Exit");
        System.out.println("---------------------------");
    }

    // ----------------------------------------------------------------
    //  ADD
    // ----------------------------------------------------------------

    /** Prompts the user to enter details for a new item and adds it to the catalog. */
    private static void addItem() {
        System.out.println("\n-- Add New Item --");

        int    id       = readIntInput("Enter Item ID (integer): ");
        String title    = readStringInput("Enter Title            : ");
        String author   = readStringInput("Enter Author/Creator   : ");

        System.out.println("Select Category:");
        System.out.println("  1. Book");
        System.out.println("  2. DVD");
        System.out.println("  3. Magazine");
        System.out.println("  4. Other");
        int catChoice = readIntInput("Enter category choice  : ");

        String category = switch (catChoice) {
            case 1  -> "Book";
            case 2  -> "DVD";
            case 3  -> "Magazine";
            default -> "Other";
        };

        try {
            LibraryItem<Integer> newItem = new LibraryItem<>(id, title, author, category);
            catalog.addItem(newItem);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    //  REMOVE
    // ----------------------------------------------------------------

    /** Prompts the user for an item ID and removes the matching item. */
    private static void removeItem() {
        System.out.println("\n-- Remove Item --");
        int id = readIntInput("Enter Item ID to remove: ");

        try {
            catalog.removeItem(id);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    //  RETRIEVE
    // ----------------------------------------------------------------

    /** Prompts the user for an item ID and displays its details. */
    private static void retrieveItem() {
        System.out.println("\n-- Retrieve Item --");
        int id = readIntInput("Enter Item ID to retrieve: ");

        try {
            catalog.retrieveItem(id);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    //  INPUT HELPERS
    // ----------------------------------------------------------------

    /**
     * Reads an integer from the console, re-prompting on invalid input.
     *
     * @param prompt The message to display before reading input
     * @return A valid integer entered by the user
     */
    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume leftover newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear invalid input
                System.out.println("[ERROR] Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a non-empty string from the console, re-prompting if blank.
     *
     * @param prompt The message to display before reading input
     * @return A non-empty string entered by the user
     */
    private static String readStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("[ERROR] Input cannot be empty. Please try again.");
        }
    }
}
