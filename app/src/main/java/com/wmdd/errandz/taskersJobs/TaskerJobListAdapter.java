package com.wmdd.errandz.taskersJobs;

import android.graphics.Color;
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

public class TaskerJobListAdapter extends RecyclerView.Adapter<TaskerJobListAdapter.ViewHolder> {

    private JobListItemCLickListener jobItemCLickListener;
    private ArrayList<Job> jobArrayList;

    public TaskerJobListAdapter(JobListItemCLickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        jobArrayList = new ArrayList<Job>();
    }

    public interface JobListItemCLickListener {
        void onJobItemClicked(int position);
//        void onSaveIconClicked(int position, boolean isSaved);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasker_job_item , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.jobNameTextView.setText(jobArrayList.get(position).getJobName());
        holder.jobDateTextView.setText(jobArrayList.get(position).getJobDate());
        holder.jobDescriptionTextView.setText(jobArrayList.get(position).getDescription());
        holder.jobCategoryImageView.setImageResource(jobArrayList.get(position).getJobCategoryImage());

        switch (jobArrayList.get(position).getStatus()) {
            case 1:
                holder.jobStatusTextView.setText("Waiting");
                holder.jobStatusTextView.setTextColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
                break;
            case 2:
                holder.jobStatusTextView.setText("Approved");
                holder.jobStatusTextView.setTextColor(holder.itemView.getContext().getColor(R.color.yellow));
                break;
            case 3:
                holder.jobStatusTextView.setText("Rejected");
                holder.jobStatusTextView.setTextColor(Color.RED);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return jobArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView jobNameTextView;
        private TextView jobDateTextView;
        private TextView jobDescriptionTextView;
        private ImageView jobCategoryImageView;
        private TextView jobStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.job_name_textview);
            jobDateTextView = itemView.findViewById(R.id.job_date_textview);
            jobDescriptionTextView = itemView.findViewById(R.id.job_description_textview);
            jobCategoryImageView = itemView.findViewById(R.id.job_category_image_view);
            jobStatusTextView = itemView.findViewById(R.id.job_status_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            if(view.getId() == R.id.save_job_image_view){
                boolean isSaved = jobArrayList.get(position).getStatus() == 4;
//                jobItemCLickListener.onSaveIconClicked(position, isSaved);
            } else {
                jobItemCLickListener.onJobItemClicked(position);
            }
        }
    }

    public void setJobArrayList(ArrayList<Job> jobList) {
        jobArrayList = jobList;
        notifyDataSetChanged();
    }
}
