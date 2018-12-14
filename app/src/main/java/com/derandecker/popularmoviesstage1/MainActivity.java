package com.derandecker.popularmoviesstage1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.widget.Toast;

import com.derandecker.popularmoviesstage1.model.Movie;
import com.derandecker.popularmoviesstage1.utils.JSONUtils;
import com.derandecker.popularmoviesstage1.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesPics;

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

        URL movie_url = NetworkUtils.buildUrl("https://api.themoviedb.org/3/movie/popular");
        new TmdbMovieTask().execute(movie_url);
    }

    public void populateUI(String tmdbMovies) {
        MovieImageAdapter mMovie = new MovieImageAdapter(this, tmdbMovies);
        mMoviesPics.setAdapter(mMovie);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "No movie data", Toast.LENGTH_SHORT).show();
    }

    public class TmdbMovieTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            String movieString;
            URL url = params[0];
            try {
                movieString = NetworkUtils.getResponseFromHttpUrl(url);
                return movieString;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String tmdbMovies) {
            populateUI(tmdbMovies);
        }
    }
}
