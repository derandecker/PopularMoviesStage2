package com.derandecker.popularmoviesstage2;

import android.arch.lifecycle.LiveData;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieImageAdapter.MovieClickListener {

    private String movieString;
    private RecyclerView mMoviesPics;
    private MovieImageAdapter mMovie;
    private static final String MOVIE_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String DEFAULT_URL = "https://api.themoviedb.org/3/movie/popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPics = (RecyclerView) findViewById(R.id.rv_movies);

        int spanSize = 3;

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, spanSize);
        mMoviesPics.setLayoutManager(gridLayoutManager);
        mMoviesPics.setHasFixedSize(true);

        setOption(DEFAULT_URL);
    }

    private void setOption(String sortBy) {
        URL movie_url = NetworkUtils.buildUrl(sortBy);
        new TmdbMovieTask().execute(movie_url);
    }

    private void showFavorites() {
        mMovie = new MovieImageAdapter(this, null, this);
        mMoviesPics.setAdapter(mMovie);
        FavoriteMoviesViewModel viewModel = ViewModelProviders.of(this).get(FavoriteMoviesViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mMovie.setMovies(movieEntries);
            }
        });
    }

    private void populateUI(String tmdbMovies) {
        MovieImageAdapter mMovie = new MovieImageAdapter(this, tmdbMovies, this);
        mMoviesPics.setAdapter(mMovie);
        this.movieString = tmdbMovies;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


//  *****************************************************************************************
//  instead of sending extras individually, send movie ID via putextra intent
//  then use that ID in detail activity to get movie using viewmodel
//    ALSO --- use saved instance state (in onresume?) to restore main activity
//             to the list the user was looking at before detail activity intent was started
//    ALSO --- change "ADD FAV" button to a star that is connected to LiveData and shows solid when
//             the movie is a favorite and removes movie from faves DB when clicked
//    ALSO --- still need to add trailers and reviews and change layout to constraint layout
//             and move
//  *****************************************************************************************

    @Override
    public void onListItemClick(List<MovieEntry> faveMovies, boolean faves, int clickedItemIndex) {
        MovieEntry movie = null;
        if (faves) {
            movie = faveMovies.get(clickedItemIndex);
            //send intent using ID
            //in detail activity if EXTRA_ID exists, it will pull data from viewmodel
        } else {
            try {
                movie = JSONUtils.parseMovieJson(movieString, clickedItemIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //send movie object with intent with parcleable
            //in detail activity if EXTRA_ID doesn't exist, it will pull data from parcleable
        }
        //this code will be removed
        if (movie != null) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_ID, movie.getId());
            intent.putExtra(DetailActivity.EXTRA_TITLE, movie.getTitle());
            intent.putExtra(DetailActivity.EXTRA_IMAGE_PATH, movie.getImagePath());
            intent.putExtra(DetailActivity.EXTRA_OVERVIEW, movie.getOverview());
            intent.putExtra(DetailActivity.EXTRA_VOTE_AVERAGE, movie.getVoteAverage());
            intent.putExtra(DetailActivity.EXTRA_RELEASE_DATE, movie.getReleaseDate());
            startActivity(intent);
        }

    }


    public class TmdbMovieTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            String movieString;
            URL url = params[0];
            if (isOnline()) {
                try {
                    movieString = NetworkUtils.getResponseFromHttpUrl(url);
                    return movieString;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String tmdbMovies) {
            if (tmdbMovies != null) {
                populateUI(tmdbMovies);
            } else {
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        }

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
                setOption(MOVIE_URL_POPULAR);
                return true;
            case R.id.highest_rated:
                setOption(MOVIE_URL_TOP_RATED);
                return true;
            case R.id.favorites:
                showFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
