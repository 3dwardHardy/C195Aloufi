package controllers;

import database.AppointmentsDAO;
import database.UsersDAO;
import helper.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Properties;
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


    public boolean handleLogin(ActionEvent actionEvent) throws IOException {
        try{
        String userName = userNameTxt.getText();
        String password = passTxt.getText();
        String dateTime = ZonedDateTime.now(ZoneId.of("UTC")).toString();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Properties/French");
        ResourceBundle rb = ResourceBundle.getBundle("Properties/English");

         boolean validatedLogon = UsersDAO.validLogin(userName, password);
         if (!validatedLogon) {
             if (Locale.getDefault().getLanguage().equals("fr")) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle(resourceBundle.getString("loginError"));
                 alert.setHeaderText(resourceBundle.getString("incorrect"));
                 alert.setContentText(resourceBundle.getString("tryAgain"));
                 alert.showAndWait();
             }

                 if (Locale.getDefault().getLanguage().equals("en")) {
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle(rb.getString("loginError"));
                     alert.setHeaderText(rb.getString("incorrect"));
                     alert.setContentText(rb.getString("tryAgain"));
                     alert.showAndWait();
                 }
         }
         if (userNameTxt.getText().isEmpty()) {
             if (Locale.getDefault().getLanguage().equals("fr")) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle(resourceBundle.getString("loginError"));
                 alert.setHeaderText(resourceBundle.getString("noUser"));
                 alert.setContentText(resourceBundle.getString("tryAgain"));
                 alert.showAndWait();
             } if (Locale.getDefault().getLanguage().equals("en")) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle(rb.getString("loginError"));
                 alert.setHeaderText(rb.getString("noUser"));
                 alert.setContentText(rb.getString("tryAgain"));
                 alert.showAndWait();
             }
         }
         if (passTxt.getText().isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("loginError"));
                alert.setHeaderText(resourceBundle.getString("noPassword"));
                alert.setContentText(resourceBundle.getString("tryAgain"));
                alert.showAndWait();
            }
            if (Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("loginError"));
                alert.setHeaderText(rb.getString("noPassword"));
                alert.setContentText(rb.getString("tryAgain"));
                alert.showAndWait();
            }
        }
         else {
             Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
             Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
             stage.setTitle("Appointment Management System");
             stage.setScene(new Scene(scene));
             stage.show();
             return true;
         }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
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
