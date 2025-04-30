package com.exemple.demo2.model;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String username;
    protected String password;
    protected List<Book> borrowedBooks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public abstract String getRole();
}
