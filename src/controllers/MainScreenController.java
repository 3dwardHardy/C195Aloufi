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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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


    /**
     * Handles the customer menu button. On click loads the customer menu page.
     * @param actionEvent
     * @throws IOException
     */
    public void handleCustomerMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/customerMenu.FXML"));
        stage.setTitle("Customer Menu");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the add appointment button. On click loads the add appointment screen.
     * @param actionEvent
     * @throws IOException
     */
    public void handleAddAppt(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/addAppointment.FXML"));
        stage.setTitle("Add A New Appointment");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the modify appointment button. On click, it will check to see if an appointment from the table view was selected.
     * If no selection has been made, will generate an error. If a selection was made it will grab the selected data and use it to populate the
     * modify appointment screen on it's launch.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
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

    /**
     * This handles the report button. On click will take the user to the report menu.
     * @param actionEvent
     * @throws IOException
     */
    public void handleReports(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/reportMenu.FXML"));
        stage.setTitle("Report Menu");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * This sets the view group for the radio buttons and will update the table view by which button is selected.
     * @param event
     * @throws SQLException
     */
    @FXML
    void ViewGroup (ActionEvent event) throws SQLException {
        /**
         * The view all button is selected by default. This ensures the appointment data is displayed in whole.
         */
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
            LocalDateTime startMonth = LocalDateTime.now().withDayOfMonth(1);
            LocalDateTime endMonth = LocalDateTime.now().withDayOfMonth(31);

            /**
             * Lambda to filter appointment list to get appointments within month view.
             * this greatly reduced my amount of code, because previously I was using a long winded SQL function.
             * which I noted out of the code to show the difference in methods.
             */
            FilteredList<Appointments> filterMonth = new FilteredList<>(appts);
            filterMonth.setPredicate(row -> {
                LocalDateTime start = (row.getStartTime().toLocalDateTime());
                if (startMonth.isAfter(start) || endMonth.isBefore(start)) {
                    return false;
                }
                return true;
            });

            appointmentsTableView.setItems(filterMonth);
            appointmentsTableView.refresh();


        } else if (ViewGroup.getSelectedToggle().equals(viewWeekBtn)) {
            ObservableList<Appointments> appts = AppointmentsDAO.getAppts();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startWeek = now.with(DayOfWeek.MONDAY);
            LocalDateTime endWeek = now.with(DayOfWeek.SUNDAY);
            /**
             * Lambda to filter by the week to generate the week appt view.
             * Also left original sql statement to show the amount of code saved by this lambda function.
             */
            FilteredList<Appointments> appointmentsFilteredList = new FilteredList<>(appts);
            appointmentsFilteredList.setPredicate(row -> {
                LocalDateTime start = (row.getStartTime().toLocalDateTime());
                if (startWeek.isAfter(start) || LocalDateTime.now().isEqual(now) || endWeek.isBefore(start)) {
                    return false;
                }
                return true;
            });
            appointmentsTableView.setItems(appointmentsFilteredList);
            appointmentsTableView.refresh();
        }
    }

    /**
     * This handles the logout button click and ensures the database connection is closed.
     * It will also take the user back to an empty login screen.
     * @param actionEvent
     * @throws IOException
     */
    public void handleLogout(ActionEvent actionEvent) throws IOException {
        JDBC.closeConnection();
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/loginScreen.FXML"));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * This handles appointment deletions by verifying that a selection was made and if no selection was made will
     * generate a user error. It also gets the selected appointment and casts it to the modify appointment screen to
     * prepopulate the form fields with the existing appointment information.
     * @param actionEvent
     */
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

    /**
     * This sets up the toggle group to adjust the table view to the desired view.
     * It also sets up the table values on the main screens launch.
     * @param url
     * @param resourceBundle
     */
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
