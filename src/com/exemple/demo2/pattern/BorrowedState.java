package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Book;

public class BorrowedState implements BookState {
    @Override
    public void borrow(Book book) {
        System.out.println(book.getTitle() + " is already borrowed.");
    }

    @Override
    public void returnBook(Book book) {
        book.setState(new AvailableState());
        System.out.println(book.getTitle() + " has been returned.");
    }
}
