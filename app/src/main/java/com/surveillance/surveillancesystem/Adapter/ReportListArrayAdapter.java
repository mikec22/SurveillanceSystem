package com.surveillance.surveillancesystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.surveillance.surveillancesystem.R;
import com.surveillance.surveillancesystem.ReportRecord;
import com.surveillance.surveillancesystem.Tools.DateTools;

import java.util.List;


public class ReportListArrayAdapter extends ArrayAdapter<ReportRecord> {
    private Context context;
    private List<ReportRecord> reportRecords;

    public ReportListArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    public ReportListArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ReportRecord> reportRecords) {
        super(context, resource, reportRecords);
        this.context = context;
        this.reportRecords = reportRecords;
    }

    private class ViewHolder {
        ImageView thumbView;
        TextView tvVideoName, tvDuration, tvRecordDate;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ReportRecord reportRecord = reportRecords.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.report_list_row, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.thumbView = (ImageView) convertView.findViewById(R.id.thumbView);
            holder.tvVideoName = (TextView) convertView.findViewById(R.id.tvVideoName);
            holder.tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
            holder.tvRecordDate = (TextView) convertView.findViewById(R.id.tvRecordDate);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvVideoName.setText("File Name: " + reportRecord.getFileName());
        int duration = reportRecord.getDuration();
        Log.e("duration", duration+"");
        int mins = duration / 60;
        int sec = duration % 60;
        holder.tvDuration.setText("Duration: " + mins + "m" + sec + "s");
        holder.tvRecordDate.setText("Record Date: " + DateTools.DateToString(reportRecord.getRecordDate()));
        holder.thumbView.setImageBitmap(reportRecord.getThumbImage());
        return convertView;
    }

    public List<ReportRecord> getReportRecords() {
        return reportRecords;
    }
}
