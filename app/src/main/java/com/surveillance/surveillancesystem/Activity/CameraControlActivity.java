package com.surveillance.surveillancesystem.Activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.surveillance.surveillancesystem.R;
import com.surveillance.surveillancesystem.raspberrypi.Camera;

import 	android.os.Handler;
import android.widget.ToggleButton;

public class CameraControlActivity extends AppCompatActivity {
    private static final String TAG = CameraControlActivity.class.getSimpleName();

    private ImageView cameraPreview;
    private PreviewImageTask loadTask;
    private TextView statusTxt;
    private Switch cameraSwitch;
    private Camera camera;
    private ToggleButton recordBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_control);
        cameraPreview = (ImageView) findViewById(R.id.camera_preview);
        statusTxt = (TextView) findViewById(R.id.status_txt);
        cameraSwitch = (Switch) findViewById(R.id.camera_switch);
        recordBtn = (ToggleButton) findViewById(R.id.record_button);

        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    new SendTurnOffRequestTask().execute();
                } else{
                    new SendTurnOnRequestTask().execute();
                }
            }
        });

        recordBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new SendStartRecordVideoRequestTask().execute();
                } else{
                    new SendStopRecordVideoRequestTask().execute();
                }
            }
        });
        camera = new Camera();
        loadTask = new PreviewImageTask();
        loadTask.execute();
        new GetCameraStatusTask().execute();
    }

    public void capturePhoto(View view) {
        new SendCaptureRequestTask().execute();
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

    class SendStartRecordVideoRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return camera.startRecordVideo();
        }

    }

    class SendStopRecordVideoRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return camera.stopRecordVideo();
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
            cameraSwitch.setChecked(!status.equals(Camera.CAMERA_STATUS_HALTED));
            recordBtn.setChecked(status.equals(Camera.CAMERA_STATUS_RECORDING));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   new GetCameraStatusTask().execute();
                }
            }, 3000);
        }
    }

    class SendTurnOffRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return camera.stopCamera();
        }

//        @Override
//        protected void onPostExecute(Boolean success) {
//            if (!success) {
//                //cameraSwitch.setChecked(true);
//                Toast.makeText(getApplicationContext(),
//                        "Cannot turn off, please try again!", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    class SendTurnOnRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return camera.startCamera();
        }

//        @Override
//        protected void onPostExecute(Boolean success) {
//            if (!success) {
//                //cameraSwitch.setChecked(false);
//                Toast.makeText(getApplicationContext(),
//                        "Cannot turn On, please try again!", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
