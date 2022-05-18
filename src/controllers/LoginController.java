package controllers;

import Models.Appointments;
import database.AppointmentsDAO;
import database.UsersDAO;
import helper.Logger;
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
import java.time.ZoneId;
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

    /**
     * This handles the login button click event.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void handleLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String username = userNameTxt.getText();
        String password = passTxt.getText();
        /**
         * this boolean will check that login is valid by compare the information in the form to the database information.
         * if the data does not match an error will generate advising the user of an invalid username or password.
         */
        boolean validLogon = UsersDAO.validLogin(username, password);
        /**
         * Checks that the username field has a value, if it does not will generate an error message advising of such.
         */
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
        /**
         * Checks that the password field has a value, if it does not will generate an error message advising of such.
         */
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
        /**
         * calls the login boolean and if a valid login is not found will generate an error stating as such. It also contains the
         * logging method call to track the login's whether they are successful or not. If a successful login has taken place
         * it will then load the main screen view.
         */
        if (!validLogon) {
            Logger.loginAttempts(username, password);

            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("loginError"));
                alert.setHeaderText(resourceBundle.getString("incorrect"));
                alert.setContentText(resourceBundle.getString("tryAgain"));
                alert.showAndWait();
            }
        }
         else {
            Logger.loginAttempts(username, password);
            apptAlert();
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
            stage.setTitle("Appointment Management System");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
    }

    /**
     * Resets the login form fields on user selection.
     * @param actionEvent
     */
    public void handleReset(ActionEvent actionEvent) {
        userNameTxt.setText("");
        passTxt.setText("");
    }

    /**
     * Exits the program on Exit button click.
     * @param actionEvent
     */
    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * This methods alerts the user that there is an appointment occurring in the next 15 minutes.
     * It does this by calling the database and comparing the stored data times to the local times.
     * It also displays the errors in Fnglish or French based on locale.
     */
    public void apptAlert() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime LDTPlus15 = localDateTime.plusMinutes(15);


        ObservableList<Appointments> upcomingAppts = FXCollections.observableArrayList();

        try {
            ObservableList<Appointments> appointments = AppointmentsDAO.getAppts();

            if (appointments != null) {
                for (Appointments appt : appointments) {
                    if (appt.getStartTime().toLocalDateTime().toLocalTime().isAfter(LocalTime.from(localDateTime))
                            && appt.getStartTime().toLocalDateTime().toLocalTime().isBefore(LocalTime.from(LDTPlus15)) &&
                            appt.getStartTime().toLocalDateTime().toLocalDate().equals(LocalDate.now())) {
                        upcomingAppts.add(appt);

                        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle(resourceBundle.getString("apptAlert"));
                            alert.setHeaderText(resourceBundle.getString("upcoming"));
                            alert.setContentText(resourceBundle.getString("lessThan15") + "\n" + appt.getApptId() + "\n" +
                                    resourceBundle.getString("occurs") + "\n" + appt.getStartTime().toLocalDateTime());
                            alert.showAndWait();
                        }
                    }
                }
                if (upcomingAppts.size() < 1) {
                    if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(resourceBundle.getString("apptAlert"));
                        alert.setHeaderText(resourceBundle.getString("noUpcoming"));
                        alert.setContentText(resourceBundle.getString("noAppts"));
                        alert.showAndWait();
                    }
                } else {
                    return;
                    }
                }
            }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * This calls the language resources to handle the French/English differences in error messages.
     * Also set the whole form to the desired language.
     * @param url
     * @param resource
     */
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

    /**
     * Handles the combo option for English language selection.
     * @param actionEvent
     */
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

    /**
     * Handles a user selecting the French option in the dropdown menu of the login form.
     * @param actionEvent
     */
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
}
