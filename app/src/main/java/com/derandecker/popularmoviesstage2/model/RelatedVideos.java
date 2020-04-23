package com.derandecker.popularmoviesstage2.model;

public class RelatedVideos {
    private String key;
    private String name;

    public RelatedVideos() {
    }

    public RelatedVideos(String key, String name){
        this.key = key;
        this.name = name;
    }

    public String getKey() { return key; }

    public String getName() { return name; }
}