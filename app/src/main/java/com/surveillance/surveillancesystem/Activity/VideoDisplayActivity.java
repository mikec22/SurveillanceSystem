package com.surveillance.surveillancesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoView.Options;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.surveillance.surveillancesystem.R;

import java.io.IOException;


public class VideoDisplayActivity extends Activity {

    /**
     * Configuration information for the video
     **/
    private Options videoOptions = new Options();

    private VideoLoaderTask videoLoaderTask;

    private VrVideoView vrVideoView;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        vrVideoView = (VrVideoView) findViewById(R.id.videoView);
        vrVideoView.setEventListener(new VrVideoEventListener());
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        videoLoaderTask = new VideoLoaderTask();
        videoLoaderTask.execute();


    }

    @Override
    protected void onPause() {
        super.onPause();
        vrVideoView.pauseRendering();
    }

    class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Options options = new Options();
                options.inputType = Options.TYPE_MONO;
                vrVideoView.loadVideoFromAsset("output.mp4", options);
                Toast.makeText(VideoDisplayActivity.this, "Loaded.", Toast.LENGTH_LONG).show();
            }catch (IOException e){
                Toast.makeText(VideoDisplayActivity.this, "Cannot Open file.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }
}
