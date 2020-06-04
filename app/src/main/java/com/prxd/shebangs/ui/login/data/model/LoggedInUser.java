package com.prxd.shebangs.ui.login.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String userKey;

    public LoggedInUser(String userId, String userKey) {
        this.userId = userId;
        this.userKey = userKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserKey() {
        return userKey;
    }
}
