package chat.service;

import chat.model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Path USERS;

    static {
        USERS = Path.of("Users.txt");
        try {
            if (Files.notExists(USERS)) {
                Files.createFile(USERS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveUser(User user) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(String.valueOf(USERS), true), true);
            writer.println(user.getLogin() + " " + user.getPasswordHashCode());

        } catch (IOException e) {
            System.out.println("Error when trying to write in file");
        }
    }

    public Map<String, User> reloadFromFile() {
        Map<String, User> users = new HashMap<>();
        try {
            File file = new File(String.valueOf(USERS));

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(Path.of(String.valueOf(USERS)).toFile()));
                String str;
                while ((str = reader.readLine()) != null) {
                    String[] arr = str.split(" ");
                    User user = new User(arr[0], arr[1]);
                    users.put(user.getLogin(), user);

                }
            }

        } catch (IOException e) {
            System.out.println("Error when trying to read from a file");
        }
        return users;
    }

    public User parseUserFromString(String message) {
        String[] str = message.split(" ");
        return new User(str[1], str[2]);

    }
}
