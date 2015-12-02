package sample;

import java.net.*;
import java.io.*;

//System.out.println("Your Host address: " + InetAddress.getLocalHost().getHostAddress()); //Print current IP address
public class Client {
    public Socket clientSocket;
    public boolean status = true;
    private DataOutputStream out;
    private DataInputStream in;
//    private BufferedReader reader;
    public String word;

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

    public void listenWord() throws IOException {
        //String prediction = reader.readLine();
        //while (word.length() != 4) {}
        String prediction = word;
        System.out.println("Prediction is: " + prediction);
        out.writeUTF(prediction);
        word="";
        //check();
    }

    public void check() throws IOException {
        String check = in.readUTF();
        String cows = in.readUTF();
        String bulls = in.readUTF();
        String count = in.readUTF();
        if (check.equals("true")) {
            System.out.println("Server says " + check + " cows: " + cows + " bulls: " + bulls + " counts: " + count);
            this.disconnect();
        } else {
            System.out.println("Server says " + check + " cows: " + cows + " bulls: " + bulls + " counts: " + count);
        }
    }

    public void disconnect() throws IOException {
  //      reader.close();
        clientSocket.close();
        status = false;
        System.out.println("Disc");
    }

    /*public void run() {
        while (status) {
            try {
                System.out.println("Connecting to " + clientSocket.getInetAddress() + " on port " + clientSocket.getLocalPort());
                connect();
                System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
                listenServer();
                listenWord();
                System.out.println("Disconnected from" + clientSocket.getRemoteSocketAddress());
                disconnect();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                status = false;
            } catch (IOException e) {
                System.out.println("Connection failed");
                status = false;
            }
        }
    }*/

    public static void main(String[] args) {
        //new Client("127.0.0.1", 6066).start();
    }
}