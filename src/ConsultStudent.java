
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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



    public void initialize() {
        // Ajouter des données au PieChart
        // Ajouter des données au PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Presence", 60),
            new PieChart.Data("Abscence justifiée", 50),
            new PieChart.Data("Abscence non justifiée", 40)
        );
        pieChart1.setData(pieChartData);

        // Calculer les pourcentages et mettre à jour les noms
        double total = pieChartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        pieChartData.forEach(data -> {
            double percentage = (data.getPieValue() / total) * 100;
            data.setName(data.getName() + " (" + String.format("%.1f%%", percentage) + ")");
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
        createLegendItem("Presence (60)", Color.web("#4C8CF8"));
        createLegendItem("Abscence justifiée (50)", Color.web("#1FE6D1"));
        createLegendItem("Abscence non justifiée (40)", Color.web("#D0F97E"));
        // Créer une légende personnalisée


        // Configurer les colonnes pour utiliser les propriétés de la classe Student
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        // Ajouter des données dans le tableau
        ObservableList<Student> students = FXCollections.observableArrayList(
            new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001"),
            new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001"),
            new Student("Mohamed Izourne", "izourne@gmail.com", "+212 683925793", "Male", "22 mai 2001")
        );
        tableView.setItems(students);
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

        ObservableList<DaysSchedule> data = FXCollections.observableArrayList(
            new DaysSchedule("✔", "✘", "—", "✔", "✘", "—", "✔")
            
        );
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
