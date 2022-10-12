package chat.server;

import chat.service.MessageLogger;

public class MessageCommand implements Command {
    private String message;
    private Server.ConnectionHandler sender;
    private Server.ConnectionHandler receiver;

    public MessageCommand(Server.ConnectionHandler sender, Server.ConnectionHandler receiver, String message) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public boolean execute() {
        if (receiver == null) {
            sender.sendMessage("Server: use /list command to choose a user to text!");
            return false;

        } else if (!message.isBlank()) {
            MessageLogger.loggerFor(sender.getClientName(), receiver.getClientName());

            message = sender.getClientName() + ": " + message;
            sender.sendMessage(message);

            if (receiver.getReceiver() == sender) {
                receiver.sendMessage(message);


            } else {
                MessageLogger.logNewMessage(message, receiver.getClientName());
            }
            MessageLogger.logAll(message);
        }
        return true;
    }
}
