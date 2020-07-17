package com.wmdd.errandz.taskersJobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeViewModel;
import com.wmdd.errandz.taskerJobDescription.TaskerJobDescriptionActivity;

import java.util.ArrayList;

public class TaskerAppliedJobListFragment extends Fragment implements TaskerJobListAdapter.JobListItemCLickListener {

    private View rootView;
    private FrameLayout progressBarLayout;
    private RecyclerView taskerJobListRecyclerView;
    private TaskerJobListAdapter taskerJobListAdapter;

    private TaskerAppliedJobListViewModel taskerAppliedJobListViewModel;
    private ArrayList<Job> jobArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_job_list, container, false);

        taskerJobListRecyclerView = rootView.findViewById(R.id.tasker_job_list_recycler_view);
        progressBarLayout = rootView.findViewById(R.id.progress_bar_view);

        taskerJobListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));

        taskerJobListAdapter = new TaskerJobListAdapter(this);
        taskerJobListRecyclerView.setAdapter(taskerJobListAdapter);
        initializeViewModel();
        return rootView;
    }

    private void initializeViewModel() {
        taskerAppliedJobListViewModel = ViewModelProviders.of(this).get(TaskerAppliedJobListViewModel.class);
        taskerAppliedJobListViewModel.init();
        progressBarLayout.setVisibility(View.VISIBLE);

        taskerAppliedJobListViewModel.getAppliedJobListMutableLiveData().observe(this, jobList -> {
            jobArrayList = jobList;
            progressBarLayout.setVisibility(View.GONE);
            taskerJobListAdapter.setJobArrayList(jobList);
        });

    }

    @Override
    public void onJobItemClicked(int position) {
        Intent intent = new Intent(getContext(), TaskerJobDescriptionActivity.class);
        intent.putExtra("JOB_ID", jobArrayList.get(position).getJobID());
        intent.putExtra("HIRER_ID", jobArrayList.get(position).getHirerID());
        startActivity(intent);
    }

//    @Override
//    public void onSaveIconClicked(int position, boolean isSaved) {
//
//    }
}
