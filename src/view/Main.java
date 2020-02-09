package view;

import control.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.System.exit;

public class Main extends Application{

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * start</br></br>
     * public void start(final Stage primaryStage)</br></br>
     * Create the workaround for the GUI.
     */
    @Override
    public void start(final Stage primaryStage){

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/gui.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Controller controller = new Controller();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);

        primaryStage.setTitle("Storage Scanner");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest( event ->
        {
            exit(0);
        });
    }
}
