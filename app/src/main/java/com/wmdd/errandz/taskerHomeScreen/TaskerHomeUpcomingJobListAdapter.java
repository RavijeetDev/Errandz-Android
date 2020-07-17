package com.wmdd.errandz.taskerHomeScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.util.Constants;

import java.util.ArrayList;

public class TaskerHomeUpcomingJobListAdapter extends RecyclerView.Adapter<TaskerHomeUpcomingJobListAdapter.ViewHolder> {

    private UpcomingJobItemCLickListener jobItemCLickListener;
    private ArrayList<Job> upcomingJobList;

    public TaskerHomeUpcomingJobListAdapter(UpcomingJobItemCLickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        upcomingJobList = new ArrayList<Job>();
    }

    public interface UpcomingJobItemCLickListener {
        void onJobItemClicked(int position);
        void onSaveIconClicked(int position, boolean isSaved);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasker_home_upcoming_job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.jobNameTextView.setText(upcomingJobList.get(position).getJobName());
        holder.jobDateTextView.setText(upcomingJobList.get(position).getJobDate());
        holder.jobDescriptionTextView.setText(upcomingJobList.get(position).getDescription());
        holder.jobCategoryImageView.setImageResource(upcomingJobList.get(position).getJobCategoryImage());

        if(upcomingJobList.get(position).getStatus() == 4) {
            holder.saveJobImageView.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.saveJobImageView.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return upcomingJobList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView jobNameTextView;
        private TextView jobDateTextView;
        private TextView jobDescriptionTextView;
        private ImageView jobCategoryImageView;
        private ImageView saveJobImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.job_name_textview);
            jobDateTextView = itemView.findViewById(R.id.job_date_textview);
            jobDescriptionTextView = itemView.findViewById(R.id.job_description_textview);
            jobCategoryImageView = itemView.findViewById(R.id.job_category_image_view);
            saveJobImageView = itemView.findViewById(R.id.save_job_image_view);

            itemView.setOnClickListener(this);
            saveJobImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            if(view.getId() == R.id.save_job_image_view){
                boolean isSaved = upcomingJobList.get(position).getStatus() == 4;
                jobItemCLickListener.onSaveIconClicked(position, isSaved);
            } else {
                jobItemCLickListener.onJobItemClicked(position);
            }
        }
    }

    public void setUpcomingJobList(ArrayList<Job> jobList) {
        upcomingJobList = jobList;
        notifyDataSetChanged();
    }
}
