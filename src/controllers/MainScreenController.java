package controllers;

import Models.Appointments;
import database.AppointmentsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;



public class MainScreenController implements Initializable {

    @FXML
    private TableView <Appointments> appointmentsTableView;

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
    private TableColumn<Appointments, LocalDateTime> startDateTime;

    @FXML
    private TableColumn<Appointments, LocalDateTime> endDateTime;

    @FXML
    private TableColumn<Appointments, Integer> customerId;

    @FXML
    private TableColumn<Appointments, Integer> userId;

    static ObservableList<Appointments> appointments;

    public void handleCustomerMenu(ActionEvent actionEvent) {
    }

    public void handleAddAppt(ActionEvent actionEvent) {
    }

    public void handleModifyAppt(ActionEvent actionEvent) {
    }

    public void handleReports(ActionEvent actionEvent) {
    }

    public void handleLogout(ActionEvent actionEvent) {
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
        contactId.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));

    } catch (
    SQLException e) {
        e.printStackTrace();
    }
}

    }
