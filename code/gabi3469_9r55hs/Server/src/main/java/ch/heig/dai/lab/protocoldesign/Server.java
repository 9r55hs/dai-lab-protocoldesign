package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import static java.nio.charset.StandardCharsets.*;

public class Server {
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }

    private String getResult(double value){
        return "RES: " + value + "\n";
    }
    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    String line;
                    double val1,val2;
                    // Send a welcome message to the client with his ip
                    out.write("HELLO " + socket.getInetAddress() + "\n");
                    out.flush();
                    
                    while ((line = in.readLine()) != null) {
                        String[] Command = line.split(" ");
                        
                        // Quit the server
                        if (Command[0].equals("QUIT")) {
                            out.write("Have a nice day !\n");
                            out.flush();
                            socket.close();
                            return;
                        }

                        if (Command.length > 3 || Command.length < 3) {
                            out.write("UNKNOWN <" + line + ">\n");
                            out.write("Usage: <OP> <val1> <val2>\n");
                            out.flush();
                            continue;
                        }
                            
                        try {
                            val1 = Double.parseDouble(Command[1]);
                            val2 = Double.parseDouble(Command[2]);
                        } catch (NumberFormatException e) {
                            out.write("INVALID_VALUES " + Command[1] + " " + Command[2] + "\n");
                            out.flush();
                            continue;
                        }
                        switch (Command[0]) {
                            case "ADD":
                                out.write(getResult(val1 + val2) + "\n");
                                break;
                            case "SUB":
                                out.write(getResult(val1 - val2) + "\n");
                                break;
                            case "MUL":
                                out.write(getResult(val1 * val2) + "\n");
                                break;
                            case "DIV":
                                if (val2 == 0) {
                                    out.write("DIV_BY_ZERO " + "\n");
                                    break;
                                }
                                out.write(getResult(val1 / val2) + "\n");
                                break;
                            default:
                                out.write("OP_NOT_FOUND " + Command[0] + "\n");
                                break;
                        }
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

}