package com.surveillance.SurveillanceSystem.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.surveillance.SurveillanceSystem.Adapter.ReportContentPagerAdapter;
import com.surveillance.SurveillanceSystem.Fragment.BlankFragment;
import com.surveillance.SurveillanceSystem.Fragment.LineChartFragment;
import com.surveillance.SurveillanceSystem.Fragment.VideoDetailsFragment;
import com.surveillance.SurveillanceSystem.R;
import com.surveillance.SurveillanceSystem.ReportRecord;
import com.surveillance.SurveillanceSystem.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VideoLineChartReportActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ReportContentPagerAdapter adapter;
    private VrVideoView vrVideoView;
    private SimpleExoPlayerView videoView;
    private SimpleExoPlayer player;
    private Intent intent;
    private static ReportRecord reportRecord;
    private ProgressBar videoProgressBar, dataProgressBar;
    private ArrayList<Fragment> fragments;
    private LoadReportDataTask loadDataTask;
    private JSONObject reportData;
    //private static final int NUM_PAGES = 5;


    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public JSONObject getReportData() {
        return reportData;
    }

    private class VrEventListener extends VrVideoEventListener{
        @Override
        public void onNewFrame() {
            super.onNewFrame();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ActionBar actionBar = getActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_line_chart_report);
        intent = getIntent();
        reportRecord = (ReportRecord) intent.getSerializableExtra("ReportRecord");
        // Specify that tabs should be displayed in the action bar.
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
        vrVideoView = (VrVideoView) findViewById(R.id.vrVideoView);

        videoProgressBar = (ProgressBar) findViewById(R.id.videoProgressBar);
        dataProgressBar = (ProgressBar) findViewById(R.id.dataProgressBar);
        initPlayer();
        loadDataTask = new LoadReportDataTask();
        loadDataTask.execute();
        fragments = new ArrayList<>();
        fragments.add(new BlankFragment());
        fragments.add(new LineChartFragment());
        fragments.add(new VideoDetailsFragment());

        adapter = new ReportContentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private void initPlayer() {
        videoView = (SimpleExoPlayerView) findViewById(R.id.videoView);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player.setPlayWhenReady(true);
        videoView.setPlayer(player);
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

                Toast.makeText(getApplicationContext(), "seconds:" + player.getCurrentPosition(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
        //

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final ProgressBar progressBar, final View view, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            //int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            view.setVisibility(show ? View.GONE : View.VISIBLE);
            view.animate().setDuration(200).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(200).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static ReportRecord getReportRecord() {
        return reportRecord;
    }

    class LoadVRVideoTask extends AsyncTask<Void, Void, Uri> {

        @Override
        protected void onPostExecute(Uri uri) {
            showProgress(videoProgressBar, vrVideoView, false);
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            Log.e("Vr doInBackground", "is Running");
            try {
                Uri videoUri = Uri.parse(Server.mediaPath + reportRecord.getResultPath());
                VrVideoView.Options options = new VrVideoView.Options();
                options.inputType = VrVideoView.Options.TYPE_MONO;
                vrVideoView.loadVideo(videoUri, options);
                return videoUri;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    class LoadVideoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Uri videoUri = Uri.parse(Server.mediaPath + reportRecord.getResultPath());
            String userAgent = Util.getUserAgent(getApplicationContext(), "yourApplicationName");
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), userAgent);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            player.prepare(videoSource);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showProgress(videoProgressBar, videoView, false);
        }
    }

    class LoadReportDataTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                URL url = new URL(Server.phpPath +
                        "/get_result.php?type=video&filename=" + reportRecord.getFileName());
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
            reportData = jsonObject;
            showProgress(dataProgressBar, viewPager, false);
            Log.e("report record type", reportRecord.getType());
            if (reportRecord.getType().equals("general")) {
                new LoadVideoTask().execute();
            } else if (reportRecord.getType().equals("panorama")) {
                new LoadVRVideoTask().execute();
            }

        }
    }

}
