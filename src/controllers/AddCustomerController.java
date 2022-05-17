package controllers;

import Models.Countries;
import Models.Customers;
import Models.FirstLevelDivisions;
import database.CountriesDAO;
import database.CustomersDAO;
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
import java.util.Objects;
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
    private ComboBox<FirstLevelDivisions> stateCombo;

    Stage stage;

    Parent scene;

    /**
     * Handles the action of a country being selected and populates the state/province combo box with the associated values.
     * @param actionEvent
     * @throws SQLException
     */
    public void handleFirstLevelDivision(ActionEvent actionEvent) throws SQLException {
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

    /**
     * Handles the save button onClick action to verify customer data has been input and pushed to the database function upon passing error
     * checks.
     * @param actionEvent
     */
    public void handleSave(ActionEvent actionEvent) {
        try {
            if (customerNameTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a name!");
                alert.setContentText("Please enter a valid value for the customer's name.");
                alert.showAndWait();
                return;
            }

            if (customerAddressTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter an address!");
                alert.setContentText("Please enter a valid value for the customer's address.");
                alert.showAndWait();
                return;
            }

            if (postalTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a postal code!");
                alert.setContentText("Please enter a valid value for the customer's postal code.");
                alert.showAndWait();
                return;
            }

            if (countryCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a country!");
                alert.setContentText("Please select a value for the customer's country.");
                alert.showAndWait();
                return;
            }

            if (stateCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a state/province!");
                alert.setContentText("Please select a value for the customer's state or province.");
                alert.showAndWait();
                return;
            }

            if (customerPhoneTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a phone number!");
                alert.setContentText("Please enter a valid value for the customer's phone number.");
                alert.showAndWait();
                return;

            } else {
                /**
                 * Code reaches this else point after error checking and gets all the form input for the database.
                 */
                Customers customer = new Customers();
                customer.setCustomerName(customerNameTxt.getText());
                customer.setAddress(customerAddressTxt.getText());
                customer.setPostalCode(postalTxt.getText());
                customer.setDivisionId(stateCombo.getSelectionModel().getSelectedItem().getDivisionId());
                customer.setPhone(customerPhoneTxt.getText());
                CustomersDAO.addNewCustomer(customer);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("The customer profile for Customer: " + customerNameTxt.getText() + " has been created.");
                alert.showAndWait();
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/customerMenu.FXML")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (SQLException | IOException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * Clears the form on a clear button click.
     * @param actionEvent
     */
    public void handleClear(ActionEvent actionEvent) {
        customerIdTxt.setText("");
        customerNameTxt.setText("");
        customerAddressTxt.setText("");
        postalTxt.setText("");
        customerPhoneTxt.setText("");
        countryCombo.getSelectionModel().clearSelection();
        stateCombo.getSelectionModel().clearSelection();
    }

    /**
     * This handle the cancel button click. It will prompt the user for confirmation that they wish to leave and takes them
     * back to the customer menu. If no confirmation received stays on the add customer form screen.
     * @param actionEvent
     * @throws IOException
     */

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to cancel creating this customer profile?");
        alert.setContentText("If yes, press OK to return to the customer menu.");
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

    /**
     * These code blocks populate the country and state/province combo boxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> countries = CountriesDAO.getCountryId();
        countryCombo.setItems(countries);

        ObservableList<FirstLevelDivisions> firstLevelDivisions = FirstLevelDivisionDAO.getFirstLevel();
        stateCombo.setItems(firstLevelDivisions);
    }
}

