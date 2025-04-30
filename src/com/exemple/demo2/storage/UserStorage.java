package com.exemple.demo2.storage;

import com.exemple.demo2.model.Book;
import com.exemple.demo2.model.Library;
import com.exemple.demo2.model.Reader;
import com.exemple.demo2.model.User;
import com.exemple.demo2.pattern.BorrowedState;
import com.exemple.demo2.pattern.UserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    private static final String FILE_NAME = "users.txt";

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    User user = UserFactory.createUser(parts[0], parts[1], parts[2]);

                    if (user instanceof Reader && parts.length == 4) {
                        String[] borrowedTitles = parts[3].split(",");
                        for (String title : borrowedTitles) {
                            title = title.trim();
                            if (!title.isEmpty()) {
                                Book book = findBookByTitle(title);
                                if (book != null) {
                                    ((Reader) user).getBorrowedBooks().add(book);
                                    book.setState(new BorrowedState());
                                }
                            }
                        }
                    }
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getRole()).append("|")
                  .append(user.getUsername()).append("|")
                  .append(user.getPassword());

                if (user instanceof Reader) {
                    sb.append("|");
                    List<Book> borrowed = ((Reader) user).getBorrowedBooks();
                    for (Book b : borrowed) {
                        sb.append(b.getTitle()).append(",");
                    }
                }
                pw.println(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private static Book findBookByTitle(String title) {
        for (Book book : Library.getInstance().getBooks()) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
}
