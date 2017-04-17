package com.surveillance.surveillancesystem.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.surveillance.surveillancesystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartFragment extends Fragment {

    private BarChart barChart;

    public BarChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        barChart = (BarChart) view.findViewById(R.id.barChart);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        List<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            values.add(new BarEntry(i, (float) (Math.random() * 50)));
        }

        List<IBarDataSet> dataSets = new ArrayList<>();

        BarDataSet dataSet = new BarDataSet(values, "This is label");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        barChart.setData(data);
        return view;
    }

}
