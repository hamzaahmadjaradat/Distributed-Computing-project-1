import java.io.*;
import java.net.*;
import java.util.*;

public class Dictionary {
    private static List<String> servers = new ArrayList<>();
    private static int currentIndex = 0;

    public static void main(String[] args) {
        try (ServerSocket dictSocket = new ServerSocket(5000)) {
            System.out.println("Dictionary started on port 5000");

            while (true) {
                try (Socket socket = dictSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    String message = in.readLine();

                    if (message.startsWith("REGISTER")) {
                        String serverInfo = socket.getInetAddress().getHostAddress() + ":" + message.split(":")[1];
                        servers.add(serverInfo);
                        System.out.println("Registered server: " + serverInfo);
                    } else if (message.equals("REQUEST_SERVER")) {
                        if (servers.isEmpty()) {
                            out.println("NO_SERVERS_AVAILABLE");
                        } else {
                            String serverInfo = servers.get(currentIndex);
                            currentIndex = (currentIndex + 1) % servers.size();
                            out.println(serverInfo);
                            System.out.println("Sent server info: " + serverInfo);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
