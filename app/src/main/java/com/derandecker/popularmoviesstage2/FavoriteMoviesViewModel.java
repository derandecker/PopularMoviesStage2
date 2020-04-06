package com.derandecker.popularmoviesstage2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;

import java.util.List;

public class FavoriteMoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> movies;
    private Boolean fave = true;
    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadFaveMovies(fave);
    }

    public LiveData<List<MovieEntry>> getFavoriteMovies() {
        return movies;
    }

}
