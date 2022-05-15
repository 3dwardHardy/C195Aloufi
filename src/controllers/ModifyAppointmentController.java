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

public class ModifyAppointmentController implements Initializable {
    @FXML
    private TextField apptIdTxt;

    @FXML
    private  TextField titleTxt;

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

    private static Appointments selected;

    public static void retrieveAppts (Appointments appointments) {
        selected = appointments;
    }



    public void handleSave(ActionEvent actionEvent) throws SQLException {
        try {
            String title =titleTxt.getText();
            if (titleTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a title!");
                alert.setContentText("Please enter a valid title for this appointment.");
                alert.showAndWait();
                return;
            }

            String description = descriptionTxt.getText();
            if (descriptionTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment Error");
                alert.setHeaderText("You did not enter a description!");
                alert.setContentText("Please enter a valid description for this appointment.");
                alert.showAndWait();
                return;
            }
            String location = locationTxt.getText();
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
            String type = typeTxt.getText();
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
            int apptId = Integer.parseInt(apptIdTxt.getText());
            int customerId = (customerIdCombo.getValue().getCustomerId());
            int userId = (userIdCombo.getValue().getUserId());
            ObservableList<Appointments> customerAppts = AppointmentsDAO.getApptsByCustomerID(customerId);
            int contactId = (contactCombo.getValue().getContactId());


            ObservableList<Appointments> timeList = AppointmentsDAO.getApptsByCustomerID(customerIdCombo.getSelectionModel().getSelectedItem().getCustomerId());
            String fullStartTime = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (startTimeCombo.getValue() + ":00");
            Timestamp startTimeStamp = Timestamp.valueOf(fullStartTime);
            String fullEndTime = endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (endTimeCombo.getValue() + ":00");
            Timestamp endTimeStamp = Timestamp.valueOf(fullEndTime);
            Boolean apptConflict = AppointmentsDAO.checkForOverlappingAppointment(startTimeStamp, endTimeStamp, (customerIdCombo.getValue().getCustomerId()));

            LocalDate setStartDate = LocalDate.parse(startDate.getValue().toString());
            LocalTime setStartTime = LocalTime.parse(startTimeCombo.getValue());
            LocalDate setEndDate = LocalDate.parse(endDate.getValue().toString());
            LocalTime setEndTime = LocalTime.parse(endTimeCombo.getValue());
            ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime startZoneTime = ZonedDateTime.of(setStartDate, setStartTime, localZone);
            ZonedDateTime endZoneTime = ZonedDateTime.of(setEndDate, setEndTime, localZone);

            ZoneId estZoneId = ZoneId.of("US/Eastern");
            LocalDate officeOpenDate = LocalDate.parse(startDate.getValue().toString());
            LocalTime officeOpenTime = LocalTime.of(8, 00, 00);
            ZonedDateTime officeOpenZDT = ZonedDateTime.of(officeOpenDate, officeOpenTime, estZoneId);
            LocalDate officeCloseDate = LocalDate.parse(endDate.getValue().toString());
            LocalTime officeCloseTime = LocalTime.of(22, 00, 00);
            ZonedDateTime officeCloseZDT = ZonedDateTime.of(officeCloseDate, officeCloseTime, localZone);

            ZonedDateTime adjustedStart = officeOpenZDT.withZoneSameInstant(localZone);
            ZonedDateTime adjustedEnd = officeCloseZDT.withZoneSameInstant(localZone);

            if (((startZoneTime.isAfter(adjustedStart)) || (startZoneTime.equals(adjustedStart))) && ((endZoneTime.isBefore(adjustedEnd)) || (endZoneTime.equals(adjustedEnd)))) {
                if (startTimeStamp.before(endTimeStamp)) {
                    AppointmentsDAO.updateAppointment(title, description,location, type, startTimeStamp, endTimeStamp, customerId,userId,contactId, apptId);

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
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to cancel modifying this appointment?");
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
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        try {
            apptIdTxt.setText(String.valueOf(selected.getApptId()));
            titleTxt.setText(selected.getTitle());
            descriptionTxt.setText(selected.getDescription());
            locationTxt.setText(selected.getLocation());
            typeTxt.setText(selected.getType());
            startTimeCombo.setValue(selected.getStartTime().toString());
            startDate.setValue(selected.getStartTime().toLocalDateTime().toLocalDate());
            endTimeCombo.setValue(selected.getEndTime().toString());
            endDate.setValue(selected.getEndTime().toLocalDateTime().toLocalDate());
            Users user = UsersDAO.getUserID(selected.getUserId());
            userIdCombo.setValue(user);
            Customers customers = CustomersDAO.getCustomerName(selected.getCustomerId());
            customerIdCombo.setValue(customers);


        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


        try {
            ObservableList<Customers> customers = CustomersDAO.getCustomerID();
            customerIdCombo.setItems(customers);

            ObservableList<Users> users = UsersDAO.getUserID();
            userIdCombo.setItems(users);

            ObservableList<Contacts> contacts = ContactsDAO.getContactID();
            contactCombo.setItems(contacts);

            ObservableList<String> time = FXCollections.observableArrayList();
            LocalTime start = LocalTime.of(7, 0, 0, 0);
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
