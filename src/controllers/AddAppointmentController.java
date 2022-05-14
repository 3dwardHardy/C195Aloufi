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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            }

            LocalTime start = LocalTime.parse(startTimeCombo.getSelectionModel().getSelectedItem());
            LocalTime end = LocalTime.parse(endTimeCombo.getSelectionModel().getSelectedItem());

            if (end.isBefore(start)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("The end time cannot be before the appointment start time!");
                alert.setContentText("Please check the times for this appointment and try again.");
                alert.showAndWait();
                return;
            }
            LocalDate startDateValue = startDate.getValue();
            LocalDate endDateValue = endDate.getValue();

            if (!startDateValue.equals(endDateValue)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Alert");
                alert.setHeaderText("Appointments cannot span days.");
                alert.setContentText("Appointments must start and end on the same date.");
                alert.showAndWait();
                return;
            }

            LocalDateTime startDateTime = startDateValue.atTime(start);
            LocalDateTime endDateTime = endDateValue.atTime(end);

            LocalDateTime selectedStart;
            LocalDateTime selectedEnd;

            try {
                ObservableList<Appointments> appts = AppointmentsDAO.getApptsByCustomerID(customerIdCombo.getSelectionModel().getSelectedItem().getCustomerId());
                for (Appointments appointments : appts) {
                    selectedStart = appointments.getStartDate().atTime(appointments.getStartTime().toLocalTime());
                    selectedEnd = appointments.getEndDate().atTime(appointments.getEndTime().toLocalTime());

                    if (selectedStart.isAfter(startDateTime) && selectedStart.isBefore(endDateTime)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Add Appointment Error");
                        alert.setHeaderText("This appointment overlaps with another!");
                        alert.setContentText("Please change the time and try again.");
                        alert.showAndWait();
                        return;
                    } else if (selectedEnd.isAfter(startDateTime) && selectedEnd.isBefore(endDateTime)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Add Appointment Error");
                        alert.setHeaderText("This appointment overlaps with another!");
                        alert.setContentText("Please change the time and try again.");
                        alert.showAndWait();
                        return;
                    }


                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
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
