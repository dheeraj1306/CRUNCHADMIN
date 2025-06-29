package CrunchAdmin;

import java.io.*;
import java.net.*;
import java.util.*;

public class CrunchServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Crunch Admin Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String keywords = in.readLine();
            String minLengthStr = in.readLine();
            String maxLengthStr = in.readLine();

            int minLength = Integer.parseInt(minLengthStr);
            int maxLength = Integer.parseInt(maxLengthStr);

            System.out.println("Received: " + keywords + ", min: " + minLength + ", max: " + maxLength);

            List<String> dictionary = generateDictionary(keywords.split(","), minLength, maxLength);

            for (String word : dictionary) {
                out.println(word);
            }
            out.println("END");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> generateDictionary(String[] keywords, int minLength, int maxLength) {
        List<String> results = new ArrayList<>();

        for (String keyword : keywords) {
            keyword = keyword.trim();
            for (int i = minLength; i <= maxLength; i++) {
                StringBuilder sb = new StringBuilder();
                while (sb.length() < i) {
                    sb.append(keyword);
                }
                results.add(sb.substring(0, i));
            }
        }
        return results;
    }

}
