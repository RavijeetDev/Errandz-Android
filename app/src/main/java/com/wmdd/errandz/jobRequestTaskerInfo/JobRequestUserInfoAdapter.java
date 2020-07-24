package com.wmdd.errandz.jobRequestTaskerInfo;

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
import com.wmdd.errandz.bean.Review;
import com.wmdd.errandz.bean.User;

import java.util.ArrayList;

public class JobRequestUserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int USER_VIEW_TYPE = 0;
    private final static int REVIEW_VIEW_TYPE = 1;

    private ArrayList<Review> reviewArrayList;
    private User user;

    public JobRequestUserInfoAdapter(User user) {
        this.user = user;
        reviewArrayList = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(viewType == USER_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_info_item, parent, false);
            viewHolder = new UserViewHolder(view);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_review_list_item, parent, false);
            viewHolder = new ReviewViewHolder(view);
        }
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            ((UserViewHolder)holder).bind();
        } else {
            ((ReviewViewHolder)holder).bind();
        }
    }


    @Override
    public int getItemCount() {
        return reviewArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? USER_VIEW_TYPE : REVIEW_VIEW_TYPE;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ImageView userProfileImageView;
        private TextView reviewerNameTextView;
        private TextView reviewPostedDateTextView;
        private TextView jobNameTextView;
        private TextView reviewTextView;
        private RatingBar reviewRatingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImageView = itemView.findViewById(R.id.user_profile_image_view);
            reviewerNameTextView = itemView.findViewById(R.id.reviewer_name_text_view);
            reviewPostedDateTextView = itemView.findViewById(R.id.review_posted_date_text_view);
            reviewRatingBar = itemView.findViewById(R.id.review_rating_bar);
            jobNameTextView = itemView.findViewById(R.id.review_job_text_view);
            reviewTextView = itemView.findViewById(R.id.review_text_view);

        }

        public void bind() {
            Review review = reviewArrayList.get(getAdapterPosition() - 1);

            reviewerNameTextView.setText(review.getReviewerName());
            reviewPostedDateTextView.setText(review.getReviewDate());
            reviewRatingBar.setRating(review.getRating());
            jobNameTextView.setText(review.getJobName());
            reviewTextView.setText(review.getReview());

            Picasso.get()
                    .load(review.getReviewerProfilePic())
                    .into(userProfileImageView);

        }

    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView userProfileImageView;
        private TextView userNameTextView;
        private TextView userAgeTextView;
        private TextView userRatingTextView;
        private TextView userBioTextView;
        private TextView noReviewTextView;
        private RatingBar userRatingBar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImageView = itemView.findViewById(R.id.user_profile_image_view);
            userNameTextView = itemView.findViewById(R.id.user_name_text_view);
            userAgeTextView = itemView.findViewById(R.id.user_dob_text_view);
            userRatingTextView = itemView.findViewById(R.id.user_rating_text_view);
            userRatingBar = itemView.findViewById(R.id.user_rating_bar);
            userBioTextView = itemView.findViewById(R.id.bio_text_view);
            noReviewTextView = itemView.findViewById(R.id.no_review_text_view);

        }

        public void bind() {
            userNameTextView.setText(user.getFirstName() + " " + user.getLastName());
            userAgeTextView.setText("Age: " +user.getAge());
            userBioTextView.setText(user.getBio());

            Picasso.get()
                    .load(user.getProfileImage())
                    .into(userProfileImageView);

            if (user.getTotalRating() > 0) {
                userRatingBar.setVisibility(View.VISIBLE);
                userRatingTextView.setText(String.format("%.1f", user.getTotalRating()));
                userRatingBar.setRating(user.getTotalRating());
            } else {
                userRatingTextView.setText("No Rating");
            }

            if(user.getNumberOfReviews() == 0) {
                noReviewTextView.setVisibility(View.VISIBLE);
            }
        }


    }

    public void setReviewArrayList(ArrayList<Review> reviews) {
        this.reviewArrayList = reviews;
        notifyDataSetChanged();
    }
}
