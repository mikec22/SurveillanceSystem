package com.surveillance.surveillancesystem.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.surveillance.surveillancesystem.Fragment.LineChartReportFragment;
import com.surveillance.surveillancesystem.Fragment.VideoDetailsFragment;

import java.util.ArrayList;


public class ReportContentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public ReportContentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new ArrayList<>();
        fragments.add(new LineChartReportFragment());
        fragments.add(new VideoDetailsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
