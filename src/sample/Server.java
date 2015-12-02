package sample;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//System.out.println("Your Host address: " + InetAddress.getLocalHost().getHostAddress()); //Print current IP address
public class Server {
    public ServerSocket serverSocket;
    private Socket server;
    public boolean status = true;
    private DataInputStream in;
    private DataOutputStream out;
    private String word;
    private String prediction;
    private ArrayList<Integer> figures = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    private ArrayList<String> letters = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private int count = 0;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(5000);
        } catch (SocketException e) {
            System.out.println("Could not set up timeout");
        } catch (IOException e) {
            System.out.println("Could not listen on port" + port);
        }
    }

    public void connect() throws IOException {
        server = serverSocket.accept();
        in = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
    }

    public void listenClient() throws IOException {
        System.out.println(in.readUTF());
        generate();


        new Thread(() -> {
            while (status) {
                try {
                    if (isReceive()) {
                        listenWord();
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

    public void generate() throws IOException {
        StringBuilder wordBuffer = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = new Random().nextInt(figures.size());
            int letter = figures.get(index);
            wordBuffer.append(letter);
            letters.add(String.valueOf(letter));
            figures.remove(index);
        }
        word = String.valueOf(wordBuffer);
        System.out.println("Word is: " + word);
    }


    public void listenWord() throws IOException {
        prediction = in.readUTF();
        values.clear();
        char[] convert = prediction.toCharArray();
        for (char aConvert : convert) {
            values.add(String.valueOf(aConvert));
        }
        System.out.println("Prediction is " + prediction);
        check();
    }

    public void check() throws IOException {
        int cows = 0;
        int bulls = 0;

        if (word.equals(prediction)) {
            out.writeUTF("true");

            count++;
            cows = 4;
            bulls = 4;
            System.out.println("Correct prediction" + " cows: " + cows + " bulls: " + bulls + " counts: " + count);

            out.writeUTF(String.valueOf(cows));
            out.writeUTF(String.valueOf(bulls));
            out.writeUTF(String.valueOf(count));


            this.disconnect();
        } else {
            out.writeUTF("false");

            count++;

            for (int i = 0; i < values.size(); i++) {
                if (values.contains(letters.get(i))) {
                    cows++;
                    if (letters.get(i).equals(values.get(i))) {
                        bulls++;
                    }
                }
            }
            out.writeUTF(String.valueOf(cows));
            out.writeUTF(String.valueOf(bulls));
            out.writeUTF(String.valueOf(count));

            System.out.println("Wrong prediction" + " cows: " + cows + " bulls: " + bulls + " counts: " + count);
        }
    }

    public void disconnect() throws IOException {
        server.close();
        status = false;
        System.out.println("Disc");
    }
}
