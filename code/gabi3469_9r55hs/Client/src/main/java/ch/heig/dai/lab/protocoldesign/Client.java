package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    final String SERVER_ADDRESS = "127.0.0.1";
    final int SERVER_PORT = 42012;

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (   Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                var scanner = new Scanner(System.in)) {
            
            String serverResponse;
            for (int i = 0; i < 3; i++) {
                if (in.ready()) {
                    serverResponse = in.readLine();
                    if (serverResponse != null && !serverResponse.isEmpty()) {
                        System.out.println(serverResponse);
                    }
                }
            }

            while (true) {
                System.out.println("Enter command to send: ");

                String userInput = scanner.nextLine();
                out.write(userInput + "\n");
                out.flush();
                
                serverResponse = in.readLine();
                if (serverResponse != null) {
                    System.out.println(serverResponse);
                } else {
                    System.out.println("Server has closed the connection.");
                    break;
                }

                if(userInput.equals("QUIT")) break;
            }
        } catch (IOException e) {
            System.err.println("Client: exc.: " + e.getMessage());
        }
    }
}
