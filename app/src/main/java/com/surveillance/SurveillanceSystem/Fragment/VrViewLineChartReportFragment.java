//package com.surveillance.surveillancesystem.Fragment;
//
//
//import android.graphics.Color;
//import android.graphics.DashPathEffect;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//import com.surveillance.surveillancesystem.R;
//import com.google.vr.sdk.widgets.video.VrVideoView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class VrViewLineChartReportFragment extends Fragment {
//
//    private VrVideoView videoView;
//    private LineChart lineChart;
//    private LoadVideo loadVideoTask;
//    private GetResultData getResultDataTask;
////    private SimpleExoPlayer player;
////    private RequestQueue mQueue;
////    private SimpleExoPlayerView simpleExoPlayerView;
//
//
//    public VrViewLineChartReportFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_line_chart_report2, container, false);
//        initView(view);
////        mQueue = Volley.newRequestQueue(getContext());
////        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
////                "http://104.199.242.151/360m/get_result.php?type=video&filename=demo7.mp4",
////                null, new Response.Listener<JSONObject>() {
////            @Override
////            public void onResponse(JSONObject response) {
////                Log.e("JSON", response.toString());
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Log.e("VolleyError", error.getMessage());
////            }
////        });
////        mQueue.add(jsonObjectRequest);
//
////        ArrayList<Entry> values = new ArrayList<>();
////        for (int i = 0; i < 10; i++) {
////            float val = (float) (Math.random() * 50) + 3;
////            values.add(new Entry(i, val));
////        }
//        getResultDataTask = new GetResultData();
//        loadVideoTask = new LoadVideo();
//        loadVideoTask.execute();
//        getResultDataTask.execute();
//        return view;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        videoView.pauseVideo();
//    }
//
////        private void initPlayer(View root) {
////        simpleExoPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.exoPlayer);
////        Handler mainHandler = new Handler();
////        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
////        TrackSelection.Factory videoTrackSelectionFactory =
////                new AdaptiveTrackSelection.Factory(bandwidthMeter);
////        TrackSelector trackSelector =
////                new DefaultTrackSelector(videoTrackSelectionFactory);
////
////        LoadControl loadControl = new DefaultLoadControl();
////        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
////        simpleExoPlayerView.setPlayer(player);
////
////        //
////        String userAgent = Util.getUserAgent(getContext(), "yourApplicationName");
////        // Produces DataSource instances through which media data is loaded.
////        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
////        // Produces Extractor instances for parsing the media data.
////        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
////        // This is the MediaSource representing the media to be played.
////
////
////        Uri mp4VideoUri = Uri.fromFile(new File("//android_asset/sample.mp4"));
////
////
////        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
////                dataSourceFactory, extractorsFactory, null, null);
////        // Prepare the player with the source.
////        player.prepare(videoSource);
////        player.setPlayWhenReady(true);
////
////    }
//
//    private void initView(View view) {
//        videoView = (VrVideoView) view.findViewById(R.id.videoView);
//        lineChart = (LineChart) view.findViewById(R.id.lineChart);
//        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e("Vr onResume", "is Running");
//        getResultDataTask = new GetResultData();
//        loadVideoTask = new LoadVideo();
//        loadVideoTask.execute();
//        getResultDataTask.execute();
////        videoView.playVideo();
//    }
//
//    class LoadVideo extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Log.e("Vr doInBackground", "is Running");
//            try {
//                Uri uri = Uri.parse("http://104.199.242.151:5000/result/CCTVTEST.mp4");
//                VrVideoView.Options options = new VrVideoView.Options();
//                options.inputType = VrVideoView.Options.TYPE_MONO;
//                videoView.loadVideo(uri, options);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//    }
//
//    class GetResultData extends AsyncTask<Void, Void, JSONObject> {
//
//        @Override
//        protected JSONObject doInBackground(Void... params) {
//            try {
//                URL url = new URL("http://104.199.242.151/360m/get_result.php?" +
//                        "type=video&filename=cctvtest.mp4");
//                //String parameters = "type=video&filename=demo7.mp4";
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoOutput(true);
//                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                //connection.setRequestMethod("GET");
//                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
//                //request.write(parameters);
//                request.flush();
//                request.close();
//                String line;
//                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(isr);
//                StringBuilder stringBuilder = new StringBuilder();
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line + "\n");
//                }
//                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//                Log.e("AsyncTask JSON", jsonObject.toString());
//                return jsonObject;
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            try {
//                JSONArray jsonArray = jsonObject.getJSONArray("result_data");
//                ArrayList<Entry> values = new ArrayList<>();
//
//                for (int i = 0; i < jsonArray.length(); ++i) {
//                    JSONObject json = jsonArray.getJSONObject(i);
//                    values.add(new Entry(i, json.getInt("total_face")));
//                }
//
//                LineDataSet set1 = new LineDataSet(values, "Number of Face(s)");
//                // set the line to be drawn like this "- - - - - -"
//                set1.enableDashedLine(10f, 5f, 0f);
//                set1.enableDashedHighlightLine(10f, 5f, 0f);
//                set1.setColor(Color.rgb(95, 161, 228));
//                set1.setValueTextColor(Color.DKGRAY);
//                set1.setCircleColor(Color.rgb(95, 161, 228));
//                set1.setLineWidth(1f);
//                set1.setCircleRadius(3f);
//                set1.setDrawCircleHole(false);
//                set1.setValueTextSize(9f);
//                set1.setDrawFilled(true);
//                set1.setFormLineWidth(1f);
//                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//                set1.setFormSize(15.f);
//                set1.setFillColor(Color.rgb(95, 161, 228));
//
//                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//                dataSets.add(set1);
//                lineChart.setData(new LineData(dataSets));
//                //Renew the Line Chart
//                lineChart.invalidate();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//}
