package com.derandecker.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.derandecker.popularmoviesstage2.model.Movie;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieImageAdapter.MovieClickListener {

    private String movieString;
    private RecyclerView mMoviesPics;
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


    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = null;
        try {
            movie = JSONUtils.parseMovieJson(movieString, clickedItemIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        }
        return super.onOptionsItemSelected(item);
    }
}
