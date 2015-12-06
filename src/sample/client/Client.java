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

    @SuppressWarnings("Duplicates")
    public void listenServer() throws IOException {
        out.writeUTF("Client ready to game from " + clientSocket.getLocalSocketAddress());

        new Thread(() -> {
            TimeoutThread timeoutThread = new TimeoutThread(10000);
            while (status) {
                try {
                    if (isReceive()) {
                        timeoutThread.stopCheckTimeout(); //stop current timeout
                        boolean gameFinished = check();
                        if (gameFinished) {
                            return;
                        }

                        timeoutThread = new TimeoutThread(5000); // start new
                        timeoutThread.start();
                    }
                } catch (Exception e) {
                    status = false;
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

    public boolean check() throws IOException {
        String check = in.readUTF();
        String cows = in.readUTF();
        String bulls = in.readUTF();
        String count = in.readUTF();
        if (check.equals("true")) {
            ClientPresenter.getInstance().handleResult(" cows: " + cows + " bulls: " + bulls + " counts: " + count);
            this.disconnect();
            return true;
        } else {
            ClientPresenter.getInstance().handleResult(" cows: " + cows + " bulls: " + bulls + " counts: " + count);
            return false;
        }
    }

    public void disconnect() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
        status = false;
        ClientPresenter.getInstance().handleResult("Disconnected!");
    }

    class TimeoutThread extends Thread {
        private final int time;
        private boolean isStop = false;

        public TimeoutThread(int time) {
            super();
            this.time = time;
        }

        public void stopCheckTimeout() {
            this.isStop = true;
        }

        @SuppressWarnings("Duplicates")
        @Override
        public void run() {
            try {
                long endTime = System.currentTimeMillis() + time;
                int count = time / 1000;
                while (!isStop) {
                    count--;
                    Thread.sleep(1000);
                    ClientPresenter.getInstance().handleResult(String.valueOf(count));
                    if (System.currentTimeMillis() > endTime) {
                        disconnect();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

