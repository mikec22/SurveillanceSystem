package com.surveillance.surveillancesystem.Fragment;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.surveillance.surveillancesystem.R;
import com.google.vr.sdk.widgets.video.VrVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartReportFragment extends Fragment {

    private VrVideoView videoView;
    private LineChart lineChart;
    private SimpleExoPlayer player;
    //private RequestQueue mQueue;
    private SimpleExoPlayerView simpleExoPlayerView;
    private LineDataSet set1;

    public LineChartReportFragment() {
        // Required empty public constructor
        new GetResultData().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart_report, container, false);
        //mQueue = Volley.newRequestQueue(getContext());
        videoView = (VrVideoView) view.findViewById(R.id.videoView);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.e("LineChart Value", "getX " + e.getX() + "XChartMax: " + lineChart.getXChartMax());
                float percentage = (e.getX() / lineChart.getXChartMax());
                Log.e("percentage", percentage + "");
                player.seekTo((long) (player.getDuration() * percentage));
            }

            @Override
            public void onNothingSelected() {

            }
        });

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://104.199.242.151/360m/get_result.php?type=video&filename=demo7.mp4", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("JSON", response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VolleyError", error.getMessage());
//            }
//        });
//        mQueue.add(jsonObjectRequest);
//        ArrayList<Entry> values = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            float val = (float) (Math.random() * 50) + 3;
//            values.add(new Entry(i, val));
//        }
    new Load360Video().execute();




        initPlayer(view);
        return view;
    }

    private void initPlayer(View root) {
        simpleExoPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.exoPlayer);
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        simpleExoPlayerView.setPlayer(player);

        //
        String userAgent = Util.getUserAgent(getContext(), "yourApplicationName");
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.


        Uri mp4VideoUri = Uri.fromFile(new File("//android_asset/sample.mp4"));


        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    class Load360Video extends AsyncTask<Void, Void, Uri> {

        @Override
        protected Uri doInBackground(Void... voids) {
            Uri uri = Uri.parse("http://104.199.242.151/360m/media/demo7.mp4");
            return uri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            try {
                VrVideoView.Options options = new VrVideoView.Options();
                options.inputType = VrVideoView.Options.TYPE_MONO;
                videoView.loadVideo(uri, options);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class GetResultData extends AsyncTask<Void, Void, JSONObject> {

        LineData data;
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject = null;
            try {
                URL url = new URL("http://104.199.242.151/360m/get_result.php?type=video&filename=demo7.mp4");
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                jsonObject = new JSONObject(buffer.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Log.e("jsonObject", jsonObject.toString());
            return jsonObject;
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
                set1 = new LineDataSet(values, "Number of Face(s)");
                // set the line to be drawn like this "- - - - - -"
                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(Color.BLACK);
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                set1.setFillColor(Color.BLACK);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                data = new LineData(dataSets);
                lineChart.setData(data);



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
