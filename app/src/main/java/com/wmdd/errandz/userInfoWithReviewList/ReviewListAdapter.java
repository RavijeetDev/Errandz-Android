package com.wmdd.errandz.userInfoWithReviewList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.Review;
import com.wmdd.errandz.util.Constants;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private ArrayList<Review> reviewArrayList;

    public ReviewListAdapter() {
        reviewArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewArrayList.get(position);

        holder.reviewerNameTextView.setText(review.getReviewerName());
        holder.reviewPostedDateTextView.setText(review.getReviewDate());
        holder.reviewRatingBar.setRating(review.getRating());
        holder.jobNameTextView.setText(review.getJobName());
        holder.reviewTextView.setText(review.getReview());

        Picasso.get()
                .load(review.getReviewerProfilePic())
                .into(holder.reviewerProfileImageView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView reviewerProfileImageView;
        private TextView reviewerNameTextView;
        private TextView reviewPostedDateTextView;
        private RatingBar reviewRatingBar;
        private TextView jobNameTextView;
        private TextView reviewTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerProfileImageView = itemView.findViewById(R.id.user_profile_image_view);
            reviewerNameTextView = itemView.findViewById(R.id.reviewer_name_text_view);
            reviewPostedDateTextView = itemView.findViewById(R.id.review_posted_date_text_view);
            reviewRatingBar = itemView.findViewById(R.id.review_rating_bar);
            jobNameTextView = itemView.findViewById(R.id.review_job_text_view);
            reviewTextView = itemView.findViewById(R.id.review_text_view);
        }

    }

    public void setReviewArrayList(ArrayList<Review> reviews) {
        reviewArrayList = reviews;
        notifyDataSetChanged();
    }
}
