package sample.client;

import java.net.*;
import java.io.*;

public class Client {
    public Socket clientSocket;
    public boolean status = true;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String serverName, int port) throws IOException {
        clientSocket = new Socket(serverName, port);
        clientSocket.setSoTimeout(3000);
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
        out.writeUTF(word);
    }

    public void check() throws IOException {
        String check = in.readUTF();
        String cows = in.readUTF();
        String bulls = in.readUTF();
        String count = in.readUTF();
        if (check.equals("true")) {
            ClientPresenter.getInstance().handleResult(" cows: " + cows + " bulls: " + bulls + " counts: " + count);
            this.disconnect();
        } else {
            ClientPresenter.getInstance().handleResult(" cows: " + cows + " bulls: " + bulls + " counts: " + count);
        }
    }

    public void disconnect() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
        status = false;
        ClientPresenter.getInstance().handleResult("Disconnected!");
    }

}