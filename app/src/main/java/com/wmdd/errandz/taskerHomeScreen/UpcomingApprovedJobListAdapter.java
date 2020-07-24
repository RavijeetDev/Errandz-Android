package com.wmdd.errandz.taskerHomeScreen;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateUtils;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UpcomingApprovedJobListAdapter extends RecyclerView.Adapter<UpcomingApprovedJobListAdapter.ViewHolder> {

    private UpcomingApprovedJobItemCLickListener jobItemCLickListener;
    private ArrayList<Job> upcomingJobList;

    public UpcomingApprovedJobListAdapter(UpcomingApprovedJobItemCLickListener jobItemCLickListener) {
        this.jobItemCLickListener = jobItemCLickListener;
        upcomingJobList = new ArrayList<Job>();
    }

    public interface UpcomingApprovedJobItemCLickListener {
        void onApprovedJobItemClicked(int position);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasker_home_approved_job_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.jobNameTextView.setText(upcomingJobList.get(position).getJobName());
        holder.jobCategoryImageView.setImageResource(upcomingJobList.get(position).getJobCategoryImage());

        try {
            String jobDate = upcomingJobList.get(position).getJobDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);
            Date date = simpleDateFormat.parse(jobDate);
            if(DateUtils.isToday(date.getTime())) {
                holder.jobDateTextView.setText("Today\n" + upcomingJobList.get(position).getJobDate());
                holder.jobDateTextView.setTextColor(holder.itemView.getContext().getColor(R.color.yellow));
                holder.jobDateTextView.setTypeface(Typeface.DEFAULT_BOLD);
            } else {

                holder.jobDateTextView.setText(upcomingJobList.get(position).getJobDate());
                holder.jobDateTextView.setTextColor(Color.parseColor("#707070"));
                holder.jobDateTextView.setTypeface(Typeface.DEFAULT);
            }

        }catch (Exception e){

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
        private ImageView jobCategoryImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.job_name_textview);
            jobDateTextView = itemView.findViewById(R.id.job_date_textview);
            jobCategoryImageView = itemView.findViewById(R.id.job_category_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            jobItemCLickListener.onApprovedJobItemClicked(position);

        }
    }

    public void setUpcomingJobList(ArrayList<Job> jobList) {
        upcomingJobList = jobList;
        notifyDataSetChanged();
    }
}
