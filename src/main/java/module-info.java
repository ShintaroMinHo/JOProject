module com.isep.joproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    exports UI to javafx.graphics;
    opens com.isep.joproject to javafx.fxml;
    exports com.isep.joproject;
}