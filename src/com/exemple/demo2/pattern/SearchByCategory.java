package com.exemple.demo2.pattern;

import com.exemple.demo2.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByCategory implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(book -> book.getCategory().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
