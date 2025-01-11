import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.protocol.a.SqlDateValueEncoder;

import db.ClassesDAO;
import db.SubjectDAO;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Classes;
import models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Subject;
import db.StudentDAO;

public class CurriculumController {

    // @FXML
    // private AnchorPane dynamicContent;

    @FXML
    private TableView<Subject> tableView;

    @FXML
    private TableColumn<Subject, String> moduleColumn;

    @FXML
    private TableColumn<Subject, String> classColumn;

    @FXML
    private TableColumn<Subject, Void> actionsColumn;

    @FXML
    private TableView<Classes> TableView2;

    @FXML
    private TableColumn<Classes, String> name;

    @FXML
    private TableColumn<Classes, String> field;

    @FXML
    private TableColumn<Classes, Void> actions1;

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private TableColumn<Student, String> fullNameColumn;

    @FXML
    private TableColumn<Student, String> emailColumn;

    @FXML
    private TableColumn<Student, String> phoneNumberColumn;

    @FXML
    private TableColumn<Student, String> genderColumn;

    @FXML
    private TableColumn<Student, String> dateOfBirthColumn;

    @FXML
    private Button add;

    @FXML
    private Button add1;

    @FXML
    private Button add2;

    private void showAddDialog() {
        // Créer une nouvelle fenêtre (modale)
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Ajouter un Module");

        // Contenu de la fenêtre
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 10;");

        // Champs pour entrer les données
        TextField moduleField = new TextField();
        moduleField.setPromptText("Module");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description du module");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        ComboBox<String> classComboBox = new ComboBox<>();
        try {
            List<Classes> classes = ClassesDAO.getClasses();
            for (Classes classe : classes) {
                classComboBox.getItems().add(classe.getClasse());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        classComboBox.setPromptText("Sélectionnez une classe");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            // Ajouter une ligne dans la table
            String module = moduleField.getText();
            String selectedClass = classComboBox.getValue();
            String description = descriptionArea.getText();

            if (!module.isEmpty() && selectedClass != null) {
                tableView.getItems().add(new Subject(module, selectedClass));
                try {
                    SubjectDAO.addSubject(module, selectedClass, description);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dialog.close(); // Fermer la fenêtre
            } else {
                // Afficher un message d'erreur si les champs ne sont pas remplis
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs obligatoires");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            }
        });

        dialogVBox.getChildren().addAll(moduleField, classComboBox, descriptionArea, submitButton);

        // Configurer et afficher la scène de la fenêtre
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showAddDialogForClasses() {
        // Créer une nouvelle fenêtre (modale)
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Ajouter une Classe");

        // Contenu de la fenêtre
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 10;");

        // Labels et champs pour entrer les données
        Label nameLabel = new Label("Nom de la classe:");
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");

        Label filiereLabel = new Label("Filière:");
        ComboBox<String> filiereComboBox = new ComboBox<>();
        filiereComboBox.getItems().addAll("GI", "GE", "TM", "TCC");
        filiereComboBox.setPromptText("Sélectionnez une filière");

        Label descriptionLabel = new Label("Description (optionnel):");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description de la classe");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            // Ajouter une ligne dans la table
            String classe = nameField.getText();
            String filiere = filiereComboBox.getValue();
            String description = descriptionArea.getText();

            if (!classe.isEmpty() && filiere != null) {
                // TableView2.getItems().add(new Classes(classe, filiere));
                try {
                    ClassesDAO.addClass(classe, filiere, description);
                    TableView2.getItems().add(new Classes(classe, description, filiere));
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    e.printStackTrace();
                } finally {
                    dialog.close(); // Fermer la fenêtre
                }
            } else {
                // Afficher un message d'erreur si les champs ne sont pas remplis
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs obligatoires");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            }
        });

        dialogVBox.getChildren().addAll(
                nameLabel, nameField,
                filiereLabel, filiereComboBox,
                descriptionLabel, descriptionArea,
                submitButton);
        // Configurer et afficher la scène de la fenêtre
        Scene dialogScene = new Scene(dialogVBox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showAddDialogForStudents() {
        // Créer une nouvelle fenêtre (modale)
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Ajouter un Étudiant");

        // Contenu de la fenêtre
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 10;");

        // Champs pour entrer les données
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Nom complet");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Numéro de téléphone");

        TextField genderField = new TextField();
        genderField.setPromptText("Genre (Male/Female)");

        TextField dateOfBirthField = new TextField();
        dateOfBirthField.setPromptText("Date de naissance (ex. 22 mai 2001)");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            // Récupérer les valeurs saisies
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            String gender = genderField.getText();
            String dateOfBirth = dateOfBirthField.getText();

            if (!fullName.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !gender.isEmpty()
                    && !dateOfBirth.isEmpty()) {
                // Ajouter une nouvelle ligne au tableau
                studentsTable.getItems().add(new Student(fullName, email, phoneNumber, gender, dateOfBirth));
                dialog.close(); // Fermer la boîte de dialogue
            } else {
                // Afficher une alerte si tous les champs ne sont pas remplis
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs obligatoires");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            }
        });

        // Ajouter les champs et le bouton à la boîte de dialogue
        dialogVBox.getChildren().addAll(fullNameField, emailField, phoneNumberField, genderField, dateOfBirthField,
                submitButton);

        // Configurer la scène et afficher la boîte de dialogue
        Scene dialogScene = new Scene(dialogVBox, 350, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void initialize() {
        // loadFXML("students.fxml");
        // setupNavigationHandlers();
        /*---------------------------------------------------------------------
         * --------------------------------------------------------------------
         */
        // Lier les colonnes aux propriétés
        moduleColumn.setCellValueFactory(new PropertyValueFactory<>("module"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("classe"));

        List<Subject> subjectsData = new ArrayList<>();
        try {
            subjectsData = SubjectDAO.getAllSubjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<Subject> data = FXCollections.observableArrayList(subjectsData);

        moduleColumn.setStyle("-fx-alignment: CENTER;");
        classColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setStyle("-fx-alignment: CENTER;");

        tableView.setItems(data);

        // // Ajouter les boutons dans la colonne "Actions"
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();

            {
                ImageView editIcon = new ImageView(
                        new Image(getClass().getResource("/images/mynaui_edit.png").toExternalForm()));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editButton.setGraphic(editIcon);
                editButton.setPrefWidth(125);
                editButton.setTranslateX(5);

                ImageView deleteIcon = new ImageView(
                        new Image(getClass().getResource("/images/Vector.png").toExternalForm()));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setPrefWidth(125);
                deleteButton.setTranslateX(10);

                editButton.setOnAction(event -> {
                    Subject module = getTableView().getItems().get(getIndex());
                    Dialog<Subject> dialog = new Dialog<>();
                    dialog.setTitle("Modifier le module");
                    dialog.setHeaderText("Modifier les informations du module");

                    ButtonType saveButtonType = new ButtonType("Enregistrer");
                    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField moduleNameField = new TextField(module.getModule());
                    String oldSubjectName = module.getModule();
                    // ComboBox<String> classeCombobox = new ComboBox<>();
                    ComboBox<String> classComboBox = new ComboBox<>();
                    try {
                        List<Classes> classes = ClassesDAO.getClasses();
                        for (Classes classe : classes) {
                            classComboBox.getItems().add(classe.getClasse());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    classComboBox.setPromptText("Sélectionnez une classe");
                    classComboBox.setValue(module.getClasse());

                    grid.add(new Label("Nom du module:"), 0, 0);
                    grid.add(moduleNameField, 1, 0);
                    grid.add(new Label("Classe:"), 0, 1);
                    grid.add(classComboBox, 1, 1);
                    grid.add(new Label("Description:"), 0, 2);
                    TextArea descriptionArea = new TextArea();
                    descriptionArea.setPrefRowCount(3);
                    descriptionArea.setPrefColumnCount(20);
                    grid.add(descriptionArea, 1, 2);
                    dialog.getDialogPane().setContent(grid);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == saveButtonType) {
                            try {
                                String selectedClass = classComboBox.getValue();
                                String selectedSubject = moduleNameField.getText();
                                module.setModule(selectedSubject);
                                module.setClasse(selectedClass);

                                SubjectDAO.updateSubject(oldSubjectName, selectedSubject, selectedClass, "");

                                // SubjectDAO.updateSubject(module, getIndex(), module.getClasse());

                                getTableView().refresh();
                            } catch (Exception e) {
                                System.out.println("The erro is about:" + e.getMessage());
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText(null);
                                alert.setContentText("Erreur lors de la modification du module.");
                                alert.showAndWait();
                            } finally {
                                dialog.close();
                            }
                        }
                        return null;
                    });

                    dialog.showAndWait();
                });

                deleteButton.setOnAction(event -> {
                    Subject module = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(module);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(editButton, deleteButton);
                    actions.setSpacing(10);
                    setGraphic(actions);
                }
            }
        });
        add.setOnAction(event -> showAddDialog()); /*-------------------------------------------------------------
         * -------------------------------------------------------------
         */
        /*------------ICI les donnees de tableau 2------------- */
        name.setCellValueFactory(new PropertyValueFactory<>("classe"));
        field.setCellValueFactory(new PropertyValueFactory<>("filiere"));

        List<Classes> classes = new ArrayList<>();
        try {
            classes = ClassesDAO.getClasses();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<Classes> data1 = FXCollections.observableArrayList(classes);
        TableView2.setItems(data1);
        name.setStyle("-fx-alignment: CENTER;");
        field.setStyle("-fx-alignment: CENTER;");
        actions1.setStyle("-fx-alignment: CENTER;");

        actions1.setCellFactory(column -> new TableCell<Classes, Void>() {
            private final Button editClasseButton = new Button();
            private final Button deleteClasseButton = new Button();
            private final Button viewStudentsButton = new Button();

            {
                // Configuration du bouton View Students
                ImageView viewStudentsIcon = new ImageView(
                        new Image(getClass().getResource("./images/mdi_eye-outline.png").toExternalForm()));
                viewStudentsIcon.setFitHeight(16);
                viewStudentsIcon.setFitWidth(16);
                viewStudentsButton.setGraphic(viewStudentsIcon);
                viewStudentsButton.setPrefWidth(55);
                viewStudentsButton.setTranslateX(5);
                // Configuration du bouton Modifier pour les Classes
                ImageView editClasseIcon = new ImageView(
                        new Image(getClass().getResource("/images/mynaui_edit.png").toExternalForm()));
                editClasseIcon.setFitHeight(16);
                editClasseIcon.setFitWidth(16);
                editClasseButton.setGraphic(editClasseIcon);
                editClasseButton.setPrefWidth(55);
                editClasseButton.setTranslateX(5);

                viewStudentsButton.setOnAction(event -> {
                    Classes selectedClasse = getTableView().getItems().get(getIndex());
                    try {
                        List<Student> students = StudentDAO.getStudentsByClassName(selectedClasse.getClasse());
                        ObservableList<Student> studentData = FXCollections.observableArrayList(students);
                        studentsTable.setItems(studentData);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Database Error");
                        alert.setContentText("An error occurred while fetching students: " + e.getMessage());
                        alert.showAndWait();
                        e.printStackTrace();
                    }
                });

                editClasseButton.setOnAction(event -> {
                    Classes selectedClasse = getTableView().getItems().get(getIndex());
                    Dialog<Classes> dialog = new Dialog<>();
                    dialog.setTitle("Modifier la classe");
                    dialog.setHeaderText("Modifier les informations de la classe");

                    ButtonType saveButtonType = new ButtonType("Enregistrer");
                    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField classNameField = new TextField(selectedClasse.getClasse());
                    ComboBox<String> filiereComboBox = new ComboBox<>();
                    filiereComboBox.getItems().addAll(
                            "GI",
                            "GE",
                            "TM",
                            "TCC");
                    filiereComboBox.setValue(selectedClasse.getFiliere());
                    TextArea descriptionField = new TextArea("optionnel");
                    descriptionField.setPrefRowCount(3);
                    descriptionField.setWrapText(true);

                    grid.add(new Label("Nom de la classe:"), 0, 0);
                    grid.add(classNameField, 1, 0);
                    grid.add(new Label("Filière:"), 0, 1);
                    grid.add(filiereComboBox, 1, 1);
                    grid.add(new Label("Description:"), 0, 2);
                    grid.add(descriptionField, 1, 2);

                    dialog.getDialogPane().setContent(grid);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == saveButtonType) {
                            try {
                                String oldClassName = selectedClasse.getClasse();
                                String newClasseName = classNameField.getText();
                                String newFiliere = filiereComboBox.getValue();
                                String newDescription = descriptionField.getText();
                                selectedClasse.setClasse(newClasseName);
                                selectedClasse.setFiliere(newFiliere);
                                ClassesDAO.updateClassByName(oldClassName, newClasseName, newFiliere, newDescription);
                                getTableView().refresh();
                                return selectedClasse;
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Database Error");
                                alert.setContentText("An error occurred while updating the class: " + e.getMessage());
                                alert.showAndWait();
                                e.printStackTrace();
                                return null;
                            }
                        }
                        return null;
                    });

                    dialog.showAndWait();
                });
                // Configuration du bouton Supprimer pour les Classes
                ImageView deleteClasseIcon = new ImageView(
                        new Image(getClass().getResource("/images/Vector.png").toExternalForm()));
                deleteClasseIcon.setFitHeight(16);
                deleteClasseIcon.setFitWidth(16);
                deleteClasseButton.setGraphic(deleteClasseIcon);
                deleteClasseButton.setPrefWidth(55);
                deleteClasseButton.setTranslateX(10);

                deleteClasseButton.setOnAction(event -> {
                    Classes selectedClasse = getTableView().getItems().get(getIndex());
                    try {
                        ClassesDAO.removeClassByName(selectedClasse.getClasse());
                        getTableView().getItems().remove(selectedClasse);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Database Error");
                        alert.setContentText("An error occurred while deleting the class: " + e.getMessage());
                        alert.showAndWait();
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionsBox = new HBox(editClasseButton, viewStudentsButton, deleteClasseButton);
                    actionsBox.setSpacing(10);
                    setGraphic(actionsBox);
                }
            }
        });
        add1.setOnAction(event -> showAddDialogForClasses());
        /*
         * Tableau3------------------------------------------
         * 
         * -------------------------------------------
         */
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        ObservableList<Student> studentData = FXCollections.observableArrayList(
                new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001"),
                new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001"),
                new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001"),
                new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001"));

        // Ajouter les données dans la TableView
        studentsTable.setItems(studentData);
        fullNameColumn.setStyle("-fx-alignment: CENTER;");
        emailColumn.setStyle("-fx-alignment: CENTER;");
        phoneNumberColumn.setStyle("-fx-alignment: CENTER;");
        genderColumn.setStyle("-fx-alignment: CENTER;");
        dateOfBirthColumn.setStyle("-fx-alignment: CENTER;");

        add2.setOnAction(event -> showAddDialogForStudents());
    }
}