package helper;

import javafx.scene.control.Alert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Conversions {

    public static void outOfOfficeHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Appointment Error");
        alert.setHeaderText("You are attempting to schedule outside business hours!");
        alert.setContentText("Please try a time between 8am - 10pm EST.");
        alert.showAndWait();
    }

    public static Timestamp getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return Timestamp.valueOf(dateFormat.format(timestamp));
    }
}
