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
    public static ObservableList<FirstLevelDivisions> getFirstLevel() {
        ObservableList<FirstLevelDivisions> states = FXCollections.observableArrayList();

        try {
            String sqlStatement = "SELECT Division_ID, Division FROM first_level_divisions";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                FirstLevelDivisions firstLevelDivisions = new FirstLevelDivisions(divisionId,division);
                states.add(firstLevelDivisions);
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return states;
    }

    public static FirstLevelDivisions returnFirstLevelDivisions (int divisionId) {
        try {
            String sqlStatement = "SELECT DivisionID, Division FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, divisionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            int checkDivisionId = resultSet.getInt("Division_ID");
            String division = resultSet.getString("Division");
            FirstLevelDivisions firstLevel= new FirstLevelDivisions(checkDivisionId, division);
            return firstLevel;
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    public static ObservableList<FirstLevelDivisions> returnDivisionCountry(int countryId) {
        ObservableList<FirstLevelDivisions> filterStates = FXCollections.observableArrayList();
        try {
            String sqlStatement = "SELECT Division_ID, Division FROM first_level_divisions WHERE Country_ID =?;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, countryId);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                FirstLevelDivisions firstLevelDivisions = new FirstLevelDivisions(divisionId, division);
                filterStates.add(firstLevelDivisions);
            }
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return filterStates;
    }
}
