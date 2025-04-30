package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Admin;
import com.exemple.demo2.model.Reader;
import com.exemple.demo2.model.User;

public class UserFactory {
    public static User createUser(String type, String username, String password) {
        if (type.equalsIgnoreCase("Admin")) {
            return new Admin(username, password);
        } else if (type.equalsIgnoreCase("Reader")) {
            return new Reader(username, password);
        } else {
            throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
