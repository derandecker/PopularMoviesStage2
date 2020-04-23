package com.derandecker.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.databinding.ActivityDetailBinding;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String relatedVideosString;
    List<RelatedVideos> relatedVideos;

    public static final String EXTRA_ID = "extra_id";

    private static final String OUT_OF_NUM = "/10";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/";
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static final int DEFAULT_INT = 0;

    private AppDatabase mDb;
    private int id;
    ToggleButton toggleButton;
    ActivityDetailBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        id = intent.getIntExtra(EXTRA_ID, DEFAULT_INT);

        mBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
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

    private void openVideoIntent(String videoID) {
        Uri videoUri = NetworkUtils.buildYoutubeUri(YOUTUBE_BASE_URL, videoID);
        Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
        startActivity(videoIntent);
        Log.d("openVideoIntent", videoUri.toString());
    }

    private void setListenerForToggleButton() {
        mBinding.myToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        mBinding.titleTv.setText(movie.getTitle());
        mBinding.voteAverageTv.setText(Integer.toString(movie.getVoteAverage()) + OUT_OF_NUM);
        mBinding.releaseDateTv.setText(movie.getReleaseDate());
        mBinding.overviewTv.setText(movie.getOverview());

//      TODO:
//          Change download and error placeholders to something
//          more aesthetically appealing
        Picasso.get()
                .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(mBinding.posterIv);

        mBinding.myToggleButton.setOnCheckedChangeListener(null);
        boolean fave = movie.getFave();
        if (fave) {
            mBinding.myToggleButton.setChecked(true);
            mBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        }
        setListenerForToggleButton();

    }


    private void populateRelatedVideos(final List<RelatedVideos> relatedVideos) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mBinding.trailerOneTv.setText(relatedVideos.get(0).getName());
                    mBinding.trailerOneTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("onClick trailer", "test");
                            openVideoIntent(relatedVideos.get(0).getKey());
                        }
                    });
                    mBinding.trailersLabel.setVisibility(View.VISIBLE);
                    mBinding.trailerOneTv.setVisibility(View.VISIBLE);
                    mBinding.cardViewTrailers.setVisibility(View.VISIBLE);
                    mBinding.trailerTwoTv.setText(relatedVideos.get(1).getName());
                    mBinding.trailerTwoTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openVideoIntent(relatedVideos.get(1).getKey());
                        }
                    });
                    mBinding.trailerTwoTv.setVisibility(View.VISIBLE);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void toggleButtonChecked() {
        mBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().setFavorite(id);
            }

        });
    }

    public void toggleButtonUnChecked() {
        mBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
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