package helper;

import javafx.scene.control.Alert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Conversions {
    /**
     * Handles calls for the out of office hours error screen.
     */
    public static void outOfOfficeHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Appointment Error");
        alert.setHeaderText("You are attempting to schedule outside business hours!");
        alert.setContentText("Please try a time between 8am - 10pm EST.");
        alert.showAndWait();
    }

    /**
     * Callable method sets the format for the current time stamps
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return Timestamp.valueOf(dateFormat.format(timestamp));
    }
}
