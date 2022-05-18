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

    private static Customers selected;

    public static void retrieveCustomer(Customers customer) {
        selected = customer;
    }
    Parent scene;

    /**
     * This handles the save button on click action. It gathers all the appointment data while ensuring all form fields contain
     * a value. Otherwise, will generate errors advising the user what customer info is missing.
     * @param actionEvent
     */
    public void handleSave(ActionEvent actionEvent) {
        try {
            if (modCustomerNameTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a name!");
                alert.setContentText("Please enter a valid value for the customer's name.");
                alert.showAndWait();
                return;
            }

            if (modCustomerAddressTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter an address!");
                alert.setContentText("Please enter a valid value for the customer's address.");
                alert.showAndWait();
                return;
            }

            if (modPostalTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a postal code!");
                alert.setContentText("Please enter a valid value for the customer's postal code.");
                alert.showAndWait();
                return;
            }

            if (countryCombo.getValue().equals(null)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a country!");
                alert.setContentText("Please select a value for the customer's country.");
                alert.showAndWait();
                return;
            }

            if (stateCombo.getValue().equals(null)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a state/province!");
                alert.setContentText("Please select a value for the customer's state or province.");
                alert.showAndWait();
                return;
            }

            if (modPhoneTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer Error");
                alert.setHeaderText("You did not enter a phone number!");
                alert.setContentText("Please enter a valid value for the customer's phone number.");
                alert.showAndWait();
                return;

            } else {
                /**
                 * This sends the form data to the method to insert it into the database as a record. Upon gathering the information
                 * the program will show a message for the user that the new customer record has been created.
                 * Once they go back to the main after a successful customer creation they will see
                 * the new customer in the tableview.
                 *
                 */
                CustomersDAO.updateCustomer(
                        Integer.parseInt(modCustomerIdTxt.getText()),
                        modCustomerNameTxt.getText(),
                        modCustomerAddressTxt.getText(),
                        modPostalTxt.getText(),
                        modPhoneTxt.getText(),
                        stateCombo.getValue().getDivisionId());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("The customer profile for Customer: " + modCustomerNameTxt.getText() + " has been updated.");
                alert.showAndWait();
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/customerMenu.FXML")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Upon selection of the clear button will wipe all existing data from the form,
     * this allows the user to change all entries with a fresh form.
     * @param actionEvent
     */
    public void handleClear(ActionEvent actionEvent) {
        modCustomerIdTxt.setText("");
        modCustomerNameTxt.setText("");
        modCustomerAddressTxt.setText("");
        modPostalTxt.setText("");
        modPhoneTxt.setText("");
        countryCombo.getSelectionModel().clearSelection();
        stateCombo.getSelectionModel().clearSelection();
    }

    /**
     * This handles a cancel button click. It will first ask the user if they want to cancel modifying the customer record.
     * Upon confirmation will close the screen without changing anything for the record and returns the user to the customer menu screen.
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * This handles the filtering for the state/province combo box based on what country is selected.
     * @param actionEvent
     * @throws SQLException
     */
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
        /**
         * This prepopulates the form with the selected customer information.
         */
        modCustomerIdTxt.setText(String.valueOf(selected.getCustomerId()));
        modCustomerNameTxt.setText(selected.getCustomerName());
        modCustomerAddressTxt.setText(selected.getAddress());
        modPostalTxt.setText(selected.getPostalCode());
        modPhoneTxt.setText(selected.getPhone());
        try {
            Countries country = CountriesDAO.getCountry(selected.getCountry());
            countryCombo.setValue(country);

            FirstLevelDivisions firstLevel = FirstLevelDivisionDAO.getState(selected.getDivisionId());
            stateCombo.setValue(firstLevel);

            /**
             * This populates the country and state/province combo boxes.
             */
            ObservableList<Countries> countries = CountriesDAO.getCountryId();
            countryCombo.setItems(countries);

            handleFilterStates(selected.getDivisionId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleFilterStates(int divisionId) {
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
}
