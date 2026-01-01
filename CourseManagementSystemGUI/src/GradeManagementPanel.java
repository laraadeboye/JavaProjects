import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;

/**
 * GradeManagementPanel provides GUI functionality for assigning grades to students.
 * Displays students, their enrolled courses, and allows grade assignment with
 * real-time updates.
 */
public class GradeManagementPanel {
    
    private VBox panel;
    private MainWindow mainWindow;
    private ComboBox<String> studentComboBox;
    private TableView<CourseGradeRow> gradeTable;
    private TextField gradeField;
    private Button assignButton, refreshButton;
    private Label studentInfoLabel;
    
    public GradeManagementPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initializePanel();
    }
    
    private void initializePanel() {
        panel = new VBox(10);
        panel.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("Grade Management");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Instructions
        Label instructionLabel = new Label(
            "Select a student to view their enrolled courses, then select a course from the table to assign a grade."
        );
        instructionLabel.setWrapText(true);
        instructionLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #666666;");
        
        // Student selection
        VBox studentBox = new VBox(5);
        studentBox.setPadding(new Insets(10));
        studentBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label studentLabel = new Label("Select Student:");
        studentLabel.setStyle("-fx-font-weight: bold;");
        
        studentComboBox = new ComboBox<>();
        studentComboBox.setPromptText("Choose a student...");
        studentComboBox.setPrefWidth(400);
        studentComboBox.setOnAction(e -> onStudentSelected());
        
        studentInfoLabel = new Label();
        studentInfoLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-size: 12px;");
        
        studentBox.getChildren().addAll(studentLabel, studentComboBox, studentInfoLabel);
        
        // Grades table
        VBox tableBox = new VBox(5);
        tableBox.setPadding(new Insets(10));
        tableBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label tableLabel = new Label("Enrolled Courses and Grades:");
        tableLabel.setStyle("-fx-font-weight: bold;");
        
        gradeTable = new TableView<>();
        setupGradeTable();
        gradeTable.setPrefHeight(250);
        
        tableBox.getChildren().addAll(tableLabel, gradeTable);
        VBox.setVgrow(tableBox, Priority.ALWAYS);
        
        // Grade assignment
        GridPane gradeGrid = new GridPane();
        gradeGrid.setHgap(10);
        gradeGrid.setVgap(10);
        gradeGrid.setPadding(new Insets(10));
        gradeGrid.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label gradeLabel = new Label("Assign Grade (0-100):");
        gradeField = new TextField();
        gradeField.setPromptText("Enter grade");
        gradeField.setPrefWidth(150);
        
        gradeGrid.add(gradeLabel, 0, 0);
        gradeGrid.add(gradeField, 1, 0);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        assignButton = new Button("Assign Grade to Selected Course");
        refreshButton = new Button("Refresh");
        
        assignButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        
        buttonBox.getChildren().addAll(assignButton, refreshButton);
        
        // Event handlers
        assignButton.setOnAction(e -> assignGrade());
        refreshButton.setOnAction(e -> refreshData());
        
        // Add all components
        panel.getChildren().addAll(titleLabel, instructionLabel, studentBox, 
                                   tableBox, gradeGrid, buttonBox);
        
        // Load initial data
        refreshData();
    }
    
    private void setupGradeTable() {
        TableColumn<CourseGradeRow, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(120);
        
        TableColumn<CourseGradeRow, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        nameCol.setPrefWidth(250);
        
        TableColumn<CourseGradeRow, String> gradeCol = new TableColumn<>("Current Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setPrefWidth(120);
        
        gradeTable.getColumns().addAll(codeCol, nameCol, gradeCol);
        gradeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private void onStudentSelected() {
        String selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) {
            gradeTable.getItems().clear();
            studentInfoLabel.setText("");
            return;
        }
        
        // Extract student ID
        String studentId = selectedStudent.split(" - ")[0];
        Student student = CourseManagement.findStudentById(studentId);
        
        if (student != null) {
            // Update student info
            double overallGrade = student.calculateOverallGrade();
            String gradeStr = overallGrade > 0 ? String.format("%.2f", overallGrade) : "N/A";
            studentInfoLabel.setText("Overall Grade: " + gradeStr + 
                " | Enrolled Courses: " + student.getEnrolledCourses().size());
            
            // Load enrolled courses
            gradeTable.getItems().clear();
            for (Course course : student.getEnrolledCourses()) {
                double grade = student.getGradeForCourse(course);
                String gradeDisplay = grade >= 0 ? String.format("%.2f", grade) : "Not Graded";
                gradeTable.getItems().add(new CourseGradeRow(
                    course.getCourseCode(),
                    course.getCourseName(),
                    gradeDisplay,
                    course
                ));
            }
            
            if (gradeTable.getItems().isEmpty()) {
                studentInfoLabel.setText(studentInfoLabel.getText() + " | No enrolled courses");
            }
        }
    }
    
    private void assignGrade() {
        String selectedStudent = studentComboBox.getValue();
        CourseGradeRow selectedCourse = gradeTable.getSelectionModel().getSelectedItem();
        String gradeStr = gradeField.getText().trim();
        
        // Validation
        if (selectedStudent == null) {
            mainWindow.showError("Selection Error", "Please select a student first.");
            return;
        }
        
        if (selectedCourse == null) {
            mainWindow.showError("Selection Error", "Please select a course from the table.");
            return;
        }
        
        if (gradeStr.isEmpty()) {
            mainWindow.showError("Validation Error", "Please enter a grade.");
            return;
        }
        
        double grade;
        try {
            grade = Double.parseDouble(gradeStr);
            if (grade < 0 || grade > 100) {
                mainWindow.showError("Validation Error", "Grade must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            mainWindow.showError("Validation Error", "Please enter a valid number for grade.");
            return;
        }
        
        // Get objects
        String studentId = selectedStudent.split(" - ")[0];
        Student student = CourseManagement.findStudentById(studentId);
        Course course = selectedCourse.getCourse();
        
        if (student == null || course == null) {
            mainWindow.showError("Error", "Failed to find student or course.");
            return;
        }
        
        // Assign grade
        boolean assigned = CourseManagement.assignGrade(student, course, grade);
        
        if (assigned) {
            mainWindow.showInfo("Success", 
                String.format("Grade assigned successfully!\n\n" +
                    "Student: %s\n" +
                    "Course: %s\n" +
                    "Grade: %.2f",
                    student.getName(), course.getCourseName(), grade));
            
            gradeField.clear();
            onStudentSelected();
            mainWindow.refreshAllPanels();
        } else {
            mainWindow.showError("Error", "Failed to assign grade. Student may not be enrolled in this course.");
        }
    }
    
    public void refreshData() {
        // Refresh student list
        studentComboBox.getItems().clear();
        for (Student student : CourseManagement.getAllStudents()) {
            studentComboBox.getItems().add(
                student.getStudentId() + " - " + student.getName()
            );
        }
        
        // Clear selections
        studentComboBox.setValue(null);
        gradeTable.getItems().clear();
        studentInfoLabel.setText("");
        gradeField.clear();
    }
    
    public VBox getPanel() {
        return panel;
    }
    
    /**
     * Helper class to represent a row in the grade table
     */
    public static class CourseGradeRow {
        private final SimpleStringProperty courseCode;
        private final SimpleStringProperty courseName;
        private final SimpleStringProperty grade;
        private final Course course;
        
        public CourseGradeRow(String courseCode, String courseName, String grade, Course course) {
            this.courseCode = new SimpleStringProperty(courseCode);
            this.courseName = new SimpleStringProperty(courseName);
            this.grade = new SimpleStringProperty(grade);
            this.course = course;
        }
        
        public String getCourseCode() { return courseCode.get(); }
        public String getCourseName() { return courseName.get(); }
        public String getGrade() { return grade.get(); }
        public Course getCourse() { return course; }
    }
}