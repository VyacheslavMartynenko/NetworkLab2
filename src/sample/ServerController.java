package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class ServerController {

    public Label out;
    public Button startGame;
    public Button startServer;

    private Server server;

    @FXML
    public void startServer() {
        startServer.setDisable(true);
        out.setText("Game started");

        server = new Server(6066);
        new Thread(() -> {
            try {
                System.out.println("Connecting to ");
                server.connect();
                System.out.println("Just connected to ");
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                server.status = false;
            } catch (IOException e) {
                //System.out.println("Connection failed");
                e.printStackTrace();
                server.status = false;
            }
        }).start();
    }

    @FXML
    public void startGame() {
        startGame.setDisable(true);
        new Thread(() -> {
            try {
                server.listenClient();
//                System.out.println("Disconnected from server");
            } catch (IOException e) {
                //System.out.println("Connection failed");
                e.printStackTrace();
                server.status = false;
            }
        }).start();
    }
}
