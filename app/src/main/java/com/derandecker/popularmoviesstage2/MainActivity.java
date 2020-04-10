package com.derandecker.popularmoviesstage2;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.utils.AppExecutors;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;
import com.derandecker.popularmoviesstage2.viewmodels.GetFavoriteMoviesViewModel;
import com.derandecker.popularmoviesstage2.viewmodels.HighestRatedMoviesViewModel;
import com.derandecker.popularmoviesstage2.viewmodels.PopularMoviesViewModel;

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
//  add code to make spanSize larger for bigger screens

    private int spanSize = 3;
    private Boolean fave;
    AppDatabase database;
    private static final String OUT_STATE_POPULAR = "out_state_popular";
    private static final String OUT_STATE_HIGHEST_RATED = "out_state_highest_rated";
    private static final String OUT_STATE_FAVORITES = "out_state_favorites";
    private static final String FAVORITES = "favorites";
    public static final boolean POPULAR = true;
    public static final boolean HIGHEST_RATED = true;
    private static final String MOVIE_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String DEFAULT_URL = "https://api.themoviedb.org/3/movie/popular";
    List<MovieEntry> movies;

    @Override
    protected void onResume() {
        super.onResume();

    }

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


//        TODO:
//        showPopularMovies();
//        replace this line with code to replace activity state with previous state
//        it was in before opening DetailActivity

    }


    private void downloadMovies(String url, final boolean popular, final boolean highestRated) {
        final URL movie_url = NetworkUtils.buildUrl(url);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    movieString = NetworkUtils.getResponseFromHttpUrl(movie_url);
                    movies = JSONUtils.parseMovieJson(movieString, popular, highestRated);
                    database.movieDao().insertMovies(movies);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    private void showFavorites() {
        GetFavoriteMoviesViewModel viewModel = ViewModelProviders.of(this).get(GetFavoriteMoviesViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
            }
        });
    }

    private void showPopularMovies() {
        downloadMovies(MOVIE_URL_POPULAR, POPULAR, false);
        PopularMoviesViewModel viewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);
        viewModel.getPopularMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
//                Log.d("showPopularMovies()", movieEntries.get(0).getTitle());
            }
        });
    }

    private void showHighestRatedMovies() {
        downloadMovies(MOVIE_URL_TOP_RATED, false, HIGHEST_RATED);
        HighestRatedMoviesViewModel viewModel = ViewModelProviders.of(this).get(HighestRatedMoviesViewModel.class);
        viewModel.getHighestRatedMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
//                Log.d("showHighestRated()", movieEntries.get(0).getTitle());
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
