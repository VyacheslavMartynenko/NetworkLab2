package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerFrame extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));
        primaryStage.setTitle("Server");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}