package com.example.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.text.DecimalFormat;

public class Controller extends DataController implements Initializable{

    @FXML
    private Pane pane_dash;
    @FXML
    private Pane pane_inventory;
    @FXML
    private VBox box_items = null;

    @FXML
    private ChoiceBox<String> storeSelector;

    private String[] stores = App.listAllTables().toArray(new String[0]);

    @FXML
    private ToggleButton btn_dash;
    @FXML
    private ToggleButton btn_inventory;
    @FXML
    private Button btn_add;

    @FXML
    private Label label_value;
    @FXML
    private Label label_amount;
    double inventoryValue = 0;
    int inventoryAmount = 0;
    SharedStorage storage = SharedStorage.getInstance();
    public Controller(){
        //no args constructor
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storeSelector.getItems().addAll(stores);
        storeSelector.setValue(stores[0]);
        storeSelector.setOnAction(this::updateStore);

        pane_dash.toFront();
        pane_dash.setVisible(true);
        pane_inventory.setVisible(false);
        btn_dash.setEffect(dropShadow);

        //Stores the database into array and counts the amount of entries
//        String[][] allValues = App.scanItems(stores[1]);
//        int numRows = 0;
//        while(allValues[numRows][0] != null) {
//            numRows++;
//        }
//
//        // fill inventory panel with some items
//        Node[] nodes = new Node[numRows];
//        for (int i = 0; i < nodes.length; i++) {
//            try {
//                final int j = i;
//                nodes[i] = FXMLLoader.load(getClass().getResource("item.fxml"));
//                staticTextBox1.setText(allValues[i][4]);
//                staticTextBox2.setText(allValues[i][5]);
//                staticTextBox3.setText(allValues[i][1]);
//                staticTextBox4.setText(allValues[i][3]);
//                staticTextBox5.setText(allValues[i][2]);
//                staticTextBox6.setText(allValues[i][0]);
//                box_items.getChildren().add(nodes[i]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    private void updateKPI() {
        DecimalFormat twoDecimal = new DecimalFormat("##.00");
        label_value.setText(String.valueOf(twoDecimal.format(inventoryValue)));
        label_amount.setText(String.valueOf(inventoryAmount));
    }
    private void clearTable() {
        box_items.getChildren().clear();
        inventoryValue = 0;
        inventoryAmount = 0;
    }
    private void updateTable(String currentStore) {
        String[][] allValues;
        if (currentStore.contains("All Stores")) {
            allValues = App.scanItems(stores[1]);
        }
        else {
            allValues = App.scanItems(currentStore);
        }
        int numRows = 0;
        while(allValues[numRows][0] != null) {
            numRows++;
        }

        // fill inventory panel with some items
        Node[] nodes = new Node[numRows];
        for (int i = 0; i < nodes.length; i++) {
            try {
                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("item.fxml"));
                staticTextBox1.setText(allValues[i][4]);
                staticTextBox2.setText(allValues[i][5]);
                staticTextBox3.setText(allValues[i][1]);
                staticTextBox4.setText(allValues[i][3]);
                staticTextBox5.setText(allValues[i][2]);
                staticTextBox6.setText(allValues[i][0]);
                inventoryValue+=Double.valueOf(allValues[i][1]);
                inventoryAmount+=Integer.valueOf(allValues[i][3]);
                box_items.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateKPI();
    }
    DropShadow dropShadow = new DropShadow();
    @FXML
    private void changePane(ActionEvent event){
        event.consume();
        if(event.getSource() == btn_dash){
            updateKPI();
            pane_dash.toFront();
            pane_dash.setVisible(true);
            pane_inventory.setVisible(false);

            btn_dash.setEffect(dropShadow);
            btn_inventory.setEffect(null);
        }else if(event.getSource() == btn_inventory){
            pane_inventory.toFront();
            pane_inventory.setVisible(true);
            pane_dash.setVisible(false);

            btn_inventory.setEffect(dropShadow);
            btn_dash.setEffect(null);
        }
    }

    @FXML
    private void storeChanged(ActionEvent actionEvent) {
        if(storeSelector.getValue().contains("All Stores")) {
            clearTable();
            updateTable(stores[1]);
            updateTable(stores[2]);
            storage.setStore("PartyStore0001");
        }
        else {
            clearTable();
            updateTable(storeSelector.getValue());
            storage.setStore(storeSelector.getValue());
        }
    }

    public void show_add_item_stage(){
        try{
            AnchorPane pane_add_item = FXMLLoader.load(getClass().getResource("new_item_box.fxml"));

            // create a new stage for the add item prompt
            Stage stage_add_item = new Stage();
            stage_add_item.setTitle("Add Item");
            stage_add_item.initModality(Modality.WINDOW_MODAL);
            stage_add_item.initOwner(App.stage_primary);

            // add the stage to a new scene
            Scene scene = new Scene(pane_add_item);
            stage_add_item.setScene(scene);
            stage_add_item.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addItem(){
            show_add_item_stage();
            String[] newValues = new String[10];
            if(storeSelector.getValue().contains("All Stores")) {
                clearTable();
                updateTable("PartyStore0001");
            }
            else {
                clearTable();
                updateTable(storeSelector.getValue());
            }
    }

    @FXML
    private void updateStore(ActionEvent event){
        storage.setStore(storeSelector.getValue());
    }

}