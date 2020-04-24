package com.derandecker.popularmoviesstage2.model;

public class Review {

    private String author;
    private String content;

    public Review() {
    }

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() { return author; }

    public String getContent() { return content; }

}
