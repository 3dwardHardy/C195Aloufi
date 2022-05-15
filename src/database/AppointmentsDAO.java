package database;

import Models.Appointments;
import helper.Conversions;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class AppointmentsDAO {

    public static ObservableList<Appointments> getAppts() throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Appointments appts = new Appointments(
                    resultSet.getInt("Appointment_ID"),
                    resultSet.getString("Title"),
                    resultSet.getString("Description"),
                    resultSet.getString("Location"),
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Type"),
                    resultSet.getTimestamp("Start"),
                    resultSet.getTimestamp("End"),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID"),
                    resultSet.getString("Contact_Name"));
            appointments.add(appts);
        }
        return appointments;
    }

    public static int checkForAppts(int customerId) {
        ObservableList<Integer> appointments = FXCollections.observableArrayList();
        try {
            String sqlStatement = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                appointments.add(appointmentId);

            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return appointments.size();
    }

    public static ObservableList<Appointments> getApptsByCustomerID(int customerID) throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=" +
                "c.Contact_ID WHERE Customer_ID=?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, customerID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Appointments appt = new Appointments(
                    resultSet.getInt("Customer_ID"),
                    resultSet.getString("Title"),
                    resultSet.getString("Description"),
                    resultSet.getString("Location"),
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Type"),
                    resultSet.getTimestamp("Start"),
                    resultSet.getTimestamp("End"),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID"),
                    resultSet.getString("Contact_Name"));

            appointments.add(appt);
        }
        return appointments;

    }

    public static void createAppt(Appointments appointments) {

        try {
            String sqlStatement = "INSERT into appointments (Appointment_ID, Title, Description, Location, Type, " +
                    "Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, appointments.getTitle());
            preparedStatement.setString(2, appointments.getDescription());
            preparedStatement.setString(3, appointments.getLocation());
            preparedStatement.setString(4, appointments.getType());
            preparedStatement.setTimestamp(5, appointments.getStartTime());
            preparedStatement.setTimestamp(6, appointments.getEndTime());
            preparedStatement.setTimestamp(7, Conversions.getCurrentTimestamp());
            preparedStatement.setString(8, UsersDAO.getCurrentUserName());
            preparedStatement.setTimestamp(9, Conversions.getCurrentTimestamp());
            preparedStatement.setString(10, UsersDAO.getCurrentUserName());
            preparedStatement.setInt(11, appointments.getCustomerId());
            preparedStatement.setInt(12, appointments.getUserId());
            preparedStatement.setInt(13, appointments.getContactId());
            preparedStatement.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteAppts(int appointmentId) throws SQLException {
        String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, appointmentId);

        preparedStatement.execute();
    }

    public static boolean checkForOverlappingAppointment(Timestamp startTime, Timestamp endTime, int customerID) {
        try {
            String sqlStatement = "SELECT * FROM appointments WHERE Customer_ID = ? and Start <= ? and end >= ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setObject(2, endTime);
            preparedStatement.setObject(3, startTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            // If there is a result row, we know it’s an overlapping appointment
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void updateAppointment(String title, String description, String location, String type,
                                         Timestamp start, Timestamp end, int customerId,
                                         int userId, int contactId, int apptId) {

        try {
            String sqlStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                    "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
                    "User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, Conversions.getCurrentTimestamp());
            ps.setString(8, UsersDAO.getCurrentUserName());
            ps.setInt(9, customerId);
            ps.setInt(10, userId);
            ps.setInt(11, contactId);
            ps.setInt(12, apptId);
            ps.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static ObservableList<Appointments> getApptsByApptId(int customerId) throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Customer_ID=?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, customerId);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Appointments newAppts = new Appointments(
                    resultSet.getInt("Appointment_ID"),
                    resultSet.getString("Title"),
                    resultSet.getString("Description"),
                    resultSet.getString("Location"),
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Type"),
                    resultSet.getTimestamp("Start"),
                    resultSet.getTimestamp("End"),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID"),
                    resultSet.getString("Contact_Name"));
            appointments.add(newAppts);
        }
        return appointments;
    }

    public static void apptIn15() {
        ObservableList<String> apptList = FXCollections.observableArrayList();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String systemTime = format.format(timestamp);

        try {
            String sqlStatement = "SELECT Appointment_ID, Start FROM appointments WHERE Start BETWEEN CONVERT" +
                    "('"+systemTime+"', DATETIME) AND CONVERT('"+systemTime+"', DATETIME) + INTERVAL 15 MINUTE;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                Timestamp startTime = resultSet.getTimestamp("Start");
                String aId = String.valueOf(apptId);
                String start = String.valueOf(startTime);
                String apptMessage = "An appointment with an ID of: " + aId + "Start Date And Time: " + start;

                apptList.add(apptMessage);
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }if (apptList.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ALERT");
            alert.setHeaderText("You have appointments soon!");
            alert.setContentText("You have an appointment that starts within the next 15 minutes.");
            alert.showAndWait();
        }
        else {
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ALERT");
            alert.setHeaderText("You have no immediate appointments!");
            alert.setContentText("You do not have any appointments starting within the next 15 minutes.");
            alert.showAndWait();
        }
    }

}