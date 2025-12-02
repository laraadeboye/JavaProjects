import java.util.Scanner;
import java.util.ArrayList;

/**
 * Student Class
 * Encapsulates student information with proper access modifiers
 * Uses private instance variables to ensure data encapsulation
 */
class Student {
    // Private instance variables for student information
    private String name;
    private String studentId;
    private int age;
    private String grade;
    
    /**
     * Constructor to initialize a new Student object
     * @param name Student's full name
     * @param studentId Unique student identifier
     * @param age Student's age
     * @param grade Student's current grade/class
     */
    public Student(String name, String studentId, int age, String grade) {
        this.name = name;
        this.studentId = studentId;
        this.age = age;
        this.grade = grade;
    }
    
    // Public getter methods to access private variables
    public String getName() {
        return name;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getGrade() {
        return grade;
    }
    
    // Public setter methods to modify private variables
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    /**
     * Displays complete student information in a formatted manner
     */
    public void displayInfo() {
        System.out.println("\n========================================");
        System.out.println("       STUDENT INFORMATION");
        System.out.println("========================================");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name:       " + name);
        System.out.println("Age:        " + age);
        System.out.println("Grade:      " + grade);
        System.out.println("========================================");
    }
}

/**
 * StudentManagement Class
 * Handles all student management operations using static methods
 * Maintains a central repository of all student records
 */
class StudentManagement {
    // Private static variables to store student data
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static int totalStudents = 0;
    
    /**
     * Adds a new student to the management system
     * @param name Student's name
     * @param studentId Unique student ID
     * @param age Student's age
     * @param grade Student's grade
     * @throws IllegalArgumentException if student ID already exists
     */
    public static void addStudent(String name, String studentId, int age, String grade) {
        // Check if student ID already exists
        if (findStudentById(studentId) != null) {
            throw new IllegalArgumentException("Error: Student ID already exists!");
        }
        
        // Validate age
        if (age < 5 || age > 100) {
            throw new IllegalArgumentException("Error: Age must be between 5 and 100!");
        }
        
        // Validate name length
        if (name.length() > 100) {
            throw new IllegalArgumentException("Error: Name cannot exceed 100 characters!");
        }
        
        // Validate grade length
        if (grade.length() > 20) {
            throw new IllegalArgumentException("Error: Grade cannot exceed 20 characters!");
        }
        
        // Create and add new student
        Student newStudent = new Student(name, studentId, age, grade);
        studentList.add(newStudent);
        totalStudents++;
        
        System.out.println("\nStudent added successfully!");
        System.out.println("Total students in system: " + totalStudents);
    }
    
    /**
     * Updates existing student information
     * @param studentId ID of the student to update
     * @param name New name (null to keep existing)
     * @param age New age (-1 to keep existing)
     * @param grade New grade (null to keep existing)
     * @throws IllegalArgumentException if student not found
     */
    public static void updateStudent(String studentId, String name, int age, String grade) {
        Student student = findStudentById(studentId);
        
        if (student == null) {
            throw new IllegalArgumentException("Error: Student ID not found!");
        }
        
        // Update only non-null/non-default values
        if (name != null && !name.trim().isEmpty()) {
            if (name.length() > 100) {
                throw new IllegalArgumentException("Error: Name cannot exceed 100 characters!");
            }
            student.setName(name);
        }
        
        if (age != -1) {
            if (age < 5 || age > 100) {
                throw new IllegalArgumentException("Error: Age must be between 5 and 100!");
            }
            student.setAge(age);
        }
        
        if (grade != null && !grade.trim().isEmpty()) {
            if (grade.length() > 20) {
                throw new IllegalArgumentException("Error: Grade cannot exceed 20 characters!");
            }
            student.setGrade(grade);
        }
        
        System.out.println("\nStudent information updated successfully!");
    }
    
    /**
     * Retrieves and displays information for a specific student
     * @param studentId ID of the student to view
     * @throws IllegalArgumentException if student not found
     */
    public static void viewStudent(String studentId) {
        Student student = findStudentById(studentId);
        
        if (student == null) {
            throw new IllegalArgumentException("Error: Student ID not found!");
        }
        
        student.displayInfo();
    }
    
    /**
     * Displays all students in the system
     */
    public static void viewAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("\nNo students in the system.");
            return;
        }
        
        System.out.println("\n========================================");
        System.out.println("     ALL STUDENTS IN SYSTEM");
        System.out.println("========================================");
        System.out.printf("%-12s %-20s %-8s %-15s%n", "Student ID", "Name", "Age", "Grade");
        System.out.println("----------------------------------------");
        
        for (Student student : studentList) {
            System.out.printf("%-12s %-20s %-8d %-15s%n", 
                student.getStudentId(), 
                student.getName(), 
                student.getAge(), 
                student.getGrade());
        }
        
        System.out.println("========================================");
        System.out.println("Total Students: " + totalStudents);
    }
    
    /**
     * Helper method to find a student by ID
     * @param studentId ID to search for
     * @return Student object if found, null otherwise
     */
    private static Student findStudentById(String studentId) {
        for (Student student : studentList) {
            if (student.getStudentId().equalsIgnoreCase(studentId)) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Returns the total number of students in the system
     * @return Total student count
     */
    public static int getTotalStudents() {
        return totalStudents;
    }
    
    /**
     * Deletes a student from the system
     * @param studentId ID of student to delete
     * @throws IllegalArgumentException if student not found
     */
    public static void deleteStudent(String studentId) {
        Student student = findStudentById(studentId);
        
        if (student == null) {
            throw new IllegalArgumentException("Error: Student ID not found!");
        }
        
        studentList.remove(student);
        totalStudents--;
        System.out.println("\nStudent deleted successfully!");
        System.out.println("Total students in system: " + totalStudents);
    }
}

/**
 * AdministratorInterface Class
 * Main class that provides the user interface for administrators
 * Handles menu display and user input processing
 */
public class AdministratorInterface {
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        displayWelcomeMessage();
        
        boolean exit = false;
        
        while (!exit) {
            try {
                displayMenu();
                int choice = getMenuChoice();
                
                switch (choice) {
                    case 1:
                        addNewStudent();
                        break;
                    case 2:
                        updateStudentInfo();
                        break;
                    case 3:
                        viewStudentDetails();
                        break;
                    case 4:
                        viewAllStudents();
                        break;
                    case 5:
                        deleteStudent();
                        break;
                    case 6:
                        exit = true;
                        displayExitMessage();
                        break;
                    default:
                        System.out.println("\nInvalid choice! Please select 1-6.");
                }
                
                if (!exit) {
                    pressEnterToContinue();
                }
                
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
                scanner.nextLine(); // Clear buffer
                pressEnterToContinue();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Displays welcome message
     */
    private static void displayWelcomeMessage() {
        System.out.println("\n========================================");
        System.out.println("STUDENT RECORD MANAGEMENT SYSTEM v1.0");
        System.out.println("Welcome Administrator!");
        System.out.println("========================================\n");
    }
    
    /**
     * Displays the main menu
     */
    private static void displayMenu() {
        System.out.println("\n--- ADMINISTRATOR MENU ---");
        System.out.println("1. Add New Student");
        System.out.println("2. Update Student Information");
        System.out.println("3. View Student Details");
        System.out.println("4. View All Students");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit System");
        System.out.println("--------------------------");
    }
    
    /**
     * Gets menu choice from user with error handling
     * @return Valid menu choice (1-6)
     */
    private static int getMenuChoice() {
        System.out.print("\nEnter your choice (1-6): ");
        
        if (!scanner.hasNextInt()) {
            scanner.nextLine(); // Clear invalid input
            throw new IllegalArgumentException("Invalid input! Please enter a number.");
        }
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        return choice;
    }
    
    /**
     * Handles adding a new student
     */
    private static void addNewStudent() {
        System.out.println("\n========================================");
        System.out.println("       ADD NEW STUDENT");
        System.out.println("========================================");
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty!");
        }
        
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty!");
        }
        
        System.out.print("Enter Student Age: ");
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            throw new IllegalArgumentException("Age must be a number!");
        }
        int age = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter Student Grade: ");
        String grade = scanner.nextLine().trim();
        
        if (grade.isEmpty()) {
            throw new IllegalArgumentException("Grade cannot be empty!");
        }
        
        StudentManagement.addStudent(name, studentId, age, grade);
    }
    
    /**
     * Handles updating student information
     */
    private static void updateStudentInfo() {
        System.out.println("\n========================================");
        System.out.println("   UPDATE STUDENT INFORMATION");
        System.out.println("========================================");
        
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine().trim();
        
        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty!");
        }
        
        System.out.println("\nLeave field blank to keep existing value");
        
        System.out.print("Enter new name (or press Enter to skip): ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter new age (or -1 to skip): ");
        int age = -1;
        if (scanner.hasNextInt()) {
            age = scanner.nextInt();
        }
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter new grade (or press Enter to skip): ");
        String grade = scanner.nextLine().trim();
        
        StudentManagement.updateStudent(studentId, name, age, grade);
    }
    
    /**
     * Handles viewing individual student details
     */
    private static void viewStudentDetails() {
        System.out.println("\n========================================");
        System.out.println("      VIEW STUDENT DETAILS");
        System.out.println("========================================");
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty!");
        }
        
        StudentManagement.viewStudent(studentId);
    }
    
    /**
     * Handles viewing all students
     */
    private static void viewAllStudents() {
        StudentManagement.viewAllStudents();
    }
    
    /**
     * Handles deleting a student
     */
    private static void deleteStudent() {
        System.out.println("\n========================================");
        System.out.println("        DELETE STUDENT");
        System.out.println("========================================");
        
        System.out.print("Enter Student ID to delete: ");
        String studentId = scanner.nextLine().trim();
        
        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty!");
        }
        
        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            StudentManagement.deleteStudent(studentId);
        } else {
            System.out.println("\nDeletion cancelled.");
        }
    }
    
    /**
     * Displays exit message
     */
    private static void displayExitMessage() {
        System.out.println("\n========================================");
        System.out.println("Thank you for using the system!");
        System.out.println("Total students managed: " + StudentManagement.getTotalStudents());
        System.out.println("Goodbye!");
        System.out.println("========================================");
    }
    
    /**
     * Pauses execution until user presses Enter
     */
    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}