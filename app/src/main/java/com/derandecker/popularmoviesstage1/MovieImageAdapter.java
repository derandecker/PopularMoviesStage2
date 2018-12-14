package com.derandecker.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derandecker.popularmoviesstage1.model.Movie;
import com.derandecker.popularmoviesstage1.utils.JSONUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import static java.security.AccessController.getContext;


public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ImageViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private String movies;
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    public MovieImageAdapter(Context context, String movies) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);
        ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        try {
            Movie movie = JSONUtils.parseMovieJson(movies, position);
            Picasso.with(context)
                    .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                    .fit()
                    .into(holder.moviePic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        //temporary placeholder int
        return 20;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePic;

        public ImageViewHolder(View itemView) {
            super(itemView);
            moviePic = (ImageView) itemView.findViewById(R.id.iv_movie_pic);
        }
    }
}


