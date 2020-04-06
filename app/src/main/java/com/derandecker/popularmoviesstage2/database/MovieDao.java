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

    @Query("SELECT * FROM movie WHERE popular = :popular ORDER BY title")
    LiveData<List<MovieEntry>> loadPopularMovies(boolean popular);

    @Query("SELECT * FROM movie WHERE popular = :highestRated ORDER BY title")
    LiveData<List<MovieEntry>> loadHighestRatedMovies(boolean highestRated);

    @Query("SELECT * FROM movie WHERE fave = :fave ORDER BY title")
    LiveData<List<MovieEntry>> loadFaveMovies(boolean fave);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<MovieEntry> movies);

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateMovie(MovieEntry movieEntry);

    @Query("DELETE FROM movie WHERE fave != :boolTrue")
    void deleteAllExceptFaves(boolean boolTrue);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> loadMovieById(int id);
}
