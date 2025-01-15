import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import models.Session;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import models.Classes;
import db.ClassesDAO;
import db.SessionDAO;
import db.SubjectDAO;
import models.Subject;

public class SessionsController implements Initializable {

    @FXML
    private TableView<Session> tableView;
    @FXML
    private TableColumn<Session, String> nameColumn;
    @FXML
    private TableColumn<Session, String> subjectColumn;
    @FXML
    private TableColumn<Session, String> salleColumn;
    @FXML
    private TableColumn<Session, String> dayColumn;
    @FXML
    private TableColumn<Session, String> hoursColumn;
    @FXML
    private TableColumn<Session, Boolean> statusColumn;
    @FXML
    private TableColumn<Session, Void> actionsColumn;
    @FXML
    private ComboBox<String> classFilter;
    @FXML
    private ComboBox<String> dayFilter;

    private ObservableList<Session> courseList = FXCollections.observableArrayList();
    private Map<String, ToggleGroup> radioGroups = new HashMap<>();
    List<Classes> classes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // Add class filter items
            classes = ClassesDAO.getClasses();
            for (Classes classe : classes) {
                classFilter.getItems().add(classe.getClasse());
            }
        } catch (SQLException e) {
            System.out.println("Exception----------" + e.getMessage());
            e.printStackTrace();
        }

        classFilter.setOnAction(event -> {
            try {
                List<Session> sessionsList;
                if (dayFilter.getValue() != null && !dayFilter.getValue().isEmpty()) {
                    sessionsList = SessionDAO.getAllByDay(classFilter.getValue(), dayFilter.getValue());
                } else {
                    sessionsList = SessionDAO.getAll(classFilter.getValue());
                }
                System.out.println("sessionsList: " + sessionsList);

                ObservableList<Session> observableList = FXCollections.observableArrayList(sessionsList);
                tableView.setItems(observableList);
            } catch (SQLException e) {
                System.out.println("Exception----------" + e.getMessage());
                e.printStackTrace();
            }
        });

        dayFilter.setOnAction(event -> {
            List<Session> sessionsList;
            try {
                if (classFilter.getValue() != null && !classFilter.getValue().isEmpty()) {
                    sessionsList = SessionDAO.getAllByDay(classFilter.getValue(), dayFilter.getValue());
                } else {
                    sessionsList = SessionDAO.getAllByDay(dayFilter.getValue());
                }
                ObservableList<Session> observableList = FXCollections.observableArrayList(sessionsList);
                tableView.setItems(observableList);
            } catch (SQLException e) {
                System.out.println("Exception----------" + e.getMessage());
                e.printStackTrace();
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        salleColumn.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        hoursColumn.setCellValueFactory(new PropertyValueFactory<>("hours"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        nameColumn.setStyle("-fx-alignment: CENTER;");
        subjectColumn.setStyle("-fx-alignment: CENTER;");
        salleColumn.setStyle("-fx-alignment: CENTER;");
        dayColumn.setStyle("-fx-alignment: CENTER;");
        hoursColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setStyle("-fx-alignment: CENTER;");

        setupStatusColumn();
        setupActionsColumn();
        tableView.setEditable(true);
    }

    @FXML
    private void handleAddButton() {
        Dialog<Session> dialog = new Dialog<>();
        dialog.setTitle("Add Session");
        dialog.setHeaderText("Enter session details");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        ComboBox<String> classComboBox = new ComboBox<>();
        for (Classes classe : classes) {
            classComboBox.getItems().add(classe.getClasse());
        }
        ComboBox<String> dayComboBox = new ComboBox<>();
        dayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        Button selectSalleButton = new Button("Select Salle");
        TextField salleField = new TextField();
        salleField.setEditable(false);

        selectSalleButton.setOnAction(event -> {
            ChoiceDialog<String> salleDialog = new ChoiceDialog<>("salle 0-1",
                    FXCollections.observableArrayList("salle 0-1", "salle 1-1", "Amphi 1"));
            salleDialog.setTitle("Select Salle");
            salleDialog.setHeaderText("Choose a salle");
            salleDialog.showAndWait().ifPresent(salleField::setText);
        });

        Button selectSubjectButton = new Button("Select Subject");
        TextField subjectField = new TextField();
        subjectField.setEditable(false);

        selectSubjectButton.setOnAction(event -> {
            String selectedClass = classComboBox.getValue();
            if (selectedClass == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Class not selected");
                alert.setContentText("Please select a class first");
                alert.showAndWait();
                return;
            }

            List<String> subjectNames = new ArrayList<>();
            try {
                List<Subject> subjects = SubjectDAO.getAllSubjects(selectedClass);
                for (Subject subject : subjects) {
                    subjectNames.add(subject.getModule());
                }
                ChoiceDialog<String> subjectDialog = new ChoiceDialog<>(subjectNames.get(0),
                        FXCollections.observableArrayList(subjectNames));
                subjectDialog.setTitle("Select Subject");
                subjectDialog.setHeaderText("Choose a subject");
                subjectDialog.showAndWait().ifPresent(subjectField::setText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ComboBox<Integer> hoursComboBox = new ComboBox<>();
        hoursComboBox.getItems().addAll(2, 4);

        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.setDisable(true);

        hoursComboBox.setOnAction(event -> {
            Integer selectedHours = hoursComboBox.getValue();
            if (selectedHours != null) {
                timeComboBox.setDisable(false);
                updateAvailableTimes(timeComboBox, selectedHours);
            }
        });

        grid.add(new Label("Session Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Class:"), 0, 1);
        grid.add(classComboBox, 1, 1);
        grid.add(new Label("Day:"), 0, 2);
        grid.add(dayComboBox, 1, 2);
        grid.add(new Label("Salle:"), 0, 3);
        grid.add(salleField, 1, 3);
        grid.add(selectSalleButton, 2, 3);
        grid.add(new Label("Subject:"), 0, 4);
        grid.add(subjectField, 1, 4);
        grid.add(selectSubjectButton, 2, 4);
        grid.add(new Label("Number of hours:"), 0, 5);
        grid.add(hoursComboBox, 1, 5);
        grid.add(new Label("Available times:"), 0, 6);
        grid.add(timeComboBox, 1, 6);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return new Session(
                        nameField.getText(),
                        subjectField.getText(),
                        salleField.getText(),
                        dayComboBox.getValue(),
                        timeComboBox.getValue(),
                        false);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(session -> {
            try {
                SessionDAO.create(
                        session.getName(),
                        session.getSubjectName(),
                        session.getRoomName(),
                        session.getDay(),
                        session.getHours(),
                        session.getStatus());
                courseList.add(session);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Database Error");
                alert.setContentText("Error creating session: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        });
    }

    private void updateAvailableTimes(ComboBox<String> timeComboBox, int selectedHours) {
        switch (selectedHours) {
            case 2:
                timeComboBox.getItems().setAll("8h30-10h15", "10h30-12h15", "14h30-16h15", "16h30-18h15");
                break;
            case 4:
                timeComboBox.getItems().setAll("8h30-12h15", "14h30-18h15");
                break;
            default:
                timeComboBox.getItems().clear();
                break;
        }
    }

    private void setupStatusColumn() {
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<Session, Boolean>() {
            private final RadioButton radioButton = new RadioButton();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Session session = getTableRow().getItem();
                    if (session != null) {
                        String sessionId = session.getName();
                        ToggleGroup group = radioGroups.computeIfAbsent(sessionId, k -> new ToggleGroup());

                        radioButton.setToggleGroup(group);
                        radioButton.setSelected(item);

                        radioButton.setOnAction(event -> {
                            session.setStatus(radioButton.isSelected());
                            System.out.println("Status changé pour " + session.getName() +
                                    " : " + radioButton.isSelected());
                        });

                        setGraphic(radioButton);
                    }
                }
            }
        });
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<Session, Void>() {
            private final Button editButton = createImageButton("/images/mynaui_edit.png");
            private final Button deleteButton = createImageButton("/images/Vector.png");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.getStyleClass().add("action-button");
                deleteButton.getStyleClass().add("action-button");

                editButton.setOnAction(event -> {
                    Session session = getTableRow().getItem();
                    if (session != null) {
                        editSession(session);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Session session = getTableRow().getItem();
                    if (session != null) {
                        deleteSession(session);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private Button createImageButton(String imagePath) {
        Button button = new Button();
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            button.setGraphic(imageView);
            button.setStyle("-fx-background-color: transparent;");
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image: " + imagePath);
            button.setText(imagePath.contains("edit") ? "✎" : "🗑");
        }
        return button;
    }

    private void deleteSession(Session session) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Session");
        confirmDialog.setHeaderText("Delete Session");
        confirmDialog.setContentText("Are you sure you want to delete this session?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                SessionDAO.delete(session.getName());

                tableView.getItems().remove(session);
                tableView.refresh();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Session deleted successfully");
                successAlert.showAndWait();
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Database Error");
                errorAlert.setContentText("Failed to delete session: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void editSession(Session session) {
        Dialog<Session> dialog = new Dialog<>();
        dialog.setTitle("Edit Session");
        String oldName = session.getName();
        dialog.setHeaderText("Edit session details");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(session.getName());

        ComboBox<String> classComboBox = new ComboBox<>();
        for (Classes classe : classes) {
            classComboBox.getItems().add(classe.getClasse());
        }

        ComboBox<String> dayComboBox = new ComboBox<>();
        dayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        dayComboBox.setValue(session.getDay());

        Button selectSalleButton = new Button("Select Salle");
        TextField salleField = new TextField(session.getRoomName());
        salleField.setEditable(false);

        selectSalleButton.setOnAction(event -> {
            ChoiceDialog<String> salleDialog = new ChoiceDialog<>("salle 0-1",
                    FXCollections.observableArrayList("salle 0-1", "salle 1-1", "Amphi 1"));
            salleDialog.setTitle("Select Salle");
            salleDialog.setHeaderText("Choose a salle");
            salleDialog.showAndWait().ifPresent(salleField::setText);
        });

        Button selectSubjectButton = new Button("Select Subject");
        TextField subjectField = new TextField(session.getSubjectName());
        subjectField.setEditable(false);

        selectSubjectButton.setOnAction(event -> {
            String selectedClass = classComboBox.getValue();
            if (selectedClass == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Class not selected");
                alert.setContentText("Please select a class first");
                alert.showAndWait();
                return;
            }

            List<String> subjectNames = new ArrayList<>();
            try {
                List<Subject> subjects = SubjectDAO.getAllSubjects(selectedClass);
                for (Subject subject : subjects) {
                    subjectNames.add(subject.getModule());
                }
                ChoiceDialog<String> subjectDialog = new ChoiceDialog<>(subjectNames.get(0),
                        FXCollections.observableArrayList(subjectNames));
                subjectDialog.setTitle("Select Subject");
                subjectDialog.setHeaderText("Choose a subject");
                subjectDialog.showAndWait().ifPresent(subjectField::setText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        ComboBox<Integer> hoursComboBox = new ComboBox<>();
        hoursComboBox.getItems().addAll(2, 4);

        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.setDisable(true);
        timeComboBox.setValue(session.getHours());

        hoursComboBox.setOnAction(event -> {
            Integer selectedHours = hoursComboBox.getValue();
            if (selectedHours != null) {
                timeComboBox.setDisable(false);
                updateAvailableTimes(timeComboBox, selectedHours);
            }
        });

        grid.add(new Label("Session Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Class:"), 0, 1);
        grid.add(classComboBox, 1, 1);
        grid.add(new Label("Day:"), 0, 2);
        grid.add(dayComboBox, 1, 2);
        grid.add(new Label("Salle:"), 0, 3);
        grid.add(salleField, 1, 3);
        grid.add(selectSalleButton, 2, 3);
        grid.add(new Label("Subject:"), 0, 4);
        grid.add(subjectField, 1, 4);
        grid.add(selectSubjectButton, 2, 4);
        grid.add(new Label("Number of hours:"), 0, 5);
        grid.add(hoursComboBox, 1, 5);
        grid.add(new Label("Available times:"), 0, 6);
        grid.add(timeComboBox, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                session.setName(nameField.getText());
                session.setSubjectId(subjectField.getText());
                session.setRoomName(salleField.getText());
                session.setDay(dayComboBox.getValue());
                session.setHours(timeComboBox.getValue());
                return session;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedSession -> {
            try {
                SessionDAO.update(
                        oldName,
                        updatedSession.getName(),
                        updatedSession.getSubjectName(),
                        updatedSession.getRoomName(),
                        updatedSession.getDay(),
                        updatedSession.getHours(),
                        updatedSession.getStatus());

                // Update the table
                tableView.getItems().remove(session);
                tableView.getItems().add(updatedSession);
                tableView.refresh();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Database Error");
                alert.setContentText("Error updating session: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        });
    }
}