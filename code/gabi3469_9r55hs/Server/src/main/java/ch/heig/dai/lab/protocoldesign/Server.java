package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import static java.nio.charset.StandardCharsets.*;

public class Server {
    final int SERVER_PORT = 42012;

    final String ERR_UNKNOWN = "UNKNOWN < %s >";
    final String ERR_DIV_BY_ZERO = "DIV_BY_ZERO";
    final String ERR_OP_NOT_FOUND = "OP_NOT_FOUND %s";
    final String ERR_INVALID_VALUES = "INVALID_VALUES %s %s";

    final String OPERATIONS = "Operations: ADD, SUB, MUL, DIV";
    final String WELCOME_MESSAGE =  "HELLO %s \n" +
                                    OPERATIONS + "\n" +
                                    "QUIT to exit";
    final String RESULT_MESSAGE = "RES: %,.1f";
    final String EXIT_MESSAGE = "Goodbye";

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }

    private void run() {
        String ans, command;
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    //Send a welcome message to the client with his ip and the available operations
                    out.write(String.format(WELCOME_MESSAGE + "\n", socket.getInetAddress()));
                    out.flush();
                    
                    while ((command = in.readLine()) != null) {
                        ans = getAnswer(command);

                        out.write(ans + "\n");
                        out.flush();

                        if(command.trim().equals("QUIT")) break;
                    }
                } catch (IOException e) {
                    System.err.println("Server: socket ex.: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server: server socket ex.: " + e.getMessage());
        }
    }

    private String getAnswer(String command){
        String[] splittedCMD = command.split(" ");

        if(splittedCMD.length != 3 && !splittedCMD[0].equals("QUIT"))
            return String.format(ERR_UNKNOWN, command);

        if (splittedCMD[0].equals("QUIT"))
            return EXIT_MESSAGE;

        try {
            double val1 = Double.parseDouble(splittedCMD[1]);
            double val2 = Double.parseDouble(splittedCMD[2]);
            
            switch (splittedCMD[0]) {
                case "ADD":
                    return String.format(RESULT_MESSAGE, val1 + val2);
                case "SUB":
                    return String.format(RESULT_MESSAGE, val1 - val2);
                case "MUL":
                    return String.format(RESULT_MESSAGE, val1 * val2);
                case "DIV":
                    return (val2 == 0) ? ERR_DIV_BY_ZERO : String.format(RESULT_MESSAGE, val1 / val2);
                default:
                    return String.format(ERR_OP_NOT_FOUND, splittedCMD[0]);
            }
        } catch (NumberFormatException e){
            return String.format(ERR_INVALID_VALUES, splittedCMD[1], splittedCMD[2]);
        }
    }
}
