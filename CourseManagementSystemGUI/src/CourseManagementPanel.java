import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * CourseManagementPanel provides GUI functionality for managing courses.
 * Allows administrators to add new courses and view all courses with
 * their enrollment status.
 */
public class CourseManagementPanel {
    
    private VBox panel;
    private MainWindow mainWindow;
    private TableView<Course> courseTable;
    private TextField codeField, nameField, capacityField;
    private Button addButton, refreshButton;
    
    public CourseManagementPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initializePanel();
    }
    
    private void initializePanel() {
        panel = new VBox(10);
        panel.setPadding(new Insets(15));
        
        // Title
        Label titleLabel = new Label("Course Management");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Input form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Label codeLabel = new Label("Course Code:");
        codeField = new TextField();
        codeField.setPromptText("e.g., CS101");
        
        Label nameLabel = new Label("Course Name:");
        nameField = new TextField();
        nameField.setPromptText("e.g., Introduction to Programming");
        
        Label capacityLabel = new Label("Max Capacity:");
        capacityField = new TextField();
        capacityField.setPromptText("e.g., 30");
        
        formGrid.add(codeLabel, 0, 0);
        formGrid.add(codeField, 1, 0);
        formGrid.add(nameLabel, 0, 1);
        formGrid.add(nameField, 1, 1);
        formGrid.add(capacityLabel, 0, 2);
        formGrid.add(capacityField, 1, 2);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Course");
        refreshButton = new Button("Refresh");
        
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        
        buttonBox.getChildren().addAll(addButton, refreshButton);
        
        // Event handlers
        addButton.setOnAction(e -> addCourse());
        refreshButton.setOnAction(e -> refreshData());
        
        // Course table
        courseTable = new TableView<>();
        setupCourseTable();
        
        // Add all components to panel
        panel.getChildren().addAll(titleLabel, formGrid, buttonBox, 
                                   new Label("All Courses:"), courseTable);
        VBox.setVgrow(courseTable, Priority.ALWAYS);
        
        // Load initial data
        refreshData();
    }
    
    private void setupCourseTable() {
        TableColumn<Course, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(120);
        
        TableColumn<Course, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        nameCol.setPrefWidth(250);
        
        TableColumn<Course, String> enrollmentCol = new TableColumn<>("Enrollment");
        enrollmentCol.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            String enrollment = course.getCurrentEnrollment() + "/" + course.getMaximumCapacity();
            return new javafx.beans.property.SimpleStringProperty(enrollment);
        });
        enrollmentCol.setPrefWidth(100);
        
        TableColumn<Course, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            String status = course.hasCapacity() ? "Available" : "Full";
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        statusCol.setPrefWidth(100);
        
        courseTable.getColumns().addAll(codeCol, nameCol, enrollmentCol, statusCol);
    }
    
    private void addCourse() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        
        // Validation
        if (code.isEmpty() || name.isEmpty() || capacityStr.isEmpty()) {
            mainWindow.showError("Validation Error", "Please fill in all fields.");
            return;
        }
        
        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                mainWindow.showError("Validation Error", "Capacity must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            mainWindow.showError("Validation Error", "Please enter a valid number for capacity.");
            return;
        }
        
        // Add course
        Course course = CourseManagement.addCourse(code, name, capacity);
        
        if (course != null) {
            mainWindow.showInfo("Success", 
                "Course added successfully!\n\nCode: " + code + 
                "\nName: " + name + 
                "\nCapacity: " + capacity);
            clearForm();
            refreshData();
            mainWindow.refreshAllPanels();
        } else {
            mainWindow.showError("Error", "Failed to add course. Course code may already exist.");
        }
    }
    
    public void refreshData() {
        courseTable.getItems().clear();
        courseTable.getItems().addAll(CourseManagement.getAllCourses());
    }
    
    private void clearForm() {
        codeField.clear();
        nameField.clear();
        capacityField.clear();
    }
    
    public VBox getPanel() {
        return panel;
    }
}