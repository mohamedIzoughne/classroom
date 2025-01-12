import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import db.ClassesDAO;
import db.StudentDAO;
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
import javafx.scene.Node;

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
        pieChart.setLegendVisible(false);

        // Créer une légende personnalisée
        legendBox.getChildren().clear();
        String garconsLabel = String.format("Garcons (%d)", studentFemaleAndMalePercentage[0]);
        String fillesLabel = String.format("Filles (%d)", studentFemaleAndMalePercentage[1]);
        createLegendItem(garconsLabel, Color.web("#4C8CF8"));
        createLegendItem(fillesLabel, Color.web("#1FE6D1"));

        // Add some vertical spacing
        legendBox.setTranslateY(40);

        // Définir les axes du BarChart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(
                FXCollections.<String>observableArrayList(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri")));

    }

    public void initialize() {
        // initialize data
        try {
            studentFemaleAndMalePercentage = StudentDAO.getStudentCountsByGender();
            String studentsNum = studentFemaleAndMalePercentage[0] + studentFemaleAndMalePercentage[1] + "";
            studentNb.setText(studentsNum);

            // Add class menu items
            List<Classes> classes = ClassesDAO.getClasses();
            for (Classes classe : classes) {
                javafx.scene.control.MenuItem menuItem = new javafx.scene.control.MenuItem(classe.getClasse());
                menuItem.setOnAction(event -> {
                    classMenu.setText(classe.getClasse());
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

        // Ajouter les séries de données
        XYChart.Series<String, Number> presentData = new XYChart.Series<>();
        presentData.setName("Présent");
        presentData.getData().add(new XYChart.Data<>("Mon", 43)); // Exemple de données
        presentData.getData().add(new XYChart.Data<>("Tue", 37));
        presentData.getData().add(new XYChart.Data<>("Wed", 13));
        presentData.getData().add(new XYChart.Data<>("Thu", 88));
        presentData.getData().add(new XYChart.Data<>("Fri", 29));

        XYChart.Series<String, Number> absentData = new XYChart.Series<>();
        absentData.setName("Absent");
        absentData.getData().add(new XYChart.Data<>("Mon", 55)); // Exemple de données
        absentData.getData().add(new XYChart.Data<>("Tue", 71));
        absentData.getData().add(new XYChart.Data<>("Wed", 14));
        absentData.getData().add(new XYChart.Data<>("Thu", 73));
        absentData.getData().add(new XYChart.Data<>("Fri", 72));
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
