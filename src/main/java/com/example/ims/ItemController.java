package com.example.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category_selector.getItems().addAll(categories);
    }

    @FXML
    private void addItem(ActionEvent event){
        if(checkFields()) {
            // add item to the database
            String[] newValues = new String[10];
            newValues[0] = text_id.getText();
            newValues[1] = text_name.getText();
            newValues[2] = category_selector.getValue();
            newValues[3] = text_price.getText();
            newValues[4] = text_quantity.getText();
            newValues[5] = "blank";

            App.addItem("PartySuppliesStore2044", newValues);
            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();
        }
    }

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

        return true;
    }
}
