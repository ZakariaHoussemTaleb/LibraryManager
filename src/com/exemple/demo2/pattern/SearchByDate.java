package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByDate implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(book -> book.getPublishDate().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
    }
}
