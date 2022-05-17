package helper;

import database.UsersDAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    /**
     * This handles writing all login attempts both successful and unsuccessful.
     * @param userName
     * @param password
     */
    public static void loginAttempts(String userName, String password) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("login_activity.txt"));
            boolean trackLogins = UsersDAO.validLogin(userName, password);
            String loginSuccess;
            if (trackLogins) {
                loginSuccess = "Username: " + userName + " had a successful login at: " + LocalDateTime.now() + "\n";
                writer.write(loginSuccess);
            } else {
                loginSuccess = "Username: " + userName + " attempted to login at: " + LocalDateTime.now() +
                        " but was unsuccessful.\n";
                writer.write(loginSuccess);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}