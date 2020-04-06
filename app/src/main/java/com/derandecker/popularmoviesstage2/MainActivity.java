package com.derandecker.popularmoviesstage2;

import android.accounts.NetworkErrorException;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;

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
    private int spanSize = 3;
    private Boolean fave;
    AppDatabase database;
    private static final String FAVORITES = "favorites";
    private static final String POPULAR = "popular";
    private static final String HIGHEST_RATED = "highest_rated";
    private static final String MOVIE_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String DEFAULT_URL = "https://api.themoviedb.org/3/movie/popular";
    List<MovieEntry> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPics = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, spanSize);
        mMoviesPics.setLayoutManager(gridLayoutManager);
        mMoviesPics.setHasFixedSize(true);
        mMovie = new MovieImageAdapter(this, this);
        mMoviesPics.setAdapter(mMovie);


        database = AppDatabase.getInstance(getApplicationContext());

        downloadMovies(MOVIE_URL_POPULAR, POPULAR);
//        downloadMovies(MOVIE_URL_TOP_RATED, HIGHEST_RATED);
    }

    private void downloadMovies(String url, final String option) {
        final URL movie_url = NetworkUtils.buildUrl(url);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //                switch (option) {
//                    case POPULAR:
//                        boolean a = true;
//                        boolean b = false;
//                    case HIGHEST_RATED:
//                        a = false;
//                        b = true;
//
                    movieString = NetworkUtils.getResponseFromHttpUrl(movie_url);
                    boolean a = true;
                    boolean b = false;
                    movies = JSONUtils.parseMovieJson(movieString, a, b);
                    database.movieDao().insertMovies(movies);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

        });
//        showPopularMovies();

    }


    private void showFavorites() {
        FavoriteMoviesViewModel viewModel = ViewModelProviders.of(this).get(FavoriteMoviesViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
            }
        });
    }

    private void showPopularMovies() {
        PopularMoviesViewModel viewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);
        viewModel.getPopularMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d("showPopularMovies()", "Updating list of Popular Movies from LiveData in ViewModel");
                mMovie.setMovies(movieEntries);
            }
        });
    }

    private void showHighestRatedMovies() {
        HighestRatedMoviesViewModel viewModel = ViewModelProviders.of(this).get(HighestRatedMoviesViewModel.class);
        viewModel.getHighestRatedMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
