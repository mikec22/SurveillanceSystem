package com.surveillance.surveillancesystem.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.surveillance.surveillancesystem.R;
import com.surveillance.surveillancesystem.raspberrypi.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CameraControlActivity extends MainActivity {
    private static final String TAG = CameraControlActivity.class.getSimpleName();

    private ImageView cameraPreview;
    private PreviewImageTask loadTask;
    private TextView statusTxt;
    private Switch cameraSwitch;
    private Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_control);
        cameraPreview = (ImageView) findViewById(R.id.camera_preview);
        statusTxt = (TextView) findViewById(R.id.status_txt);
        cameraSwitch = (Switch) findViewById(R.id.camera_switch);
        camera = new Camera();
        loadTask = new PreviewImageTask();
        loadTask.execute();
        new GetCameraStatusTask().execute();
    }

    public void capturePhoto(View view) {
        new SendCaptureRequestTask().execute();
    }

    public void recordVideo(View view) {

    }


    class PreviewImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            return camera.getPreviewImage();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            cameraPreview.setImageBitmap(bitmap);
            new PreviewImageTask().execute();
        }
    }

    class SendCaptureRequestTask extends AsyncTask<Void, String, Boolean> {
//        public static final String REQUEST_METHOD = "GET";
//        public static final int READ_TIMEOUT = 15000;
//        public static final int CONNECTION_TIMEOUT = 15000;


        @Override
        protected Boolean doInBackground(Void... params) {
            return camera.capturePhoto();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        }
    }

    class SendRecordVideoRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }

    class GetCameraStatusTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return camera.getStatus();
        }

        @Override
        protected void onPostExecute(String status) {
            statusTxt.setText("Status: " + status);
            new GetCameraStatusTask().execute();
        }
    }
}
