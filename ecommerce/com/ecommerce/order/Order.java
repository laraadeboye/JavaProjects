package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a placed order in the e-commerce system.
 */
public class Order {

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    private int orderID;
    private Customer customer;
    private List<Product> products;
    private double orderTotal;
    private OrderStatus status;
    private String orderDate;

    public Order(int orderID, Customer customer, List<Product> products, double orderTotal) {
        if (orderID <= 0) throw new IllegalArgumentException("Order ID must be positive.");
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null.");
        if (products == null || products.isEmpty()) throw new IllegalArgumentException("Order must contain at least one product.");
        if (orderTotal < 0) throw new IllegalArgumentException("Order total cannot be negative.");

        this.orderID = orderID;
        this.customer = customer;
        this.products = new ArrayList<>(products);
        this.orderTotal = orderTotal;
        this.status = OrderStatus.PENDING;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.orderDate = LocalDateTime.now().format(formatter);
    }

    // Getters
    public int getOrderID() { return orderID; }
    public Customer getCustomer() { return customer; }
    public List<Product> getProducts() { return new ArrayList<>(products); }
    public double getOrderTotal() { return orderTotal; }
    public OrderStatus getStatus() { return status; }
    public String getOrderDate() { return orderDate; }

    /**
     * Updates the order status with validation to prevent invalid transitions.
     */
    public void updateStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled order.");
        }
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update an already delivered order.");
        }
        System.out.println("  Order #" + orderID + " status updated: " + this.status + " -> " + newStatus);
        this.status = newStatus;
    }

    /**
     * Cancels the order if it has not yet been shipped.
     */
    public void cancelOrder() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has been shipped or delivered.");
        }
        this.status = OrderStatus.CANCELLED;
        System.out.println("  Order #" + orderID + " has been cancelled.");
    }

    /**
     * Generates a formatted summary of the order.
     */
    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  +-----------------------------------------+\n");
        sb.append(  "  |           ORDER SUMMARY                 |\n");
        sb.append(  "  +-----------------------------------------+\n");
        sb.append(String.format("  | Order ID   : %-27d|\n", orderID));
        sb.append(String.format("  | Date       : %-27s|\n", orderDate));
        sb.append(String.format("  | Customer   : %-27s|\n", customer.getName()));
        sb.append(String.format("  | Status     : %-27s|\n", status));
        sb.append(  "  +-----------------------------------------+\n");
        sb.append(  "  | Items Ordered:                          |\n");
        for (Product p : products) {
            sb.append(String.format("  |   - %-30s $%6.2f |\n", p.getName(), p.getPrice()));
        }
        sb.append(  "  +-----------------------------------------+\n");
        sb.append(String.format("  | ORDER TOTAL: %27s |\n", String.format("$%.2f", orderTotal)));
        sb.append(  "  +-----------------------------------------+\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Order [ID: %d | Customer: %s | Items: %d | Total: $%.2f | Status: %s]",
                orderID, customer.getName(), products.size(), orderTotal, status);
    }
}
