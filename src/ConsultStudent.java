
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import db.AttendanceDAO;
import db.ClassesDAO;
import db.StudentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Classes;
import models.Student;


public class ConsultStudent {
    @FXML
    private PieChart pieChart1;

    @FXML
    private VBox legendBox1;

    @FXML
    private TableView<Student> tableView;

    @FXML
    private TableColumn<Student, String> fullNameColumn;

    @FXML
    private TableColumn<Student, String> emailColumn;

    @FXML
    private TableColumn<Student, String> phoneColumn;

    @FXML
    private TableColumn<Student, String> genderColumn;

    @FXML
    private TableColumn<Student, String> dobColumn;

    @FXML
    private TableView<DaysSchedule> tableView1;

    @FXML
    private TableColumn<DaysSchedule,String> colMon;

    @FXML
    private TableColumn<DaysSchedule, String> colTue;
    @FXML
    private TableColumn<DaysSchedule, String> colWed;
    @FXML
    private TableColumn<DaysSchedule, String> colThu;
    @FXML
    private TableColumn<DaysSchedule, String> colFri;
    @FXML
    private TableColumn<DaysSchedule, String> colSat;
    @FXML
    private TableColumn<DaysSchedule, String> colSun;

    @FXML
        private TextField searchField;

    @FXML
    void searchStudentHandler() {
        searchStudent();
    }


    @FXML
    private MenuButton classesMenu;
    
    private String selectedClass = "";

    void searchStudent() {
        String searchText = searchField.getText().toLowerCase();
        List<Student> filteredStudents = StudentDAO.getStudentByName(searchText, selectedClass);
        
        ObservableList<Student> students = FXCollections.observableArrayList(filteredStudents);

        tableView.setItems(students);
    }

    Student selected = null;

    DaysSchedule schedule = new DaysSchedule("—", "—", "—", "—", "—", "—", "—");
    public void initialize() {
        // Ajouter des données au PieChart
        
                // Add row selection listener to tableView
                tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Student selectedStudent = newSelection;
                        String fullName = selectedStudent.getFullName();
                        selected = selectedStudent;
                        try {
                            Map<String, Integer> scheduleData = AttendanceDAO.getWeeklyAttendanceByStudent(fullName);
                            int[] stats = AttendanceDAO.getAttendanceStatistics(selectedStudent);
                            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                                new PieChart.Data("Presence", stats[0]),
                                new PieChart.Data("Abscence justifiée",stats[1]),
                                new PieChart.Data("Abscence non justifiée", stats[2])
                            );
                            pieChart1.setData(pieChartData);
                    
                            // Calculer les pourcentages et mettre à jour les noms
                            double total = pieChartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();
                            pieChartData.forEach(pieData -> {
                                double percentage = (pieData.getPieValue() / total) * 100;
                                pieData.setName(pieData.getName() + " (" + String.format("%.1f%%", percentage) + ")");
                            });
                    
                            Text totalText = new Text("Total des étudiants : " + total);
                            totalText.setStyle("-fx-font-size: 14; -fx-font-style: italic;");
                    
                            // Personnaliser les couleurs des segments
                            pieChartData.get(0).getNode().setStyle("-fx-pie-color: #4C8CF8;"); // Bleu pour "Boys"
                            pieChartData.get(1).getNode().setStyle("-fx-pie-color: #1FE6D1;"); // Rouge pour "Girls"
                            pieChartData.get(2).getNode().setStyle("-fx-pie-color: #D0F97E;");
                    
                            // Désactiver la légende automatique
                            pieChart1.setLegendVisible(false);
                    
                            // Créer une légende personnalisée
                            legendBox1.getChildren().clear();
                            createLegendItem("Presence (" + stats[0] + ")", Color.web("#4C8CF8"));
                            createLegendItem("Abscence justifiée (" + stats[1] + ")", Color.web("#1FE6D1"));
                            createLegendItem("Abscence non justifiée (" + stats[2] + ")", Color.web("#D0F97E"));                            // Créer une légende personnalisée
                            schedule = new DaysSchedule(
                                scheduleData.get("Monday") == 1 ? "✔" : scheduleData.get("Monday") == 0 ? "✘" : "—",
                                scheduleData.get("Tuesday") == 1 ? "✔" : scheduleData.get("Tuesday") == 0 ? "✘" : "—",
                                scheduleData.get("Wednesday") == 1 ? "✔" : scheduleData.get("Wednesday") == 0 ? "✘" : "—",
                                scheduleData.get("Thursday") == 1 ? "✔" : scheduleData.get("Thursday") == 0 ? "✘" : "—",
                                scheduleData.get("Friday") == 1 ? "✔" : scheduleData.get("Friday") == 0 ? "✘" : "—",
                                scheduleData.get("Saturday") == 1 ? "✔" : scheduleData.get("Saturday") == 0 ? "✘" : "—",
                                "—"
                            );
                            ObservableList<DaysSchedule> data = FXCollections.observableArrayList(schedule);
                            tableView1.setItems(data);
                            // schedule.set(scheduleData.get("Monday"));
                            for (Map.Entry<String, Integer> entry : scheduleData.entrySet()) {
                                System.out.println(entry.getKey() + ": " + entry.getValue());
                            }
                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                        // Add more fields as needed based on your Student class properties
                    }
                });
        

        try {
            // Add class menu items
            List<Classes> classes = ClassesDAO.getClasses();
            for (Classes classe : classes) {
                javafx.scene.control.MenuItem menuItem = new javafx.scene.control.MenuItem(classe.getClasse());
                menuItem.setOnAction(event -> {
                    classesMenu.setText(classe.getClasse());
                    selectedClass = classe.getClasse();
                    searchStudent();
                });

                classesMenu.getItems().add(menuItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        // Ajouter des données au PieChart
        
    


        // Configurer les colonnes pour utiliser les propriétés de la classe Student
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        // Ajouter des données dans le tableau
        fullNameColumn.setStyle("-fx-alignment: CENTER;");
        emailColumn.setStyle("-fx-alignment: CENTER;");
        phoneColumn.setStyle("-fx-alignment: CENTER;");
        genderColumn.setStyle("-fx-alignment: CENTER;");
        dobColumn.setStyle("-fx-alignment: CENTER;");


        colMon.setCellValueFactory(cellData -> cellData.getValue().mondayProperty());
        colTue.setCellValueFactory(cellData -> cellData.getValue().tuesdayProperty());
        colWed.setCellValueFactory(cellData -> cellData.getValue().wednesdayProperty());
        colThu.setCellValueFactory(cellData -> cellData.getValue().thursdayProperty());
        colFri.setCellValueFactory(cellData -> cellData.getValue().fridayProperty());
        colSat.setCellValueFactory(cellData -> cellData.getValue().saturdayProperty());
        colSun.setCellValueFactory(cellData -> cellData.getValue().sundayProperty());

        configureColumn(colMon);
        configureColumn(colTue);
        configureColumn(colWed);
        configureColumn(colThu);
        configureColumn(colFri);
        configureColumn(colSat);
        configureColumn(colSun);

        ObservableList<DaysSchedule> data = FXCollections.observableArrayList(schedule);
        tableView1.setItems(data);
   
}

private void createLegendItem(String label, Color color) {
    // Créer un petit cercle pour représenter la couleur
    Circle circle = new Circle(10, color);

    // Créer un texte pour la légende
    Text text = new Text(label);
    text.setStyle("-fx-font-size: 14;");

    // Ajouter le cercle et le texte dans un HBox
    HBox legendItem = new HBox(10, circle, text);
    legendBox1.getChildren().add(legendItem);
}


private void configureColumn(TableColumn<DaysSchedule, String> column) {
    column.setCellFactory(col -> new TableCell<DaysSchedule, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setStyle("");
            } else {
                setText(item); // Affiche "✔", "✘", ou "—"
                if (item.equals("✔")) {
                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;-fx-font-size: 50px;-fx-alignment:CENTER");
                } else if (item.equals("✘")) {
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 50px ; -fx-alignment:CENTER");
                } else {
                    setStyle("-fx-text-fill: orange;-fx-font-weight: bold;-fx-font-size: 50px ; -fx-alignment:CENTER");
                }
            }
        }
    });
}

}
