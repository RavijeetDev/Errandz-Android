package com.wmdd.errandz.taskersJobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.wmdd.errandz.R;

public class TaskerJobsActivity extends Fragment {

    private TabLayout taskerJobListTabLayout;
    private ViewPager taskerJobListViewPager;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_tasker_jobs, container, false);

        taskerJobListTabLayout = rootView.findViewById(R.id.my_jobs_tabs);
        taskerJobListViewPager = rootView.findViewById(R.id.my_jobs_view_pager);

        taskerJobListTabLayout.setupWithViewPager(taskerJobListViewPager);
        taskerJobListViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        return rootView;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager fragmentManager;
        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragmentManager = fragmentManager;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if(position == 0) fragment = new TaskerSavedJobListFragment();
            else fragment = new TaskerAppliedJobListFragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Saved Jobs";
            else return "Applied Jobs";

        }
    }

}