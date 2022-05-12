package database;

import Models.Countries;
import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesDAO {
    public static Countries getCountryId(String country) throws SQLException {
        String sqlStatement = "SELECT * FROM countries WHERE Country = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, country);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Countries countries = new Countries(
                    resultSet.getInt("Country_ID"),
                    resultSet.getString("Country"));
            return countries;
        }
        return null;
    }
}
