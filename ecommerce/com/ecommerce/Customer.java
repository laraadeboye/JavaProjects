package com.ecommerce;

import com.ecommerce.orders.Order;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer of the online store, including their shopping cart
 * and order history.
 */
public class Customer {

    private int customerID;
    private String name;
    private String email;
    private List<Product> shoppingCart;
    private List<Order> orderHistory;

    public Customer(int customerID, String name, String email) {
        if (customerID <= 0) throw new IllegalArgumentException("Customer ID must be positive.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Customer name cannot be empty.");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email address.");

        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.shoppingCart = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    // Getters
    public int getCustomerID() { return customerID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Product> getShoppingCart() { return new ArrayList<>(shoppingCart); }
    public List<Order> getOrderHistory() { return new ArrayList<>(orderHistory); }

    /**
     * Adds a product to the customer's shopping cart.
     */
    public void addToCart(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        if (!product.isInStock()) throw new IllegalStateException("'" + product.getName() + "' is out of stock.");
        shoppingCart.add(product);
        System.out.println("  Added \"" + product.getName() + "\" to " + name + "'s cart.");
    }

    /**
     * Removes a product from the shopping cart by product ID.
     */
    public void removeFromCart(int productID) {
        Product toRemove = null;
        for (Product p : shoppingCart) {
            if (p.getProductID() == productID) {
                toRemove = p;
                break;
            }
        }
        if (toRemove == null) throw new IllegalArgumentException("Product ID " + productID + " not found in cart.");
        shoppingCart.remove(toRemove);
        System.out.println("  Removed \"" + toRemove.getName() + "\" from cart.");
    }

    /**
     * Calculates the total cost of all items in the shopping cart.
     */
    public double calculateCartTotal() {
        double total = 0;
        for (Product p : shoppingCart) {
            total += p.getPrice();
        }
        return total;
    }

    /**
     * Places an order for all items currently in the shopping cart.
     */
    public Order placeOrder(int orderID) {
        if (shoppingCart.isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart.");
        }

        List<Product> orderedProducts = new ArrayList<>(shoppingCart);
        double total = calculateCartTotal();

        // Reduce stock for each ordered product
        for (Product p : orderedProducts) {
            p.reduceStock(1);
        }

        Order newOrder = new Order(orderID, this, orderedProducts, total);
        orderHistory.add(newOrder);
        shoppingCart.clear();

        System.out.println("  Order #" + orderID + " placed successfully for " + name + "!");
        return newOrder;
    }

    /**
     * Displays the current contents and total of the shopping cart.
     */
    public void displayCart() {
        System.out.println("\n  Shopping Cart for " + name + ":");
        if (shoppingCart.isEmpty()) {
            System.out.println("    (Cart is empty)");
            return;
        }
        for (Product p : shoppingCart) {
            System.out.printf("    - %-25s $%.2f%n", p.getName(), p.getPrice());
        }
        System.out.printf("    %-25s $%.2f%n", "TOTAL:", calculateCartTotal());
    }

    @Override
    public String toString() {
        return String.format("Customer [ID: %d | Name: %s | Email: %s | Cart Items: %d | Orders: %d]",
                customerID, name, email, shoppingCart.size(), orderHistory.size());
    }
}
