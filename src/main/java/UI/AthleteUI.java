
package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import p1.Athlete;
import p1.SportEvent;
import p2.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class AthleteUI extends Application {
    private ObservableList<Athlete> athletes = FXCollections.observableArrayList();

    private ObservableList<SportEvent> availableEvents = FXCollections.observableArrayList(
            new SportEvent(1, "100m Dash", "Track and Field"),
            new SportEvent(2, "Marathon", "Road Race")
    );

    private ListView<Athlete> listView;
    private TextField nameField = new TextField();
    private TextField nationalityField = new TextField();
    private TextField ageField = new TextField();
    private ComboBox<String> genderComboBox = new ComboBox<>(FXCollections.observableArrayList("M", "F"));
    private ListView<SportEvent> eventListView;
    private Button addButton = new Button("Add Athlete");
    private Button deleteButton = new Button("Delete Athlete");

    @Override
    public void start(Stage primaryStage) {
        // Initialize ObservableLists
        athletes = FXCollections.observableArrayList(DatabaseManager.loadAthletes());  // Load athletes from the database
        listView = new ListView<>(athletes);
        eventListView = new ListView<>(availableEvents);

        VBox root = new VBox(10);
        HBox inputArea = new HBox(10);
        HBox eventSelectionArea = new HBox(10);

        setupListView();
        configureInputArea(inputArea);
        configureEventSelectionArea(eventSelectionArea);

        root.getChildren().addAll(listView, inputArea, eventSelectionArea);

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setTitle("Athlete Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupListView() {
        listView.setCellFactory(param -> new ListCell<Athlete>() {
            @Override
            protected void updateItem(Athlete item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String text = String.format("%s - %s, Age: %d, Gender: %s",
                            item.getName(), item.getNationality(), item.getAge(), item.getGender());
                    setText(text);
                    // Assuming `getEvents` returns a list of `SportEvent` objects
                    String eventsText = item.getEvents().stream()
                            .map(SportEvent::getName)
                            .reduce("", (acc, name) -> acc + name + ", ");
                    Tooltip tooltip = new Tooltip(eventsText);
                    setTooltip(tooltip);
                }
            }
        });
    }


    private void configureInputArea(HBox inputArea) {
        // Creating labels for each input field
        Label nameLabel = new Label("Name:");
        Label nationalityLabel = new Label("Nationality:");
        Label ageLabel = new Label("Age:");
        Label genderLabel = new Label("Gender:");

        // Set up input fields with corresponding labels
        VBox nameBox = new VBox(5, nameLabel, nameField);
        VBox nationalityBox = new VBox(5, nationalityLabel, nationalityField);
        VBox ageBox = new VBox(5, ageLabel, ageField);
        VBox genderBox = new VBox(5, genderLabel, genderComboBox);

        // Set up buttons with some spacing
        VBox buttonBox = new VBox(5, addButton, deleteButton);

        // Adding all VBox containers to the input area HBox
        inputArea.getChildren().addAll(nameBox, nationalityBox, ageBox, genderBox, buttonBox);

        // Configuring actions
        addButton.setOnAction(e -> addAthlete());
        deleteButton.setOnAction(e -> deleteAthlete());
    }


    private void configureEventSelectionArea(HBox eventSelectionArea) {
        eventListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        eventListView.setPrefHeight(100);
        eventSelectionArea.getChildren().addAll(new Label("Select Events:"), eventListView);
    }

    private void addAthlete() {
        // Validate input fields
        if (nameField.getText().trim().isEmpty() ||
                nationalityField.getText().trim().isEmpty() ||
                ageField.getText().trim().isEmpty() ||
                genderComboBox.getValue() == null) {

            // Show an alert if any field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields before adding an athlete.");
            alert.showAndWait();
            return; // Do not proceed with adding the athlete
        }

        // Assuming age is a numeric field that should be validated
        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Age");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid age.");
            alert.showAndWait();
            return;
        }

        // Retrieve selected events
        List<SportEvent> selectedEvents = new ArrayList<>(eventListView.getSelectionModel().getSelectedItems());

        // Create a new athlete object
        Athlete newAthlete = new Athlete(
                athletes.size() + 1,  // This ID generation is simplistic; in practice, the DB should handle it.
                nameField.getText(),
                nationalityField.getText(),
                age,
                genderComboBox.getValue(),
                selectedEvents
        );

        // Add to database and update observable list if successful
        DatabaseManager.addAthlete(newAthlete);
        athletes.add(newAthlete); // Only add to the list if the DB add was successful
    }


    private void deleteAthlete() {
        Athlete selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DatabaseManager.deleteAthlete(selected.getId());
            athletes.remove(selected);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}