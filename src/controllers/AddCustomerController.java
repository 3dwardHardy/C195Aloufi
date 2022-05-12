package controllers;

import Models.Countries;
import Models.FirstLevelDivisions;
import database.FirstLevelDivisionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField customerNameTxt;

    @FXML
    private TextField customerAddressTxt;

    @FXML
    private TextField postalTxt;

    @FXML
    private TextField customerPhoneTxt;

    @FXML
    private ComboBox<Countries> countryCombo;

    @FXML
    private ComboBox<String> stateCombo;

    public void handleFirstLevelDivision(ActionEvent actionEvent) {
        ObservableList<String> stateList = FXCollections.observableArrayList();
        ObservableList<FirstLevelDivisions> firstLevelDivisions = FirstLevelDivisionDAO.getStatesByCountry(countryCombo.getSelectionModel().getSelectedItem());
        if (firstLevelDivisions != null) {
            for (FirstLevelDivisions firstLevel : firstLevelDivisions) {
                stateList.add(firstLevel.getFirstLevel());
            }
        }
        stateCombo.setItems(stateList);
    }

    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleClear(ActionEvent actionEvent) {
        customerIdTxt.setText("");
        customerNameTxt.setText("");
        customerAddressTxt.setText("");
        postalTxt.setText("");
        countryCombo.getSelectionModel().clearSelection();
        stateCombo.getSelectionModel().clearSelection();
        customerPhoneTxt.setText("");
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to cancel creating this customer profile?");
        alert.setContentText("If yes, press OK to return to the main screen.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/customerMenu.FXML"));
            stage.setTitle("Customer Menu");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
