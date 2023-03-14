package com.example.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class ItemController implements Initializable {


    @FXML
    private TextField text_id;
    @FXML
    private TextField text_name;
    @FXML
    private TextField text_quantity;
    @FXML
    private TextField text_price;
    @FXML
    private DatePicker expiration_date;
    @FXML
    private ChoiceBox<String> category_selector;
    private String[] categories = {"Consumables", "Decorations", "Lighting",
            "Cards", "Gift Cards", "Variety", "Other"};

    @FXML
    private Button btn_add;
    @FXML
    private Label text_warning;
    @FXML
    private Label label_category;
    @FXML
    private Label label_expiration;

    SharedStorage storage = SharedStorage.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category_selector.getItems().addAll(categories);
        category_selector.setOnAction(this::categoryMonitor);
        expiration_date.getEditor().setDisable(true);

        expiration_date.setVisible(false);
        label_expiration.setVisible(false);
        category_selector.setLayoutX(115);
        label_category.setLayoutX(150);
    }

    @FXML
    private void addItem(ActionEvent event){
        if(checkFields()) {
            // add item to the database
            String[] item = {text_id.getText(), text_name.getText(), category_selector.getValue(),
                    text_price.getText(), text_quantity.getText(), expiration_date.getEditor().getText()};
            App.addItem(storage.getStore(), item);

            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();
        }
    }

    // Verify all fields are filled with valid data
    private boolean checkFields(){
        if(text_id.getText().isEmpty() || text_name.getText().isEmpty() ||
                text_price.getText().isEmpty() || text_quantity.getText().isEmpty()){
            text_warning.setText("Please fill all required fields");
            return false;
        }else if(!(text_quantity.getText() + text_id.getText()).matches("\\d+") ||
                !text_price.getText().matches("([0-9]*[.])?\\d+")){
            text_warning.setText("Please enter a valid data type");
            return false;
        }

        // Check if date entered is valid
        try{
            if(category_selector.getValue().equals("Consumables") &&
                    expiration_date.getValue() == null){
                text_warning.setText("Please enter a valid expiration date");
                return false;
            }
            expiration_date.getConverter().fromString(expiration_date.getEditor().getText());
        }catch(DateTimeParseException e){
            text_warning.setText("Please enter a valid expiration date");
            return false;
        }
        return true;
    }

    // Reveal expiration date field if item is consumable
    @FXML
    private void categoryMonitor(ActionEvent event){
        if(category_selector.getValue().equals("Consumables")){
            category_selector.setLayoutX(42);
            label_category.setLayoutX(78);
            expiration_date.setVisible(true);
            label_expiration.setVisible(true);
        }else{
            expiration_date.setVisible(false);
            label_expiration.setVisible(false);
            category_selector.setLayoutX(115);
            label_category.setLayoutX(150);
        }
    }
}
