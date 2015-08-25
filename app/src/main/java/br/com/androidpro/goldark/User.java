package br.com.androidpro.goldark;

/**
 * @author Thiago Pagonha
 * @version 25/08/15.
 */
public class User {
    private String username;
    private String password;

    public User(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
