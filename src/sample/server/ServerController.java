package sample.server;

import javafx.application.Platform;
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

    public ServerController() {
        ServerPresenter.getInstance().setServerController(this);
    }


    @FXML
    public void startServer() {
        startServer.setDisable(true);
        server = new Server(6066);
        new Thread(() -> {
            try {
                Platform.runLater(() -> out.setText("Connecting..."));
                server.connect();
                Platform.runLater(() -> out.setText("Just connected."));
            } catch (SocketTimeoutException s) {
                startServer.setDisable(false);
                Platform.runLater(() -> out.setText("Socket timed out!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void startGame() {
        startGame.setDisable(true);
        out.setText("Game started!");
        new Thread(() -> {
            try {
                server.listenClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setResult(String s) {
        out.setText(s);
    }
}
