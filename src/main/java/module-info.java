module com.isep.joproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.isep.joproject to javafx.fxml;
    exports com.isep.joproject;
}