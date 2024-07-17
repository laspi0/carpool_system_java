package com.groupeisi.rent.Config;

import com.groupeisi.rent.entities.User;

public class SessionManager {
    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        loggedInUser = null;
    }
}