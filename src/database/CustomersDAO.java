package database;

import Models.Customers;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomersDAO {
    public static ObservableList<Customers> getAllCustomers() throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID =" +
                " d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Customers customer = new Customers(
                    resultSet.getInt("Customer_ID"),
                    resultSet.getString("Customer_Name"),
                    resultSet.getString("Address"),
                    resultSet.getString("Postal_Code"),
                    resultSet.getString("Country"),
                    resultSet.getInt("Division_ID"),
                    resultSet.getString("Phone"));
            customers.add(customer);

        }
        return customers;
    }

    public static void addNewCustomer(Customers customers) throws SQLException {
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

            String sqlStatement = "INSERT into customers (Customer_ID, Customer_Name, Address, Postal_Code, " +
                    "Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES" +
                    "(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, customers.getCustomerName());
            preparedStatement.setString(2, customers.getAddress());
            preparedStatement.setString(3, customers.getPostalCode());
            preparedStatement.setString(4, customers.getPhone());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now().format(timeFormatter)));
            preparedStatement.setString(6, UsersDAO.getCurrentUserName());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now().format(timeFormatter)));
            preparedStatement.setString(8, UsersDAO.getCurrentUserName());
            preparedStatement.setInt(9, customers.getDivisionId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JDBC.closeConnection();
        }
    }
}
