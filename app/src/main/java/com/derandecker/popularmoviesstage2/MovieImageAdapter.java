package com.derandecker.popularmoviesstage2;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ImageViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<MovieEntry> faveMovies;
    final private MovieClickListener mOnClickListener;
    private MovieEntry movie;
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    public interface MovieClickListener {
        void onListItemClick(List<MovieEntry> faveMovies, int clickedItemIndex);
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
        displayFavoriteMovies(holder, position);
    }

    private void displayFavoriteMovies(ImageViewHolder holder, int position) {
        movie = faveMovies.get(position);
        //this is coming back as null, which means movies aren't getting added into db
        //or aren't reading from it properly
        setMoviePicHolder(holder, movie);
    }

    private void setMoviePicHolder(ImageViewHolder holder, MovieEntry movie) {
        Picasso.get()
                .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.unknownerror)
                .fit()
                .into(holder.moviePic);
    }


    @Override
    public int getItemCount() {
//        if (faveMovies == null) {
//            return 0;
//        } else {
//            return faveMovies.size();
//        }
        return 20;
    }


    public void setMovies(List<MovieEntry> movieEntries) {
        faveMovies = movieEntries;
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
            mOnClickListener.onListItemClick(faveMovies, clickedPosition);
        }
    }

}



