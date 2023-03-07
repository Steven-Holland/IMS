module com.example.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires aws.java.sdk.mediastoredata;
    requires aws.java.sdk.dynamodb;
    requires aws.java.sdk.core;


    opens com.example.ims to javafx.fxml;
    exports com.example.ims;
}