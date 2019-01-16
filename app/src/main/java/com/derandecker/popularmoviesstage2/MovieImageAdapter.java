package com.derandecker.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;


public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ImageViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private String movies;
    private List<MovieEntry> faveMovies;
    private boolean faves;
    final private MovieClickListener mOnClickListener;
    private MovieEntry movie;
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    public interface MovieClickListener {
        void onListItemClick(List<MovieEntry> faveMovies, boolean faves, int clickedItemIndex);
    }

    public MovieImageAdapter(Context context, String movies, MovieClickListener listener) {
        inflater = LayoutInflater.from(context);
        mOnClickListener = listener;
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
        if (faves) {
            displayFavoriteMovies(holder, position);
        } else if (movies != null) {
            displayNetworkAPIMovies(holder, position);
        }
    }

    private void displayNetworkAPIMovies(ImageViewHolder holder, int position) {
        try {
            movie = JSONUtils.parseMovieJson(movies, position);
            setMoviePicHolder(holder, movie);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayFavoriteMovies(ImageViewHolder holder, int position) {
        movie = faveMovies.get(position);
        setMoviePicHolder(holder, movie);
    }

    private void setMoviePicHolder(ImageViewHolder holder, MovieEntry movie) {
        Picasso.with(context)
                .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(holder.moviePic);
    }


    @Override
    public int getItemCount() {
        if (faves) {
            if (faveMovies == null) {
                return 0;
            } else {
                return faveMovies.size();
            }
        }
        return 20;
    }


    public void setMovies(List<MovieEntry> movieEntries) {
        faveMovies = movieEntries;
        faves = true;
        notifyDataSetChanged();
    }



    //problem here with onclick event
    //It's pulling up incorrect movie object for the position sent to onclick listener
    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePic;

        public ImageViewHolder(View itemView) {
            super(itemView);
            moviePic = (ImageView) itemView.findViewById(R.id.iv_movie_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(faveMovies, faves, clickedPosition);
        }
    }

}



