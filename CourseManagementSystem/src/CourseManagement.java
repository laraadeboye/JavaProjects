import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseManagement class serves as the central management system for
 * courses, student enrollments, and grade management.
 * 
 * This class uses static variables and methods to maintain system-wide
 * data and operations, demonstrating class-level functionality.
 */
public class CourseManagement {
    // Private static variables to store system-wide data
    private static List<Course> courses = new ArrayList<>();
    private static Map<Student, Map<Course, Double>> overallGrades = new HashMap<>();
    private static List<Student> students = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation
     * This class is designed to be used through static methods only
     */
    private CourseManagement() {
        // Prevent instantiation
    }

    /**
     * Adds a new course to the system
     * 
     * @param courseCode The unique course code
     * @param courseName The name of the course
     * @param maximumCapacity The maximum number of students allowed
     * @return The created Course object, or null if invalid parameters
     */
    public static Course addCourse(String courseCode, String courseName, int maximumCapacity) {
        if (courseCode == null || courseCode.trim().isEmpty() || 
            courseName == null || courseName.trim().isEmpty() || 
            maximumCapacity <= 0) {
            return null;
        }

        // Check if course already exists
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return null; // Course with this code already exists
            }
        }

        Course newCourse = new Course(courseCode, courseName, maximumCapacity);
        courses.add(newCourse);
        return newCourse;
    }

    /**
     * Enrolls a student in a course
     * 
     * @param student The student to enroll
     * @param course The course to enroll in
     * @return true if enrollment is successful, false otherwise
     */
    public static boolean enrollStudent(Student student, Course course) {
        if (student == null || course == null) {
            return false;
        }

        // Check if course exists in the system
        if (!courses.contains(course)) {
            return false;
        }

        // Check if course has capacity
        if (!course.hasCapacity()) {
            return false;
        }

        // Enroll student using the Student class method
        boolean enrolled = student.enrollInCourse(course);
        
        if (enrolled) {
            course.incrementEnrollment();
            
            // Add student to system if not already present
            if (!students.contains(student)) {
                students.add(student);
            }
            
            // Initialize grade tracking for this student if needed
            if (!overallGrades.containsKey(student)) {
                overallGrades.put(student, new HashMap<>());
            }
        }

        return enrolled;
    }

    /**
     * Assigns a grade to a student for a specific course
     * 
     * @param student The student to assign the grade to
     * @param course The course for which to assign the grade
     * @param grade The grade to assign (0.0 to 100.0)
     * @return true if grade assignment is successful, false otherwise
     */
    public static boolean assignGrade(Student student, Course course, double grade) {
        if (student == null || course == null || grade < 0 || grade > 100) {
            return false;
        }

        // Assign grade using the Student class method
        boolean gradeAssigned = student.assignGrade(course, grade);

        if (gradeAssigned) {
            // Update the overall grades map
            if (!overallGrades.containsKey(student)) {
                overallGrades.put(student, new HashMap<>());
            }
            overallGrades.get(student).put(course, grade);
        }

        return gradeAssigned;
    }

    /**
     * Calculates the overall course grade for a student
     * This method computes the average of all grades assigned to the student
     * 
     * @param student The student for whom to calculate the overall grade
     * @return The overall grade (average), or 0.0 if no grades assigned
     */
    public static double calculateOverallGrade(Student student) {
        if (student == null) {
            return 0.0;
        }

        return student.calculateOverallGrade();
    }

    /**
     * Gets all courses in the system
     * @return List of all courses
     */
    public static List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    /**
     * Gets all students in the system
     * @return List of all students
     */
    public static List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Finds a course by its course code
     * @param courseCode The course code to search for
     * @return The Course object, or null if not found
     */
    public static Course findCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Finds a student by their ID
     * @param studentId The student ID to search for
     * @return The Student object, or null if not found
     */
    public static Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Adds a student to the system
     * @param student The student to add
     * @return true if added successfully, false if student already exists
     */
    public static boolean addStudent(Student student) {
        if (student == null || students.contains(student)) {
            return false;
        }
        
        // Check for duplicate student ID
        for (Student s : students) {
            if (s.getStudentId().equals(student.getStudentId())) {
                return false;
            }
        }
        
        students.add(student);
        return true;
    }

    /**
     * Gets enrollment statistics
     * @return String containing enrollment statistics
     */
    public static String getEnrollmentStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Total Courses: ").append(courses.size()).append("\n");
        stats.append("Total Students: ").append(students.size()).append("\n");
        stats.append("Total Enrollments: ").append(Course.getTotalEnrolledStudents()).append("\n");
        return stats.toString();
    }
}