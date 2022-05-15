package database;

import Models.Customers;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public static void deleteCustomer(int customerId) throws SQLException {
        String sqlStatement = "DElETE FROM customers WHERE Customer_ID = ?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, customerId);
        preparedStatement.execute();
    }

    public static void updateCustomer(int customerId, String name, String address, String postalCode,
                                      String phone, int divisionId) {

        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

            String sqlStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                    "Phone = ?, Last_Update = ?, Last_Updated_By = ?,  Division_ID = ? WHERE Customer_ID = ?";

            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phone);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now().format(timeFormatter)));
            preparedStatement.setString(6, UsersDAO.getCurrentUserName());
            preparedStatement.setInt(7, divisionId);
            preparedStatement.setInt(8, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JDBC.closeConnection();
        }
    }

    public static ObservableList<Customers> getCustomerID() throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT Customer_ID, Customer_Name FROM customers;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int customerId = resultSet.getInt("Customer_ID");
            String customerName = resultSet.getString("Customer_Name");
            Customers customers1 = new Customers(customerId,customerName);
            customers.add(customers1);

        }
        return customers;
    }
}
