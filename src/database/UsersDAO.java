package database;

import Models.Users;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    static String currentUser;

    /**
     * Checks the database for a matching username and password on an attempted login action.
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean validLogin(String userName, String password) throws SQLException {
        try {
            String sqlStatement = "SELECT * FROM users WHERE User_Name = ? AND Password = ?;";
            JDBC.openConnection();
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                setCurrentUserName(userName);
                return true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        JDBC.closeConnection();
        return false;
    }

    /**
     * Sets the current username.
     * @param userName
     */
    public static void setCurrentUserName(String userName) {
        currentUser = userName;
    }

    /**
     * Gets the current username.
     * @return
     */
    public static String getCurrentUserName() {
        return currentUser;
    }
    /**
     * Creates a list of all username and user Id's.
     */
    public static ObservableList<Users> getUserID() throws SQLException {
        ObservableList<Users> users = FXCollections.observableArrayList();
        String sqlStatement = "SELECT User_ID, User_Name FROM users;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int userId = resultSet.getInt("User_ID");
            String userName = resultSet.getString("User_Name");
            Users user = new Users (userId,userName);
            users.add(user);
        }
        return users;
    }

    /**
     * list of users where the user Id is selected.
     * @param userId
     * @return
     */
    public static Users getUserID(int userId) {
            try {
                String sqlStatement = "SELECT User_ID, User_Name FROM users WHERE User_ID = ?";
                PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int selectedUserId = resultSet.getInt("User_ID");
                    String userName1 = resultSet.getString("User_Name");
                    Users user = new Users(selectedUserId, userName1);
                    return user;
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return null;
        }
    }

