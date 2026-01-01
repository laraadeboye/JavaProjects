import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * StatisticsPanel provides a dashboard view of system-wide statistics
 * including total courses, students, enrollments, and other metrics.
 */
public class StatisticsPanel {
    
    private VBox panel;
    private MainWindow mainWindow;
    private Label totalCoursesLabel, totalStudentsLabel, totalEnrollmentsLabel;
    private Label avgEnrollmentLabel, avgGradeLabel;
    private Button refreshButton;
    
    public StatisticsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initializePanel();
    }
    
    private void initializePanel() {
        panel = new VBox(20);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_CENTER);
        
        // Title
        Label titleLabel = new Label("System Statistics");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Statistics grid
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setPadding(new Insets(20));
        
        // Create stat boxes
        VBox coursesBox = createStatBox("Total Courses", "0", "#4CAF50");
        VBox studentsBox = createStatBox("Total Students", "0", "#2196F3");
        VBox enrollmentsBox = createStatBox("Total Enrollments", "0", "#FF9800");
        VBox avgEnrollBox = createStatBox("Avg Enrollment/Course", "0.00", "#9C27B0");
        VBox avgGradeBox = createStatBox("System Average Grade", "N/A", "#F44336");
        
        totalCoursesLabel = (Label) coursesBox.getChildren().get(1);
        totalStudentsLabel = (Label) studentsBox.getChildren().get(1);
        totalEnrollmentsLabel = (Label) enrollmentsBox.getChildren().get(1);
        avgEnrollmentLabel = (Label) avgEnrollBox.getChildren().get(1);
        avgGradeLabel = (Label) avgGradeBox.getChildren().get(1);
        
        // Add to grid
        statsGrid.add(coursesBox, 0, 0);
        statsGrid.add(studentsBox, 1, 0);
        statsGrid.add(enrollmentsBox, 2, 0);
        statsGrid.add(avgEnrollBox, 0, 1);
        statsGrid.add(avgGradeBox, 1, 1);
        
        // Detailed statistics area
        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(10));
        detailsBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label detailsTitle = new Label("Detailed Information");
        detailsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(10);
        detailsArea.setWrapText(true);
        detailsArea.setId("detailsArea");
        
        detailsBox.getChildren().addAll(detailsTitle, detailsArea);
        VBox.setVgrow(detailsBox, Priority.ALWAYS);
        
        // Refresh button
        refreshButton = new Button("Refresh Statistics");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setOnAction(e -> refreshData());
        
        // Add all components
        panel.getChildren().addAll(titleLabel, statsGrid, detailsBox, refreshButton);
        
        // Load initial data
        refreshData();
    }
    
    private VBox createStatBox(String label, String value, String color) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: " + color + "; -fx-border-width: 2px; " +
                    "-fx-border-radius: 10px; -fx-background-color: #f9f9f9; " +
                    "-fx-min-width: 180px; -fx-min-height: 100px;");
        
        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }
    
    public void refreshData() {
        int totalCourses = CourseManagement.getAllCourses().size();
        int totalStudents = CourseManagement.getAllStudents().size();
        int totalEnrollments = Course.getTotalEnrolledStudents();
        
        // Update basic stats
        totalCoursesLabel.setText(String.valueOf(totalCourses));
        totalStudentsLabel.setText(String.valueOf(totalStudents));
        totalEnrollmentsLabel.setText(String.valueOf(totalEnrollments));
        
        // Calculate average enrollment per course
        double avgEnrollment = totalCourses > 0 ? 
            (double) totalEnrollments / totalCourses : 0.0;
        avgEnrollmentLabel.setText(String.format("%.2f", avgEnrollment));
        
        // Calculate system average grade
        double totalGrade = 0.0;
        int gradedStudents = 0;
        
        for (Student student : CourseManagement.getAllStudents()) {
            double grade = student.calculateOverallGrade();
            if (grade > 0) {
                totalGrade += grade;
                gradedStudents++;
            }
        }
        
        if (gradedStudents > 0) {
            double avgGrade = totalGrade / gradedStudents;
            avgGradeLabel.setText(String.format("%.2f", avgGrade));
        } else {
            avgGradeLabel.setText("N/A");
        }
        
        // Build detailed information
        StringBuilder details = new StringBuilder();
        details.append("COURSE DETAILS\n");
        details.append("=".repeat(50)).append("\n\n");
        
        if (totalCourses == 0) {
            details.append("No courses available.\n\n");
        } else {
            for (Course course : CourseManagement.getAllCourses()) {
                details.append(String.format("%-15s | %-30s | %d/%d %s\n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getCurrentEnrollment(),
                    course.getMaximumCapacity(),
                    course.hasCapacity() ? "(Available)" : "(Full)"));
            }
        }
        
        details.append("\n\nSTUDENT DETAILS\n");
        details.append("=".repeat(50)).append("\n\n");
        
        if (totalStudents == 0) {
            details.append("No students registered.\n");
        } else {
            for (Student student : CourseManagement.getAllStudents()) {
                double grade = student.calculateOverallGrade();
                String gradeStr = grade > 0 ? String.format("%.2f", grade) : "N/A";
                details.append(String.format("%-10s | %-25s | Courses: %d | Grade: %s\n",
                    student.getStudentId(),
                    student.getName(),
                    student.getEnrolledCourses().size(),
                    gradeStr));
            }
        }
        
        details.append("\n\nSYSTEM SUMMARY\n");
        details.append("=".repeat(50)).append("\n");
        details.append(String.format("Total System Capacity: %d students\n", 
            CourseManagement.getAllCourses().stream()
                .mapToInt(Course::getMaximumCapacity)
                .sum()));
        details.append(String.format("Available Spots: %d\n",
            CourseManagement.getAllCourses().stream()
                .mapToInt(c -> c.getMaximumCapacity() - c.getCurrentEnrollment())
                .sum()));
        details.append(String.format("Capacity Utilization: %.1f%%\n",
            totalCourses > 0 ? (avgEnrollment / (CourseManagement.getAllCourses().stream()
                .mapToDouble(Course::getMaximumCapacity)
                .average()
                .orElse(1.0)) * 100) : 0.0));
        
        // Update details area
        TextArea detailsArea = (TextArea) panel.lookup("#detailsArea");
        if (detailsArea != null) {
            detailsArea.setText(details.toString());
        }
    }
    
    public VBox getPanel() {
        return panel;
    }
}