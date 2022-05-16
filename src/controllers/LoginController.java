package controllers;

import database.UsersDAO;
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
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static database.UsersDAO.validLogin;

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
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("loginError"));
                alert.setHeaderText(resourceBundle.getString("incorrect"));
                alert.setContentText(resourceBundle.getString("tryAgain"));
                alert.showAndWait();
            }
        }
         else {
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
            stage.setTitle("Appointment Management System");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
    }

    //private void loginSuccess() {

    //}

   // private void loginFailed() {

   // }

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
}
