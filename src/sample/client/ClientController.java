package sample.client;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class ClientController {
    public TextField in;
    public Label out;
    public Button submit;
    public Button start;

    private Client client;

    public ClientController() {
        ClientPresenter.getInstance().setClientController(this);
    }

    public void startGame() {
        start.setDisable(true);

        new Thread(() -> {
            try {
                Platform.runLater(() -> out.setText("Connecting..."));
                client = new Client("127.0.0.1", 6066);
                try {
                    client.connect();
                    Platform.runLater(() -> out.setText("Just connected"));
                    client.listenServer();
                } catch (SocketTimeoutException s) {
                    Platform.runLater(() -> out.setText("Socket timed out!"));
                    start.setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Platform.runLater(() -> out.setText("No Server"));
                start.setDisable(false);
            }
        }).start();
    }

    public void submitResult() {
        if (in.getText().length() < 4) {
            out.setText("Write more " + (4 - in.getText().length()));
        } else {
            out.setText(in.getText());
            new Thread(() -> {
                try {
                    client.listenWord(in.getText());
                    in.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void check() {
        in.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                char ch = in.getText().charAt(oldValue.intValue());
                if (!(ch >= '0' && ch <= '9') || in.getText().length() > 4) {
                    in.setText(in.getText().substring(0, in.getText().length() - 1));
                }
            }
        });
    }


    public void setResult(String s) {
        out.setText(s);
    }
}