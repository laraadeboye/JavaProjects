import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * LibrarySystem - A simple library management system
 * Allows users to add, borrow, and return books
 */
public class LibrarySystem {
    
    // Inner class to represent a Book with its details
    static class Book {
        private String title;
        private String author;
        private int quantity;
        
        // Constructor to initialize a book
        public Book(String title, String author, int quantity) {
            this.title = title;
            this.author = author;
            this.quantity = quantity;
        }
        
        // Getter methods
        public String getTitle() {
            return title;
        }
        
        public String getAuthor() {
            return author;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        // Setter method for quantity
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        // Method to add more copies of the book
        public void addQuantity(int amount) {
            this.quantity += amount;
        }
        
        @Override
        public String toString() {
            return "Title: " + title + ", Author: " + author + ", Quantity: " + quantity;
        }
    }
    
    // HashMap to store books with normalized title as key
    private Map<String, Book> library;
    private Scanner scanner;
    
    // Constructor to initialize the library system
    public LibrarySystem() {
        library = new HashMap<>();
        scanner = new Scanner(System.in);
    }
    
    /**
     * Helper method to normalize book titles for consistent lookup
     */
    private String normalizeTitle(String title) {
        return title.trim().toLowerCase();
    }
    
    /**
     * Main menu display
     */
    public void displayMenu() {
        System.out.println("\n========== LIBRARY MANAGEMENT SYSTEM ==========");
        System.out.println("1. Add Books");
        System.out.println("2. Borrow Books");
        System.out.println("3. Return Books");
        System.out.println("4. Display All Books");
        System.out.println("5. Exit");
        System.out.println("===============================================");
        System.out.print("Enter your choice: ");
    }
    
    /**
     * Method to add books to the library
     */
    public void addBooks() {
        try {
            System.out.println("\n--- Add Books ---");
            
            // Get book title
            System.out.print("Enter book title: ");
            String title = scanner.nextLine().trim();
            
            // Validate title input
            if (title.isEmpty()) {
                System.out.println("Error: Book title cannot be empty!");
                return;
            }
            
            // Get author name
            System.out.print("Enter author name: ");
            String author = scanner.nextLine().trim();
            
            // Validate author input
            if (author.isEmpty()) {
                System.out.println("Error: Author name cannot be empty!");
                return;
            }
            
            // Get quantity
            System.out.print("Enter quantity: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Quantity must be a valid number!");
                scanner.nextLine(); // Clear invalid input
                return;
            }
            
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after integer input
            
            // Validate quantity
            if (quantity <= 0) {
                System.out.println("Error: Quantity must be greater than 0!");
                return;
            }
            
            // Use normalized title as key
            String normalizedTitle = normalizeTitle(title);
            
            // Check if book already exists
            if (library.containsKey(normalizedTitle)) {
                Book existingBook = library.get(normalizedTitle);
                existingBook.addQuantity(quantity);
                System.out.println("Success: Book already exists. Updated quantity to " + existingBook.getQuantity());
            } else {
                // Add new book to library
                Book newBook = new Book(title, author, quantity);
                library.put(normalizedTitle, newBook);
                System.out.println("Success: New book added to the library!");
            }
            
            System.out.println(library.get(normalizedTitle));
            
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    /**
     * Method to borrow books from the library
     */
    public void borrowBooks() {
        try {
            System.out.println("\n--- Borrow Books ---");
            
            // Get book title
            System.out.print("Enter book title: ");
            String title = scanner.nextLine().trim();
            
            // Validate title input
            if (title.isEmpty()) {
                System.out.println("Error: Book title cannot be empty!");
                return;
            }
            
            // Use normalized title for lookup
            String normalizedTitle = normalizeTitle(title);
            
            // Check if book exists in library
            if (!library.containsKey(normalizedTitle)) {
                System.out.println("Error: Book not found in the library!");
                return;
            }
            
            Book book = library.get(normalizedTitle);
            
            // Get number of books to borrow
            System.out.print("Enter number of books to borrow: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Please enter a valid number!");
                scanner.nextLine(); // Clear invalid input
                return;
            }
            
            int borrowCount = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after integer input
            
            // Validate borrow count
            if (borrowCount <= 0) {
                System.out.println("Error: Number of books must be greater than 0!");
                return;
            }
            
            // Check if sufficient books are available
            if (borrowCount > book.getQuantity()) {
                System.out.println("Error: Insufficient books available!");
                System.out.println("Available quantity: " + book.getQuantity());
                return;
            }
            
            // Update quantity after borrowing
            book.setQuantity(book.getQuantity() - borrowCount);
            System.out.println("Success: You have borrowed " + borrowCount + " book(s)!");
            System.out.println("Remaining quantity: " + book.getQuantity());
            
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    /**
     * Method to return books to the library
     */
    public void returnBooks() {
        try {
            System.out.println("\n--- Return Books ---");
            
            // Get book title
            System.out.print("Enter book title: ");
            String title = scanner.nextLine().trim();
            
            // Validate title input
            if (title.isEmpty()) {
                System.out.println("Error: Book title cannot be empty!");
                return;
            }
            
            // Use normalized title for lookup
            String normalizedTitle = normalizeTitle(title);
            
            // Check if book exists in library system
            if (!library.containsKey(normalizedTitle)) {
                System.out.println("Error: This book does not belong to our library system!");
                return;
            }
            
            Book book = library.get(normalizedTitle);
            
            // Get number of books to return
            System.out.print("Enter number of books to return: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Please enter a valid number!");
                scanner.nextLine(); // Clear invalid input
                return;
            }
            
            int returnCount = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after integer input
            
            // Validate return count
            if (returnCount <= 0) {
                System.out.println("Error: Number of books must be greater than 0!");
                return;
            }
            
            // Update quantity after returning
            book.setQuantity(book.getQuantity() + returnCount);
            System.out.println("Success: You have returned " + returnCount + " book(s)!");
            System.out.println("Updated quantity: " + book.getQuantity());
            
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    /**
     * Method to display all books in the library
     */
    public void displayAllBooks() {
        System.out.println("\n--- Library Inventory ---");
        
        if (library.isEmpty()) {
            System.out.println("The library is currently empty.");
            return;
        }
        
        System.out.println("Total books in library: " + library.size());
        System.out.println("---------------------------------------");
        
        for (Book book : library.values()) {
            System.out.println(book);
        }
        
        System.out.println("---------------------------------------");
    }
    
    /**
     * Main method to run the library system
     */
    public void run() {
        boolean running = true;
        
        System.out.println("Welcome to the Library Management System!");
        
        while (running) {
            displayMenu();
            
            try {
                if (!scanner.hasNextInt()) {
                    System.out.println("Error: Please enter a valid option (1-5)!");
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline after integer input
                
                switch (choice) {
                    case 1:
                        addBooks();
                        break;
                    case 2:
                        borrowBooks();
                        break;
                    case 3:
                        returnBooks();
                        break;
                    case 4:
                        displayAllBooks();
                        break;
                    case 5:
                        System.out.println("\nThank you for using the Library Management System!");
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Error: Invalid choice! Please select between 1-5.");
                }
                
            } catch (Exception e) {
                System.out.println("Error: An unexpected error occurred. Please try again.");
                scanner.nextLine(); // Clear buffer
            }
        }
        
        scanner.close();
    }
    
    /**
     * Main entry point of the program
     */
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.run();
    }
}