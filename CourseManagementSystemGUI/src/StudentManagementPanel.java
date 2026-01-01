import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * StudentManagementPanel provides GUI functionality for managing students.
 * Allows administrators to add new students, update student information,
 * and view all students in a table format.
 */
public class StudentManagementPanel {
    
    private VBox panel;
    private MainWindow mainWindow;
    private TableView<Student> studentTable;
    private TextField nameField, idField;
    private Button addButton, updateButton, refreshButton;
    
    public StudentManagementPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initializePanel();
    }
    
    private void initializePanel() {
        panel = new VBox(10);
        panel.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("Student Management");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Input form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label nameLabel = new Label("Student Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter student name");
        
        Label idLabel = new Label("Student ID:");
        idField = new TextField();
        idField.setPromptText("Enter student ID");
        
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(idLabel, 0, 1);
        formGrid.add(idField, 1, 1);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Student");
        updateButton = new Button("Update Selected");
        refreshButton = new Button("Refresh");
        
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        
        buttonBox.getChildren().addAll(addButton, updateButton, refreshButton);
        
        // Event handlers
        addButton.setOnAction(e -> addStudent());
        updateButton.setOnAction(e -> updateStudent());
        refreshButton.setOnAction(e -> refreshData());
        
        // Student table
        studentTable = new TableView<>();
        setupStudentTable();
        
        // Add all components to panel
        panel.getChildren().addAll(titleLabel, formGrid, buttonBox, 
                                   new Label("All Students:"), studentTable);
        VBox.setVgrow(studentTable, Priority.ALWAYS);
        
        // Load initial data
        refreshData();
    }
    
    private void setupStudentTable() {
        TableColumn<Student, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        idCol.setPrefWidth(150);
        
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        
        TableColumn<Student, Integer> coursesCol = new TableColumn<>("Enrolled Courses");
        coursesCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getEnrolledCourses().size()).asObject());
        coursesCol.setPrefWidth(150);
        
        TableColumn<Student, String> gradeCol = new TableColumn<>("Overall Grade");
        gradeCol.setCellValueFactory(cellData -> {
            double grade = cellData.getValue().calculateOverallGrade();
            String gradeStr = grade > 0 ? String.format("%.2f", grade) : "N/A";
            return new javafx.beans.property.SimpleStringProperty(gradeStr);
        });
        gradeCol.setPrefWidth(120);
        
        studentTable.getColumns().addAll(idCol, nameCol, coursesCol, gradeCol);
    }
    
    private void addStudent() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        
        // Validation
        if (name.isEmpty() || id.isEmpty()) {
            mainWindow.showError("Validation Error", "Please fill in all fields.");
            return;
        }
        
        if (name.length() > 100) {
            mainWindow.showError("Validation Error", "Name cannot exceed 100 characters.");
            return;
        }
        
        // Create and add student
        Student student = new Student(name, id);
        boolean added = CourseManagement.addStudent(student);
        
        if (added) {
            mainWindow.showInfo("Success", "Student added successfully!\n\nName: " + name + "\nID: " + id);
            clearForm();
            refreshData();
            mainWindow.refreshAllPanels();
        } else {
            mainWindow.showError("Error", "Failed to add student. Student ID may already exist.");
        }
    }
    
    private void updateStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            mainWindow.showError("Selection Error", "Please select a student from the table to update.");
            return;
        }
        
        String newName = nameField.getText().trim();
        
        if (newName.isEmpty()) {
            mainWindow.showError("Validation Error", "Please enter a new name.");
            return;
        }
        
        if (newName.length() > 100) {
            mainWindow.showError("Validation Error", "Name cannot exceed 100 characters.");
            return;
        }
        
        // Update student name
        selected.setName(newName);
        
        mainWindow.showInfo("Success", "Student information updated successfully!");
        clearForm();
        refreshData();
        mainWindow.refreshAllPanels();
    }
    
    public void refreshData() {
        studentTable.getItems().clear();
        studentTable.getItems().addAll(CourseManagement.getAllStudents());
    }
    
    private void clearForm() {
        nameField.clear();
        idField.clear();
    }
    
    public VBox getPanel() {
        return panel;
    }
}