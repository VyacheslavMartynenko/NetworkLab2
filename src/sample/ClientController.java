package sample;

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

    public void startGame() {
        out.setText("Game started");
        start.setDisable(true);
        client = new Client("127.0.0.1", 6066);
        if (client.clientSocket == null) {
            System.out.println("Cant connect");
        } else {
            new Thread(() -> {
                try {
                    System.out.println("Connecting to ");
                    client.connect();
                    System.out.println("Just connected to ");
                    client.listenServer();
                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                    client.status = false;
                } catch (IOException e) {
                    //System.out.println("Connection failed");
                    e.printStackTrace();
                    client.status = false;
                }
            }).start();
        }
    }

    public void submitResult() {
        if (in.getText().length() < 4) {
            out.setText("Write more " + (4 - in.getText().length()));
        } else {
            out.setText(in.getText());
            new Thread(() -> {
                try {
                    client.word = in.getText();
                    in.clear();
                    client.listenWord();
//                    in.setDisable(true);
//                    submit.setDisable(true);
                    //System.out.println("Disconnected from");
                } catch (IOException e) {
                    //System.out.println("Connection failed");
                    e.printStackTrace();
                    client.status = false;
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


}
