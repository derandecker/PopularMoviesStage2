package com.derandecker.popularmoviesstage2;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.model.RelatedVideos;
import com.derandecker.popularmoviesstage2.utils.AppExecutors;
import com.derandecker.popularmoviesstage2.viewmodels.MovieDetailViewModel;
import com.derandecker.popularmoviesstage2.viewmodels.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";

    private static final String OUT_OF_NUM = "/10";

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

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        id = intent.getIntExtra(EXTRA_ID, DEFAULT_INT);

        toggleButton = (ToggleButton) findViewById(R.id.myToggleButton);
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonChecked();
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
                } else {
                    toggleButtonUnChecked();
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
                }
            }
        });


        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(mDb, id);
        final MovieDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
        viewModel.getMovie().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable MovieEntry movie) {
                viewModel.getMovie().removeObserver(this);
                populateUI(movie);
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void populateUI(MovieEntry movie) {
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

        if (movie.getFave()) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }

    }

    private void populateRelatedVideos(ArrayList<RelatedVideos> relatedVideos) {
        TextView trailerOne = (TextView) findViewById(R.id.trailer_one_tv);
        TextView trailerTwo = (TextView) findViewById(R.id.trailer_two_tv);

        trailerOne.setText(relatedVideos.get(0).getName());
        trailerTwo.setText(relatedVideos.get(1).getName());

    }


    public void toggleButtonChecked() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().setFavorite(id);
            }

        });
    }

    public void toggleButtonUnChecked() {
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
}
