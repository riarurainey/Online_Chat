package chat.server;

import chat.service.MessageLogger;

import java.util.List;

public class ChatCommand implements Command {
    private Server.ConnectionHandler sender;
    private Server.ConnectionHandler receiver;

    public ChatCommand(Server.ConnectionHandler sender, Server.ConnectionHandler receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public boolean execute() {
        if (receiver == null) {
            sender.sendMessage("Server: the user is not online!");
            return false;
        } else {
            sendLastMessage();
            return true;
        }

    }

    private void sendLastMessage() {

        MessageLogger.loggerFor(sender.getClientName(), receiver.getClientName());

        List<String> messages = MessageLogger.getChatLog(sender.getClientName());

        if (messages.size() > 0) {
            if (messages.size() >= 10) {
                messages = messages.subList(messages.size() - 10, messages.size());
            }

            for (String message : messages) {
                sender.sendMessage(message);
            }

        }
    }
}