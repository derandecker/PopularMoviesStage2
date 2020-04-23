package com.derandecker.popularmoviesstage2.model;

public class Reviews {

    private String author;
    private String content;

    public Reviews() {
    }

    public Reviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() { return author; }

    public String getContent() { return content; }

}
