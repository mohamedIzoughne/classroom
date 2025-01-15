import java.sql.SQLException;
import java.util.List;

import db.AttendanceDAO;
import db.ClassesDAO;
import db.SessionDAO;
import db.StudentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Attendance;
import models.Classes;
import models.Session;
import models.Student;

public class AttendanceController {

    @FXML
    private TableView<Attendance> tableView;
    @FXML
    private TableColumn<Attendance, String> studentNameColumn;
    @FXML
    private TableColumn<Attendance, Double> attendanceColumn;
    @FXML
    private TableColumn<Attendance, Boolean> attendantColumn;
    @FXML
    private ComboBox<String> sessionCombo;
    @FXML
    private ComboBox<String> classCombo;
    @FXML
    private TextField searchField;

    @FXML
    void submitHandler() {
         try {
            String selectedClass = classCombo.getValue();
            List<Student> students = StudentDAO.getStudentsByClassName(selectedClass, searchField.getText());
            List<Attendance> attendances = AttendanceDAO.getAttendanceRatesForStudents(students, sessionCombo.getValue());
            ObservableList<Attendance> data = FXCollections.observableArrayList(attendances);
            tableView.setItems(data);
         } catch(SQLException e) {
             e.printStackTrace();
         }

        // try {
        //     loadReclamations();
        // } catch (SQLException e) {
        //     System.out.println("Exception----------" + e.getMessage());
        //     e.printStackTrace();
        // }
    }

    public void initialize() {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        attendantColumn.setCellValueFactory(data -> data.getValue().getAttendant());

        studentNameColumn.setStyle("-fx-alignment: CENTER;");
        attendanceColumn.setStyle("-fx-alignment: CENTER;");
        attendantColumn.setStyle("-fx-alignment: CENTER;");

        // Configure checkbox column with editable cells
        attendantColumn.setCellFactory(CheckBoxTableCell.forTableColumn(attendantColumn));
        attendantColumn.setEditable(true);
        tableView.setEditable(true);

        // Load sessions and classes
        try {
            loadSessionsAndClasses();
        } catch (SQLException e) {
            System.out.println("Error loading sessions and classes: " + e.getMessage());
        }
    }

    private void loadSessionsAndClasses() throws SQLException {
        // Load sessions
        // Add sample data
        List<Classes> classes = ClassesDAO.getClasses();
        for (Classes classe : classes) {
            classCombo.getItems().add(classe.getClasse());
        }

        sessionCombo.setOnAction(event -> {
            String selectedClass = classCombo.getValue().toString();
            try {
                List<Student> students = StudentDAO.getStudentsByClassName(selectedClass, searchField.getText());
                List<Attendance> attendances = AttendanceDAO.getAttendanceRatesForStudents(students, sessionCombo.getValue());
                ObservableList<Attendance> data = FXCollections.observableArrayList(attendances);
                tableView.setItems(data);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        });

        classCombo.setOnAction(event -> {
            try {
                String selectedClass = classCombo.getValue().toString();
                List<Session> sessions = SessionDAO.getAll(selectedClass);
                sessionCombo.getItems().clear();
                for (Session session : sessions) {
                    sessionCombo.getItems().add(session.getName());
                }
                
                List<Student> students = StudentDAO.getStudentsByClassName(selectedClass, searchField.getText());
                
                List<Attendance> attendances = AttendanceDAO.getAttendanceRatesForStudents(students);
                ObservableList<Attendance> data = FXCollections.observableArrayList(attendances);
                tableView.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleSaveButton() {
        if (sessionCombo.getValue() == null || sessionCombo.getValue().toString().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a session first");
            alert.showAndWait();
            return;
        }

        ObservableList<Attendance> attendanceList = tableView.getItems();
        try {
            AttendanceDAO.saveAttendance(attendanceList, sessionCombo.getValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Attendance data saved successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while saving attendance data: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
}
