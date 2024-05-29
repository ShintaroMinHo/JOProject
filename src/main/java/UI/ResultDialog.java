package UI;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import p1.Result;

public class ResultDialog extends Dialog<Result> {
    private TextField eventIdField = new TextField();
    private TextField athleteIdField = new TextField();
    private TextField scoreField = new TextField();
    private TextField rankField = new TextField();
    private TextField medalField = new TextField();

    public ResultDialog(Result result) {
        setTitle(result == null ? "Add Result" : "Edit Result");
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        eventIdField.setText(result != null ? String.valueOf(result.getCompetitionEventId()) : "");
        athleteIdField.setText(result != null ? String.valueOf(result.getAthleteId()) : "");
        scoreField.setText(result != null ? String.valueOf(result.getScore()) : "");
        rankField.setText(result != null ? String.valueOf(result.getRank()) : "");
        medalField.setText(result != null ? result.getMedal() : "");

        VBox form = new VBox(10);
        form.getChildren().addAll(
                new HBox(5, new Label("Event ID:"), eventIdField),
                new HBox(5, new Label("Athlete ID:"), athleteIdField),
                new HBox(5, new Label("Score:"), scoreField),
                new HBox(5, new Label("Rank:"), rankField),
                new HBox(5, new Label("Medal:"), medalField)
        );

        getDialogPane().setContent(form);

        setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Result(
                        Integer.parseInt(eventIdField.getText()),
                        Integer.parseInt(athleteIdField.getText()),
                        Double.parseDouble(scoreField.getText()),
                        Integer.parseInt(rankField.getText()),
                        medalField.getText()
                );
            }
            return null;
        });
    }
}
