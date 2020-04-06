package com.derandecker.popularmoviesstage2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
