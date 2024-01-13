package models;

public class UserCred {
    private String email;
    private String password;

    public UserCred(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCred fromUser(User user){
        return new UserCred(user.getEmail(), user.getPassword());
    }
}
