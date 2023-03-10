module com.example.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires software.amazon.awssdk.auth;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.dynamodb;


    opens com.example.ims to javafx.fxml;
    exports com.example.ims;
}