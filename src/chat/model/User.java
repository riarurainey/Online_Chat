package chat.model;

public class User {
    private String login;
    private String password;

    public User(String login, String pass) {
        this.login = login;
        this.password = pass;
    }

    public int getPasswordHashCode() {
        return password.hashCode();

    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

}
