package com.surveillance.surveillancesystem.Fragment;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.surveillance.surveillancesystem.Activity.VideoLineChartReportActivity;
import com.surveillance.surveillancesystem.R;
import com.surveillance.surveillancesystem.ReportRecord;
import com.surveillance.surveillancesystem.Server;

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
public class LineChartFragment extends Fragment {

    private LineChart lineChart;
    private ReportRecord reportRecord;

    public LineChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_line_chart, container, false);
        lineChart = (LineChart) root.findViewById(R.id.lineChart);
        reportRecord = ((VideoLineChartReportActivity) getActivity()).getReportRecord();
        new GetResultDataTask().execute();
        return root;
 //       lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                Log.e("LineChart Value", "getX " + e.getX() + " XChartMax: " + lineChart.getXChartMax());
//                float percentage = (e.getX() / lineChart.getXChartMax());
//                Log.e("percentage", percentage + "");
////                player.seekTo((long) (player.getDuration() * percentage));
//                videoView.seekTo((long) (videoView.getDuration() * percentage));
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
    }

    class GetResultDataTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                URL url = new URL(Server.phpPath +
                        "/get_result.php?type=video&filename="+ reportRecord.getFileName());
                //String parameters = "type=video&filename=demo7.mp4";
                Log.e("ResultData url", url.toString());
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
                Log.e("AsyncTask JSON", jsonObject.toString());
                return jsonObject;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("result_data");
                ArrayList<Entry> values = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    values.add(new Entry(i, json.getInt("total_face")));
                }

                LineDataSet set1 = new LineDataSet(values, "Number of Face(s)");

                // set the line to be drawn like this "- - - - - -"
                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(Color.rgb(95, 161, 228));
                set1.setValueTextColor(Color.WHITE);
                set1.setCircleColor(Color.rgb(95, 161, 228));
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);
                set1.setFillColor(Color.rgb(95, 161, 228));

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                lineChart.setData(new LineData(dataSets));
                //Renew the Line Chart
                lineChart.invalidate();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
