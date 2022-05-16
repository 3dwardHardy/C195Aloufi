package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportsController {

    public void handleByTypeMonth(ActionEvent actionEvent) {
    }

    public void handleByContact(ActionEvent actionEvent) {
    }

    public void handleByCustomer(ActionEvent actionEvent) {
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
        stage.setTitle("Appointment Management System");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }
}
