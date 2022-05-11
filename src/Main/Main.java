package Main;
/**
 * Main method
 */

/**
 * @author Tricia Aloufi
 */
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/** initializes the main method and accesses the login screen GUI.
 *
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/loginScreen.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

    }

    public static void main(String[] args) {
        launch(args);
        JDBC.openConnection();
        JDBC.closeConnection();
    }
}

