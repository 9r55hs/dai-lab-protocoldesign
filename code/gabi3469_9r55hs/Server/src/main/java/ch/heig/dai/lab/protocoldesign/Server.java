package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import static java.nio.charset.StandardCharsets.*;
import java.net.*;

public class Server {
    final int SERVER_PORT = 1234;
    final static String OPERATION_REGEX = "[+\\-*]";
    static int a;
    static int b;
    static char c;

    public static void main(String[] args) {
        // Create a new server and run it
        //Server server = new Server();
        //server.run();
        run();
    }

    private static void run() {
        String command = "";
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                try (   Socket socket = serverSocket.accept();
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {
                    
                    String line;
                    while ((line = in.readLine()) != null) {
                        command = line;
                        System.out.println("Received command:" + command);
                        parseCommand(command);
                        
                        
                        out.write(executeOperation() + "\n");
                        out.flush();
                    }
                    
                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }

    private static int parseCommand(String cmd){
        String[] splittedCMD = cmd.split(" ");
        if(splittedCMD.length != 3){
            return 1;
        }

        try {
            a = Integer.parseInt(splittedCMD[0]);
            b = Integer.parseInt(splittedCMD[2]);
        } catch (NumberFormatException e){
            return 2;
        }

        if (splittedCMD[1].length() > 1 || !splittedCMD[1].matches(OPERATION_REGEX)) {
            return 3;
        }

        c = splittedCMD[1].charAt(0);
        
        return 0;
    }

    private static int executeOperation(){
        switch (c) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            default:
                return 0;
        }
    }
}