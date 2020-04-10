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

    @Query("SELECT * FROM movie WHERE popular ORDER BY `order`")
    LiveData<List<MovieEntry>> loadPopularMovies();

    @Query("SELECT * FROM movie WHERE highest_rated ORDER BY `order`")
    LiveData<List<MovieEntry>> loadHighestRatedMovies();

    @Query("SELECT * FROM movie WHERE fave ORDER BY title")
    LiveData<List<MovieEntry>> loadFaveMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovies(List<MovieEntry> movies);

    @Update
    void updateMovie(List<MovieEntry> movies);

    @Query("DELETE FROM movie WHERE NOT fave")
    void deleteAllExceptFaves();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> loadMovieById(int id);
}
