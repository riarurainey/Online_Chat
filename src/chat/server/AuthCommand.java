package chat.server;

import chat.model.User;
import chat.service.UserService;

import java.util.Map;

public class AuthCommand implements Command {
    private Server.ConnectionHandler handler;
    private User user;
    private Map<String, User> users;
    private UserService userService;

    public AuthCommand(Server.ConnectionHandler handler, String message) {
        userService = new UserService();
        user = userService.parseUserFromString(message);
        users = userService.reloadFromFile();
        this.handler = handler;

    }

    @Override
    public boolean execute() {
        if (!users.containsKey(user.getLogin())) {
            handler.sendMessage("Server: incorrect login!");
            return false;
        } else {
            User user1 = users.get(user.getLogin());

            if (Integer.parseInt(user1.getPassword()) != user.getPasswordHashCode()) {
                handler.sendMessage("Server: incorrect password!");
                return false;
            } else {
                handler.sendMessage("Server: you are authorized successfully!");
                handler.setClientName(user1.getLogin());
                return true;
            }
        }

    }
}

