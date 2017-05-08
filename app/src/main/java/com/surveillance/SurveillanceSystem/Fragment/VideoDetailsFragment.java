package com.surveillance.SurveillanceSystem.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surveillance.SurveillanceSystem.Activity.VideoLineChartReportActivity;
import com.surveillance.SurveillanceSystem.R;
import com.surveillance.SurveillanceSystem.ReportRecord;
import com.surveillance.SurveillanceSystem.Tools.DateTools;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoDetailsFragment extends Fragment {

    private TextView tvFileName, tvRecordDate, tvFileSize, tvAvgFace, tvRecordBy, tvFps, tvWidth, tvHeight;
    private ReportRecord reportRecord;

    public VideoDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_video_details, container, false);
        Activity activity = getActivity();
        reportRecord = ((VideoLineChartReportActivity) activity).getReportRecord();
        tvFileName = (TextView) root.findViewById(R.id.tvFileName);
        tvRecordBy = (TextView) root.findViewById(R.id.tvRecordBy);
        tvRecordDate = (TextView) root.findViewById(R.id.tvRecordDate);
        tvFileSize = (TextView) root.findViewById(R.id.tvFileSize);
        tvAvgFace = (TextView) root.findViewById(R.id.tvAvgFace);
        tvFps = (TextView) root.findViewById(R.id.tvFps);
        tvWidth = (TextView) root.findViewById(R.id.tvWidth);
        tvHeight = (TextView) root.findViewById(R.id.tvHeight);
        tvFileName.setText("File Name: " + reportRecord.getFileName());
        tvRecordBy.setText("Record By: " + reportRecord.getRecordBy());
        tvRecordDate.setText("Record Date: " + DateTools.DateToString(reportRecord.getRecordDate()));
        tvFileSize.setText("File Size: " + reportRecord.getAvgFace());
        tvAvgFace.setText("Avg Face" + reportRecord.getAvgFace());
        tvFps.setText("FPS: " + reportRecord.getFps());
        tvWidth.setText("Width: " + reportRecord.getWidth());
        tvHeight.setText("Height: " + reportRecord.getHeight());
        return root;
    }

}
