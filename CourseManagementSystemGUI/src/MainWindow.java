import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * MainWindow serves as the primary GUI interface for the Course Enrollment
 * and Grade Management System. It uses JavaFX to provide an intuitive,
 * user-friendly interface with tabbed navigation.
 * 
 * This class coordinates between the GUI components and the backend
 * CourseManagement system.
 */
public class MainWindow extends Application {
    
    private TabPane tabPane;
    private StudentManagementPanel studentPanel;
    private CourseManagementPanel coursePanel;
    private EnrollmentPanel enrollmentPanel;
    private GradeManagementPanel gradePanel;
    private StatisticsPanel statisticsPanel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Course Enrollment and Grade Management System");
        
        // Create the main layout
        BorderPane mainLayout = new BorderPane();
        
        // Create header
        Label headerLabel = new Label("Course Enrollment and Grade Management System");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        mainLayout.setTop(headerLabel);
        
        // Create tab pane with different panels
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Initialize panels
        studentPanel = new StudentManagementPanel(this);
        coursePanel = new CourseManagementPanel(this);
        enrollmentPanel = new EnrollmentPanel(this);
        gradePanel = new GradeManagementPanel(this);
        statisticsPanel = new StatisticsPanel(this);
        
        // Create tabs
        Tab studentTab = new Tab("Student Management", studentPanel.getPanel());
        Tab courseTab = new Tab("Course Management", coursePanel.getPanel());
        Tab enrollmentTab = new Tab("Course Enrollment", enrollmentPanel.getPanel());
        Tab gradeTab = new Tab("Grade Management", gradePanel.getPanel());
        Tab statsTab = new Tab("Statistics", statisticsPanel.getPanel());
        
        tabPane.getTabs().addAll(studentTab, courseTab, enrollmentTab, gradeTab, statsTab);
        
        mainLayout.setCenter(tabPane);
        
        // Create footer with status bar
        Label footerLabel = new Label("Ready");
        footerLabel.setStyle("-fx-padding: 5px; -fx-background-color: #f0f0f0;");
        mainLayout.setBottom(footerLabel);
        
        // Create scene and show
        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Refreshes all panels to reflect updated data
     */
    public void refreshAllPanels() {
        studentPanel.refreshData();
        coursePanel.refreshData();
        enrollmentPanel.refreshData();
        gradePanel.refreshData();
        statisticsPanel.refreshData();
    }
    
    /**
     * Shows an information alert dialog
     * @param title The dialog title
     * @param message The message to display
     */
    public void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows an error alert dialog
     * @param title The dialog title
     * @param message The error message to display
     */
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog
     * @param title The dialog title
     * @param message The confirmation message
     * @return true if user confirms, false otherwise
     */
    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }
}