package controllers;

import Models.Customers;
import database.CustomersDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
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

    @FXML
    private Button addCustomerBtn;

    @FXML
    private Button modCustomerBtn;

    @FXML
    private Button deleteCustomerBtn;

    @FXML
    private Button cancelBtn;

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
}