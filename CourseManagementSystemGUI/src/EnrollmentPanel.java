import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;

/**
 * EnrollmentPanel provides GUI functionality for enrolling students in courses.
 * Displays available courses and eligible students, allowing administrators
 * to perform enrollments through an intuitive interface.
 */
public class EnrollmentPanel {
    
    private VBox panel;
    private MainWindow mainWindow;
    private ComboBox<String> courseComboBox;
    private ListView<String> studentListView;
    private Button enrollButton, refreshButton;
    private Label courseInfoLabel;
    
    public EnrollmentPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initializePanel();
    }
    
    private void initializePanel() {
        panel = new VBox(10);
        panel.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("Course Enrollment");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Instructions
        Label instructionLabel = new Label(
            "Select a course from the dropdown, then select a student from the list to enroll them."
        );
        instructionLabel.setWrapText(true);
        instructionLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #666666;");
        
        // Course selection
        VBox courseBox = new VBox(5);
        courseBox.setPadding(new Insets(10));
        courseBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label courseLabel = new Label("Select Course:");
        courseLabel.setStyle("-fx-font-weight: bold;");
        
        courseComboBox = new ComboBox<>();
        courseComboBox.setPromptText("Choose a course...");
        courseComboBox.setPrefWidth(400);
        courseComboBox.setOnAction(e -> onCourseSelected());
        
        courseInfoLabel = new Label();
        courseInfoLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-size: 12px;");
        
        courseBox.getChildren().addAll(courseLabel, courseComboBox, courseInfoLabel);
        
        // Student selection
        VBox studentBox = new VBox(5);
        studentBox.setPadding(new Insets(10));
        studentBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label studentLabel = new Label("Select Student to Enroll:");
        studentLabel.setStyle("-fx-font-weight: bold;");
        
        studentListView = new ListView<>();
        studentListView.setPrefHeight(300);
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        studentBox.getChildren().addAll(studentLabel, studentListView);
        VBox.setVgrow(studentBox, Priority.ALWAYS);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        enrollButton = new Button("Enroll Selected Student");
        refreshButton = new Button("Refresh");
        
        enrollButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        
        buttonBox.getChildren().addAll(enrollButton, refreshButton);
        
        // Event handlers
        enrollButton.setOnAction(e -> enrollStudent());
        refreshButton.setOnAction(e -> refreshData());
        
        // Add all components
        panel.getChildren().addAll(titleLabel, instructionLabel, courseBox, studentBox, buttonBox);
        
        // Load initial data
        refreshData();
    }
    
    private void onCourseSelected() {
        String selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null) {
            studentListView.getItems().clear();
            courseInfoLabel.setText("");
            return;
        }
        
        // Extract course code from selection
        String courseCode = selectedCourse.split(" - ")[0];
        Course course = CourseManagement.findCourseByCode(courseCode);
        
        if (course != null) {
            // Update course info
            String info = String.format("Enrollment: %d/%d | Status: %s",
                course.getCurrentEnrollment(),
                course.getMaximumCapacity(),
                course.hasCapacity() ? "Available" : "FULL");
            courseInfoLabel.setText(info);
            
            // Load eligible students (all students not enrolled in this course)
            studentListView.getItems().clear();
            for (Student student : CourseManagement.getAllStudents()) {
                if (!student.getEnrolledCourses().contains(course)) {
                    studentListView.getItems().add(
                        student.getStudentId() + " - " + student.getName()
                    );
                }
            }
            
            if (studentListView.getItems().isEmpty()) {
                studentListView.getItems().add("No eligible students available");
            }
        }
    }
    
    private void enrollStudent() {
        String selectedCourse = courseComboBox.getValue();
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        
        // Validation
        if (selectedCourse == null) {
            mainWindow.showError("Selection Error", "Please select a course first.");
            return;
        }
        
        if (selectedStudent == null || selectedStudent.equals("No eligible students available")) {
            mainWindow.showError("Selection Error", "Please select a student to enroll.");
            return;
        }
        
        // Extract IDs
        String courseCode = selectedCourse.split(" - ")[0];
        String studentId = selectedStudent.split(" - ")[0];
        
        // Get objects
        Course course = CourseManagement.findCourseByCode(courseCode);
        Student student = CourseManagement.findStudentById(studentId);
        
        if (course == null || student == null) {
            mainWindow.showError("Error", "Failed to find course or student.");
            return;
        }
        
        // Check capacity
        if (!course.hasCapacity()) {
            mainWindow.showError("Enrollment Error", 
                "Course has reached maximum capacity.\n\n" +
                "Current enrollment: " + course.getCurrentEnrollment() + "/" + 
                course.getMaximumCapacity());
            return;
        }
        
        // Enroll student
        boolean enrolled = CourseManagement.enrollStudent(student, course);
        
        if (enrolled) {
            mainWindow.showInfo("Success", 
                "Student enrolled successfully!\n\n" +
                "Student: " + student.getName() + " (" + student.getStudentId() + ")\n" +
                "Course: " + course.getCourseName() + " (" + course.getCourseCode() + ")");
            
            // Refresh the display
            onCourseSelected();
            mainWindow.refreshAllPanels();
        } else {
            mainWindow.showError("Enrollment Error", 
                "Failed to enroll student. Student may already be enrolled in this course.");
        }
    }
    
    public void refreshData() {
        // Refresh course list
        courseComboBox.getItems().clear();
        for (Course course : CourseManagement.getAllCourses()) {
            courseComboBox.getItems().add(
                course.getCourseCode() + " - " + course.getCourseName()
            );
        }
        
        // Clear selections
        courseComboBox.setValue(null);
        studentListView.getItems().clear();
        courseInfoLabel.setText("");
    }
    
    public VBox getPanel() {
        return panel;
    }
}