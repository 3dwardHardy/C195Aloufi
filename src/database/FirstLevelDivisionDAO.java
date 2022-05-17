package database;

import Models.FirstLevelDivisions;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDAO {
    /**
     * Generates a list of all first level division names and id's.
     * @return
     */
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

    /**
     * Generates a list we can call that only shows the first level divisions that match a specified country ID.
     * @param countryId
     * @return
     */
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

    /**
     * Creates a list that only gives the division name when the division Id matches what was selected.
     * @param state
     * @return
     * @throws SQLException
     */
    public static FirstLevelDivisions getState(int state) throws SQLException {
        try {
            String sqlStatement = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, state);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String selectedState = resultSet.getString("Division");
                int divisionId = resultSet.getInt("Division_ID");
                FirstLevelDivisions chosenDivision= new FirstLevelDivisions(divisionId, selectedState);
                return chosenDivision;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
}
