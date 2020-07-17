package com.wmdd.errandz.hirerUpcomingJobList;

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

public class HirerUpcomingJobListAdapter extends RecyclerView.Adapter<HirerUpcomingJobListAdapter.ViewHolder> {

    private UpcomingJobItemCLickListener jobItemCLickListener;
    private ArrayList<Job> upcomingJobList;

    public HirerUpcomingJobListAdapter(UpcomingJobItemCLickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        upcomingJobList = new ArrayList<Job>();
    }

    public interface UpcomingJobItemCLickListener {
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
        holder.jobNameTextView.setText(upcomingJobList.get(position).getJobName());
        holder.jobCategoryTextView.setText(upcomingJobList.get(position).getJobCategoryName());
        holder.jobDateTextView.setText(upcomingJobList.get(position).getJobDate());
        holder.jobCategoryImageView.setImageResource(upcomingJobList.get(position).getJobCategoryImage());

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

    public void setUpcomingJobList(ArrayList<Job> jobList) {
        upcomingJobList = jobList;
        notifyDataSetChanged();
    }
}
