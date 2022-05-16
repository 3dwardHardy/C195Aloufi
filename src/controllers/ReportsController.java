package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportsController {

    public void handleByTypeMonth(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/typeMonthReport.FXML"));
        stage.setTitle("Report By Type and Month");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    public void handleByContact(ActionEvent actionEvent) {
    }

    public void handleByCustomer(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/appointmentsByCustomer.FXML"));
        stage.setTitle("Report By Customer");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
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
