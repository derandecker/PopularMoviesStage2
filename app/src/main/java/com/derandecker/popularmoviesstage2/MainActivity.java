package com.derandecker.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.utils.AppExecutors;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;
import com.derandecker.popularmoviesstage2.viewmodels.MainActivityViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

//star_empty.png and star_filled.png in drawable folder are FREE under license https://creativecommons.org/licenses/by-nd/3.0/
//via https://icons8.com/license

public class MainActivity extends AppCompatActivity implements MovieImageAdapter.MovieClickListener {

    private String movieString;
    private RecyclerView mMoviesPics;
    private MovieImageAdapter mMovie;

//  TODO:
//      add code to make spanSize larger for bigger screens

    private int spanSize = 3;
    private Boolean fave;
    AppDatabase database;
    public static final boolean POPULAR = true;
    public static final boolean HIGHEST_RATED = true;
    private static final String MOVIE_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    List<MovieEntry> movies;

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("onCreate", "MainActivity was recreated");
        setContentView(R.layout.activity_main);

        mMoviesPics = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, spanSize);
        mMoviesPics.setLayoutManager(gridLayoutManager);
        mMoviesPics.setHasFixedSize(true);
        mMovie = new MovieImageAdapter(this, this);
        mMoviesPics.setAdapter(mMovie);

        database = AppDatabase.getInstance(getApplicationContext());

        showPopularMovies();

    }


    private void downloadMovies(String url, final boolean popular, final boolean highestRated) {
        final URL movie_url = NetworkUtils.buildMoviesUrl(url);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isOnline()) {
                        movieString = NetworkUtils.getResponseFromHttpUrl(movie_url);
                    } else {
                        return;
                    }
                    movies = JSONUtils.parseMovieJson(movieString, popular, highestRated);
                    database.movieDao().insertMovies(movies);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    private void showFavorites() {
        setTitle(R.string.favorite_movies_title);
        final MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
                Log.d("showFavorites", "showFavorites triggered");
            }
        });
    }

    private void showPopularMovies() {
        setTitle(R.string.popular_movies_title);
        downloadMovies(MOVIE_URL_POPULAR, POPULAR, false);
        final MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getFavoriteMovies().removeObservers(this);
        viewModel.getPopularMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                viewModel.getPopularMovies().removeObserver(this);
                mMovie.setMovies(movieEntries);
                Log.d("showPopular", "showPopular triggered");
            }
        });
    }

    private void showHighestRatedMovies() {
        setTitle(R.string.highest_rated_movies_title);
        downloadMovies(MOVIE_URL_TOP_RATED, false, HIGHEST_RATED);
        final MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getFavoriteMovies().removeObservers(this);
        viewModel.getHighestRatedMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                viewModel.getHighestRatedMovies().removeObserver(this);
                mMovie.setMovies(movieEntries);
                Log.d("showHighestRated", "showHighestRated triggered");
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onListItemClick(List<MovieEntry> faveMovies, int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        MovieEntry movie = faveMovies.get(clickedItemIndex);
        intent.putExtra(DetailActivity.EXTRA_ID, movie.getId());
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                showPopularMovies();
                return true;
            case R.id.highest_rated:
                showHighestRatedMovies();
                return true;
            case R.id.favorites:
                showFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
