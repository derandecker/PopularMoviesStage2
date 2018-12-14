package com.derandecker.popularmoviesstage1.model;

public class Movie {

    private int id;
    private String title;
    private String imagePath;
    private String overview;
    private int voteAverage;
    private String releaseDate;

    public Movie() {
    }

    public Movie(int id, String title, String imagePath, String overview, int voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getImagePath() {
        return imagePath;
    }


}
