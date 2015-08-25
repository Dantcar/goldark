package br.com.androidpro.goldark.rest;

/**
 * @author Thiago Pagonha
 * @version 25/08/15.
 */
public class Session {
    private String id;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
