package controllers;

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
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIdTxt;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField typeTxt;

    @FXML
    private ComboBox<String> contactCombo;

    @FXML
    private ComboBox<String> customerIdCombo;

    @FXML
    private ComboBox<String> userIdCombo;

    @FXML
    private ComboBox<String> startTimeCombo;

    @FXML
    private ComboBox<String> endTimeCombo;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;
    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are sure you wish to cancel modifying this appointment?");
        alert.setContentText("If yes, press OK to return to the main screen.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
            Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
            stage.setTitle("Appointment Management System");
            stage.setScene(new Scene(scene));
            stage.show();
            stage.centerOnScreen();
        }
    }

    public void handleClear(ActionEvent actionEvent) {
        appointmentIdTxt.setText("");
        titleTxt.setText("");
        descriptionTxt.setText("");
        locationTxt.setText("");
        contactCombo.getSelectionModel().clearSelection();
        typeTxt.setText("");
        customerIdCombo.getSelectionModel().clearSelection();
        userIdCombo.getSelectionModel().clearSelection();
        startDate.setValue(null);
        startTimeCombo.getSelectionModel().clearSelection();
        endDate.setValue(null);
        endTimeCombo.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
