package com.derandecker.popularmoviesstage2.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derandecker.popularmoviesstage2.R;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ImageViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<MovieEntry> mMovies;
    final private MovieClickListener mOnClickListener;
    private MovieEntry movie;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    public interface MovieClickListener {
        void onListItemClick(List<MovieEntry> mMovies, int clickedItemIndex);
    }

    public MovieImageAdapter(Context context, MovieClickListener listener) {
        inflater = LayoutInflater.from(context);
        mOnClickListener = listener;
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);
        ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        displayMovies(holder, position);
    }

    private void displayMovies(ImageViewHolder holder, int position) {
        movie = mMovies.get(position);
        setMoviePicHolder(holder, movie);
    }

    private void setMoviePicHolder(ImageViewHolder holder, MovieEntry movie) {
        String path = BASE_URL + IMAGE_SIZE + movie.getImagePath();
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(holder.moviePic);
    }


    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        } else {
            return mMovies.size();
        }
    }


    public void setMovies(List<MovieEntry> movieEntries) {
        mMovies = movieEntries;
        notifyDataSetChanged();
    }


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
            mOnClickListener.onListItemClick(mMovies, clickedPosition);
        }
    }

}



