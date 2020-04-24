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
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.derandecker.popularmoviesstage2.adapters.ReviewsAdapter;
import com.derandecker.popularmoviesstage2.database.AppDatabase;
import com.derandecker.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.model.RelatedVideos;
import com.derandecker.popularmoviesstage2.model.Review;
import com.derandecker.popularmoviesstage2.utils.AppExecutors;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.derandecker.popularmoviesstage2.utils.NetworkUtils;
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

    private String reviewsString;
    List<Review> reviews;

    public static final String EXTRA_ID = "extra_id";

    private static final String OUT_OF_NUM = "/10";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/";
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static final int DEFAULT_INT = 0;

    private AppDatabase mDb;
    private int id;
    ActivityDetailBinding mPrimaryMovieInfoBinding;

    private RecyclerView mReviewsRV;
    private ReviewsAdapter mReviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPrimaryMovieInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        if (intent.hasExtra(EXTRA_ID)) {
            id = intent.getIntExtra(EXTRA_ID, DEFAULT_INT);
        } else closeOnError();

        mPrimaryMovieInfoBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
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


        mReviewsRV = (RecyclerView) findViewById(R.id.rv_reviews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mReviewsRV.setLayoutManager(layoutManager);
        mReviewsRV.setHasFixedSize(false);
        mReviewAdapter = new ReviewsAdapter(this);
        mReviewsRV.setAdapter(mReviewAdapter);

        downloadRelatedMovies(id);

        downloadReviews(id);
    }

    private void openVideoIntent(String videoID) {
        Uri videoUri = NetworkUtils.buildYoutubeUri(YOUTUBE_BASE_URL, videoID);
        Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
        startActivity(videoIntent);
        Log.d("openVideoIntent", videoUri.toString());
    }

    private void setListenerForToggleButton() {
        mPrimaryMovieInfoBinding.myToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
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

    private void downloadReviews(int movieId) {
        final URL reviews_url = NetworkUtils.buildReviewsUrl(VIDEOS_URL, movieId);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isOnline()) {
                        reviewsString = NetworkUtils.getResponseFromHttpUrl(reviews_url);
                    } else {
                        return;
                    }
                    reviews = JSONUtils.parseReviewsJson(reviewsString);
                    mReviewAdapter.setReviews(reviews);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    private void populateMainUI(MovieEntry movie) {
        if (movie == null) {
            return;
        }
        mPrimaryMovieInfoBinding.titleTv.setText(movie.getTitle());
        mPrimaryMovieInfoBinding.voteAverageTv.setText(movie.getVoteAverage() + OUT_OF_NUM);
        mPrimaryMovieInfoBinding.releaseDateTv.setText(movie.getReleaseDate());
        mPrimaryMovieInfoBinding.overviewTv.setText(movie.getOverview());

//      TODO:
//          Change download and error placeholders to something
//          more aesthetically appealing
        Picasso.get()
                .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(mPrimaryMovieInfoBinding.posterIv);

        mPrimaryMovieInfoBinding.myToggleButton.setOnCheckedChangeListener(null);
        boolean fave = movie.getFave();
        if (fave) {
            mPrimaryMovieInfoBinding.myToggleButton.setChecked(true);
            mPrimaryMovieInfoBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        }
        setListenerForToggleButton();

    }


    private void populateRelatedVideos(final List<RelatedVideos> relatedVideos) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mPrimaryMovieInfoBinding.trailerOneTv.setText(relatedVideos.get(0).getName());
                    mPrimaryMovieInfoBinding.trailerOneTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("onClick trailer", "test");
                            openVideoIntent(relatedVideos.get(0).getKey());
                        }
                    });
                    mPrimaryMovieInfoBinding.trailersLabel.setVisibility(View.VISIBLE);
                    mPrimaryMovieInfoBinding.trailerOneTv.setVisibility(View.VISIBLE);
                    mPrimaryMovieInfoBinding.cardViewTrailers.setVisibility(View.VISIBLE);
                    mPrimaryMovieInfoBinding.trailerTwoTv.setText(relatedVideos.get(1).getName());
                    mPrimaryMovieInfoBinding.trailerTwoTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openVideoIntent(relatedVideos.get(1).getKey());
                        }
                    });
                    mPrimaryMovieInfoBinding.trailerTwoTv.setVisibility(View.VISIBLE);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void toggleButtonChecked() {
        mPrimaryMovieInfoBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().setFavorite(id);
            }

        });
    }

    public void toggleButtonUnChecked() {
        mPrimaryMovieInfoBinding.myToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty));
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