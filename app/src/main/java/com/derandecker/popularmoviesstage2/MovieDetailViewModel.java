package com.derandecker.popularmoviesstage2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;


public class MovieDetailViewModel extends ViewModel {

    private LiveData<MovieEntry> movie;

    public MovieDetailViewModel(AppDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<MovieEntry> getMovie() {
        return movie;
    }
}
