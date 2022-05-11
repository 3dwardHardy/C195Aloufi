package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;


import java.net.URL;
import java.time.ZoneId;
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
        if(userNameTxt.getText().isEmpty()) {
            if(Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }
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
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentTimeZoneLabel.setText(String.valueOf(ZoneId.systemDefault()));

      ResourceBundle  resource = ResourceBundle.getBundle("Properties/French", Locale.getDefault());
      if(Locale.getDefault().getLanguage().equals("fr")) {
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
}
