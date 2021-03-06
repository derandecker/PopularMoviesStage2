package com.derandecker.popularmoviesstage2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey
    private int id;
    private int order;
    private String title;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    private String overview;
    @ColumnInfo(name = "vote_average")
    private int voteAverage;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private boolean popular;
    @ColumnInfo(name = "highest_rated")
    private boolean highestRated;
    private boolean fave;

    @Ignore
    public MovieEntry() {
    }

    public MovieEntry(int id, int order, String title, String imagePath, String overview,
                      int voteAverage, String releaseDate, boolean popular, boolean highestRated,  boolean fave) {
        this.id = id;
        this.order = order;
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

    public int getOrder() { return order;}

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
}
