package com.derandecker.popularmoviesstage2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;

import java.util.List;

public class HighestRatedMoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> movies;
    private Boolean highestRated = true;

    public HighestRatedMoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadHighestRatedMovies(highestRated);
    }

    public LiveData<List<MovieEntry>> getHighestRatedMovies() {
        return movies;
    }

}