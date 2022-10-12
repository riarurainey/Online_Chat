package chat.server;

import java.util.Locale;

public enum CommandType {
    MESSAGE,
    REGISTRATION,
    AUTH,
    LIST,
    CHAT,
    EXIT,
    INVALID;


    public static CommandType commandType(String message) {
        message = message.toLowerCase(Locale.ROOT);
        return !message.startsWith("/") ? MESSAGE :
                message.startsWith("/registration") ? REGISTRATION :
                        message.startsWith("/auth") ? AUTH :
                                message.startsWith("/list") ? LIST :
                                        message.startsWith("/chat") ? CHAT :
                                                message.startsWith("/exit") ? EXIT : INVALID;
    }

}
