import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import db.ClassesDAO;
import db.ReclamationDAO;
import db.SessionDAO;
import db.StudentDAO;
import models.Classes;
import models.Reclamation;
import models.Session;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReclamationsController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sessionFilter;
    @FXML
    private ComboBox<String> classFilter;
    @FXML
    private TableView<Reclamation> reclamationsTable;
    @FXML
    private TableColumn<Reclamation, String> studentNameColumn;
    @FXML
    private TableColumn<Reclamation, String> groupColumn;
    @FXML
    private TableColumn<Reclamation, String> dateColumn;
    @FXML
    private TableColumn<Reclamation, String> excuseColumn;
    @FXML
    private TableColumn<Reclamation, String> sessionColumn;
    @FXML
    private TableColumn<Reclamation, Void> actionsColumn;

    @FXML
    void handleClassFilter() {
        String selectedClass = classFilter.getValue();
        System.out.println("Selected class: " + selectedClass);
        try {
            handleSessions();
        } catch (SQLException e) {
            System.out.println("Exception----------" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void submitHandler() {
        try {
            loadReclamations();
        } catch (SQLException e) {
            System.out.println("Exception----------" + e.getMessage());
            e.printStackTrace();
        }
    }

    void handleSessions() throws SQLException {
        List<Session> sessions = SessionDAO.getAll(classFilter.getValue());
        sessionFilter.getItems().clear();
        for (Session session : sessions) {
            sessionFilter.getItems().add(session.getName());
        }
    }

    private ObservableList<Reclamation> reclamationsList = FXCollections.observableArrayList();
    private FilteredList<Reclamation> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize table columns
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        excuseColumn.setCellValueFactory(new PropertyValueFactory<>("excuse"));
        sessionColumn.setCellValueFactory(new PropertyValueFactory<>("session"));

        // Configure actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView vectorIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/approveIcon.png")));
            private final ImageView removeIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Vector.png")));
            private final Button approveBtn = new Button("", vectorIcon);
            private final Button removeBtn = new Button("", removeIcon);
            private final HBox buttons = new HBox(5);

            {
                // Style buttons and resize icons
                vectorIcon.setFitHeight(18);
                vectorIcon.setFitWidth(18);
                removeIcon.setFitHeight(18);
                removeIcon.setFitWidth(18);

                approveBtn.setStyle("-fx-background-color: transparent;");
                removeBtn.setStyle("-fx-background-color: transparent;");

                buttons.getChildren().addAll(removeBtn, approveBtn);
                buttons.setAlignment(Pos.CENTER);

                // Add button handlers
                approveBtn.setOnAction(event -> handleApprove(getTableRow().getItem()));
                removeBtn.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        // Initialize filters
        try {
            // Add session filter items
            handleSessions();
        } catch (Exception e) {
            System.out.println("Exception----------" + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Add class filter items
            List<Classes> classes = ClassesDAO.getClasses();
            for (Classes classe : classes) {
                classFilter.getItems().add(classe.getClasse());
            }
        } catch (SQLException e) {
            System.out.println("Exception----------" + e.getMessage());
            e.printStackTrace();
        }

        // Setup search functionality
        // setupSearch();

        // Load example data
        try {
            loadReclamations();
        } catch (SQLException e) {
            System.out.println("Exception----------" + e.getMessage());
            e.printStackTrace();
        }
    }

    // private void setupSearch() {
    // filteredData = new FilteredList<>(reclamationsList, p -> true);

    // searchField.textProperty().addListener((observable, oldValue, newValue) -> {
    // filteredData.setPredicate(reclamation -> {
    // if (newValue == null || newValue.isEmpty()) {
    // return true;
    // }

    // String lowerCaseFilter = newValue.toLowerCase();

    // if (reclamation.getStudentName().toLowerCase().contains(lowerCaseFilter)) {
    // return true;
    // }
    // if (reclamation.getGroup().toLowerCase().contains(lowerCaseFilter)) {
    // return true;
    // }
    // if (reclamation.getSession().toLowerCase().contains(lowerCaseFilter)) {
    // return true;
    // }
    // return false;
    // });
    // });

    // reclamationsTable.setItems(filteredData);
    // }

    private void handleApprove(Reclamation reclamation) {

        try {
            ReclamationDAO.approveReclamation(reclamation.getStudentName(), reclamation.getDate(),
                    reclamation.getSession());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Approve Reclamation");
            alert.setHeaderText(null);
            alert.setContentText("Approved reclamation for: " + reclamation.getStudentName());
            alert.showAndWait();
            reclamationsList.remove(reclamation);
        } catch(SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error: " + e.getMessage());
            errorAlert.showAndWait();        
        }
    }

    // private void handleEdit(Reclamation reclamation) {
    //     Alert alert = new Alert(Alert.AlertType.INFORMATION);
    //     alert.setTitle("Edit Reclamation");
    //     alert.setHeaderText(null);
    //     alert.setContentText("Editing reclamation for: " + reclamation.getStudentName());
    //     alert.showAndWait();
    // }

    private void handleDelete(Reclamation reclamation) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Reclamation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete this reclamation?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                ReclamationDAO.deleteReclamation(reclamation.getStudentName(), reclamation.getDate(),
                        reclamation.getSession());
                        reclamationsList.remove(reclamation);
            } catch(SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error deleting reclamation: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void loadReclamations() throws SQLException {
        // Add example data
        System.out.println("Yesss");
        reclamationsList.clear();
        List<Reclamation> reclamations = ReclamationDAO.getAllReclamations(searchField.getText(),
                sessionFilter.getValue());
        reclamationsList.addAll(reclamations);
        reclamationsTable.setItems(reclamationsList);
    }
}