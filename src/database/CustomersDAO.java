package database;

import Models.Customers;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
