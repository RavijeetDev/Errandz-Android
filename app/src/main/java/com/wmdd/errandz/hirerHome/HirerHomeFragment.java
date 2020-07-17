package com.wmdd.errandz.hirerHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wmdd.errandz.hirerJobHistoryList.HirerJobHistoryListActivity;
import com.wmdd.errandz.hirerPostJob.HirerPostJobActivity;
import com.wmdd.errandz.R;
import com.wmdd.errandz.hirerUpcomingJobList.HirerUpcomingJobListActivity;

public class HirerHomeFragment extends Fragment implements View.OnClickListener {

    private HirerHomeViewModel hirerHomeViewModel;

    private View rootView;

    private ConstraintLayout postFirstJobLayoutContainer;
    private ConstraintLayout jobListContainer;
    private CardView addJobButtonContainer;
    private FrameLayout progressBarLayout;

    private  CardView upcomingJobCardView;
    private CardView jobHistoryCardView;

    private Callback callback;

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
        progressBarLayout = rootView.findViewById(R.id.progress_bar_view);

        upcomingJobCardView = rootView.findViewById(R.id.upcoming_job_view_container);
        jobHistoryCardView = rootView.findViewById(R.id.job_history_view_container);

        progressBarLayout.setVisibility(View.VISIBLE);

        addJobButtonContainer.setOnClickListener(this);
        upcomingJobCardView.setOnClickListener(this);
        jobHistoryCardView.setOnClickListener(this);

        initializeViewModel();

        return rootView;
    }

    private void initializeViewModel() {
        hirerHomeViewModel = ViewModelProviders.of(this).get(HirerHomeViewModel.class);
        hirerHomeViewModel.init();

        hirerHomeViewModel.getHomeData().observe(this, homeData -> {

            progressBarLayout.setVisibility(View.GONE);

            if(homeData.getNumberOfJobs() == 0) {
                postFirstJobLayoutContainer.setVisibility(View.VISIBLE);
                callback.onListFetched(false);
            } else {
                jobListContainer.setVisibility(View.VISIBLE);
                callback.onListFetched(true);
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
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        hirerHomeViewModel.makeHirerHomeDataApiCall();
    }

}
