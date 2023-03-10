package com.example.ims;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataController implements Initializable {

    @FXML
    public static Label staticTextBox1;
    @FXML
    public static Label staticTextBox2;
    @FXML
    public static Label staticTextBox3;
    @FXML
    public static Label staticTextBox4;
    @FXML
    public static Label staticTextBox5;
    @FXML
    public static Label staticTextBox6;
    @FXML
    private Label textBox1;
    @FXML
    private Label textBox2;
    @FXML
    private Label textBox3;
    @FXML
    private Label textBox4;
    @FXML
    private Label textBox5;
    @FXML
    private Label textBox6;

    int loopNumber = 0;

    public DataController() {
        //no args constructor
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticTextBox1 = textBox1;
        staticTextBox2 = textBox2;
        staticTextBox3 = textBox3;
        staticTextBox4 = textBox4;
        staticTextBox5 = textBox5;
        staticTextBox6 = textBox6;
    }
}
