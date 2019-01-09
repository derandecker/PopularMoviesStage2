package com.derandecker.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derandecker.popularmoviesstage2.model.Movie;
import com.derandecker.popularmoviesstage2.utils.JSONUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ImageViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private String movies;
    final private MovieClickListener mOnClickListener;
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    public interface MovieClickListener {
        void onListItemClick(int clickedItemIndex);
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
        try {
            Movie movie = JSONUtils.parseMovieJson(movies, position);
            Picasso.with(context)
                    .load(BASE_URL + IMAGE_SIZE + movie.getImagePath())
                    .placeholder(R.drawable.downloading)
                    .error(R.drawable.unknownerror)
                    .fit()
                    .into(holder.moviePic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 20;
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
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}



