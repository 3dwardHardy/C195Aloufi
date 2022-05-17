package controllers;

import Models.Appointments;
import database.AppointmentsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class TypeMonthController implements Initializable {

    @FXML
    private TableView<Appointments> typeMonthTableView;

    @FXML
    private TableColumn<Appointments, Integer>  apptId;

    @FXML
    private TableColumn<Appointments, Timestamp> startTime;

    @FXML
    private TableColumn<Appointments, Integer> customerId;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TextField monthTxt;

    /**
     * Sets the type combo box with all the types currently listed in the database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> types = AppointmentsDAO.getApptsByType();
        typeCombo.setItems(types);
    }

    /**
     * Upon a user click will transport back to the report menu screen.
     * @param actionEvent
     * @throws IOException
     */
    public void handleBack(ActionEvent actionEvent) throws IOException {
        Stage stage = ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
        Parent scene = FXMLLoader.load(getClass().getResource("/reportMenu.FXML"));
        stage.setTitle("Report Menu");
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * On user click this will fill the tableview with the user selected data.
     * @param actionEvent
     */
    public void handleGenerate(ActionEvent actionEvent) {
        /**
         * Verifies the user entered a month number to search by. I.E. 1- for January etc. Will generate an error if empty.
         */
        int month = Integer.parseInt(monthTxt.getText());
            if(monthTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You did not enter a month!");
                alert.setContentText("Please enter a month number and try again.");
                alert.showAndWait();
                return;
            }
        /**
         * Ensure a combo box selection or will generate and error.
         */
        String type = typeCombo.getSelectionModel().getSelectedItem();
            if(type == null){
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText("You did not select a type!");
                alert1.setContentText("Please select a type and try again.");
                alert1.showAndWait();
                return;
            }
            typeMonthTableView.setItems(AppointmentsDAO.getApptsByTypeMonth(type, month));
            apptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }
}
