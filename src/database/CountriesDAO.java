package database;

import Models.Countries;
import Models.Customers;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesDAO {
    /**
     * Generates a list of countries and their id's.
     * @return
     */
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

    /**
     * Generates a list with data filtered by country.
     * @param country
     * @return
     */
    public static Countries getCountry(String country) {
        try {
            String sqlStatement = "SELECT Country, Country_ID FROM countries WHERE Country = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, country);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String selectedCountry = resultSet.getString("Country");
                int countryId = resultSet.getInt("Country_ID");
                Countries chosenCountry = new Countries(countryId, selectedCountry);
                return chosenCountry;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
    }
