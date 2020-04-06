package com.derandecker.popularmoviesstage2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    private String overview;
    @ColumnInfo(name = "vote_average")
    private int voteAverage;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private boolean popular;
    private boolean highestRated;
    private boolean fave;

    @Ignore
    public MovieEntry() {
    }

    @Ignore
    public MovieEntry(String title, String imagePath, String overview,
                      int voteAverage, String releaseDate, boolean popular, boolean highestRated,  boolean fave) {
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.popular = popular;
        this.highestRated = highestRated;
        this.fave = fave;
    }

    public MovieEntry(int id, String title, String imagePath, String overview,
                      int voteAverage, String releaseDate, boolean popular, boolean highestRated,  boolean fave) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.popular = popular;
        this.highestRated = highestRated;
        this.fave = fave;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Boolean getPopular(){
        return popular;
    }

    public Boolean getHighestRated(){
        return highestRated;
    }

    public Boolean getFave() {
        return fave;
    }

    public void setFave(Boolean favorite){
        this.fave = favorite;
    }
}