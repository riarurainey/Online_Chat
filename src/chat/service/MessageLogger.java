package chat.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MessageLogger {
    private static String fileName;
    private static Path folder;
    private static Path chatLog;
    private static Path receiverLog;

    public static void loggerFor(String sender, String receiver) {
        fileName = sender.compareTo(receiver) < 0 ? sender + receiver : receiver + sender;

        folder = Path.of("logs/" + fileName);
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatLog = folder.resolve(fileName);

    }

    public static void logAll(String message) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(String.valueOf(chatLog), true), true)) {
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static synchronized void logNewMessage(String message, String receiverName) {
        receiverLog = folder.resolve(receiverName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(String.valueOf(receiverLog), true), true)) {
            writer.println("(new) " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized List<String> getChatLog(String receiverName) {
        receiverLog = folder.resolve(receiverName);
        File file = new File(String.valueOf(receiverLog));
        List<String> lines = new ArrayList<>();
        try (var sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }


        } catch (FileNotFoundException ignored) {
        }

        try {
            if (Files.exists(receiverLog)) {
                Files.newBufferedWriter(receiverLog, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines.size() == 0) {

            file = new File(String.valueOf(chatLog));
            lines = new ArrayList<>();
            try (var sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    lines.add(sc.nextLine());
                }
            } catch (FileNotFoundException ignored) {
            }
        }

        return lines;
    }
}


