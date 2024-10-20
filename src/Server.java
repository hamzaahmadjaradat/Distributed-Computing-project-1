import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (Socket dictSocket = new Socket("localhost", 5000);
             PrintWriter dictOut = new PrintWriter(dictSocket.getOutputStream(), true);
             ServerSocket serverSocket = new ServerSocket(0)) {

            int serverPort = serverSocket.getLocalPort();
            dictOut.println("REGISTER:" + serverPort);
            System.out.println("Server started on port " + serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = clientIn.readLine()) != null) {
                System.out.println("Received request: " + request);
                String[] requestParts = request.split(":");
                String function = requestParts[0];
                int num = Integer.parseInt(requestParts[1]);

                if (function.equals("isPrime")) {
                    boolean result = PrimeUtils.isPrime(num);
                    clientOut.println("isPrime:" + result);
                    System.out.println("Processed isPrime request for " + num + ": " + result);
                } else if (function.equals("nextPrime")) {
                    int result = PrimeUtils.nextPrime(num);
                    clientOut.println("nextPrime:" + result);
                    System.out.println("Processed nextPrime request for " + num + ": " + result);
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Closed connection with client");
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
