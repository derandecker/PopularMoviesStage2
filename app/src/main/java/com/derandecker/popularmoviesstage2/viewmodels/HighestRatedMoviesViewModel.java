package com.derandecker.popularmoviesstage2.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;

import java.util.List;

public class HighestRatedMoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> movies;
    private Boolean highestRated = true;

    public HighestRatedMoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadHighestRatedMovies();
    }

    public LiveData<List<MovieEntry>> getHighestRatedMovies() {
        return movies;
    }

}
