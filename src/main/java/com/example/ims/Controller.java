package com.example.ims;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

public class Controller extends DataController implements Initializable{

    @FXML
    private Pane pane_dash;
    @FXML
    private Pane pane_inventory;
    @FXML
    private VBox box_items = null;

    @FXML
    private ChoiceBox<String> storeSelector;
    private String[] stores = {"All Stores", "Store1", "Store2"};

    @FXML
    private ToggleButton btn_dash;
    @FXML
    private ToggleButton btn_inventory;
    @FXML
    private Button btn_add;

    public Controller(){
        //no args constructor
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storeSelector.getItems().addAll(stores);
        storeSelector.setValue(stores[0]);

        pane_dash.toFront();
        pane_dash.setVisible(true);
        pane_inventory.setVisible(false);
        btn_dash.setEffect(dropShadow);

        // fill inventory panel with some items
        Node[] nodes = new Node[5];
        for (int i = 0; i < nodes.length; i++) {
            try {
                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("item.fxml"));
               // staticTextBox1.setText(App.getItemInfo(2044, 1135));
                box_items.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    DropShadow dropShadow = new DropShadow();
    @FXML
    private void changePane(ActionEvent event){
        event.consume();
        if(event.getSource() == btn_dash){
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
        try{
            show_add_item_stage();

            Node node = FXMLLoader.load(getClass().getResource("item.fxml"));
            box_items.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}