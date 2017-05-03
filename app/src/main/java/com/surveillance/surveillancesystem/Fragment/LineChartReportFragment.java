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
import android.widget.VideoView;


import com.surveillance.surveillancesystem.R;


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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.surveillance.surveillancesystem.R.id.container;


/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartReportFragment extends Fragment {

    private VrVideoView videoView;
    private LineChart lineChart;
    private SimpleExoPlayer player;
    //

    private SimpleExoPlayerView simlePlayerView;


    public LineChartReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_line_chart_report, container, false);
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
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 50) + 3;
            values.add(new Entry(i, val));
        }
        LineDataSet set1;

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
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        LoadVideo task = new LoadVideo();
        task.execute();


        //
        initPlayer(view);
        return view;
    }

    private void initPlayer(View root) {
        simlePlayerView = (SimpleExoPlayerView) root.findViewById(R.id.exoPlayer);
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        simlePlayerView.setPlayer(player);

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

    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.playVideo();
    }

    class LoadVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            VrVideoView.Options options = new VrVideoView.Options();
            options.inputType = VrVideoView.Options.FORMAT_DEFAULT;
            try {
                videoView.loadVideoFromAsset("sample.mp4", options);
                videoView.playVideo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
