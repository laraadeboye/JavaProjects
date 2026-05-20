import java.util.ArrayList;
import java.util.List;

/**
 * Catalog.java
 * A generic catalog class that stores and manages library items.
 * Works with any subtype of LibraryItem<T>.
 *
 * @param <T> The type of the itemID used by the stored LibraryItem objects
 */
public class Catalog<T> {

    // Internal list that holds all library items in the catalog
    private List<LibraryItem<T>> items;

    // Name/label for this catalog instance
    private String catalogName;

    /**
     * Constructor to initialize an empty catalog with a given name.
     *
     * @param catalogName The name of this catalog
     */
    public Catalog(String catalogName) {
        this.catalogName = catalogName;
        this.items       = new ArrayList<>();
    }

    // ----------------------------------------------------------------
    //  ADD
    // ----------------------------------------------------------------

    /**
     * Adds a new LibraryItem to the catalog.
     * Rejects the item if an item with the same ID already exists.
     *
     * @param item The LibraryItem to add
     * @throws IllegalArgumentException if an item with the same ID exists
     */
    public void addItem(LibraryItem<T> item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to the catalog.");
        }

        // Check for duplicate IDs
        for (LibraryItem<T> existing : items) {
            if (existing.getItemID().equals(item.getItemID())) {
                throw new IllegalArgumentException(
                    "Item with ID '" + item.getItemID() + "' already exists in the catalog."
                );
            }
        }

        items.add(item);
        System.out.println("[SUCCESS] Added: \"" + item.getTitle() + "\" (ID: " + item.getItemID() + ")");
    }

    // ----------------------------------------------------------------
    //  REMOVE
    // ----------------------------------------------------------------

    /**
     * Removes a LibraryItem from the catalog by its ID.
     *
     * @param itemID The ID of the item to remove
     * @throws IllegalArgumentException if no item with that ID exists
     */
    public void removeItem(T itemID) {
        LibraryItem<T> target = findByID(itemID);

        if (target == null) {
            throw new IllegalArgumentException(
                "Item with ID '" + itemID + "' does not exist in the catalog."
            );
        }

        items.remove(target);
        System.out.println("[SUCCESS] Removed: \"" + target.getTitle() + "\" (ID: " + itemID + ")");
    }

    // ----------------------------------------------------------------
    //  RETRIEVE
    // ----------------------------------------------------------------

    /**
     * Retrieves and prints the details of a specific item by ID.
     *
     * @param itemID The ID of the item to retrieve
     * @throws IllegalArgumentException if no item with that ID exists
     */
    public void retrieveItem(T itemID) {
        LibraryItem<T> item = findByID(itemID);

        if (item == null) {
            throw new IllegalArgumentException(
                "Item with ID '" + itemID + "' does not exist in the catalog."
            );
        }

        System.out.println("\n--- Item Details ---");
        System.out.println(item);
        System.out.println("--------------------");
    }

    // ----------------------------------------------------------------
    //  VIEW ALL
    // ----------------------------------------------------------------

    /**
     * Displays all items currently stored in the catalog.
     * Shows a message if the catalog is empty.
     */
    public void displayAllItems() {
        System.out.println("\n===== " + catalogName + " =====");

        if (items.isEmpty()) {
            System.out.println("  (No items in the catalog)");
        } else {
            int counter = 1;
            for (LibraryItem<T> item : items) {
                System.out.println("\n[" + counter + "]");
                System.out.println(item);
                counter++;
            }
        }

        System.out.println("=".repeat(catalogName.length() + 12));
        System.out.println("Total items: " + items.size());
    }

    // ----------------------------------------------------------------
    //  HELPER
    // ----------------------------------------------------------------

    /**
     * Internal helper: searches the catalog for an item by its ID.
     *
     * @param itemID The ID to search for
     * @return The matching LibraryItem, or null if not found
     */
    private LibraryItem<T> findByID(T itemID) {
        for (LibraryItem<T> item : items) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }

    /** @return The number of items in the catalog */
    public int size() {
        return items.size();
    }

    /** @return The name of this catalog */
    public String getCatalogName() {
        return catalogName;
    }
}
