package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import p1.Result;
import p2.DatabaseManager;

public class ResultManagementUI extends Application {
    private ObservableList<Result> results = FXCollections.observableArrayList();

    private TableView<Result> tableView;
    private Button addButton = new Button("Add Result");
    private Button editButton = new Button("Edit Result");
    private Button validateButton = new Button("Validate Result");

    @Override
    public void start(Stage primaryStage) {
        tableView = new TableView<>();
        tableView.setItems(results);
        setupTableView();
        loadResults();

        // Button area for Add, Edit, and Validate
        HBox buttonArea = new HBox(10, addButton, editButton, validateButton);
        addButton.setOnAction(e -> showAddResultDialog());
        editButton.setOnAction(e -> showEditResultDialog());
        validateButton.setOnAction(e -> validateResult());

        VBox root = new VBox(10, tableView, buttonArea);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Result Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTableView() {
        TableColumn<Result, Integer> eventIdColumn = new TableColumn<>("Event ID");
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("competitionEventId"));

        TableColumn<Result, Integer> athleteIdColumn = new TableColumn<>("Athlete ID");
        athleteIdColumn.setCellValueFactory(new PropertyValueFactory<>("athleteId"));

        TableColumn<Result, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<Result, Integer> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn<Result, String> medalColumn = new TableColumn<>("Medal");
        medalColumn.setCellValueFactory(new PropertyValueFactory<>("medal"));

        tableView.getColumns().addAll(eventIdColumn, athleteIdColumn, scoreColumn, rankColumn, medalColumn);
    }


    private void loadResults() {
        results.addAll(DatabaseManager.loadResults());
    }

    private void showAddResultDialog() {
        // Implement a dialog to add new results
        Dialog<Result> dialog = new ResultDialog(null);
        dialog.showAndWait().ifPresent(result -> {
            DatabaseManager.addResult(result);
            results.add(result);
        });
    }

    private void showEditResultDialog() {
        Result selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Result> dialog = new ResultDialog(selected);
            dialog.showAndWait().ifPresent(result -> {
                DatabaseManager.updateResult(result);
                int index = results.indexOf(selected);
                results.set(index, result);
            });
        }
    }

    private void validateResult() {
        Result selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null && isValidResult(selected)) {
            showAlert(Alert.AlertType.INFORMATION, "Validation Success", "This result is valid.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "This result is not valid.");
        }
    }

    private boolean isValidResult(Result result) {
        // Add validation logic here
        return result.getScore() >= 0; // Example validation
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
