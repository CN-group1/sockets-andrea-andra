import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ServerUDP {
    public static void main(String[] args) {
        String ip = "100.79.133.37";
        int port = 5000;

        try {
            DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName(ip));
            System.out.println("UDP server started on " + ip + ":" + port);
            System.out.println("Waiting for messages...");

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            AtomicReference<InetAddress> clientAddress = new AtomicReference<>(null);
            AtomicInteger clientPort = new AtomicInteger(-1);

            new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        clientAddress.set(packet.getAddress());
                        clientPort.set(packet.getPort());

                        String message = new String(
                                packet.getData(),
                                0,
                                packet.getLength(),
                                StandardCharsets.UTF_8
                        );

                        System.out.println("Client: " + message);
                    }
                } catch (Exception e) {
                    System.out.println("Receive error: " + e.getMessage());
                }
            }).start();

            while (true) {
                String input = keyboard.readLine();

                if (clientAddress.get() == null || clientPort.get() == -1) {
                    System.out.println("Wait until the client sends the first message.");
                    continue;
                }

                byte[] data = input.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        clientAddress.get(),
                        clientPort.get()
                );

                socket.send(packet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}