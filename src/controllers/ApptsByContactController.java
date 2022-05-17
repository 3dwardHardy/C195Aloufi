package controllers;

import Models.Appointments;
import Models.Contacts;
import database.AppointmentsDAO;
import database.ContactsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ApptsByContactController implements Initializable {
    @FXML
    private TableView<Appointments> contactTableView;

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
    private TableColumn<Appointments, Integer> apptId;

    @FXML
    private ComboBox<Contacts> contactCombo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            /**
             * This code populates the contact combo box so a user can select the contact for the report they wish to view.
             * also sets the table values to match the data to the appropriate columns.
             */

            ObservableList<Contacts> contacts = ContactsDAO.getContactID();
            contactCombo.setItems(contacts);

            apptId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            end.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * Handles the back button action, takes the user back to the report menu.
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
     * This handles the Generate button action and uses a sql statement to get the data for the chosen contact.
     * Then populates the table with the recieved data.
     * @param actionEvent
     */
    public void handleGenerate(ActionEvent actionEvent) {
        int contactId = contactCombo.getSelectionModel().getSelectedItem().getContactId();

        if (contactId > 0) {
            contactTableView.setItems(AppointmentsDAO.getApptsByContactId(contactId));
            apptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        }
    }
}

