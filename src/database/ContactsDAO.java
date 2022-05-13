package database;

import Models.Contacts;
import Models.Users;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.rowset.serial.SerialException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactsDAO {
    public static ObservableList<Contacts> getContactID() throws SQLException {
        ObservableList<Contacts> contacts = FXCollections.observableArrayList();
        String sqlStatement = "SELECT Contact_ID, Contact_Name FROM contacts;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int contactId = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");
            Contacts contact = new Contacts (contactId,contactName);
            contacts.add(contact);
        }
        return contacts;
    }
}
