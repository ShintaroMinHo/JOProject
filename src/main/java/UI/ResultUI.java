package UI;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ResultUI {
    private VBox view;

    public ResultUI() {
        view = new VBox();
        view.getChildren().add(new Label("Result Management Section"));
    }

    public VBox getView() {
        return view;
    }
}