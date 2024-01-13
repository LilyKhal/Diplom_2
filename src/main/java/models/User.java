package models;

public class User {
    private final String email;
    private final String password;
    private final String name;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
