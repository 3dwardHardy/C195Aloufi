package controllers;

import Models.Appointments;
import Models.Contacts;
import Models.Customers;
import Models.Users;
import database.AppointmentsDAO;
import database.ContactsDAO;
import database.CustomersDAO;
import database.UsersDAO;
import helper.Conversions;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;


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


    /**
     * This handles all the needed actions for gathering and setting up a new appointment.
     *
     * @param actionEvent
     * On click save appointment
     *
     * @throws IOException
     * In case of error prints stack trace.
     *
     */
    public void handleSave(ActionEvent actionEvent) throws IOException {
        try {
            /**
             * this if sequence handles the exceptions required to alert the user of missing data entries.
             */
            Appointments appointments = new Appointments();
            if (titleTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a title!");
                alert.setContentText("Please enter a valid title for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setTitle(titleTxt.getText());
            }

            if (descriptionTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a description!");
                alert.setContentText("Please enter a valid description for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setDescription(descriptionTxt.getText());
            }
            if (locationTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a location!");
                alert.setContentText("Please enter a valid location for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setLocation(locationTxt.getText());
            }
            if (contactCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a contact!");
                alert.setContentText("Please select a contact for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setContactId(contactCombo.getValue().getContactId());
            }
            if (typeTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a type!");
                alert.setContentText("Please enter a valid type for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setType(typeTxt.getText());
            }
            if (customerIdCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a customer ID!");
                alert.setContentText("Please select a customer ID for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setCustomerId(customerIdCombo.getValue().getCustomerId());
            }
            if (userIdCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not select a user ID!");
                alert.setContentText("Please select a user ID for this appointment.");
                alert.showAndWait();
                return;
            } else {
                appointments.setUserId(userIdCombo.getValue().getUserId());
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

            /**
             * this code block handles the  time zone conversions for appointments when the data is collected from the add appointment form.
             * it also handles exception checking to ensure appointment overlap does not exist.
             */
            ObservableList<Appointments> timeList = AppointmentsDAO.getApptsByCustomerID(customerIdCombo.getSelectionModel().getSelectedItem().getCustomerId());
            String fullStartTime = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (startTimeCombo.getValue() + ":00");
            Timestamp startTimeStamp = Timestamp.valueOf(fullStartTime);
            String fullEndTime = endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (endTimeCombo.getValue() + ":00");
            Timestamp endTimeStamp = Timestamp.valueOf(fullEndTime);
            Boolean apptConflict = AppointmentsDAO.checkForOverlappingAppointment(startTimeStamp, endTimeStamp, customerIdCombo.getValue().getCustomerId());

            /**
             * Gathers the local date and time info.
             */
            LocalDate setStartDate = LocalDate.parse(startDate.getValue().toString());
            LocalTime setStartTime = LocalTime.parse(startTimeCombo.getValue());
            LocalDate setEndDate = LocalDate.parse(endDate.getValue().toString());
            LocalTime setEndTime = LocalTime.parse(endTimeCombo.getValue());
            ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime startZoneTime = ZonedDateTime.of(setStartDate, setStartTime, localZone);
            ZonedDateTime endZoneTime = ZonedDateTime.of(setEndDate, setEndTime, localZone);

            /**
             * Sets the office hours values in the EST time zone.
             */

            ZoneId estZoneId = ZoneId.of("US/Eastern");
            LocalDate officeOpenDate = LocalDate.parse(startDate.getValue().toString());
            LocalTime officeOpenTime = LocalTime.of(8, 00, 00);
            ZonedDateTime officeOpenZDT = ZonedDateTime.of(officeOpenDate, officeOpenTime, estZoneId);
            LocalDate officeCloseDate = LocalDate.parse(endDate.getValue().toString());
            LocalTime officeCloseTime = LocalTime.of(22, 00, 00);
            ZonedDateTime officeCloseZDT = ZonedDateTime.of(officeCloseDate, officeCloseTime, localZone);

            /**
             * This converts the office times to the local times zone to ensure scheduling cannot occur outside the
             * offices EST business hours.
             * also sets exception handlers to inform the user they cannot schedule out business hours.
             */
            ZonedDateTime adjustedStart = officeOpenZDT.withZoneSameInstant(localZone);
            ZonedDateTime adjustedEnd = officeCloseZDT.withZoneSameInstant(localZone);

                        if (((startZoneTime.isAfter(adjustedStart)) || (startZoneTime.equals(adjustedStart))) && ((endZoneTime.isBefore(adjustedEnd)) || (endZoneTime.equals(adjustedEnd)))) {
                            if (startTimeStamp.before(endTimeStamp)) {
                                appointments.setStartTime(Timestamp.valueOf(fullStartTime));
                                appointments.setEndTime(Timestamp.valueOf(fullEndTime));
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Add Appointment Error");
                                alert.setHeaderText("The Start time must be before the appointment end time!");
                                alert.setContentText("Please adjust the time for the appointment.");
                                alert.showAndWait();
                                return;
                            }
                        } else {
                            Conversions.outOfOfficeHours();
                            return;
                        }
            if (apptConflict == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("This appointment overlaps another appointment.");
                alert.setContentText("Please adjust the times and try again.");
                alert.showAndWait();
                return;
            } else {
                /**
                 * Creates the new appointment if all exception handlers pass without error.
                 */
                AppointmentsDAO.createAppt(appointments);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Created");
                alert.setHeaderText("Appointment is scheduled for: " + customerIdCombo.getValue());
                alert.setContentText("The appointment has been added to the schedule.");
                alert.showAndWait();
                /**
                 * takes user back to main screen after appointment creation message is given.
                 */
                Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
                Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
                stage.setTitle("Appointment Management System");
                stage.setScene(new Scene(scene));
                stage.show();
                stage.centerOnScreen();
                    }
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * This method block handles the cancel buttons and if the user confirms cancellation returns them to the main screen
     * if they confirm they want to cancel appointment creation. Otherwise, keeps them on the creation screen if confirmation is not given.
     * @param actionEvent
     * Handles Cancel button click
     * @throws IOException
     *In case of error prints to stack trace.
     */
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


    /**
     * This code handles the clear button which wipes all entries from the form and selects all combo boxes and datepickers
     * to null values, giving the user a clean slate to work on so to speak.
     * @param actionEvent
     *Handles clear on click
     */
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
        /**
         * These observable lists are generated and set to fill the combo boxes on the form.
         */
        try {
            ObservableList<Customers> customers = CustomersDAO.getCustomerID();
            customerIdCombo.setItems(customers);

            ObservableList<Users> users = UsersDAO.getUserID();
            userIdCombo.setItems(users);

            ObservableList<Contacts> contacts = ContactsDAO.getContactID();
            contactCombo.setItems(contacts);

            /**
             * This code block generates the time selection for the Start and End time Combo boxes.
             */
            ObservableList<String> time = FXCollections.observableArrayList();
            LocalTime start = LocalTime.of(7,0, 0, 0);
            LocalTime end = LocalTime.of(23, 0, 0, 0);

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
