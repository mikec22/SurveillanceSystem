package com.surveillance.surveillancesystem.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.surveillance.surveillancesystem.ReportRecord;

import java.util.List;

/**
 * Created by Mike on 1/5/2017.
 */

public class ReportListArrayAdapter extends ArrayAdapter<ReportRecord> {

    public ReportListArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public ReportListArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ReportRecord> reportRecords) {
        super(context, resource, reportRecords);
    }
}
