package chat.client;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    @Override
    public void run() {
        try {
            System.out.println("Client started!");
            socket = new Socket("localhost", 23456);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputHandler inputHandler = new InputHandler();
            Thread thread = new Thread(inputHandler);
            thread.start();

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String message = "";
            do {
                try {
                    message = userInput.readLine();
                    writer.println(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!Thread.currentThread().isInterrupted() && !message.equals("/exit"));

            thread.interrupt();
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            reader.close();
            writer.close();
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {

        }
    }

    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                String inMessage;
                while ((inMessage = reader.readLine()) != null) {
                    System.out.println(inMessage);

                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }
}