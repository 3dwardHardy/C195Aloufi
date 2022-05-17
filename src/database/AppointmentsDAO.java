package database;

import Models.Appointments;

import helper.Conversions;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class AppointmentsDAO {
    /**
     * This calls the database and returns the appointment info as a list.
     * @return
     * @throws SQLException
     */
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

    /**
     * This checks for appointments by customer ID and returns the matching data as a list.
     * @param customerId
     * @return
     */
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

    /**
     * This also searches by customerID by returns other relevent information.
     * @param customerID
     * @return
     * @throws SQLException
     */
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

    /**
     * This handles pushing the newly acquired appointment data to the appointment database and inserts the appointment database.
     * @param appointments
     */
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

    /**
     * This handles deleting an appointment record and all its associated data from the database.
     * @param appointmentId
     * @throws SQLException
     */
    public static void deleteAppts(int appointmentId) throws SQLException {
        String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

        preparedStatement.setInt(1, appointmentId);

        preparedStatement.execute();
    }

    /**
     * This check the database records to ensure that appointment start and end times do not overlap.
     * @param startTime
     * @param endTime
     * @param customerID
     * @return
     */
    public static boolean checkForOverlappingAppointment(Timestamp startTime, Timestamp endTime, int customerID) {
        try {
            String sqlStatement = "SELECT * FROM appointments WHERE Customer_ID = ? and Start <= ? and end >= ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setObject(2, endTime);
            preparedStatement.setObject(3, startTime);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * This will update the appointment record in the database. It updates the record based on the values recieved from the
     * modify appointment form.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     * @param apptId
     */
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

    /**
     * This returns a list of appointments in the database by the contact ID.
     * @param contactId
     * @return
     */
    public static ObservableList<Appointments> getApptsByContactId(int contactId) {

        ObservableList<Appointments> appts = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Type, Start, End, Customer_ID from appointments WHERE Contact_ID = ?";

            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setInt(1, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                Timestamp startDate = resultSet.getTimestamp("Start");
                Timestamp endDate = resultSet.getTimestamp("End");
                int customerId = resultSet.getInt("Customer_ID");
                Appointments apts = new Appointments(apptId, title, description, type, startDate, endDate, customerId, contactId);

                appts.add(apts);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appts;

    }

    /**
     * This sql statement gathers the appointments by type and month and returns it as a list.
     * @param type
     * @param month
     * @return
     */
    public static ObservableList<Appointments> getApptsByTypeMonth(String type, int month) {

        ObservableList<Appointments> appts = FXCollections.observableArrayList();

        try {
            String sqlStatement = "SELECT Appointment_ID, Start, Customer_ID from appointments WHERE Type = ? AND MONTH(Start) = ?";

            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                Timestamp startTime = resultSet.getTimestamp("Start");
                int customerId = resultSet.getInt("Customer_ID");

                Appointments apts = new Appointments(apptId, startTime, customerId);

                appts.add(apts);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appts;
    }

    /**
     * Generates a list returning all the type values.
     * @return
     */
    public static ObservableList<String> getApptsByType() {
        ObservableList<String> type = FXCollections.observableArrayList();

        try {
            String sql = "SELECT DISTINCT Type FROM appointments";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String types = rs.getString("Type");
                type.add(types);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * I replaced both of the functions with lambda expressions. I left the code here as a note to show how much code I was able to save
     * with the lamba's rather than writing out all of this. The lambda's perform the same objective on the data, but are much more
     * efficient and use an existing function rather than having to create new functions.
     * @return
     * @throws SQLException

    public static ObservableList<Appointments> getApptsMonth() throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();

        try {

            String sqlStatement = "SELECT * from appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID" +
                    " WHERE MONTH(start) = MONTH(NOW()) AND YEAR(start) = YEAR(NOW());";

            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Appointments newList = new Appointments(
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
                appointments.add(newList);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return appointments;
    }

    public static ObservableList<Appointments> getApptsWeek() throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();

        try {

            String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c " +
                    "ON a.Contact_ID=c.Contact_ID WHERE YEARWEEK(start) = YEARWEEK(NOW())";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Appointments newList = new Appointments(
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
                appointments.add(newList);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return appointments;
    }
    */
}
