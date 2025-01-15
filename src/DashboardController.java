import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import db.AttendanceDAO;
import db.ClassesDAO;
import db.ReclamationDAO;
import db.SessionDAO;
import db.StudentDAO;
import db.SubjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Classes;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.Node;
import db.AttendanceDAO;

public class DashboardController {

    @FXML
    private PieChart pieChart;

    @FXML
    private VBox legendBox; // Un VBox pour la légende dans le FXML

    @FXML
    private AnchorPane Container;

    @FXML
    private HBox legendBar;

    @FXML
    private Label studentNb;

    @FXML
    private MenuButton classMenu;

    @FXML
    private MenuButton weekMenuButton;

    @FXML
    private Label subjectsNumLabel;

    @FXML
    private Label sessionsNumLabel;

    @FXML
    private Label reclamationsNumLabel;

    // @Xml
    // private AnchorPane contentPane;
    int[] studentFemaleAndMalePercentage = { 0, 0 };

        void refreshPieChart() {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Garcons", studentFemaleAndMalePercentage[0]),
                    new PieChart.Data("Filles", studentFemaleAndMalePercentage[1]));
            pieChart.setData(pieChartData);

            // Calculer les pourcentages et mettre à jour les noms
            double total = pieChartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();
            if (total == 0) {
                pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Aucun étudiant", 1));
                pieChart.setData(pieChartData);
            } else {
                pieChartData.forEach(data -> {
                    double percentage = (data.getPieValue() / total) * 100;
                    data.setName(data.getName() + " (" + String.format("%.1f%%", percentage) + ")");
                });
            }

            Text totalText = new Text("Total des étudiants : " + total);
            totalText.setStyle("-fx-font-size: 14; -fx-font-style: italic;");

            // Personnaliser les couleurs des segments
            for (PieChart.Data data : pieChartData) {
                if (data.getName().startsWith("Garcons")) {
                    data.getNode().setStyle("-fx-pie-color: #4C8CF8;");
                } else if (data.getName().startsWith("Filles")) {
                    data.getNode().setStyle("-fx-pie-color: #1FE6D1;");
                } else if (data.getName().startsWith("Aucun")) {
                    data.getNode().setStyle("-fx-pie-color: #CCCCCC;");
                }
            }
            pieChart.setLegendVisible(false);

            // Créer une légende personnalisée
            legendBox.getChildren().clear();
            if (total == 0) {
                createLegendItem("Aucun étudiant", Color.web("#CCCCCC"));
            } else {
                String garconsLabel = String.format("Garcons (%d)", studentFemaleAndMalePercentage[0]);
                String fillesLabel = String.format("Filles (%d)", studentFemaleAndMalePercentage[1]);
                createLegendItem(garconsLabel, Color.web("#4C8CF8"));
                createLegendItem(fillesLabel, Color.web("#1FE6D1"));
            }

            // Add some vertical spacing
            legendBox.setTranslateY(40);

            // Définir les axes du BarChart
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setCategories(
                    FXCollections.<String>observableArrayList(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri")));
        }
    void setBarChart(XYChart.Series<String, Number> presentData, XYChart.Series<String, Number> absentData,
            Map<String, Map<String, Integer>> attendanceData) {

        presentData.setName("Présent");
        presentData.getData().add(new XYChart.Data<>("Mon",
                attendanceData.get("Monday") != null ? attendanceData.get("Monday").get("present_count") : 0)); // Exemple//
                                                                                                                // données
        presentData.getData().add(new XYChart.Data<>("Tue",
                attendanceData.get("Tuesday") != null ? attendanceData.get("Tuesday").get("present_count") : 0));
        presentData.getData().add(new XYChart.Data<>("Wed",
                attendanceData.get("Wednesday") != null ? attendanceData.get("Wednesday").get("present_count") : 0));
        presentData.getData().add(new XYChart.Data<>("Thu",
                attendanceData.get("Thursday") != null ? attendanceData.get("Thursday").get("present_count") : 0));
        presentData.getData().add(new XYChart.Data<>("Fri",
                attendanceData.get("Friday") != null ? attendanceData.get("Friday").get("present_count") : 0));

        absentData.setName("Absent");
        absentData.getData().add(new XYChart.Data<>("Mon",
                attendanceData.get("Monday") != null ? attendanceData.get("Monday").get("absent_count") : 0));
        absentData.getData().add(new XYChart.Data<>("Tue",
                attendanceData.get("Tuesday") != null ? attendanceData.get("Tuesday").get("absent_count") : 0));
        absentData.getData().add(new XYChart.Data<>("Wed",
                attendanceData.get("Wednesday") != null ? attendanceData.get("Wednesday").get("absent_count") : 0));
        absentData.getData().add(new XYChart.Data<>("Thu",
                attendanceData.get("Thursday") != null ? attendanceData.get("Thursday").get("absent_count") : 0));
        absentData.getData().add(new XYChart.Data<>("Fri",
                attendanceData.get("Friday") != null ? attendanceData.get("Friday").get("absent_count") : 0));

    }

    public void initialize() {
        // initialize data
        XYChart.Series<String, Number> presentData = new XYChart.Series<>();
        XYChart.Series<String, Number> absentData = new XYChart.Series<>();
        try {
            studentFemaleAndMalePercentage = StudentDAO.getStudentCountsByGender();
            String studentsNum = studentFemaleAndMalePercentage[0] + studentFemaleAndMalePercentage[1] + "";
            studentNb.setText(studentsNum);
            int subjectsNum = SubjectDAO.getSubjectCount();
            subjectsNumLabel.setText(subjectsNum + "");
            int sessionsNum = SessionDAO.getSessionsCount();
            sessionsNumLabel.setText(sessionsNum + "");
            int reclamationsNum = ReclamationDAO.getReclamationsCount();
            reclamationsNumLabel.setText(reclamationsNum + "");


            // Add class menu items
            List<Classes> classes = ClassesDAO.getClasses();
            for (Classes classe : classes) {
                javafx.scene.control.MenuItem menuItem = new javafx.scene.control.MenuItem(classe.getClasse());
                menuItem.setOnAction(event -> {
                    classMenu.setText(classe.getClasse());
                    try {
                        Map<String, Map<String, Integer>> attendanceData;
                        if (weekMenuButton.getText() != null && !weekMenuButton.getText().isEmpty() && !weekMenuButton.getText().equals("cette semaine")) {
                            attendanceData = AttendanceDAO
                                    .getLastWeekAttendanceRateByClass(classe.getClasse(), Integer.parseInt(weekMenuButton.getText().split(" ")[1]));
                                } else {
                            attendanceData = AttendanceDAO
                                    .getLastWeekAttendanceRateByClass(classe.getClasse());
                        }
                        setBarChart(presentData, absentData, attendanceData);                    
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    studentFemaleAndMalePercentage = StudentDAO.getStudentCountsByGender(classe.getClasse());
                    refreshPieChart();
                });

                classMenu.getItems().add(menuItem);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ajouter des données au PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Garcons", studentFemaleAndMalePercentage[0]),
                new PieChart.Data("Filles", studentFemaleAndMalePercentage[1]));
        pieChart.setData(pieChartData);

        // Calculer les pourcentages et mettre à jour les noms
        double total = pieChartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        pieChartData.forEach(data -> {
            double percentage = (data.getPieValue() / total) * 100;
            data.setName(data.getName() + " (" + String.format("%.1f%%", percentage) + ")");
        });

        Text totalText = new Text("Total des étudiants : " + total);
        totalText.setStyle("-fx-font-size: 14; -fx-font-style: italic;");

        // Personnaliser les couleurs des segments
        for (PieChart.Data data : pieChartData) {
            if (data.getName().startsWith("Garcons")) {
                data.getNode().setStyle("-fx-pie-color: #4C8CF8;");
            } else if (data.getName().startsWith("Filles")) {
                data.getNode().setStyle("-fx-pie-color: #1FE6D1;");
            }
        }

        // Add week menu items
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        int totalWeeks = now.get(weekFields.weekOfWeekBasedYear());

        List<String> weeks = new ArrayList<>();
        for (int i = totalWeeks; i >= 1; i--) {
            weeks.add(String.valueOf(i));
        }
        for (String week : weeks) {
            MenuItem menuItem = new MenuItem("Week " + week);
            menuItem.setOnAction(event -> {
                weekMenuButton.setText("Week " + week);
                try {
                    Map<String, Map<String, Integer>> attendanceData = AttendanceDAO
                            .getLastWeekAttendanceRateByClass(classMenu.getText(), Integer.parseInt(week));
                    setBarChart(presentData, absentData, attendanceData);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            weekMenuButton.getItems().add(menuItem);
        }
        // Désactiver la légende automatique
        pieChart.setLegendVisible(false);

        // Créer une légende personnalisée
        legendBox.getChildren().clear();
        legendBox.setTranslateY(40);
        String garconsLabel = String.format("Garcons (%d)", studentFemaleAndMalePercentage[0]);
        String fillesLabel = String.format("Filles (%d)", studentFemaleAndMalePercentage[1]);
        createLegendItem(garconsLabel, Color.web("#4C8CF8"));
        createLegendItem(fillesLabel, Color.web("#1FE6D1")); // Définir les axes du BarChart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(
                FXCollections.<String>observableArrayList(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri")));
        NumberAxis yAxis = new NumberAxis();

        // Créer le graphique
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Présence et Absence Hebdomadaire");
        Map<String, Map<String, Integer>> attendanceData = null;
        try {
            attendanceData = AttendanceDAO.getLastWeekAttendanceRateByClass();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajouter les séries de données

        setBarChart(presentData, absentData, attendanceData);

        barChart.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            for (XYChart.Data<String, Number> data : presentData.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: #4C8CF8;");
                }
            }
            for (XYChart.Data<String, Number> data : absentData.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: #1FE6D1;");
                }
            }
        });

        // Ajouter les séries au graphique
        barChart.getData().addAll(presentData, absentData);

        // Ajouter le graphique au conteneur FXML
        Container.getChildren().add(barChart);
        AnchorPane.setTopAnchor(barChart, 0.0);
        AnchorPane.setBottomAnchor(barChart, 0.0);
        AnchorPane.setLeftAnchor(barChart, 0.0);
        AnchorPane.setRightAnchor(barChart, 0.0);
    }

    private void createLegendItem(String label, Color color) {
        // Créer un petit cercle pour représenter la couleur
        Circle circle = new Circle(10, color);

        // Créer un texte pour la légende
        Text text = new Text(label);
        text.setStyle("-fx-font-size: 14;");

        // Ajouter le cercle et le texte dans un HBox
        HBox legendItem = new HBox(10, circle, text);
        legendBox.getChildren().add(legendItem);
    }

    // private void loadPage(String fxmlFile) throws IOException {
    // // Load the new content into the container
    // AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
    // contentPane.getChildren().setAll(pane);
    // }
}
