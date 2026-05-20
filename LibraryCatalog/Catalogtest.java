/**
 * CatalogTest.java
 * Comprehensive tests for the generic Catalog and LibraryItem classes.
 * Tests cover: adding items, removing items, duplicate prevention,
 * removing non-existent items, retrieving items, and mixed category support.
 */
public class CatalogTest {

    // Counters for pass/fail tracking
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("       Running Library Catalog Tests        ");
        System.out.println("============================================\n");

        testAddItems();
        testDuplicateItemPrevention();
        testRemoveExistingItem();
        testRemoveNonExistentItem();
        testRetrieveExistingItem();
        testRetrieveNonExistentItem();
        testAddNullItem();
        testMixedCategories();
        testCatalogWithStringIDs();
        testDisplayEmptyCatalog();

        printSummary();
    }

    // ----------------------------------------------------------------
    //  TEST CASES
    // ----------------------------------------------------------------

    /** Test 1: Adding valid items increases catalog size. */
    private static void testAddItems() {
        System.out.println("Test 1: Add valid items");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        catalog.addItem(new LibraryItem<>(1, "Clean Code", "Robert C. Martin", "Book"));
        catalog.addItem(new LibraryItem<>(2, "Inception", "Christopher Nolan", "DVD"));

        assertEqual("Catalog size should be 2 after adding two items", 2, catalog.size());
    }

    /** Test 2: Adding an item with a duplicate ID should throw an exception. */
    private static void testDuplicateItemPrevention() {
        System.out.println("\nTest 2: Prevent duplicate IDs");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        catalog.addItem(new LibraryItem<>(1, "The Hobbit", "J.R.R. Tolkien", "Book"));

        try {
            catalog.addItem(new LibraryItem<>(1, "Duplicate Item", "Unknown", "Book"));
            fail("Should have thrown IllegalArgumentException for duplicate ID");
        } catch (IllegalArgumentException e) {
            pass("Correctly rejected duplicate ID: " + e.getMessage());
        }
    }

    /** Test 3: Removing an item that exists should succeed and reduce catalog size. */
    private static void testRemoveExistingItem() {
        System.out.println("\nTest 3: Remove an existing item");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        catalog.addItem(new LibraryItem<>(10, "Time Magazine", "Various", "Magazine"));
        catalog.removeItem(10);

        assertEqual("Catalog should be empty after removing the only item", 0, catalog.size());
    }

    /** Test 4: Removing a non-existent item should throw an exception with a clear message. */
    private static void testRemoveNonExistentItem() {
        System.out.println("\nTest 4: Remove non-existent item");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        try {
            catalog.removeItem(999);
            fail("Should have thrown IllegalArgumentException for non-existent item");
        } catch (IllegalArgumentException e) {
            pass("Correctly reported missing item: " + e.getMessage());
        }
    }

    /** Test 5: Retrieving an existing item should not throw an exception. */
    private static void testRetrieveExistingItem() {
        System.out.println("\nTest 5: Retrieve an existing item");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        catalog.addItem(new LibraryItem<>(5, "Dune", "Frank Herbert", "Book"));

        try {
            catalog.retrieveItem(5);
            pass("Successfully retrieved item with ID 5");
        } catch (IllegalArgumentException e) {
            fail("Should not have thrown exception: " + e.getMessage());
        }
    }

    /** Test 6: Retrieving a non-existent item should throw an exception. */
    private static void testRetrieveNonExistentItem() {
        System.out.println("\nTest 6: Retrieve non-existent item");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        try {
            catalog.retrieveItem(404);
            fail("Should have thrown IllegalArgumentException for non-existent item");
        } catch (IllegalArgumentException e) {
            pass("Correctly reported missing item on retrieve: " + e.getMessage());
        }
    }

    /** Test 7: Adding a null item should throw an exception. */
    private static void testAddNullItem() {
        System.out.println("\nTest 7: Add null item");
        Catalog<Integer> catalog = new Catalog<>("Test Catalog");

        try {
            catalog.addItem(null);
            fail("Should have thrown IllegalArgumentException for null item");
        } catch (IllegalArgumentException e) {
            pass("Correctly rejected null item: " + e.getMessage());
        }
    }

    /** Test 8: Catalog should work seamlessly with mixed categories (Book, DVD, Magazine). */
    private static void testMixedCategories() {
        System.out.println("\nTest 8: Mixed category items");
        Catalog<Integer> catalog = new Catalog<>("Mixed Catalog");

        catalog.addItem(new LibraryItem<>(101, "National Geographic", "Nat Geo Team", "Magazine"));
        catalog.addItem(new LibraryItem<>(102, "The Matrix",          "Wachowski",   "DVD"));
        catalog.addItem(new LibraryItem<>(103, "Effective Java",      "Joshua Bloch", "Book"));

        assertEqual("Catalog should contain 3 items of different categories", 3, catalog.size());
        catalog.displayAllItems();
    }

    /** Test 9: Catalog should work with String-based IDs as well. */
    private static void testCatalogWithStringIDs() {
        System.out.println("\nTest 9: Catalog with String IDs");
        Catalog<String> catalog = new Catalog<>("String-ID Catalog");

        catalog.addItem(new LibraryItem<>("BK-001", "Design Patterns", "Gang of Four", "Book"));
        catalog.addItem(new LibraryItem<>("DV-001", "Avatar",          "James Cameron", "DVD"));

        assertEqual("String-ID catalog should have 2 items", 2, catalog.size());

        try {
            catalog.removeItem("BK-001");
            assertEqual("After removal, size should be 1", 1, catalog.size());
        } catch (IllegalArgumentException e) {
            fail("Should not throw on removing existing String ID: " + e.getMessage());
        }
    }

    /** Test 10: Display on an empty catalog should not throw any exception. */
    private static void testDisplayEmptyCatalog() {
        System.out.println("\nTest 10: Display empty catalog");
        Catalog<Integer> catalog = new Catalog<>("Empty Catalog");

        try {
            catalog.displayAllItems();
            pass("displayAllItems() works on empty catalog without exception");
        } catch (Exception e) {
            fail("Should not throw on empty catalog display: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    //  ASSERT HELPERS
    // ----------------------------------------------------------------

    private static void assertEqual(String message, int expected, int actual) {
        if (expected == actual) {
            pass(message + " [expected=" + expected + ", actual=" + actual + "]");
        } else {
            fail(message + " [expected=" + expected + ", actual=" + actual + "]");
        }
    }

    private static void pass(String message) {
        System.out.println("  [PASS] " + message);
        passed++;
    }

    private static void fail(String message) {
        System.out.println("  [FAIL] " + message);
        failed++;
    }

    private static void printSummary() {
        System.out.println("\n============================================");
        System.out.println("              Test Summary                  ");
        System.out.println("============================================");
        System.out.println("  Tests Passed : " + passed);
        System.out.println("  Tests Failed : " + failed);
        System.out.println("  Total Tests  : " + (passed + failed));
        System.out.println("============================================");
    }
}
