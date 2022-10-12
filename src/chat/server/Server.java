package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private Map<String, ConnectionHandler> connections;
    private ServerSocket serverSocket;
    private boolean done;
    private ExecutorService executorService;
    private Command command;
    private Controller controller;


    public Server() {
        connections = new HashMap<>();
        done = false;

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    private Server.ConnectionHandler getHandler(String name) {
        return connections.get(name);

    }

    @Override
    public void run() {
        try {
            System.out.println("Server started!");
            serverSocket = new ServerSocket(23456, 50, InetAddress.getByName("localhost"));
            executorService = Executors.newCachedThreadPool();

            while (!done) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                executorService.execute(connectionHandler);

            }

        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            done = true;
            executorService.shutdown();

            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }

            connections.forEach((k, v) -> v.shutdown());

        } catch (IOException ignored) {

        }
    }


    class ConnectionHandler implements Runnable {
        private ConnectionHandler receiver;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String clientName;

        public ConnectionHandler(Socket socket) throws IOException {
            this.socket = socket;
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {

                writer.println("Server: authorize or register");
                controller = new Controller();

                String message;
                boolean isAuth = false;
                while (!isAuth) {
                    message = reader.readLine();

                    switch (CommandType.commandType(message)) {
                        case REGISTRATION -> {
                            command = new RegCommand(this, message);
                            controller.setCommand(command);
                            isAuth = controller.executeCommand();
                            if (isAuth) {
                                connections.put(this.clientName, this);
                            }
                        }
                        case AUTH -> {
                            command = new AuthCommand(this, message);
                            controller.setCommand(command);
                            isAuth = controller.executeCommand();
                            if (isAuth) {
                                connections.put(this.clientName, this);
                            }
                        }
                        default -> sendMessage("Server: you are not in the chat!");
                    }
                }

                while (!Thread.currentThread().isInterrupted()) {
                    message = reader.readLine();
                    switch (CommandType.commandType(message)) {
                        case LIST -> {
                            command = new ListCommand(this, connections);
                            controller.setCommand(command);
                            controller.executeCommand();
                        }
                        case CHAT -> {
                            receiver = getHandler(message.split(" ")[1]);
                            command = new ChatCommand(this, receiver);
                            controller.setCommand(command);
                            controller.executeCommand();
                        }
                        case MESSAGE -> {
                            command = new MessageCommand(this, receiver, message);
                            controller.setCommand(command);
                            controller.executeCommand();
                        }
                        case EXIT -> {
                            command = new ExitCommand(this);
                            controller.setCommand(command);
                            controller.executeCommand();
                            connections.remove(this);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        public void sendMessage(String message) {
            writer.println(message);
        }

        public void shutdown() {
            try {
                writer.close();
                reader.close();
                if (!socket.isClosed()) {
                    socket.close();
                }

            } catch (IOException ignored) {

            }
        }

        public ConnectionHandler getReceiver() {
            return receiver;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }
    }


}