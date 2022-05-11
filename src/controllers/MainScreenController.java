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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    private TableColumn<Appointments, LocalDateTime> startDateTime;

    @FXML
    private TableColumn<Appointments, LocalDateTime> endDateTime;

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

    public void handleAddAppt(ActionEvent actionEvent) {

    }

    public void handleModifyAppt(ActionEvent actionEvent) {
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
