package controllers;

import Models.Appointments;
import database.AppointmentsDAO;
import helper.JDBC;
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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;



public class MainScreenController implements Initializable {

    @FXML
    private TableView<Appointments> appointmentsTableView;

    @FXML
    private TableColumn<Appointments, Integer> apptId;

    @FXML
    private TableColumn<Appointments, String> title;

    @FXML
    private TableColumn<Appointments, String> description;

    @FXML
    private TableColumn<Appointments, String> location;

    @FXML
    private TableColumn<Appointments, Integer> contactId;

    @FXML
    private TableColumn<Appointments, String> type;

    @FXML
    private TableColumn<Appointments, Timestamp> startTime;

    @FXML
    private TableColumn<Appointments, Timestamp> endTime;

    @FXML
    private TableColumn<Appointments, Integer> customerId;

    @FXML
    private TableColumn<Appointments, Integer> userId;

    static ObservableList<Appointments> appointments;


    public void handleCustomerMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/customerMenu.FXML"));
        stage.setTitle("Customer Menu");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    public void handleAddAppt(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/addAppointment.FXML"));
        stage.setTitle("Add A New Appointment");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    public void handleModifyAppt(ActionEvent actionEvent) throws IOException, SQLException {
    ModifyAppointmentController.retrieveAppts(appointmentsTableView.getSelectionModel().getSelectedItem());
        if (appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/modifyAppointment.FXML"));
            stage.setTitle("Modify An Existing Appointment");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Modify Appointment Error");
            alert.setHeaderText("You did not select an appointment!");
            alert.setContentText("Please make a selection and try again.");
            alert.showAndWait();
        }
    }

    public void handleReports(ActionEvent actionEvent) {
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        JDBC.closeConnection();
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/loginScreen.FXML"));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    public void handleCustomerView(ActionEvent actionEvent) {
    }

    public void handleWeekView(ActionEvent actionEvent) {
    }

    public void handleMonthView(ActionEvent actionEvent) {
    }

    public void handleAllView(ActionEvent actionEvent) {
    }

    public void handleApptDelete(ActionEvent actionEvent) {
        Appointments selectedAppt = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("");
            alert.showAndWait();
        } else if (selectedAppt != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete Appointment");
            alert.setHeaderText("Do you wish to delete this appointment?");
            alert.setContentText("This will remove the appointment from the database. Press OK to confirm.");
            Optional<ButtonType> result = alert.showAndWait();

            try {
                AppointmentsDAO.deleteAppts(appointmentsTableView.getSelectionModel().getSelectedItem().getApptId());
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Appointment Deleted");
                alert1.setHeaderText("Appointment delete was successful.");
                alert1.setContentText("Successfully removed Appointment ID: " + selectedAppt.getApptId() + " for Appointment TYPE: " + selectedAppt.getType() +".");
                alert1.showAndWait();

                appointments = AppointmentsDAO.getAppts();
                appointmentsTableView.setItems(appointments);
                appointmentsTableView.refresh();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointments = AppointmentsDAO.getAppts();
            appointmentsTableView.setItems(appointments);
            apptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactId.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

        public static void apptsIn15() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ALERT");
            alert.setHeaderText("You have appointments soon!");
            alert.setContentText("You have an appointment that starts within the next 15 minutes.");
            alert.showAndWait();
        }

        public static void noCloseAppts () {
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ALERT");
            alert.setHeaderText("You have no immediate appointments!");
            alert.setContentText("You do not have any appointments starting within the next 15 minutes.");
            alert.showAndWait();
        }
}
