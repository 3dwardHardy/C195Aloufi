package database;

import Models.Appointments;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentsDAO {

    public static ObservableList<Appointments> getAppts() throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments;";
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
                    resultSet.getDate("Start").toLocalDate(),
                    resultSet.getTimestamp("Start").toLocalDateTime(),
                    resultSet.getDate("End").toLocalDate(),
                    resultSet.getTimestamp("End").toLocalDateTime(),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID"));
            appointments.add(appts);
            }
        return appointments;
        }
    }

