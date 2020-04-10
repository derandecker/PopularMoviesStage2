package com.derandecker.popularmoviesstage2;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;

import java.util.List;

public class PopularMoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> movies;

    public PopularMoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadPopularMovies();
    }

    public LiveData<List<MovieEntry>> getPopularMovies() {
        return movies;
    }


}
