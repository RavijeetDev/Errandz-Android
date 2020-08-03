package com.wmdd.errandz.hirerHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wmdd.errandz.bean.JobDescription;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.hirerJobHistoryList.HirerJobHistoryListActivity;
import com.wmdd.errandz.hirerPostJob.HirerPostJobActivity;
import com.wmdd.errandz.R;
import com.wmdd.errandz.hirerUpcomingJobList.HirerUpcomingJobListActivity;
import com.wmdd.errandz.jobRequestList.JobRequestListActivity;
import com.wmdd.errandz.jobRequestTaskerInfo.JobRequestUserInfoActivity;

import java.util.ArrayList;

public class HirerHomeFragment extends Fragment implements View.OnClickListener, HirerHomeJobRequestListAdapter.JobItemClickListener {

    private HirerHomeViewModel hirerHomeViewModel;

    private View rootView;

    private ConstraintLayout postFirstJobLayoutContainer;
    private ConstraintLayout jobListContainer;
    private CardView addJobButtonContainer;
    private FrameLayout progressBarLayout;
    private RecyclerView jobRequestListRecyclerView;
    private TextView seeMoreLink;

    private  CardView upcomingJobCardView;
    private CardView jobHistoryCardView;
    private TextView jobRequestLabel;

    private HirerHomeJobRequestListAdapter hirerHomeJobRequestListAdapter;

    private Callback callback;

    private ArrayList<JobDescription> jobDescriptionArrayList;



    public interface Callback {
        void onListFetched(boolean isThereAnyJobPosted);
    }

    public void newInstance(Callback callbackListener) {
        callback = callbackListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home_screen_client, container, false);

        postFirstJobLayoutContainer = rootView.findViewById(R.id.no_post_layout_container);
        addJobButtonContainer = rootView.findViewById(R.id.add_job_button_container);
        jobListContainer = rootView.findViewById(R.id.job_list_container);
        seeMoreLink = rootView.findViewById(R.id.see_more_job_request_link);
        progressBarLayout = rootView.findViewById(R.id.progress_bar_view);

        upcomingJobCardView = rootView.findViewById(R.id.upcoming_job_view_container);
        jobHistoryCardView = rootView.findViewById(R.id.job_history_view_container);
        jobRequestListRecyclerView = rootView.findViewById(R.id.worker_request_recycler_view);

        jobRequestLabel = rootView.findViewById(R.id.job_request_label);

        jobRequestListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));
        hirerHomeJobRequestListAdapter = new HirerHomeJobRequestListAdapter(this);
        jobRequestListRecyclerView.setAdapter(hirerHomeJobRequestListAdapter);

        progressBarLayout.setVisibility(View.VISIBLE);

        addJobButtonContainer.setOnClickListener(this);
        upcomingJobCardView.setOnClickListener(this);
        jobHistoryCardView.setOnClickListener(this);
        seeMoreLink.setOnClickListener(this);

        initializeViewModel();

        return rootView;
    }

    private void initializeViewModel() {
        hirerHomeViewModel = ViewModelProviders.of(this).get(HirerHomeViewModel.class);
        hirerHomeViewModel.init();


        hirerHomeViewModel.getNumberOfJobs().observe(this, numberOfJobs -> {

            progressBarLayout.setVisibility(View.GONE);

            if(numberOfJobs == 0) {
                postFirstJobLayoutContainer.setVisibility(View.VISIBLE);
                callback.onListFetched(false);
            } else {
                jobListContainer.setVisibility(View.VISIBLE);
                callback.onListFetched(true);
            }

        });

        hirerHomeViewModel.getJobArrayList().observe(this, jobs -> {
            if(jobs.size() > 0) {
                jobRequestLabel.setVisibility(View.VISIBLE);
                seeMoreLink.setVisibility(View.VISIBLE);
                jobRequestListRecyclerView.setVisibility(View.VISIBLE);

                jobDescriptionArrayList = jobs;
                hirerHomeJobRequestListAdapter.setJobList(jobs);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_job_button_container:

                Intent intent = new Intent(getContext(), HirerPostJobActivity.class);
                startActivity(intent);
                break;

            case R.id.upcoming_job_view_container:
                Intent upcomingJobIntent = new Intent(getContext(), HirerUpcomingJobListActivity.class);
                startActivity(upcomingJobIntent);
                break;

            case R.id.job_history_view_container:
                Intent jobHistoryIntent = new Intent(getContext(), HirerJobHistoryListActivity.class);
                startActivity(jobHistoryIntent);
                break;

            case R.id.see_more_job_request_link:
                Intent jobRequestListIntent = new Intent(getContext(), JobRequestListActivity.class);
                startActivity(jobRequestListIntent);
                break;
        }
    }

    @Override
    public void onJobItemClicked(int position) {
        Intent intent = new Intent(getActivity(), JobRequestUserInfoActivity.class);
        intent.putExtra("JOB_ID", jobDescriptionArrayList.get(position).getJob().getJobID());
        intent.putExtra("JOB_STATUS_ID", jobDescriptionArrayList.get(position).getJob().getJobStatusID());
        intent.putExtra("USER", jobDescriptionArrayList.get(position).getUser());
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        hirerHomeViewModel.makeHirerHomeDataApiCall();
        jobRequestLabel.setVisibility(View.GONE);
        seeMoreLink.setVisibility(View.GONE);
        jobRequestListRecyclerView.setVisibility(View.GONE);
    }

}
