package com.derandecker.popularmoviesstage1;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_IMAGE_PATH = "extra_image_path";
    public static final String EXTRA_OVERVIEW = "extra_overview";
    public static final String EXTRA_VOTE_AVERAGE = "extra_vote_average";
    public static final String EXTRA_RELEASE_DATE = "extra_release_date";

    private static final String OUT_OF_NUM = "/10";

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static final int DEFAULT_INT = 0;
//    private static final String DEFAULT_STRING = "Data not available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int id = intent.getIntExtra(EXTRA_ID, DEFAULT_INT);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH);
        String overview = intent.getStringExtra(EXTRA_OVERVIEW);
        int voteAverage = intent.getIntExtra(EXTRA_VOTE_AVERAGE, DEFAULT_INT);
        String releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE);

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

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
