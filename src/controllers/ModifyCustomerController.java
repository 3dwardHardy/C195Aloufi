package controllers;

import Models.Countries;
import Models.FirstLevelDivisions;
import database.CountriesDAO;
import database.FirstLevelDivisionDAO;
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


public class ModifyCustomerController implements Initializable {

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
    private ComboBox<Countries> countryCombo;

    @FXML
    private ComboBox<FirstLevelDivisions> stateCombo;



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

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to cancel modifying this customer profile?");
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

    public void handleFilterStates(ActionEvent actionEvent) throws SQLException {
        try {
            ObservableList<FirstLevelDivisions> divisions = FirstLevelDivisionDAO.getFirstLevel();
            stateCombo.setItems(divisions);

            ObservableList<FirstLevelDivisions> division = FirstLevelDivisionDAO.returnDivisionCountry
                    (countryCombo.getSelectionModel().getSelectedItem().getCountryId());
            stateCombo.setItems(division);
        } catch (NullPointerException nullPointerException) {
            stateCombo.setItems(null);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> countries = CountriesDAO.getCountryId();
        countryCombo.setItems(countries);

        ObservableList<FirstLevelDivisions> firstLevelDivisions = FirstLevelDivisionDAO.getFirstLevel();
        stateCombo.setItems(firstLevelDivisions);
    }
}
