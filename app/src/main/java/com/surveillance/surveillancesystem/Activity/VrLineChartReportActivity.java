package com.surveillance.surveillancesystem.Activity;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.vr.sdk.widgets.video.VrVideoView;
import com.surveillance.surveillancesystem.Adapter.ReportContentPagerAdapter;
import com.surveillance.surveillancesystem.R;

public class VrLineChartReportActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ReportContentPagerAdapter adapter;
    private VrVideoView vrVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ActionBar actionBar = getActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_line_chart_report);
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
        vrVideoView = (VrVideoView) findViewById(R.id.vrVideoView);
        adapter = new ReportContentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
