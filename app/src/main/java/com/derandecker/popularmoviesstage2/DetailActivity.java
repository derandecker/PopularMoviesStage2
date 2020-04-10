package com.derandecker.popularmoviesstage2;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String FAVE = "fave";

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

//      TODO:
//      use databinding to set UI fields



        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        id = intent.getIntExtra(EXTRA_ID, DEFAULT_INT);

        toggleButton = (ToggleButton) findViewById(R.id.myToggleButton);
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));

//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    toggleButtonChecked();
//                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.star_filled));
//                }
//                else{
//                    toggleButtonUnChecked();
//                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
//                }
//            }
//        });
//
//        if(fave){
//            toggleButton.setChecked(true);
//        }
//        else{
//            toggleButton.setChecked(false);
//        }


        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(mDb, id);
            final MovieDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
            viewModel.getMovie().observe(this, new Observer<MovieEntry>() {
                @Override
                public void onChanged(@Nullable MovieEntry movie) {
                    viewModel.getMovie().removeObserver(this);
                    populateUI(movie);
                }
            });

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

        Picasso.get()
                .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(posterIv);

    }


//    public void toggleButtonChecked() {
//        final MovieEntry favoriteMovie = new MovieEntry(id, title, imagePath, overview, voteAverage, releaseDate, true);
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.movieDao().insertMovie(favoriteMovie);
//            }
//        });
//    }
//
//    public void toggleButtonUnChecked() {
//        final MovieEntry favoriteMovie = new MovieEntry(id, title, imagePath, overview, voteAverage, releaseDate, fave);
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.movieDao().deleteMovie(favoriteMovie);
//            }
//        });
//
//    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
