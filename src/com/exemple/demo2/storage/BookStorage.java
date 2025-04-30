package com.exemple.demo2.storage;

import com.exemple.demo2.model.Book;
import com.exemple.demo2.pattern.AvailableState;
import com.exemple.demo2.pattern.BorrowedState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {

    private static final String FILE_NAME = "books.txt";

    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Book book = new Book(parts[0], parts[1], parts[2], parts[3]);
                    if (parts[4].equalsIgnoreCase("Available")) {
                        book.setState(new AvailableState());
                    } else {
                        book.setState(new BorrowedState());
                    }
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    public static void saveBooks(List<Book> books) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                String state = (book.getState() instanceof AvailableState) ? "Available" : "Borrowed";
                pw.println(book.getTitle() + "|" + book.getAuthor() + "|" + book.getCategory() + "|" + book.getPublishDate() + "|" + state);
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }
}
