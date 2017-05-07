package com.surveillance.SurveillanceSystem.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SearchFaceAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public SearchFaceAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
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
                return "Face Similarity";
            case 1:
                return "Existing Photo";
            case 2:
                return "Existing Video";
            default:
                return "";
        }
    }
}
