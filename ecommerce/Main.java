import com.ecommerce.Customer;
import com.ecommerce.Product;
import com.ecommerce.orders.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Main program demonstrating the functionality of the e-commerce system.
 * Shows product browsing, cart management, order placement, and status updates.
 */
public class Main {

    // ---------------------------------------------------------------
    // Helper: display a section header
    // ---------------------------------------------------------------
    private static void section(String title) {
        System.out.println("\n========================================");
        System.out.println("  " + title);
        System.out.println("========================================");
    }

    // ---------------------------------------------------------------
    // Helper: display all available products
    // ---------------------------------------------------------------
    private static void browseProducts(List<Product> catalog) {
        System.out.println("\n  Available Products:");
        System.out.println("  " + "-".repeat(52));
        System.out.printf("  %-5s %-25s %-10s %-8s%n", "ID", "Name", "Price", "Stock");
        System.out.println("  " + "-".repeat(52));
        for (Product p : catalog) {
            System.out.printf("  %-5d %-25s $%-9.2f %-8d%n",
                    p.getProductID(), p.getName(), p.getPrice(), p.getStockQuantity());
        }
        System.out.println("  " + "-".repeat(52));
    }

    // ---------------------------------------------------------------
    // Helper: find a product by ID
    // ---------------------------------------------------------------
    private static Product findProduct(List<Product> catalog, int id) {
        for (Product p : catalog) {
            if (p.getProductID() == id) return p;
        }
        throw new IllegalArgumentException("Product with ID " + id + " not found.");
    }

    // ---------------------------------------------------------------
    // Main
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        // ==============================================================
        // STEP 1: Build the product catalog
        // ==============================================================
        section("STEP 1: Product Catalog Setup");

        List<Product> catalog = new ArrayList<>();
        catalog.add(new Product(101, "Wireless Headphones",  79.99, 15));
        catalog.add(new Product(102, "Mechanical Keyboard",  49.99, 10));
        catalog.add(new Product(103, "USB-C Hub",            29.99, 25));
        catalog.add(new Product(104, "Webcam HD 1080p",      59.99,  8));
        catalog.add(new Product(105, "LED Desk Lamp",        24.99, 20));
        catalog.add(new Product(106, "Laptop Stand",         34.99, 12));

        System.out.println("  Product catalog created with " + catalog.size() + " items.");

        // ==============================================================
        // STEP 2: Create customers
        // ==============================================================
        section("STEP 2: Customer Registration");

        Customer alice = new Customer(1, "Alice Johnson", "alice@example.com");
        Customer bob   = new Customer(2, "Bob Smith",     "bob@example.com");

        System.out.println("  Registered: " + alice);
        System.out.println("  Registered: " + bob);

        // ==============================================================
        // STEP 3: Browse products
        // ==============================================================
        section("STEP 3: Browsing Products");
        browseProducts(catalog);

        // ==============================================================
        // STEP 4: Alice shops - add items to cart
        // ==============================================================
        section("STEP 4: Alice Adds Items to Her Cart");

        try {
            alice.addToCart(findProduct(catalog, 101)); // Wireless Headphones
            alice.addToCart(findProduct(catalog, 103)); // USB-C Hub
            alice.addToCart(findProduct(catalog, 106)); // Laptop Stand
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("  Cart Error: " + e.getMessage());
        }

        alice.displayCart();

        // ==============================================================
        // STEP 5: Alice removes an item
        // ==============================================================
        section("STEP 5: Alice Removes an Item from Cart");

        try {
            alice.removeFromCart(103); // Remove USB-C Hub
        } catch (IllegalArgumentException e) {
            System.out.println("  Remove Error: " + e.getMessage());
        }

        alice.displayCart();

        // ==============================================================
        // STEP 6: Bob shops
        // ==============================================================
        section("STEP 6: Bob Adds Items to His Cart");

        try {
            bob.addToCart(findProduct(catalog, 102)); // Mechanical Keyboard
            bob.addToCart(findProduct(catalog, 104)); // Webcam HD
            bob.addToCart(findProduct(catalog, 105)); // LED Desk Lamp
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("  Cart Error: " + e.getMessage());
        }

        bob.displayCart();

        // ==============================================================
        // STEP 7: Place orders
        // ==============================================================
        section("STEP 7: Placing Orders");

        Order aliceOrder = null;
        Order bobOrder   = null;

        try {
            aliceOrder = alice.placeOrder(1001);
        } catch (IllegalStateException e) {
            System.out.println("  Order Error (Alice): " + e.getMessage());
        }

        try {
            bobOrder = bob.placeOrder(1002);
        } catch (IllegalStateException e) {
            System.out.println("  Order Error (Bob): " + e.getMessage());
        }

        // ==============================================================
        // STEP 8: Display order summaries
        // ==============================================================
        section("STEP 8: Order Summaries");

        if (aliceOrder != null) System.out.println(aliceOrder.generateSummary());
        if (bobOrder   != null) System.out.println(bobOrder.generateSummary());

        // ==============================================================
        // STEP 9: Update order statuses
        // ==============================================================
        section("STEP 9: Order Status Updates");

        try {
            if (aliceOrder != null) {
                aliceOrder.updateStatus(Order.OrderStatus.CONFIRMED);
                aliceOrder.updateStatus(Order.OrderStatus.SHIPPED);
                aliceOrder.updateStatus(Order.OrderStatus.DELIVERED);
            }
            if (bobOrder != null) {
                bobOrder.updateStatus(Order.OrderStatus.CONFIRMED);
                bobOrder.cancelOrder(); // Bob decides to cancel
            }
        } catch (IllegalStateException e) {
            System.out.println("  Status Error: " + e.getMessage());
        }

        // ==============================================================
        // STEP 10: Final order info
        // ==============================================================
        section("STEP 10: Final Order Info");

        if (aliceOrder != null) System.out.println("  " + aliceOrder);
        if (bobOrder   != null) System.out.println("  " + bobOrder);

        // ==============================================================
        // STEP 11: Updated catalog (stock levels after purchases)
        // ==============================================================
        section("STEP 11: Updated Product Catalog (After Orders)");
        browseProducts(catalog);

        // ==============================================================
        // STEP 12: Input validation demonstration
        // ==============================================================
        section("STEP 12: Input Validation Demonstration");

        System.out.println("\n  Testing invalid Product creation (negative price):");
        try {
            Product badProduct = new Product(999, "Bad Item", -10.00, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("    Caught: " + e.getMessage());
        }

        System.out.println("\n  Testing invalid Customer creation (bad email):");
        try {
            Customer badCustomer = new Customer(99, "No Email", "notanemail");
        } catch (IllegalArgumentException e) {
            System.out.println("    Caught: " + e.getMessage());
        }

        System.out.println("\n  Testing placing an order with an empty cart:");
        try {
            Customer emptyCartUser = new Customer(3, "Empty Eddie", "eddie@example.com");
            emptyCartUser.placeOrder(9999);
        } catch (IllegalStateException e) {
            System.out.println("    Caught: " + e.getMessage());
        }

        System.out.println("\n  Testing updating a cancelled order:");
        try {
            if (bobOrder != null) {
                bobOrder.updateStatus(Order.OrderStatus.SHIPPED);
            }
        } catch (IllegalStateException e) {
            System.out.println("    Caught: " + e.getMessage());
        }

        System.out.println("\n========================================");
        System.out.println("  E-Commerce System Demo Complete.");
        System.out.println("========================================\n");
    }
}
