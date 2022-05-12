package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class ModifyCustomerController {

    @FXML
    private TextField modCustomerIdTxt;

    @FXML
    private TextField modCustomerNameTxt;

    @FXML
    private TextField modCustomerAddressTxt;

    @FXML
    private TextField modPostalTxt;

    @FXML
    private TextField modPhoneTxt;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> stateCombo;



    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleClear(ActionEvent actionEvent) {
        modCustomerIdTxt.setText("");
        modCustomerNameTxt.setText("");
        modCustomerAddressTxt.setText("");
        modPostalTxt.setText("");
        modPhoneTxt.setText("");
        countryCombo.getSelectionModel().clearSelection();
        stateCombo.getSelectionModel().clearSelection();
    }

    public void handleCancel(ActionEvent actionEvent) {
    }

    public void handleFilterStates(ActionEvent actionEvent) {
    }
}
