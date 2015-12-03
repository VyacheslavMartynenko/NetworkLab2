package sample.client;

import java.net.*;
import java.io.*;

public class Client {
    public Socket clientSocket;
    public boolean status = true;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String serverName, int port) {
        try {
            clientSocket = new Socket(serverName, port);
            clientSocket.setSoTimeout(3000);
        } catch (SocketException e) {
            System.out.println("Could not set up timeout");
        } catch (IOException e) {
            System.out.println("Could not listen on port" + port);
        }
    }

    public void connect() throws IOException {
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public void listenServer() throws IOException {
        out.writeUTF("Client ready to game from " + clientSocket.getLocalSocketAddress());

        new Thread(() -> {
            while (status) {
                try {
                    if (isReceive()) {
                        check();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isReceive() throws IOException {
        return in.available() != 0;
    }

    public void listenWord(String word) throws IOException {
        System.out.println("Prediction is: " + word);
        out.writeUTF(word);
    }

    public void check() throws IOException {
        String check = in.readUTF();
        String cows = in.readUTF();
        String bulls = in.readUTF();
        String count = in.readUTF();
        if (check.equals("true")) {
            ClientPresenter.getInstance().handleResult("blaaaaaaaa1");
            System.out.println("Server says " + check + " cows: " + cows + " bulls: " + bulls + " counts: " + count);
            this.disconnect();
        } else {
            ClientPresenter.getInstance().handleResult("asdasaaaaaaaa1");
            System.out.println("Server says " + check + " cows: " + cows + " bulls: " + bulls + " counts: " + count);
        }
    }

    public void disconnect() throws IOException {
        clientSocket.close();
        status = false;
        System.out.println("Disc");
    }

}