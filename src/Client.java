import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        int number=7;
        System.out.println(isPrime(number));
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter an integer number:");
            int num = Integer.parseInt(userInput.readLine());

            // Establish connection with the Dictionary process
            try (Socket dictSocket = new Socket("localhost", 5000);
                 BufferedReader dictIn = new BufferedReader(new InputStreamReader(dictSocket.getInputStream()));
                 PrintWriter dictOut = new PrintWriter(dictSocket.getOutputStream(), true)) {

                dictOut.println("REQUEST_SERVER");
                String serverInfo = dictIn.readLine();
                if (serverInfo.equals("NO_SERVERS_AVAILABLE")) {
                    System.out.println("No servers available. Please try again later.");
                    return;
                }

                String[] serverDetails = serverInfo.split(":");
                String serverIP = serverDetails[0];
                int serverPort = Integer.parseInt(serverDetails[1]);

                // Print the server IP and port
                System.out.println("Connecting to server at " + serverIP + ":" + serverPort);

                // Establish connection with the Server process
                try (Socket serverSocket = new Socket(serverIP, serverPort);
                     BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                     PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true)) {

                    // Send RPC request to check if the number is prime
                    serverOut.println("isPrime:" + num);
                    String isPrimeResponse = serverIn.readLine();
                    System.out.println("Is " + num + " a prime number? " + isPrimeResponse.split(":")[1]);

                    // Send RPC request to get the next prime number
                    serverOut.println("nextPrime:" + num);
                    String nextPrimeResponse = serverIn.readLine();
                    System.out.println("The next prime number after " + num + " is " + nextPrimeResponse.split(":")[1]);

                } catch (IOException e) {
                    System.err.println("Error communicating with server: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.err.println("Error communicating with dictionary: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPrime(int number) {
        for(int i=2;i<=number;i++){
            if((number%i)==0&&number!=i){
                return false;
            }
        }
        return true;
    }
}
