/**
 * Course class represents a university course with course information
 * and enrollment capacity.
 * 
 * This class uses a static variable to track total enrolled students
 * across all course instances, demonstrating the use of class-level data.
 */
public class Course {
    // Private instance variables for encapsulation
    private String courseCode;
    private String courseName;
    private int maximumCapacity;
    private int currentEnrollment;
    
    // Static variable to track total enrolled students across all courses
    private static int totalEnrolledStudents = 0;

    /**
     * Constructor to create a new Course object
     * @param courseCode The unique code for the course
     * @param courseName The name of the course
     * @param maximumCapacity The maximum number of students allowed
     */
    public Course(String courseCode, String courseName, int maximumCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maximumCapacity = maximumCapacity;
        this.currentEnrollment = 0;
    }

    // Getter methods for accessing course information
    
    /**
     * Gets the course code
     * @return The course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Gets the course name
     * @return The course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Gets the maximum capacity
     * @return The maximum capacity
     */
    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    /**
     * Gets the current enrollment count
     * @return The current enrollment count
     */
    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    /**
     * Checks if the course has available capacity
     * @return true if there is space, false otherwise
     */
    public boolean hasCapacity() {
        return currentEnrollment < maximumCapacity;
    }

    /**
     * Increments the current enrollment for this course
     */
    public void incrementEnrollment() {
        if (hasCapacity()) {
            currentEnrollment++;
        }
    }

    /**
     * Static method to increment total enrolled students across all courses
     * Demonstrates the use of static methods for class-level operations
     */
    public static void incrementTotalEnrolledStudents() {
        totalEnrolledStudents++;
    }

    /**
     * Static method to retrieve the total number of enrolled students
     * across all course instances.
     * 
     * This demonstrates how static methods can access and return static variables
     * to provide class-level information.
     * 
     * @return The total number of enrolled students across all courses
     */
    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    /**
     * Static method to reset the total enrolled students counter
     * Useful for testing or system resets
     */
    public static void resetTotalEnrolledStudents() {
        totalEnrolledStudents = 0;
    }

    @Override
    public String toString() {
        return "Course{Code='" + courseCode + "', Name='" + courseName + 
               "', Capacity=" + currentEnrollment + "/" + maximumCapacity + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseCode.equals(course.courseCode);
    }

    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }
}