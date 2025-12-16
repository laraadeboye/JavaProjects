import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student class represents a university student with personal information,
 * enrolled courses, and grades.
 * 
 * This class demonstrates encapsulation by using private instance variables
 * and providing public getter/setter methods for controlled access.
 */
public class Student {
    // Private instance variables for encapsulation
    private String name;
    private String studentId;
    private List<Course> enrolledCourses;
    private Map<Course, Double> courseGrades;

    /**
     * Constructor to create a new Student object
     * @param name The student's name
     * @param studentId The student's unique ID
     */
    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.enrolledCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>();
    }

    // Getter and Setter methods for accessing and updating student information
    
    /**
     * Gets the student's name
     * @return The student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student's name
     * @param name The new name for the student
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the student's ID
     * @return The student's ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Sets the student's ID
     * @param studentId The new ID for the student
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the list of courses the student is enrolled in
     * @return List of enrolled courses
     */
    public List<Course> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);
    }

    /**
     * Gets the student's grades for all courses
     * @return Map of courses and their corresponding grades
     */
    public Map<Course, Double> getCourseGrades() {
        return new HashMap<>(courseGrades);
    }

    /**
     * Enrolls the student in a course
     * Demonstrates instance method for manipulating object state
     * @param course The course to enroll in
     * @return true if enrollment is successful, false if already enrolled
     */
    public boolean enrollInCourse(Course course) {
        if (course == null) {
            return false;
        }
        
        if (enrolledCourses.contains(course)) {
            return false; // Already enrolled
        }
        
        enrolledCourses.add(course);
        Course.incrementTotalEnrolledStudents();
        return true;
    }

    /**
     * Assigns a grade to the student for a specific course
     * Demonstrates instance method for manipulating object state
     * @param course The course for which to assign the grade
     * @param grade The grade to assign (0.0 to 100.0)
     * @return true if grade assignment is successful, false otherwise
     */
    public boolean assignGrade(Course course, double grade) {
        if (course == null || grade < 0 || grade > 100) {
            return false;
        }
        
        if (!enrolledCourses.contains(course)) {
            return false; // Student not enrolled in this course
        }
        
        courseGrades.put(course, grade);
        return true;
    }

    /**
     * Gets the grade for a specific course
     * @param course The course to get the grade for
     * @return The grade, or -1 if no grade assigned
     */
    public double getGradeForCourse(Course course) {
        return courseGrades.getOrDefault(course, -1.0);
    }

    /**
     * Calculates the overall average grade for the student
     * @return The average grade, or 0.0 if no grades assigned
     */
    public double calculateOverallGrade() {
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (double grade : courseGrades.values()) {
            sum += grade;
        }
        
        return sum / courseGrades.size();
    }

    @Override
    public String toString() {
        return "Student{ID='" + studentId + "', Name='" + name + 
               "', Enrolled Courses=" + enrolledCourses.size() + "}";
    }
}