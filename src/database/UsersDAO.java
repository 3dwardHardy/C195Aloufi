package database;

import Models.Customers;
import Models.Users;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    static String currentUser;
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

    public static void setCurrentUserName(String userName) {
        currentUser = userName;
    }

    public static String getCurrentUserName() {
        return currentUser;
    }

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

