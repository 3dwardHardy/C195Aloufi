package database;

import Models.Countries;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesDAO {
    public static ObservableList<Countries> getCountryId(){
        ObservableList<Countries> countries = FXCollections.observableArrayList();
        try {
            String sqlStatement = "Select Country_ID, Country FROM countries;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");
                String country = resultSet.getString("Country");
                Countries countries1 = new Countries(countryId, country);
                countries.add(countries1);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return countries;
    }

    public static Countries returnCountryId(int countryId) {
            try {
                String sqlStatement = "SELECT Country_ID, Country FROM countries WHERE Country_ID = ?";
                PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

                preparedStatement.setInt(1, countryId);
                preparedStatement.execute();

                ResultSet resultSet = preparedStatement.getResultSet();

                resultSet.next();
                int setCountryId = resultSet.getInt("Country_ID");
                String country1 = resultSet.getString("Country");
                Countries countryName = new Countries(setCountryId, country1);
                return countryName;

            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            return null;
        }
    }
