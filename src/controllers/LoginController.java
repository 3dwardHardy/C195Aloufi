package controllers;


import Models.Appointments;
import Models.Users;
import database.AppointmentsDAO;
import database.UsersDAO;
import helper.JDBC;
import helper.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private TextField userNameTxt;

    @FXML
    private PasswordField passTxt;

    @FXML
    private Label userLabel;

    @FXML
    private Label passLabel;

    @FXML
    private Label timeZoneLabel;

    @FXML
    private Label currentTimeZoneLabel;

    @FXML
    private SplitMenuButton languageMenu;

    @FXML
    private MenuItem frenchLangSelection;

    @FXML
    private MenuItem englishLangSelection;

    @FXML
    private Button loginBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button exitBtn;

    private ResourceBundle resourceBundle;

    ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList();

    private DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private ZoneId localZoneId = ZoneId.systemDefault();

    public void handleLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String username = userNameTxt.getText();
        String password = passTxt.getText();

        boolean validLogon = UsersDAO.validLogin(username, password);

        if (username.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("loginError"));
                alert.setHeaderText(resourceBundle.getString("noUser"));
                alert.setContentText(resourceBundle.getString("tryAgain"));
                alert.showAndWait();
                return;
            }
        }

        if (password.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("loginError"));
                alert.setHeaderText(resourceBundle.getString("noPassword"));
                alert.setContentText(resourceBundle.getString("tryAgain"));
                alert.showAndWait();
                return;
            }
        }

        if (!validLogon) {
            Logger.loginAttempts(username,password);
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("loginError"));
                alert.setHeaderText(resourceBundle.getString("incorrect"));
                alert.setContentText(resourceBundle.getString("tryAgain"));
                alert.showAndWait();
            }
        }
         else {
            apptList();
            appointmentAlert();

            Logger.loginAttempts(username,password);
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
            stage.setTitle("Appointment Management System");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
    }

    public void handleReset(ActionEvent actionEvent) {
        userNameTxt.setText("");
        passTxt.setText("");
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }


        @Override
    public void initialize(URL url, ResourceBundle resource) {

        resourceBundle = ResourceBundle.getBundle("Language/language", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            userLabel.setText(resourceBundle.getString("username"));
            passLabel.setText(resourceBundle.getString("password"));
            timeZoneLabel.setText(resourceBundle.getString("timezone"));
            currentTimeZoneLabel.setText(String.valueOf(ZoneId.systemDefault()));
            languageMenu.setText(resourceBundle.getString("language"));
            englishLangSelection.setText(resourceBundle.getString("english"));
            frenchLangSelection.setText(resourceBundle.getString("french"));
            loginBtn.setText(resourceBundle.getString("login"));
            resetBtn.setText(resourceBundle.getString("reset"));
            exitBtn.setText(resourceBundle.getString("exit"));
        }
    }

    public void handleEnglish(ActionEvent actionEvent) {
        ResourceBundle  resource = ResourceBundle.getBundle("Language/language", Locale.ENGLISH);
        userLabel.setText(resource.getString("username"));
        passLabel.setText(resource.getString("password"));
        timeZoneLabel.setText(resource.getString("timezone"));
        languageMenu.setText(resource.getString("language"));
        englishLangSelection.setText(resource.getString("english"));
        frenchLangSelection.setText(resource.getString("french"));
        loginBtn.setText(resource.getString("login"));
        resetBtn.setText(resource.getString("reset"));
        exitBtn.setText(resource.getString("exit"));

    }

    public void handleFrench(ActionEvent actionEvent) {
        ResourceBundle  resource = ResourceBundle.getBundle("Language/language", Locale.FRENCH);
        userLabel.setText(resource.getString("username"));
        passLabel.setText(resource.getString("password"));
        timeZoneLabel.setText(resource.getString("timezone"));
        languageMenu.setText(resource.getString("language"));
        englishLangSelection.setText(resource.getString("english"));
        frenchLangSelection.setText(resource.getString("french"));
        loginBtn.setText(resource.getString("login"));
        resetBtn.setText(resource.getString("reset"));
        exitBtn.setText(resource.getString("exit"));
    }
    private void appointmentAlert() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);

        FilteredList<Appointments> filteredData = new FilteredList<>(appointmentsObservableList);

        /**
         * lamba to neatly handle getting the appointmentss that occur within 15 mins.
         */
        filteredData.setPredicate(row -> {
                    LocalDateTime rowDate = LocalDateTime.parse(row.getStartTime().toString().substring(0, 16), dtformat);
                    return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
                }
        );
        if (filteredData.isEmpty()) {
            MainScreenController.noCloseAppts();

        } else {
            String type = filteredData.get(0).getDescription();
            String customer = String.valueOf(filteredData.get(0).getCustomerId());
            String start = filteredData.get(0).getStartTime().toString().substring(0, 16);
            MainScreenController.apptsIn15();
        }
    }
    private void apptList() {

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.description, "
                            + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId AND appointment.createdBy = ? "
                            + "ORDER BY `start`");
            ps.setString(1, Users.getUserName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                /**
                 * coverts to local time.
                 */
                Timestamp timestampStart = rs.getTimestamp("start");
                ZonedDateTime startUTC = timestampStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = startUTC.withZoneSameInstant(localZoneId);

                /**
                converts end time to local time
                 */
                Timestamp timestampEnd = rs.getTimestamp("end");
                ZonedDateTime endUTC = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = endUTC.withZoneSameInstant(localZoneId);

                /**
                 * pulls data needed to perform method
                 */
                int appointmentId = rs.getInt("appointmentId");
                String title = rs.getString("title");
                String type = rs.getString("description");
                String customerName = rs.getString("customerName");
                int customerId = rs.getInt("customerId");
                String user = rs.getString("createdBy");

                /**
                 * inserts the appointments into an observable list
                 */
                appointmentsObservableList.add(new Appointments(appointmentId, newLocalStart.toString(), newLocalEnd.toString(),
                        title, type, customerId, customerName, user));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
