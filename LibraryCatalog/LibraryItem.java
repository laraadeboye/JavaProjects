/**
 * LibraryItem.java
 * A generic class representing an item in the library catalog.
 *
 * @param <T> The type of the itemID (e.g., Integer, String)
 */
public class LibraryItem<T> {

    // Unique identifier for the library item
    private T itemID;

    // Title of the library item
    private String title;

    // Author or creator of the library item
    private String author;

    // Category/type of the item (e.g., "Book", "DVD", "Magazine")
    private String category;

    /**
     * Constructor to initialize a LibraryItem with all attributes.
     *
     * @param itemID   Unique identifier for the item
     * @param title    Title of the item
     * @param author   Author or creator of the item
     * @param category Category/type of the item
     */
    public LibraryItem(T itemID, String title, String author, String category) {
        this.itemID   = itemID;
        this.title    = title;
        this.author   = author;
        this.category = category;
    }

    // ----------------------------- Getters -----------------------------

    /** @return The unique ID of this item */
    public T getItemID() {
        return itemID;
    }

    /** @return The title of this item */
    public String getTitle() {
        return title;
    }

    /** @return The author/creator of this item */
    public String getAuthor() {
        return author;
    }

    /** @return The category/type of this item */
    public String getCategory() {
        return category;
    }

    // ----------------------------- Setters -----------------------------

    /** @param title New title for this item */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @param author New author for this item */
    public void setAuthor(String author) {
        this.author = author;
    }

    /** @param category New category for this item */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns a formatted string representation of the library item.
     *
     * @return Formatted item details
     */
    @Override
    public String toString() {
        return String.format(
            "  ID       : %s%n  Title    : %s%n  Author   : %s%n  Category : %s",
            itemID, title, author, category
        );
    }
}
