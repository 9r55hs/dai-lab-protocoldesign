package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    final String SERVER_ADDRESS = "127.0.0.1";
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // Create a new client and run it
        //Client client = new Client();
        run();
    }

    private static void run() {
        try (   Socket socket = new Socket("127.0.0.1", 1234);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                var scanner = new Scanner(System.in)) {
                
            System.out.println("Sucessfully connected to MOP server!");
            while (true) {
                System.out.println("Enter command to send: ");

                String userInput = scanner.nextLine();
                out.write(userInput + "\n");
                out.flush();
                System.out.println("Sent: " + userInput);

                String serverResponse = in.readLine();
                if (serverResponse != null) {
                    System.out.println("Received: " + serverResponse);
                }
            }
        } catch (IOException e) {
            System.out.println("Client: exc.: " + e);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException thException) {
                thException.printStackTrace();
            }
        }
    }
}