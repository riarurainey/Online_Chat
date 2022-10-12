package chat.server;

import chat.model.User;
import chat.service.UserService;

import java.util.Map;

public class RegCommand implements Command {
    private Server.ConnectionHandler handler;
    private User user;
    private Map<String, User> users;
    private UserService userService;

    public RegCommand(Server.ConnectionHandler handler, String message) {
        userService = new UserService();
        user = userService.parseUserFromString(message);
        users = userService.reloadFromFile();
        this.handler = handler;
    }

    @Override
    public boolean execute() {
        if (users.containsKey(user.getLogin())) {
            handler.sendMessage("Server: this login is already taken! Choose another one.");
            return false;

        } else if (String.valueOf(user.getPassword()).length() < 8) {
            handler.sendMessage("Server: the password is too short!");
            return false;
        } else {
            users.put(user.getLogin(), user);
            userService.saveUser(user);
            handler.setClientName(user.getLogin());
            handler.sendMessage("Server: you are registered successfully!");
            return true;
        }

    }
}