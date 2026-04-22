import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerTCP {
    public static void main(String[] args) {
        String ip = "100.79.133.37";
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip))) {

            System.out.println("Server started on " + ip + ":" + port);
            System.out.println("Waiting for connection...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));


            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        System.out.println("Client: " + msg);
                    }
                } catch (Exception e) {
                    System.out.println("Client disconnected.");
                }
            }).start();


            String input;
            while ((input = keyboard.readLine()) != null) {
                writer.write(input);
                writer.newLine();
                writer.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}