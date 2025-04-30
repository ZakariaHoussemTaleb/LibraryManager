package com.exemple.demo2.model;

import com.exemple.demo2.pattern.AvailableState;
import com.exemple.demo2.pattern.BookState;

public class Book {
    private String title;
    private String author;
    private String category;
    private String publishDate;
    private BookState state;

    public Book(String title, String author, String category, String publishDate) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.publishDate = publishDate;
        this.state = new AvailableState();
    }

    public void borrow() {
        state.borrow(this);
    }

    public void returnBook() {
        state.returnBook(this);
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public BookState getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getPublishDate() {
        return publishDate;
    }
}
