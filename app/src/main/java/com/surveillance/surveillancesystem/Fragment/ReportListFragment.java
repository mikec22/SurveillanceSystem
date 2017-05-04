package com.surveillance.surveillancesystem.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.surveillance.surveillancesystem.Adapter.ReportListArrayAdapter;
import com.surveillance.surveillancesystem.R;
import com.surveillance.surveillancesystem.ReportRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportListFragment extends ListFragment {

    private ReportListArrayAdapter reportListArrayAdapter;
    private ArrayList<ReportRecord> reportRecords;

    public ReportListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        Toolbar mActionBarToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //mActionBarToolbar.setTitle("Report List");

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LoadReportListTask().execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String item = reportListArrayAdapter.getItem(position).toString();
        Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
    }

    private class LoadReportListTask extends AsyncTask<Void, Void, ArrayList<ReportRecord>> {
        @Override
        protected ArrayList<ReportRecord> doInBackground(Void... params) {
            try {
                URL url = new URL("http://104.199.242.151/360m/get_video_report_list.php");
                //String parameters = "type=video&filename=demo7.mp4";
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //connection.setRequestMethod("GET");
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                //request.write(parameters);
                request.flush();
                request.close();
                String line;
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("report_list");
                reportRecords = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ReportRecord reportRecord = new ReportRecord(jsonArray.getJSONObject(i));
//                    reportRecord.setFileName(jsonArray.getJSONObject(i).getString("fileName"));
//                    reportRecord.setAvgFace(jsonArray.getJSONObject(i).getInt("avgFace"));
//                    reportRecord.setDuration(jsonArray.getJSONObject(i).getInt("duration"));
//                    reportRecord.setFps(jsonArray.getJSONObject(i).getInt("fps"));
//                    reportRecord.setcDate(DateTools.StringToDate(jsonArray.getJSONObject(i).getString("cDate")));
//                    reportRecord.setmDate(DateTools.StringToDate(jsonArray.getJSONObject(i).getString("mDate")));
//                    reportRecord.setRecordDate(DateTools.StringToDate(jsonArray.getJSONObject(i).getString("recordDate")));
//                    Log.e("setRecordDate", reportRecord.getRecordDate().toString());
//                    //URL imageURL = new URL("http://104.199.242.151:5000/thumbs/CCTVTEST_th.jpg");
//                    URL imageURL = new URL("http://104.199.242.151:5000"
//                            + jsonArray.getJSONObject(i).getString("thumbsPath"));
//                    HttpURLConnection imageConnection = (HttpURLConnection) imageURL.openConnection();
//                    imageConnection.setConnectTimeout(2000);
//                    imageConnection.setReadTimeout(2000);
//                    //Log.e("URL", imageURL.toString());
//                    reportRecord.setThumbImage(BitmapFactory.decodeStream(imageConnection.getInputStream()));
                    reportRecords.add(reportRecord);
                }
                return reportRecords;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ReportRecord> reportRecords) {
            reportListArrayAdapter = new ReportListArrayAdapter(getActivity(),
                    android.R.layout.list_content, reportRecords);
            setListAdapter(reportListArrayAdapter);
        }
    }
}
