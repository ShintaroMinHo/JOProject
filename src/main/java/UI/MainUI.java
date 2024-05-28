package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main UI");

        Button athleteButton = new Button("Athlete Management");
        Button eventButton = new Button("Event Management");
        Button resultButton = new Button("Result Management");
        Button medalsAnalysisButton = new Button("Medals Analysis");

        athleteButton.setOnAction(e -> new AthleteUI().start(new Stage()));
        eventButton.setOnAction(e -> new EventManagementUI().start(new Stage()));
        resultButton.setOnAction(e -> new ResultManagementUI().start(new Stage()));
        medalsAnalysisButton.setOnAction(e -> new MedalsAnalysisUI().start(new Stage()));

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(athleteButton, 0, 0);
        grid.add(eventButton, 1, 0);
        grid.add(resultButton, 0, 1);
        grid.add(medalsAnalysisButton, 1, 1);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}