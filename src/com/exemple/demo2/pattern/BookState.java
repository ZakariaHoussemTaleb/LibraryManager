package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Book;

public interface BookState {
    void borrow(Book book);
    void returnBook(Book book);
}
