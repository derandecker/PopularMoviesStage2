package com.derandecker.popularmoviesstage2;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_IMAGE_PATH = "extra_image_path";
    public static final String EXTRA_OVERVIEW = "extra_overview";
    public static final String EXTRA_VOTE_AVERAGE = "extra_vote_average";
    public static final String EXTRA_RELEASE_DATE = "extra_release_date";
    public static final String FAVE = "fave";

    private static final String OUT_OF_NUM = "/10";

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static final int DEFAULT_INT = 0;
//    private static final String DEFAULT_STRING = "Data not available";

    private AppDatabase mDb;
    private int id;
    private String title;
    private String imagePath;
    private String overview;
    private int voteAverage;
    private String releaseDate;
    private Boolean fave;
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
        title = intent.getStringExtra(EXTRA_TITLE);
        imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH);
        overview = intent.getStringExtra(EXTRA_OVERVIEW);
        voteAverage = intent.getIntExtra(EXTRA_VOTE_AVERAGE, DEFAULT_INT);
        releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE);
        fave = intent.getBooleanExtra(FAVE, false);

//        toggleButton = (ToggleButton) findViewById(R.id.myToggleButton);
//        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
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





        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        ImageView posterIv = (ImageView) findViewById(R.id.poster_iv);
        TextView voteAvgTv = (TextView) findViewById(R.id.vote_average_tv);
        TextView releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        TextView overviewTv = (TextView) findViewById(R.id.overview_tv);

        titleTv.setText(title);
        voteAvgTv.setText(Integer.toString(voteAverage) + OUT_OF_NUM);
        releaseDateTv.setText(releaseDate);
        overviewTv.setText(overview);

        Picasso.with(this)
                .load(BASE_URL + IMAGE_SIZE + imagePath)
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
