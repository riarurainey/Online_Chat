package chat.server;

import java.util.Map;

public class ListCommand implements Command {
    private Server.ConnectionHandler handler;
    private Map<String, Server.ConnectionHandler> connections;

    public ListCommand(Server.ConnectionHandler handler, Map<String, Server.ConnectionHandler> connections) {
        this.handler = handler;
        this.connections = connections;
    }


    @Override
    public boolean execute() {


        if (connections.size() == 1) {
            handler.sendMessage("Server: no one online");
            return false;

        } else {

            for (String name : connections.keySet()) {
                if (!name.equals(handler.getClientName())) {
                    handler.sendMessage("Server: online: " + name);
                }
            }
            return true;
        }

    }
}