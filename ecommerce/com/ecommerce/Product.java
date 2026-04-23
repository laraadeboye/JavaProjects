package com.ecommerce;

/**
 * Represents a product available for purchase in the online store.
 */
public class Product {

    private int productID;
    private String name;
    private double price;
    private int stockQuantity;

    public Product(int productID, String name, double price, int stockQuantity) {
        if (productID <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Product name cannot be empty.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        if (stockQuantity < 0) throw new IllegalArgumentException("Stock quantity cannot be negative.");

        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Product name cannot be empty.");
        this.name = name;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) throw new IllegalArgumentException("Stock quantity cannot be negative.");
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity > stockQuantity) throw new IllegalStateException("Insufficient stock for: " + name);
        this.stockQuantity -= quantity;
    }

    @Override
    public String toString() {
        return String.format("  [ID: %d] %-25s $%.2f   (Stock: %d)",
                productID, name, price, stockQuantity);
    }
}
