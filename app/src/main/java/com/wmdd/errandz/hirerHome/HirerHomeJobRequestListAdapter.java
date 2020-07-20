package com.wmdd.errandz.hirerHome;

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

public class HirerHomeJobRequestListAdapter extends RecyclerView.Adapter<HirerHomeJobRequestListAdapter.ViewHolder> {

    private JobItemClickListener jobItemCLickListener;
    private ArrayList<JobDescription> jobDescriptionArrayList;

    public HirerHomeJobRequestListAdapter(JobItemClickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        jobDescriptionArrayList = new ArrayList<>();
    }

    public interface JobItemClickListener {
        void onJobItemClicked(User user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hirer_job_request_list_item, parent, false);
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
        private Button jobRequestButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskerProfileImageView = itemView.findViewById(R.id.profile_image_view);
            taskerNameTextView = itemView.findViewById(R.id.tasker_name_text_view);
            jobNameTextView = itemView.findViewById(R.id.job_name_text_view);
            taskerRatingBar = itemView.findViewById(R.id.tasker_rating_bar);
            taskerRatingTextView = itemView.findViewById(R.id.tasker_rating_text_view);
            jobRequestButton = itemView.findViewById(R.id.job_request_button);

            jobRequestButton.setOnClickListener(this);
        }

        public void bind() {
            JobDescription jobDescription = jobDescriptionArrayList.get(getAdapterPosition());
            taskerNameTextView.setText(jobDescription.getUser().getFirstName());
            jobNameTextView.setText(jobDescription.getJob().getJobName());

            Picasso.get()
                    .load(jobDescription.getUser().getProfileImage())
                    .into(taskerProfileImageView);

            if(jobDescription.getUser().getTotalRating() > 0) {
                taskerRatingBar.setVisibility(View.VISIBLE);
                taskerRatingBar.setRating(jobDescription.getUser().getTotalRating() /
                        jobDescription.getUser().getNumberOfReviews());
            } else {
                taskerRatingTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            JobDescription jobDescription = jobDescriptionArrayList.get(getAdapterPosition());
            jobItemCLickListener.onJobItemClicked(jobDescription.getUser());
        }
    }

    public void setJobList(ArrayList<JobDescription> jobList) {
        jobDescriptionArrayList = jobList;
        notifyDataSetChanged();
    }
}
