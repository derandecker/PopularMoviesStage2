package com.derandecker.popularmoviesstage2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.derandecker.popularmoviesstage2.R;
import com.derandecker.popularmoviesstage2.model.Review;

import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<Review> mReviews;
    private LayoutInflater inflater;
    private Review review;
    private Context context;

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView author_tv;
        TextView content_tv;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            author_tv = (TextView) itemView.findViewById(R.id.author_tv);
            content_tv = (TextView) itemView.findViewById(R.id.content_tv);
        }
    }

    public ReviewsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_item, parent, false);
        ReviewsViewHolder holder = new ReviewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        displayReviews(holder, position);
    }

    private void displayReviews(ReviewsViewHolder holder, int position) {
        review = mReviews.get(position);
        holder.author_tv.setText(review.getAuthor());
        holder.content_tv.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        } else {
            return mReviews.size();
        }
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

}
