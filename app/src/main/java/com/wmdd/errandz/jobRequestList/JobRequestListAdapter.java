package com.wmdd.errandz.jobRequestList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.JobDescription;
import com.wmdd.errandz.bean.User;


import java.util.ArrayList;

public class JobRequestListAdapter extends RecyclerView.Adapter<JobRequestListAdapter.ViewHolder> {

    private JobItemClickListener jobItemCLickListener;
    private ArrayList<JobDescription> jobDescriptionArrayList;

    public JobRequestListAdapter(JobItemClickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        jobDescriptionArrayList = new ArrayList<>();
    }

    public interface JobItemClickListener {
        void onJobItemClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_request_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Math.min(jobDescriptionArrayList.size(), 10);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView taskerProfileImageView;
        private TextView taskerNameTextView;
        private TextView jobNameTextView;
        private RatingBar taskerRatingBar;
        private TextView taskerRatingTextView;
        private TextView taskerTotalReviewsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskerProfileImageView = itemView.findViewById(R.id.profile_image_view);
            taskerNameTextView = itemView.findViewById(R.id.tasker_name_text_view);
            jobNameTextView = itemView.findViewById(R.id.job_name_text_view);
            taskerRatingBar = itemView.findViewById(R.id.tasker_rating_bar);
            taskerRatingTextView = itemView.findViewById(R.id.tasker_rating_text_view);
            taskerTotalReviewsTextView = itemView.findViewById(R.id.tasker_total_reviews_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind() {
            JobDescription jobDescription = jobDescriptionArrayList.get(getAdapterPosition());
            taskerNameTextView.setText(jobDescription.getUser().getFirstName());
            jobNameTextView.setText(jobDescription.getJob().getJobName());

            Picasso.get()
                    .load(jobDescription.getUser().getProfileImage())
                    .into(taskerProfileImageView);

            if (jobDescription.getUser().getTotalRating() > 0) {
                taskerRatingBar.setVisibility(View.VISIBLE);
                taskerRatingBar.setRating(jobDescription.getUser().getTotalRating() /
                        jobDescription.getUser().getNumberOfReviews());
                taskerRatingTextView.setText(String.format("%.1f", jobDescription.getUser().getTotalRating()));
            } else {
                taskerRatingTextView.setText("No rating");
            }

            taskerTotalReviewsTextView.setText(jobDescription.getUser().getNumberOfReviews() == 0 ||
                    jobDescription.getUser().getNumberOfReviews() == 1
                    ? jobDescription.getUser().getNumberOfReviews() + " Review" :
                    jobDescription.getUser().getNumberOfReviews() + " Reviews");
        }

        @Override
        public void onClick(View v) {
            JobDescription jobDescription = jobDescriptionArrayList.get(getAdapterPosition());
            jobItemCLickListener.onJobItemClicked(getAdapterPosition());
        }
    }

    public void setJobList(ArrayList<JobDescription> jobList) {
        jobDescriptionArrayList = jobList;
        notifyDataSetChanged();
    }
}
