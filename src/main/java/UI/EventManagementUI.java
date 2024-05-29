package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import p1.SportEvent;
import p2.DatabaseManager;


public class EventManagementUI extends Application {
    private ObservableList<SportEvent> events = FXCollections.observableArrayList();

    private ListView<SportEvent> listView;
    private TextField eventNameField = new TextField();
    private TextField eventTypeField = new TextField();
    private Button addButton = new Button("Add Event");
    private Button editButton = new Button("Edit Event");
    private Button deleteButton = new Button("Delete Event");

    @Override
    public void start(Stage primaryStage) {
        events.addAll(DatabaseManager.loadEvents());

        listView = new ListView<>(events);
        setupListView();

        VBox root = new VBox(10);
        HBox inputArea = new HBox(10);
        configureInputArea(inputArea);

        root.getChildren().addAll(listView, inputArea);

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setTitle("Event Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupListView() {
        listView.setCellFactory(param -> new ListCell<SportEvent>() {
            @Override
            protected void updateItem(SportEvent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getType());
                }
            }
        });
    }

    private void configureInputArea(HBox inputArea) {
        Label eventNameLabel = new Label("Event Name:");
        Label eventTypeLabel = new Label("Event Type:");

        VBox eventNameBox = new VBox(5, eventNameLabel, eventNameField);
        VBox eventTypeBox = new VBox(5, eventTypeLabel, eventTypeField);

        addButton.setOnAction(e -> addEvent());
        editButton.setOnAction(e -> editEvent());
        deleteButton.setOnAction(e -> deleteEvent());

        inputArea.getChildren().addAll(eventNameBox, eventTypeBox, addButton, editButton, deleteButton);
    }

    private void addEvent() {
        String name = eventNameField.getText().trim();
        String type = eventTypeField.getText().trim();
        if (name.isEmpty() || type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        SportEvent newEvent = new SportEvent(events.size() + 1, name, type);
        DatabaseManager.addEvent(newEvent);
        events.add(newEvent);
    }

    private void editEvent() {
        SportEvent selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(eventNameField.getText().trim());
            selected.setType(eventTypeField.getText().trim());
            DatabaseManager.updateEvent(selected);
            listView.refresh();
        }
    }

    private void deleteEvent() {
        SportEvent selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DatabaseManager.deleteEvent(selected.getEventId());
            events.remove(selected);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
