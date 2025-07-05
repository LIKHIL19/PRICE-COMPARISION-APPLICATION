package utils;

import model.User;

public class SessionManager {
    private static SessionManager instance;
    private int userId;
    private String username;
    private boolean isLoggedIn;
    private User currentUser;

    private SessionManager() {
        isLoggedIn = false;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(int userId, String username) {
        this.userId = userId;
        this.username = username;
        this.isLoggedIn = true;
        this.currentUser = new User(userId, username);
    }

    public void logout() {
        this.userId = 0;
        this.username = null;
        this.isLoggedIn = false;
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
