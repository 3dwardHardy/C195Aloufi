package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportsController {
    /**
     * On user click opens the screen to select and display a report containing all appointments by a user selected type.
     * @param actionEvent
     * @throws IOException
     */
    public void handleByTypeMonth(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/typeMonthReport.FXML"));
        stage.setTitle("Report By Type and Month");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * On click opens the screen to allow the user to generate reports by the user selected contact.
     * @param actionEvent
     * @throws IOException
     */
    public void handleByContact(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/apptsByContact.FXML"));
        stage.setTitle("Report By Contact");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * On click opens a menu to allow the user to generate reports by the user selected customer.
     * @param actionEvent
     * @throws IOException
     */
    public void handleByCustomer(ActionEvent actionEvent) throws IOException{
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/appointmentsByCustomer.FXML"));
        stage.setTitle("Report By Customer");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles a back button click event and sends the user back to the programs main screen.
     * @param actionEvent
     * @throws IOException
     */
    public void handleBack(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/mainScreen.FXML"));
        stage.setTitle("Appointment Management System");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }
}
