package com.wmdd.errandz.taskerJobHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;


import java.util.ArrayList;

public class TaskerJobHistoryListAdapter extends RecyclerView.Adapter<TaskerJobHistoryListAdapter.ViewHolder>{

    private JobHistoryItemCLickListener jobItemCLickListener;
    private ArrayList<Job> jobHistoryList;

    public TaskerJobHistoryListAdapter(JobHistoryItemCLickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        jobHistoryList = new ArrayList<Job>();
    }

    public interface JobHistoryItemCLickListener {
        void onJobItemClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hirer_job_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.jobNameTextView.setText(jobHistoryList.get(position).getJobName());
        holder.jobCategoryTextView.setText(jobHistoryList.get(position).getJobCategoryName());
        holder.jobDateTextView.setText(jobHistoryList.get(position).getJobDate());
        holder.jobCategoryImageView.setImageResource(jobHistoryList.get(position).getJobCategoryImage());

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return jobHistoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView jobNameTextView;
        private TextView jobCategoryTextView;
        private TextView jobDateTextView;
        private ImageView jobCategoryImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.job_name_label);
            jobCategoryTextView = itemView.findViewById(R.id.job_category_label);
            jobDateTextView = itemView.findViewById(R.id.job_date_label);
            jobCategoryImageView = itemView.findViewById(R.id.job_category_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            jobItemCLickListener.onJobItemClicked(position);
        }
    }

    public void setJobHistoryList(ArrayList<Job> jobList) {
        jobHistoryList = jobList;
        notifyDataSetChanged();
    }
}
