import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * AdministratorInterface provides an interactive command-line interface
 * for administrators to manage the Course Enrollment and Grade Management System.
 * 
 * This class handles user interaction, input validation, and delegates
 * operations to the appropriate classes (CourseManagement, Student, Course).
 */
public class AdministratorInterface {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Main method to start the administrator interface
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Course Enrollment and Grade Management System");
        System.out.println("========================================\n");

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    addNewCourse();
                    break;
                case 2:
                    addNewStudent();
                    break;
                case 3:
                    enrollStudentInCourse();
                    break;
                case 4:
                    assignGradeToStudent();
                    break;
                case 5:
                    calculateStudentOverallGrade();
                    break;
                case 6:
                    displayAllCourses();
                    break;
                case 7:
                    displayAllStudents();
                    break;
                case 8:
                    displayEnrollmentStatistics();
                    break;
                case 9:
                    running = false;
                    System.out.println("\nThank you for using the Course Management System. Goodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu
     */
    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("Administrator Menu");
        System.out.println("========================================");
        System.out.println("1. Add a New Course");
        System.out.println("2. Add a New Student");
        System.out.println("3. Enroll Student in Course");
        System.out.println("4. Assign Grade to Student");
        System.out.println("5. Calculate Overall Grade for Student");
        System.out.println("6. Display All Courses");
        System.out.println("7. Display All Students");
        System.out.println("8. Display Enrollment Statistics");
        System.out.println("9. Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice (1-9): ");
    }

    /**
     * Gets and validates the menu choice from the user
     * @return The validated menu choice
     */
    private static int getMenuChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    /**
     * Handles adding a new course
     */
    private static void addNewCourse() {
        System.out.println("\n--- Add New Course ---");
        
        try {
            System.out.print("Enter course code: ");
            String courseCode = scanner.nextLine().trim();

            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine().trim();

            System.out.print("Enter maximum capacity: ");
            int maxCapacity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (maxCapacity <= 0) {
                System.out.println("Error: Maximum capacity must be greater than 0.");
                return;
            }

            Course course = CourseManagement.addCourse(courseCode, courseName, maxCapacity);

            if (course != null) {
                System.out.println("Success: Course added successfully!");
                System.out.println(course);
            } else {
                System.out.println("Error: Failed to add course. Course code may already exist or invalid input.");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            System.out.println("Error: Invalid input. Please enter valid data.");
        }
    }

    /**
     * Handles adding a new student
     */
    private static void addNewStudent() {
        System.out.println("\n--- Add New Student ---");

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine().trim();

        if (name.isEmpty() || studentId.isEmpty()) {
            System.out.println("Error: Name and ID cannot be empty.");
            return;
        }

        Student student = new Student(name, studentId);
        boolean added = CourseManagement.addStudent(student);

        if (added) {
            System.out.println("Success: Student added successfully!");
            System.out.println(student);
        } else {
            System.out.println("Error: Failed to add student. Student ID may already exist.");
        }
    }

    /**
     * Handles enrolling a student in a course
     */
    private static void enrollStudentInCourse() {
        System.out.println("\n--- Enroll Student in Course ---");

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = CourseManagement.findStudentById(studentId);
        if (student == null) {
            System.out.println("Error: Student not found with ID: " + studentId);
            return;
        }

        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = CourseManagement.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Error: Course not found with code: " + courseCode);
            return;
        }

        if (!course.hasCapacity()) {
            System.out.println("Error: Course has reached maximum capacity.");
            System.out.println("Current enrollment: " + course.getCurrentEnrollment() + 
                             "/" + course.getMaximumCapacity());
            return;
        }

        boolean enrolled = CourseManagement.enrollStudent(student, course);

        if (enrolled) {
            System.out.println("Success: Student enrolled successfully!");
            System.out.println("Student: " + student.getName());
            System.out.println("Course: " + course.getCourseName());
        } else {
            System.out.println("Error: Failed to enroll student. Student may already be enrolled in this course.");
        }
    }

    /**
     * Handles assigning a grade to a student
     */
    private static void assignGradeToStudent() {
        System.out.println("\n--- Assign Grade to Student ---");

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = CourseManagement.findStudentById(studentId);
        if (student == null) {
            System.out.println("Error: Student not found with ID: " + studentId);
            return;
        }

        List<Course> enrolledCourses = student.getEnrolledCourses();
        if (enrolledCourses.isEmpty()) {
            System.out.println("Error: Student is not enrolled in any courses.");
            return;
        }

        System.out.println("\nEnrolled courses:");
        for (int i = 0; i < enrolledCourses.size(); i++) {
            Course c = enrolledCourses.get(i);
            double currentGrade = student.getGradeForCourse(c);
            String gradeStr = currentGrade >= 0 ? String.format("%.2f", currentGrade) : "Not graded";
            System.out.println((i + 1) + ". " + c.getCourseCode() + " - " + 
                             c.getCourseName() + " (Grade: " + gradeStr + ")");
        }

        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = CourseManagement.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Error: Course not found with code: " + courseCode);
            return;
        }

        try {
            System.out.print("Enter grade (0-100): ");
            double grade = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            if (grade < 0 || grade > 100) {
                System.out.println("Error: Grade must be between 0 and 100.");
                return;
            }

            boolean assigned = CourseManagement.assignGrade(student, course, grade);

            if (assigned) {
                System.out.println("Success: Grade assigned successfully!");
                System.out.println("Student: " + student.getName());
                System.out.println("Course: " + course.getCourseName());
                System.out.printf("Grade: %.2f\n", grade);
            } else {
                System.out.println("Error: Failed to assign grade. Student may not be enrolled in this course.");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            System.out.println("Error: Invalid grade. Please enter a number between 0 and 100.");
        }
    }

    /**
     * Handles calculating and displaying overall grade for a student
     */
    private static void calculateStudentOverallGrade() {
        System.out.println("\n--- Calculate Overall Grade ---");

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = CourseManagement.findStudentById(studentId);
        if (student == null) {
            System.out.println("Error: Student not found with ID: " + studentId);
            return;
        }

        double overallGrade = CourseManagement.calculateOverallGrade(student);

        System.out.println("\n--- Student Grade Report ---");
        System.out.println("Student: " + student.getName() + " (ID: " + student.getStudentId() + ")");
        System.out.println("\nCourse Grades:");

        List<Course> enrolledCourses = student.getEnrolledCourses();
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            for (Course course : enrolledCourses) {
                double grade = student.getGradeForCourse(course);
                if (grade >= 0) {
                    System.out.printf("  %s - %s: %.2f\n", 
                        course.getCourseCode(), course.getCourseName(), grade);
                } else {
                    System.out.printf("  %s - %s: Not graded\n", 
                        course.getCourseCode(), course.getCourseName());
                }
            }
            System.out.printf("\nOverall Average Grade: %.2f\n", overallGrade);
        }
    }

    /**
     * Displays all courses in the system
     */
    private static void displayAllCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> courses = CourseManagement.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }

    /**
     * Displays all students in the system
     */
    private static void displayAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = CourseManagement.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students registered.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    /**
     * Displays enrollment statistics
     */
    private static void displayEnrollmentStatistics() {
        System.out.println("\n--- Enrollment Statistics ---");
        System.out.println(CourseManagement.getEnrollmentStatistics());
    }
}