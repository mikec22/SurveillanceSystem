package com.surveillance.surveillancesystem.Fragment;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.surveillance.surveillancesystem.R;
import com.surveillance.surveillancesystem.raspberrypi.Camera;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraControlFragment extends Fragment {

    private ImageView cameraPreview;
    private PreviewImageTask loadTask;
    private TextView statusTxt;
    private Switch cameraSwitch;
    private Camera camera;
    private Button captureBtn;
    private ToggleButton recordBtn;
    private FragmentActivity activity;


    public CameraControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_control, container, false);
        camera = new Camera();
        activity = getActivity();
        cameraPreview = (ImageView) view.findViewById(R.id.camera_preview);
        statusTxt = (TextView) view.findViewById(R.id.status_txt);
        cameraSwitch = (Switch) view.findViewById(R.id.camera_switch);
        recordBtn = (ToggleButton) view.findViewById(R.id.record_button);
        captureBtn = (Button) view.findViewById(R.id.capture_button);

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

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendCaptureRequestTask().execute();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



        loadTask = new PreviewImageTask();
        loadTask.execute();
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

        @Override
        protected Boolean doInBackground(Void... params) {
            return camera.capturePhoto();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(activity.getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
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
