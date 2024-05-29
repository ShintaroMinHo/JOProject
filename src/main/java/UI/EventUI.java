package UI;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class EventUI {
    private VBox view;

    public EventUI() {
        view = new VBox();
        view.getChildren().add(new Label("Event Management Section"));
    }

    public VBox getView() {
        return view;
    }
}

