package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import p2.DatabaseManager;

public class MedalsAnalysisUI extends Application {
    private BarChart<String, Number> barChart;
    private TableView<MedalDetail> medalDetailsTable;
    public static class MedalDetail {
        private String nationality;
        private String athleteName;
        private String medal;

        public MedalDetail(String nationality, String athleteName, String medal) {
            this.nationality = nationality;
            this.athleteName = athleteName;
            this.medal = medal;
        }

        // Getters
        public String getNationality() {
            return nationality;
        }

        public String getAthleteName() {
            return athleteName;
        }

        public String getMedal() {
            return medal;
        }

    }

    @Override
    public void start(Stage primaryStage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Medal Counts by Country");

        medalDetailsTable = new TableView<>();
        setupTableView();
        refreshData(); // Load data

        Button refreshButton = new Button("Refresh Data");
        refreshButton.setOnAction(e -> refreshData());

        VBox root = new VBox(10, barChart, medalDetailsTable, refreshButton);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Medals Analysis");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTableView() {
        TableColumn<MedalDetail, String> nationalityColumn = new TableColumn<>("Nationality");
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        TableColumn<MedalDetail, String> athleteColumn = new TableColumn<>("Athlete");
        athleteColumn.setCellValueFactory(new PropertyValueFactory<>("athleteName"));
        TableColumn<MedalDetail, String> medalColumn = new TableColumn<>("Medal");
        medalColumn.setCellValueFactory(new PropertyValueFactory<>("medal"));

        medalDetailsTable.getColumns().addAll(nationalityColumn, athleteColumn, medalColumn);
    }

    private void refreshData() {
        ObservableList<XYChart.Series<String, Number>> seriesData = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(DatabaseManager.loadMedalCountsByNationality());
        seriesData.add(series);
        barChart.setData(seriesData);

        medalDetailsTable.setItems(DatabaseManager.loadMedalDetails());
    }

    public static void main(String[] args) {
        launch(args);
    }
}