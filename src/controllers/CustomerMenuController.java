package controllers;

import Models.Customers;
import database.AppointmentsDAO;
import database.CustomersDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerMenuController implements Initializable {

    @FXML
    private TableView<Customers> customerTableView;

    @FXML
    private TableColumn<Customers, Integer> customerId;

    @FXML
    private TableColumn<Customers, String> customerName;

    @FXML
    private TableColumn<Customers, String> address;

    @FXML
    private TableColumn<Customers, String> postalCode;

    @FXML
    private TableColumn<Customers, String> country;

    @FXML
    private TableColumn<Customers, Integer> divisionId;

    @FXML
    private TableColumn<Customers, String> phone;

    static ObservableList<Customers> customers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customers = CustomersDAO.getAllCustomers();
            customerTableView.setItems(customers);
            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            country.setCellValueFactory(new PropertyValueFactory<>("country"));
            divisionId.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void handleAddCustomer(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/addCustomer.FXML"));
        stage.setTitle("Create New Customer Profile");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    public void handleModCustomer(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/modifyCustomer.FXML"));
        stage.setTitle("Modify A Customer Profile");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    public void handleDeleteCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        if (customerTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Customer Delete");
            alert.setHeaderText("Are sure you wish to delete customer: " + customerTableView.getSelectionModel()
                    .getSelectedItem().getCustomerName() + "?");
            alert.setContentText("If yes, press OK to proceed deleting the profile.");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    if (AppointmentsDAO.checkForAppts(customerTableView.getSelectionModel().getSelectedItem().getCustomerId()) > 0) {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Appointments Present");
                        alert1.setHeaderText("This customer has an existing appointment!");
                        alert1.setContentText("You must delete the appointment before the customer profile can be removed.");
                        alert1.showAndWait();
                    }
                    else {
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Customer Removed");
                        alert2.setHeaderText("The customer profile has been deleted.");
                        alert2.setContentText("Customer " + customerTableView.getSelectionModel().
                                getSelectedItem().getCustomerName() + " has been removed from the database.");
                        alert2.showAndWait();
                        CustomersDAO.deleteCustomer(customerTableView.getSelectionModel().
                                getSelectedItem().getCustomerId());
                    }
                }
                else {
                    customerTableView.getSelectionModel().clearSelection();
                }
            }
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/customerMenu.FXML"));
            stage.setTitle("Customer Menu");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Customer Error");
            alert.setHeaderText("You did not select a customer to delete.");
            alert.setContentText("Please select the profile you wish to remove and try again.");
            alert.showAndWait();
            return;
        }
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to exit the Customer Menu?");
        alert.setContentText("If yes, press OK to return to the main screen.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
            stage.setTitle("Appointment Management System");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
    }
}