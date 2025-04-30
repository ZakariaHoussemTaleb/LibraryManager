package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Book;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String keyword);
}
