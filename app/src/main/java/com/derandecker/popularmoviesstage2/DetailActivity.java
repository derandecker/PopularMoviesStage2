package com.derandecker.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.model.RelatedVideos;
import com.derandecker.popularmoviesstage2.utils.AppExecutors;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;
import com.derandecker.popularmoviesstage2.viewmodels.MainActivityViewModel;
import com.derandecker.popularmoviesstage2.viewmodels.MovieDetailViewModel;
import com.derandecker.popularmoviesstage2.viewmodels.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String relatedVideosString;
    List<RelatedVideos> relatedVideos;

    public static final String EXTRA_ID = "extra_id";

    private static final String OUT_OF_NUM = "/10";

    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static final int DEFAULT_INT = 0;
//    private static final String DEFAULT_STRING = "Data not available";

    private AppDatabase mDb;
    private int id;
    ToggleButton toggleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        id = intent.getIntExtra(EXTRA_ID, DEFAULT_INT);

        toggleButton = (ToggleButton) findViewById(R.id.myToggleButton);
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
        setListenerForToggleButton();

        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(mDb, id);
        final MovieDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
        viewModel.getMovie().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable MovieEntry movie) {
                viewModel.getMovie().removeObserver(this);
                populateMainUI(movie);
            }
        });


        downloadRelatedMovies(id);

    }

    private void setListenerForToggleButton() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonChecked();
                } else {
                    toggleButtonUnChecked();
                }
            }
        });
    }

    //TODO:
    // try to do this work in the viewmodel
    // so it doesn't reload when changing configurations
    private void downloadRelatedMovies(int movieId) {
        final URL movie_url = NetworkUtils.buildRelatedVideosUrl(VIDEOS_URL, movieId);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isOnline()) {
                        relatedVideosString = NetworkUtils.getResponseFromHttpUrl(movie_url);
                    } else {
                        return;
                    }
                    relatedVideos = JSONUtils.parseRelatedVideoJson(relatedVideosString);
                    populateRelatedVideos(relatedVideos);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateMainUI(MovieEntry movie) {
        if (movie == null) {
            return;
        }

        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        ImageView posterIv = (ImageView) findViewById(R.id.poster_iv);
        TextView voteAvgTv = (TextView) findViewById(R.id.vote_average_tv);
        TextView releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        TextView overviewTv = (TextView) findViewById(R.id.overview_tv);

        titleTv.setText(movie.getTitle());
        voteAvgTv.setText(Integer.toString(movie.getVoteAverage()) + OUT_OF_NUM);
        releaseDateTv.setText(movie.getReleaseDate());
        overviewTv.setText(movie.getOverview());

//      TODO:
//          Change download and error placeholders to something
//          more aesthetically appealing
        Picasso.get()
                .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(posterIv);

        toggleButton.setOnCheckedChangeListener(null);
        boolean fave = movie.getFave();
        if (fave) {
            toggleButton.setChecked(true);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        }
        setListenerForToggleButton();

    }


    private void populateRelatedVideos(final List<RelatedVideos> relatedVideos) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                TextView trailerOne = (TextView) findViewById(R.id.trailer_one_tv);
                TextView trailerTwo = (TextView) findViewById(R.id.trailer_two_tv);
                TextView relatedTrailersLabel = (TextView) findViewById(R.id.trailers_label);
                CardView trailerCardView = (CardView) findViewById(R.id.card_view_trailers);

                try {
                    trailerOne.setText(relatedVideos.get(0).getName());
                    relatedTrailersLabel.setVisibility(View.VISIBLE);
                    trailerOne.setVisibility(View.VISIBLE);
                    trailerCardView.setVisibility(View.VISIBLE);
                    trailerTwo.setText(relatedVideos.get(1).getName());
                    trailerTwo.setVisibility(View.VISIBLE);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void toggleButtonChecked() {
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().setFavorite(id);
            }

        });
    }

    public void toggleButtonUnChecked() {
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().removeFavorite(id);
            }

        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}