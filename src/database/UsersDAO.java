package database;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    static String currentUser;
    public static boolean validLogin(String userName, String password) throws SQLException {
        try {
            String sqlStatement = "SELECT * FROM users WHERE User_Name = ? AND Password = ?;";
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
}
