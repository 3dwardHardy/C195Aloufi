package database;

import Models.Countries;
import Models.FirstLevelDivisions;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDAO {
    public static ObservableList<FirstLevelDivisions> getFirstLevel(String country) throws SQLException {
        Countries countries = CountriesDAO.getCountryId(country);
        ObservableList<FirstLevelDivisions> firstLevelDivisions = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM first_level_divisions WHERE Country_ID = ?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, CountriesDAO.getCountryId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            FirstLevelDivisions firstLevelDivision = new FirstLevelDivisions(
                    resultSet.getInt("Division_ID"),
                    resultSet.getString("Division"),
                    resultSet.getInt("Country_ID"));
            firstLevelDivisions.add(firstLevelDivision);
        }
        return firstLevelDivisions;
    }
    public static ObservableList<FirstLevelDivisions> getStatesByCountry(Countries selectedItem) {
    }
}
