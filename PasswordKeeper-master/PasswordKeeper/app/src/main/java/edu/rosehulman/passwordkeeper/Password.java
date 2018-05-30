package edu.rosehulman.passwordkeeper;


import com.google.firebase.database.Exclude;

public class Password {

    private String key;
    private String service;
    private String username;
    private String password;

    public Password() {}

    public Password(String username, String password, String service) {
        this(null, username, password, service);
    }

    public Password(String key, String username, String password, String service) {
        this.key = key;
        this.service = service;
        this.username = username;
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
