import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private AnchorPane dynamicContent;

    @FXML
    private Label navDashboard;

    @FXML
    private Label navConsultStudent;

    @FXML
    private Label navSubjects;

    @FXML
    private Label navSessions;

    @FXML
    private Label navAttendance;

    @FXML
    private Label navReclamations;

    public void initialize() {
        loadFXML("dashboard.fxml");
        setupNavigationHandlers();
        navDashboard.setStyle("-fx-background-color: #4C8CF8;");
        navDashboard.setTextFill(Color.WHITE);
    }

    private void setupNavigationHandlers() {
        navDashboard.setOnMouseClicked(event -> handleNavigation(navDashboard, "dashboard.fxml")); // works
        navConsultStudent.setOnMouseClicked(event -> handleNavigation(navConsultStudent, "consult_student.fxml"));
        navSubjects.setOnMouseClicked(event -> handleNavigation(navSubjects, "curriculum.fxml")); // works
        navSessions.setOnMouseClicked(event -> handleNavigation(navSessions, "sessions.fxml"));
        navAttendance.setOnMouseClicked(event -> handleNavigation(navAttendance, "attendance.fxml"));
        navReclamations.setOnMouseClicked(event -> handleNavigation(navReclamations, "reclamations.fxml"));
    }

    private void handleNavigation(Label clickedLabel, String fxmlFile) {
        resetLabelsStyle();
        clickedLabel.setStyle("-fx-background-color: #4C8CF8;");
        clickedLabel.setTextFill(Color.WHITE);
        loadFXML(fxmlFile);
    }

    private void resetLabelsStyle() {
        navDashboard.setStyle("");
        navDashboard.setTextFill(Color.BLACK);
        navConsultStudent.setStyle("");
        navConsultStudent.setTextFill(Color.BLACK);
        navSubjects.setStyle("");
        navSubjects.setTextFill(Color.BLACK);
        navSessions.setStyle("");
        navSessions.setTextFill(Color.BLACK);
        navAttendance.setStyle("");
        navAttendance.setTextFill(Color.BLACK);
        navReclamations.setStyle("");
        navReclamations.setTextFill(Color.BLACK);
    }

    private void loadFXML(String fxmlFile) {
        try {
            AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
            dynamicContent.getChildren().clear();
            dynamicContent.getChildren().add(newContent);
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}