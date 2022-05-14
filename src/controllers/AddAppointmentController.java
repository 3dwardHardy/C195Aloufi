package controllers;

import Models.Appointments;
import Models.Contacts;
import Models.Customers;
import Models.Users;
import database.AppointmentsDAO;
import database.ContactsDAO;
import database.CustomersDAO;
import database.UsersDAO;
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
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIdTxt;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField typeTxt;

    @FXML
    private ComboBox<Contacts> contactCombo;

    @FXML
    private ComboBox<Customers> customerIdCombo;

    @FXML
    private ComboBox<Users> userIdCombo;

    @FXML
    private ComboBox<String> startTimeCombo;

    @FXML
    private ComboBox<String> endTimeCombo;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    private ZonedDateTime conversionEST(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneId.of("America/New_York"));
    }


    public void handleSave(ActionEvent actionEvent) throws SQLException {
        try {
            if (titleTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a title!");
                alert.setContentText("Please enter a valid title for this appointment.");
                alert.showAndWait();
                return;
            }
            if (descriptionTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a description!");
                alert.setContentText("Please enter a valid description for this appointment.");
                alert.showAndWait();
                return;
            }
            if (locationTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a location!");
                alert.setContentText("Please enter a valid location for this appointment.");
                alert.showAndWait();
                return;
            }
            if (contactCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a contact!");
                alert.setContentText("Please select a contact for this appointment.");
                alert.showAndWait();
                return;
            }
            if (typeTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a type!");
                alert.setContentText("Please enter a valid type for this appointment.");
                alert.showAndWait();
                return;
            }
            if (customerIdCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a customer ID!");
                alert.setContentText("Please select a customer ID for this appointment.");
                alert.showAndWait();
                return;
            }
            if (userIdCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a user ID!");
                alert.setContentText("Please select a user ID for this appointment.");
                alert.showAndWait();
                return;
            }
            if (startDate.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a date!");
                alert.setContentText("Please select a date for this appointment.");
                alert.showAndWait();
                return;
            }
            if (startTimeCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a start time!");
                alert.setContentText("Please select a start time for this appointment.");
                alert.showAndWait();
                return;
            }
            if (endDate.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select an end date!");
                alert.setContentText("Please select an end date for this appointment.");
                alert.showAndWait();
                return;
            }
            if (endDate.getValue().isBefore(startDate.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("The end date cannot be before the start date!");
                alert.setContentText("Please check your entry and try again.");
                alert.showAndWait();
                return;
            }
            if (endTimeCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select an end time!");
                alert.setContentText("Please select an end time for this appointment.");
                alert.showAndWait();
                return;
            } else {
                try {
                    Appointments appointments = new Appointments();
                    appointments.setTitle(titleTxt.getText());
                    appointments.setDescription(descriptionTxt.getText());
                    appointments.setLocation(locationTxt.getText());
                    appointments.setContactId(contactCombo.getValue().getContactId());
                    appointments.setType(typeTxt.getText());
                    appointments.setCustomerId(customerIdCombo.getValue().getCustomerId());
                    appointments.setUserId(userIdCombo.getValue().getUserId());
                    appointments.setStartDate(Timestamp.valueOf(LocalDateTime.of(startDate.getValue(), LocalTime.parse
                            (startTimeCombo.getSelectionModel().getSelectedItem()))));
                    appointments.setEndDate(Timestamp.valueOf(LocalDateTime.of(endDate.getValue(), LocalTime.parse
                            (endTimeCombo.getSelectionModel().getSelectedItem()))));
                    AppointmentsDAO.createAppt(appointments);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Created");
                    alert.setHeaderText("Appointment is scheduled for: " + customerIdCombo.getValue());
                    alert.setContentText("The appointment has been added to the schedule.");
                    alert.showAndWait();
                    Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
                    Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
                    stage.setTitle("Appointment Management System");
                    stage.setScene(new Scene(scene));
                    stage.show();
                    stage.centerOnScreen();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

        public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to cancel creating this appointment?");
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

    public void handleClear(ActionEvent actionEvent) {
        appointmentIdTxt.setText("");
        titleTxt.setText("");
        descriptionTxt.setText("");
        locationTxt.setText("");
        contactCombo.getSelectionModel().clearSelection();
        typeTxt.setText("");
        customerIdCombo.getSelectionModel().clearSelection();
        userIdCombo.getSelectionModel().clearSelection();
        startDate.setValue(null);
        startTimeCombo.getSelectionModel().clearSelection();
        endDate.setValue(null);
        endTimeCombo.getSelectionModel().clearSelection();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Customers> customers = CustomersDAO.getCustomerID();
            customerIdCombo.setItems(customers);

            ObservableList<Users> users = UsersDAO.getUserID();
            userIdCombo.setItems(users);

            ObservableList<Contacts> contacts = ContactsDAO.getContactID();
            contactCombo.setItems(contacts);

            ObservableList<String> time = FXCollections.observableArrayList();
            LocalTime start = LocalTime.of(7,0);
            LocalTime end = LocalTime.of(23,0);

            time.add(start.toString());
            while (start.isBefore(end)) {
                start = start.plusMinutes(15);
                time.add(start.toString());
            }

            startTimeCombo.setItems(time);
            endTimeCombo.setItems(time);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
