package com.derandecker.popularmoviesstage2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.derandecker.popularmoviesstage2.model.MovieEntry;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovies(List<MovieEntry> movies);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> loadMovieById(int id);

    @Query("SELECT * FROM movie WHERE popular ORDER BY `order`")
    LiveData<List<MovieEntry>> loadPopularMovies();

    @Query("SELECT * FROM movie WHERE highest_rated ORDER BY `order`")
    LiveData<List<MovieEntry>> loadHighestRatedMovies();

    @Query("SELECT * FROM movie WHERE fave ORDER BY title")
    LiveData<List<MovieEntry>> loadFaveMovies();

    @Query("UPDATE movie SET fave = 1 WHERE id = :id")
    void setFavorite(int id);

    @Query("UPDATE movie SET fave = 0 WHERE id = :id")
    void removeFavorite(int id);

}
