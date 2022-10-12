package chat.server;

public class ExitCommand implements Command {
    private Server.ConnectionHandler handler;

    public ExitCommand(Server.ConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean execute() {
        handler.shutdown();
        return true;
    }
}
