package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField userNameTxt;

    @FXML
    private TextField passTxt;

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
    public void handleLogin(KeyEvent keyEvent) {
    }

    /**
     * handles form reset by clearing input in text fields.
     * @param actionEvent
     */
    public void handleReset(ActionEvent actionEvent) {
        userNameTxt.setText("");
        passTxt.setText("");
    }
    /**
     * Gracefully exits the program.
     */
    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resourceBundle.getBundle("properties/French", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") {
            userLabel.setText(resourceBundle.getString("username"));
            passLabel.setText(resourceBundle.getString("password"));
            timeZoneLabel.setText(resourceBundle.getString("timezone"));
            currentTimeZoneLabel.setText(resourceBundle.getString("currentTimeZone"));
            languageMenu.setText(resourceBundle.getString("language"));
            englishLangSelection.setText(resourceBundle.getString("english"));
            frenchLangSelection.setText(resourceBundle.getString("french"));
            loginBtn.setText(resourceBundle.getString("login"));
            resetBtn.setText(resourceBundle.getString("reset"));
            exitBtn.setText(resourceBundle.getString("exit"));
        }

    }
}
