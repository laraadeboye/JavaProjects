import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Employee Management System demonstrating Function interface and Streams API
 * 
 * Purpose: This program processes employee data using Java 8+ functional programming
 * features to efficiently filter, transform, and aggregate employee information.
 */
class Employee {
    private String name;
    private int age;
    private String department;
    private double salary;

    public Employee(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return String.format("Employee{name='%s', age=%d, department='%s', salary=%.2f}",
                name, age, department, salary);
    }
}

public class EmployeeStreamProcessor {
    
    // Age threshold constant for filtering
    private static final int AGE_THRESHOLD = 30;
    
    /**
     * Function Interface Implementation:
     * The Function interface represents a function that accepts one argument and produces a result.
     * It's a functional interface with a single abstract method: R apply(T t)
     * 
     * Characteristics:
     * - Takes input of type T and returns output of type R
     * - Can be used as lambda expressions or method references
     * - Supports composition through andThen() and compose() methods
     * - Enables declarative programming style
     */
    private static final Function<Employee, String> employeeNameDeptConcatenator = 
            employee -> employee.getName() + " - " + employee.getDepartment();
    

    
    /**
     * Predicate for filtering employees above age threshold
     * Demonstrates proper use of Predicate functional interface
     */
    private static final Predicate<Employee> ageAboveThreshold = 
            employee -> employee.getAge() > AGE_THRESHOLD;
    
    public static void main(String[] args) {
        // Step 1: Read dataset and store in collection
        List<Employee> employees = loadEmployeeDataset();
        
        System.out.println("=== Employee Management System ===\n");
        System.out.println("Total Employees: " + employees.size());
        System.out.println("\n--- Original Employee Dataset ---");
        employees.forEach(System.out::println);
        
        // Step 2 & 3: Use Function interface to concatenate name and department
        // and generate new collection using streams
        System.out.println("\n--- Employee Name-Department Concatenation ---");
        List<String> nameDeptList = employees.stream()
                .map(employeeNameDeptConcatenator)  // Apply Function interface
                .collect(Collectors.toList());       // Terminal operation
        
        nameDeptList.forEach(System.out::println);
        
        // Step 4: Calculate average salary using streams
        System.out.println("\n--- Salary Statistics ---");
        double averageSalary = employees.stream()
                .mapToDouble(Employee::getSalary)    // Convert to DoubleStream
                .average()                            // Built-in average function
                .orElse(0.0);                        // Handle empty stream
        
        System.out.printf("Average Salary (All Employees): $%.2f%n", averageSalary);
        
        // Step 5: Filter employees above age threshold and perform operations
        System.out.println("\n--- Filtered Employees (Age > " + AGE_THRESHOLD + ") ---");
        
        // Demonstrate stream chaining with filter
        List<Employee> filteredEmployees = employees.stream()
                .filter(ageAboveThreshold)           // Apply age filter
                .collect(Collectors.toList());
        
        System.out.println("Filtered Employee Count: " + filteredEmployees.size());
        filteredEmployees.forEach(System.out::println);
        
        // Calculate average salary for filtered employees
        double filteredAvgSalary = employees.stream()
                .filter(ageAboveThreshold)           // Filter first
                .mapToDouble(Employee::getSalary)    // Then map to salary
                .average()
                .orElse(0.0);
        
        System.out.printf("Average Salary (Age > %d): $%.2f%n", 
                AGE_THRESHOLD, filteredAvgSalary);
        
        // Additional Features: Advanced Analytics
        performAdvancedAnalytics(employees);
        
        // Demonstrate function composition
        demonstrateFunctionComposition(employees);
    }
    
    /**
     * Loads sample employee dataset
     * In production, this would read from database or file
     */
    private static List<Employee> loadEmployeeDataset() {
        return Arrays.asList(
                new Employee("Lara Adeboye", 34, "Engineering", 175000),
                new Employee("Bob Smith", 35, "Marketing", 68000),
                new Employee("Charlie Brown", 42, "Engineering", 95000),
                new Employee("Diana Prince", 31, "HR", 72000),
                new Employee("Eve Wilson", 29, "Marketing", 65000),
                new Employee("Frank Miller", 38, "Engineering", 88000),
                new Employee("Grace Lee", 45, "Finance", 92000),
                new Employee("Henry Davis", 27, "HR", 58000),
                new Employee("Ivy Chen", 33, "Finance", 81000),
                new Employee("Jack Taylor", 50, "Engineering", 105000)
        );
    }
    
    /**
     * ADDITIONAL FEATURE: Advanced analytics demonstrating stream operations
     * Showcases grouping, counting, and statistical operations
     */
    private static void performAdvancedAnalytics(List<Employee> employees) {
        System.out.println("\n=== Advanced Analytics (Additional Features) ===");
        
        // 1. Group employees by department
        Map<String, List<Employee>> byDepartment = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        
        System.out.println("\n--- Employees by Department ---");
        byDepartment.forEach((dept, empList) -> {
            System.out.printf("%s (%d employees):%n", dept, empList.size());
            empList.forEach(e -> System.out.println("  - " + e.getName()));
        });
        
        // 2. Calculate average salary by department
        System.out.println("\n--- Average Salary by Department ---");
        Map<String, Double> avgSalaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)
                ));
        
        avgSalaryByDept.forEach((dept, avg) -> 
                System.out.printf("%s: $%.2f%n", dept, avg));
        
        // 3. Find highest paid employee using stream reduction
        employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary))
                .ifPresent(emp -> System.out.printf(
                        "%n--- Highest Paid Employee ---%n%s - $%.2f%n",
                        emp.getName(), emp.getSalary()));
        
        // 4. Count employees in each age group (demonstrates partitioning)
        Map<Boolean, Long> ageGroupCount = employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.getAge() > AGE_THRESHOLD,
                        Collectors.counting()
                ));
        
        System.out.println("\n--- Age Distribution ---");
        System.out.printf("Age ≤ %d: %d employees%n", 
                AGE_THRESHOLD, ageGroupCount.get(false));
        System.out.printf("Age > %d: %d employees%n", 
                AGE_THRESHOLD, ageGroupCount.get(true));
    }
    
    /**
     * ADDITIONAL FEATURE: Demonstrates function composition
     * Shows how Functions can be chained using andThen()
     */
    private static void demonstrateFunctionComposition(List<Employee> employees) {
        System.out.println("\n=== Function Composition Demonstration ===");
        
        // Create a function that converts string to uppercase
        Function<String, String> toUpperCase = String::toUpperCase;
        
        // Compose functions: first concatenate, then convert to uppercase
        Function<Employee, String> composedFunction = 
                employeeNameDeptConcatenator.andThen(toUpperCase);
        
        System.out.println("\n--- Composed Function Output (Name-Dept in UPPERCASE) ---");
        employees.stream()
                .limit(5)  // Show first 5 for brevity
                .map(composedFunction)
                .forEach(System.out::println);
    }
}

/**
 * EXPLANATION OF FUNCTION INTERFACE:
 * 
 * Purpose:
 * - The Function<T, R> interface represents a function that accepts an argument 
 *   of type T and produces a result of type R.
 * - It's part of java.util.function package introduced in Java 8.
 * 
 * Characteristics:
 * - Functional interface with single abstract method: R apply(T t)
 * - Enables functional programming paradigm in Java
 * - Supports lambda expressions and method references
 * - Provides default methods for function composition (andThen, compose)
 * - Immutable and stateless by design
 * 
 * Usage in this Program:
 * - employeeNameDeptConcatenator: Transforms Employee -> String (concatenation)
 * - detailedEmployeeInfo: Transforms Employee -> String (detailed format)
 * - Used with stream's map() operation for transformations
 * - Demonstrates composition with andThen() method
 * 
 * Benefits:
 * - Code reusability and modularity
 * - Clean, declarative code style
 * - Easy to test and maintain
 * - Supports lazy evaluation when used with streams
 * - Type-safe transformations
 */