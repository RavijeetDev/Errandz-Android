package com.wmdd.errandz.taskerJobHistory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.hirerJobHistoryList.HirerJobHistoryListViewModel;
import com.wmdd.errandz.hirerPastJobDescription.HirerPastJobDescriptionActivity;
import com.wmdd.errandz.taskerHistorJobInfo.TaskerHistoryJobInfoActivity;
import com.wmdd.errandz.util.Constants;

import java.util.ArrayList;

public class TaskerJobHistoryListFragment extends Fragment implements TaskerJobHistoryListAdapter.JobHistoryItemCLickListener{

    private View rootView;
    private RecyclerView jobHistoryRecyclerView;
    private TaskerJobHistoryListAdapter taskerJobHistoryListAdapter;
    private ArrayList<Job> jobArrayList;

    private TaskerJobHistoryListViewModel taskerJobHistoryListViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_tasker_job_history_list, container, false);

        jobHistoryRecyclerView = rootView.findViewById(R.id.upcoming_job_recycler_view);

        jobHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));

        taskerJobHistoryListAdapter = new TaskerJobHistoryListAdapter(this);
        jobHistoryRecyclerView.setAdapter(taskerJobHistoryListAdapter);

        initializeViewModel();

        return rootView;
    }

    private void initializeViewModel() {
        taskerJobHistoryListViewModel = ViewModelProviders.of(this).get(TaskerJobHistoryListViewModel.class);
        taskerJobHistoryListViewModel.init();

        taskerJobHistoryListViewModel.getJobArrayList().observe(this, jobs -> {
            this.jobArrayList = jobs;
            taskerJobHistoryListAdapter.setJobHistoryList(jobs);
        });

    }

    @Override
    public void onJobItemClicked(int position) {
        Intent intent = new Intent(getActivity(), TaskerHistoryJobInfoActivity.class);
        intent.putExtra("JOB_ID", jobArrayList.get(position).getJobID());
        intent.putExtra("HIRER_ID", jobArrayList.get(position).getHirerID());
//        intent.putExtra(Constants.FROM_ACTIVITY, HIRER_HISTORY_LIST);
        startActivity(intent);
    }
}