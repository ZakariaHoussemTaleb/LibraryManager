package com.exemple.demo2.model;

import com.exemple.demo2.pattern.*;

public class Reader extends User {
    private Badge badge;
    private int readerScore ;
    public Reader(String username, String password) {
        super(username, password);
        badge = new ReaderBadge();
        readerScore = 0;
    }

    @Override
    public String getRole() {
        return "Reader";
    }

    public String borrowBook(Book book) {
        if (borrowedBooks.contains(book)) {
            System.out.println("You have already borrowed this book.");
            return "You have already borrowed this book.";
        } else if (book.getState() instanceof BorrowedState) {
            System.out.println("the book is already borrowed.");
            return "the book is already borrowed.";
        }
        book.borrow();
        readerScore++;
        borrowedBooks.add(book);
        updateBadge();
        return "ok";
    }

    public String returnBook(Book book) {
        if (!borrowedBooks.contains(book)) {
            System.out.println("You didn't borrow this book.");
            return "You didn't borrow this book.";
        }
        book.returnBook();
        borrowedBooks.remove(book);
        updateBadge();
        return "ok";
    }

    public void updateBadge() {
        int count = readerScore;

        if (count >= 5 && count <= 10) {
            badge = new SilverBadge(badge);
        }
        else if (count >= 10 && count <= 50) {
            badge = new GoldBadge(badge);
        }
        else if (count >= 50) {
            badge = new DiamondBadge(badge);
        }
    }

    public int getReaderScore() {
        return readerScore;
    }

    public String getBadge() {
        return badge.getBadge();
    }
}
