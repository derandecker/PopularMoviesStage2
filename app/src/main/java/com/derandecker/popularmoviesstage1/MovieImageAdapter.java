package com.derandecker.popularmoviesstage1;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.PicViewHolder> {

    private static final String TAG = MovieImageAdapter.class.getSimpleName();

    private int mNumberItems;

    public MovieImageAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    @Override
    public void onBindViewHolder(PicViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public PicViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForMoviePic = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForMoviePic, viewGroup, shouldAttachToParentImmediately);
        PicViewHolder viewHolder = new PicViewHolder(view);

        return viewHolder;
    }


    class PicViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePicView;

        public PicViewHolder(View itemView) {
            super(itemView);
            moviePicView = (ImageView) itemView.findViewById(R.id.iv_movie_pic);
        }

        void bind() {
            moviePicView.setImageResource(R.drawable.testimage);
        }
    }




}

