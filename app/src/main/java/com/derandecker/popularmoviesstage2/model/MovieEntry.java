package com.derandecker.popularmoviesstage2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey
    private int id;
    private String title;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    private String overview;
    @ColumnInfo(name = "vote_average")
    private int voteAverage;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @Ignore
    public MovieEntry() {
    }

    public MovieEntry(int id, String title, String imagePath, String overview, int voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }


    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getOverview(){
        return overview;
    }

    public int getVoteAverage(){
        return voteAverage;
    }

    public String getReleaseDate(){
        return releaseDate;
    }
}
