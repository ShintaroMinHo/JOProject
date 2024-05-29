package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import p2.DatabaseManager;
import p1.EventWithDateTime;

import java.time.LocalDate;
import java.util.List;

public class ScheduleUI extends Application {
    private DatePicker datePicker;
    private ListView<EventWithDateTime> eventsListView;
    private ObservableList<EventWithDateTime> eventsList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Schedule");

        datePicker = new DatePicker();
        eventsListView = new ListView<>();
        eventsList = FXCollections.observableArrayList();

        datePicker.setOnAction(e -> updateEventsList(datePicker.getValue()));

        VBox root = new VBox(10, datePicker, eventsListView);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        updateEventsList(LocalDate.now());
    }

    private void updateEventsList(LocalDate date) {
        eventsList.clear();
        List<EventWithDateTime> allEvents = DatabaseManager.loadEvents();
        for (EventWithDateTime event : allEvents) {
            if (event.getDateTime().toLocalDate().equals(date)) {
                eventsList.add(event);
            }
        }
        eventsListView.setItems(eventsList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}