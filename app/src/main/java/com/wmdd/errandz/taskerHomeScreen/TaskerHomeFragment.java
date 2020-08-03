package com.wmdd.errandz.taskerHomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.taskerJobDescription.TaskerJobDescriptionActivity;

import java.util.ArrayList;

public class TaskerHomeFragment extends Fragment implements TaskerHomeUpcomingJobListAdapter.UpcomingJobItemCLickListener,
        UpcomingApprovedJobListAdapter.UpcomingApprovedJobItemCLickListener {

    private View rootView;

    private ImageView illustrationImageView;
    private TextView approvedJobsLabel;
    private TextView nearbyJobsLabel;
    private RecyclerView approvedJobsRecyclerView;
    private RecyclerView nearbyJobsRecyclerView;
    private FrameLayout progressBarLayout;

    private ArrayList<Job> approvedJobList;
    private ArrayList<Job> upcomingJobList;

    private TaskerHomeUpcomingJobListAdapter taskerHomeUpcomingJobListAdapter;
    private UpcomingApprovedJobListAdapter upcomingApprovedJobListAdapter;

    private TaskerHomeViewModel taskerHomeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_tasker_home, container, false);

        illustrationImageView = rootView.findViewById(R.id.illustration_image_view);
        approvedJobsLabel = rootView.findViewById(R.id.approved_jobs_label);
        nearbyJobsLabel = rootView.findViewById(R.id.nearby_jobs_label);
        approvedJobsRecyclerView = rootView.findViewById(R.id.approved_jobs_recycler_view);
        nearbyJobsRecyclerView = rootView.findViewById(R.id.nearby_jobs_recycler_view);
        progressBarLayout = rootView.findViewById(R.id.progress_bar_view);

        nearbyJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        taskerHomeUpcomingJobListAdapter = new TaskerHomeUpcomingJobListAdapter(this);
        nearbyJobsRecyclerView.setAdapter(taskerHomeUpcomingJobListAdapter);

        approvedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,
                false));
        upcomingApprovedJobListAdapter = new UpcomingApprovedJobListAdapter(this);
        approvedJobsRecyclerView.setAdapter(upcomingApprovedJobListAdapter);

        initializeViewModel();

        return rootView;
    }

    private void initializeViewModel() {
        taskerHomeViewModel = ViewModelProviders.of(this).get(TaskerHomeViewModel.class);
        taskerHomeViewModel.init();
        progressBarLayout.setVisibility(View.VISIBLE);

        taskerHomeViewModel.getApprovedJobList().observe(this, jobList -> {
            if (jobList.size() > 0) {
                approvedJobList = jobList;
                illustrationImageView.setVisibility(View.GONE);
                approvedJobsLabel.setVisibility(View.VISIBLE);
                approvedJobsRecyclerView.setVisibility(View.VISIBLE);
                upcomingApprovedJobListAdapter.setUpcomingJobList(jobList);
            }
        });

        taskerHomeViewModel.getUpcomingJobList().observe(this, jobList -> {
            upcomingJobList = jobList;
            if (approvedJobList == null || approvedJobList.size() == 0)
                illustrationImageView.setVisibility(View.VISIBLE);
            else {
                illustrationImageView.setVisibility(View.GONE);
            }
            nearbyJobsLabel.setVisibility(View.VISIBLE);
            nearbyJobsRecyclerView.setVisibility(View.VISIBLE);
            taskerHomeUpcomingJobListAdapter.setUpcomingJobList(jobList);
            progressBarLayout.setVisibility(View.GONE);

        });

        taskerHomeViewModel.getSavedResponse().observe(this, response -> {
            if (response.getStatus().equals("success")) {
                taskerHomeViewModel.makeClientHomeDataApiCall();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        taskerHomeViewModel.makeClientHomeDataApiCall();
    }

    @Override
    public void onJobItemClicked(int position) {
        Job job = upcomingJobList.get(position);
        Intent intent = new Intent(getContext(), TaskerJobDescriptionActivity.class);
        intent.putExtra("JOB_ID", job.getJobID());
        intent.putExtra("HIRER_ID", job.getHirerID());
        startActivity(intent);
    }

    @Override
    public void onSaveIconClicked(int position, boolean isSaved) {
        if (!isSaved)
            taskerHomeViewModel.saveJob(upcomingJobList.get(position));
        else
            taskerHomeViewModel.unsaveJob(upcomingJobList.get(position));
        progressBarLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onApprovedJobItemClicked(int position) {
        Job job = approvedJobList.get(position);
        Intent intent = new Intent(getContext(), TaskerJobDescriptionActivity.class);
        intent.putExtra("JOB_ID", job.getJobID());
        intent.putExtra("HIRER_ID", job.getHirerID());
        startActivity(intent);
    }
}
