package com.surveillance.SurveillanceSystem.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class ReportContentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public ReportContentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Face Detected";
            case 1:
                return "Line Chart Result";
            case 2:
                return "Video Details";
            default:
                return "";
        }
    }
}
