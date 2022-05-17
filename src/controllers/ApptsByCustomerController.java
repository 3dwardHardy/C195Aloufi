package controllers;

import Models.Appointments;
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
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ApptsByCustomerController implements Initializable {

    @FXML
    private TableView<Appointments> customerTableView;

    @FXML
    private TableColumn<Appointments, Integer> customerId;

    @FXML
    private TableColumn<Appointments, String> title;

    @FXML
    private TableColumn<Appointments, String> type;

    @FXML
    private TableColumn<Appointments, String> description;

    @FXML
    private TableColumn<Appointments, Timestamp> start;

    @FXML
    private TableColumn<Appointments, Timestamp> end;

    @FXML
    private TableColumn<Appointments, Integer> contactId;

    @FXML
    private ComboBox<Customers> customerCombo;

    /**
     * Handle the back button onClick action and takes the user back to the report menu.
     * @param actionEvent
     * @throws IOException
     */
    public void handleBack(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/reportMenu.FXML"));
        stage.setTitle("Report Menu");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * This calls for the database info for the appointent data for the chosen customer and uses the data to populate
     * the table view.
     * @param actionEvent
     * @throws SQLException
     */
    public void handleGenerate(ActionEvent actionEvent) throws SQLException {
        int customerIds = customerCombo.getSelectionModel().getSelectedItem().getCustomerId();

        if(customerIds > 0){
            customerTableView.setItems(AppointmentsDAO.getApptsByCustomerID(customerIds));
            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            contactId.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        }
    }

    /**
     * This sets up the initial table view and populates the customer combo box for the user to select.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Customers> customers = CustomersDAO.getCustomerID();
            customerCombo.setItems(customers);

            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            contactId.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
