package com.derandecker.popularmoviesstage2.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> faveMovies;
    private LiveData<List<MovieEntry>> popMovies;
    private LiveData<List<MovieEntry>> highestRatedMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        faveMovies = database.movieDao().loadFaveMovies();
        popMovies = database.movieDao().loadPopularMovies();
        highestRatedMovies = database.movieDao().loadHighestRatedMovies();
    }

    public LiveData<List<MovieEntry>> getFavoriteMovies() {
        return faveMovies;
    }

    public LiveData<List<MovieEntry>> getPopularMovies() { return popMovies; }

    public LiveData<List<MovieEntry>> getHighestRatedMovies() { return highestRatedMovies; }

}
