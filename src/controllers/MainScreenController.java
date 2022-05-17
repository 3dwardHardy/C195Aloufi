package controllers;

import Models.Appointments;
import database.AppointmentsDAO;
import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @FXML
    private ToggleGroup ViewGroup;

    @FXML
    private RadioButton viewAllBtn;

    @FXML
    private RadioButton viewMonthBtn;

    @FXML
    private RadioButton viewWeekBtn;

    static ObservableList<Appointments> appointments;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



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

    public void handleReports(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/reportMenu.FXML"));
        stage.setTitle("Report Selection");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }
    @FXML
    void ViewGroup (ActionEvent event) throws SQLException {

        if (viewAllBtn.isSelected()) {
            try {
                appointments = AppointmentsDAO.getAppts();
                appointmentsTableView.setItems(appointments);
                appointmentsTableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (ViewGroup.getSelectedToggle().equals(viewMonthBtn)) {
            ObservableList<Appointments> appts = AppointmentsDAO.getAppts();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime month = now.plusMonths(1);

            /**
             * lambda to filter appointment list to get appointments within month view,
             * this shows all appointments with in the next 30 days.
             * this greatly reduced my amount of code.
             */
            FilteredList<Appointments> filterMonth = new FilteredList<>(appts);
            filterMonth.setPredicate(row -> {
                LocalDateTime start = (row.getStartTime().toLocalDateTime());
                return start.isAfter(now) && start.isBefore(month);
            });

            appointmentsTableView.setItems(filterMonth);


        } else if (ViewGroup.getSelectedToggle().equals(viewWeekBtn)) {
            ObservableList<Appointments> appts = AppointmentsDAO.getAppts();
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime currentWeek = current.plusWeeks(1);
            /**
             * lambda to filter by the next 7 days;  to generate within week appt view.
             */
            FilteredList<Appointments> appointmentsFilteredList = new FilteredList<>(appts);
            appointmentsFilteredList.setPredicate(row -> {
                LocalDateTime start = (row.getStartTime().toLocalDateTime());
                return start.isAfter(current) && start.isBefore(currentWeek);
            });
            appointmentsTableView.setItems(appointmentsFilteredList);
        }
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
        viewAllBtn.setToggleGroup(ViewGroup);
        viewMonthBtn.setToggleGroup(ViewGroup);
        viewWeekBtn.setToggleGroup(ViewGroup);
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
}
