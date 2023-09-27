module com.example.final_proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.final_proj to javafx.fxml;
    exports com.example.final_proj;
}