package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void handleExit (ActionEvent actionEvent) {
        System.exit(0);
    }
}
