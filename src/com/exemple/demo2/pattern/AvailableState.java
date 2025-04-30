package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Book;

public class AvailableState implements BookState {
    @Override
    public void borrow(Book book) {
        book.setState(new BorrowedState());
        System.out.println(book.getTitle() + " has been borrowed.");
    }

    @Override
    public void returnBook(Book book) {
        System.out.println(book.getTitle() + " is already available.");
    }
}
