package com.surveillance.SurveillanceSystem.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.surveillance.SurveillanceSystem.R;
import com.surveillance.SurveillanceSystem.raspberrypi.Camera;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

//    private Handler mThreadHandler;
//    private HandlerThread mThread;
    /**
     * View Object
     */
    private FrameLayout progressFrame1, progressFrame2, progressFrame3, progressFrame4;
    private ProgressBar loadImageProgress1, loadImageProgress2, loadImageProgress3, loadImageProgress4;
    private ImageView cameraPreviewImage1, cameraPreviewImage2, cameraPreviewImage3, cameraPreviewImage4;
    private Camera camera1, camera2, camera3, camera4;

    private PreviewImageTask previewImageTask;

    //private boolean camera1isOn, isCamera2isOn, isCamera3isOn, isCamera4isOn;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        initView(root);
        camera1 = new Camera();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mThread = new HandlerThread("Loader");
//        mThread.start();
//        mThreadHandler = new Handler(mThread.getLooper());
//        mThreadHandler.post(KeepLoadPreviewImageThread1);
        previewImageTask = new PreviewImageTask(progressFrame1, loadImageProgress1,
                cameraPreviewImage1, camera1);
        previewImageTask.execute();
        Log.e("onResume", "is Running");
    }

    @Override
    public void onStop() {
        previewImageTask = null;
        super.onStop();
    }

    @Override
    public void onPause() {
        previewImageTask.cancel(false);
        super.onPause();
//        if (mThreadHandler != null) {
//            mThreadHandler.removeCallbacks(KeepLoadPreviewImageThread1);
//        }
//        if (mThread != null) {
//            mThread.quit();
//        }
    }

    @Override
    public void onDestroy() {
        previewImageTask = null;
        super.onDestroy();
    }

    private void initView(View root) {
        progressFrame1 = (FrameLayout) root.findViewById(R.id.progress_frame1);
        progressFrame2 = (FrameLayout) root.findViewById(R.id.progress_frame2);
        progressFrame3 = (FrameLayout) root.findViewById(R.id.progress_frame3);
        progressFrame4 = (FrameLayout) root.findViewById(R.id.progress_frame4);
        loadImageProgress1 = (ProgressBar) root.findViewById(R.id.load_image_progress1);
        loadImageProgress2 = (ProgressBar) root.findViewById(R.id.load_image_progress2);
        loadImageProgress3 = (ProgressBar) root.findViewById(R.id.load_image_progress3);
        loadImageProgress4 = (ProgressBar) root.findViewById(R.id.load_image_progress4);
        cameraPreviewImage1 = (ImageView) root.findViewById(R.id.camera_preview_image1);
        cameraPreviewImage2 = (ImageView) root.findViewById(R.id.camera_preview_image2);
        cameraPreviewImage3 = (ImageView) root.findViewById(R.id.camera_preview_image3);
        cameraPreviewImage4 = (ImageView) root.findViewById(R.id.camera_preview_image4);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showImageProgress(FrameLayout progressFrame, final ProgressBar progressBar,
                                   final ImageView cameraPreviewImage, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            //int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            cameraPreviewImage.setVisibility(show ? View.GONE : View.VISIBLE);
            cameraPreviewImage.animate().setDuration(200).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cameraPreviewImage.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressFrame.setVisibility(show ? View.VISIBLE : View.GONE);
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
            progressFrame.setVisibility(show ? View.VISIBLE : View.GONE);
            cameraPreviewImage.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

//    class InitCameraTask extends AsyncTask<Void, Void, Void> {
//        private final Camera camera;
//        public InitCameraTask() {
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            return null;
//        }
//    }

    class PreviewImageTask extends AsyncTask<Void, Void, Bitmap> {

        private final FrameLayout progressFrame;
        private final ProgressBar progressBar;
        private final ImageView cameraPreviewImage;
        private final Camera camera;

        PreviewImageTask(FrameLayout progressFrame, ProgressBar progressBar,
                         ImageView cameraPreviewImage, Camera camera) {
            super();
            this.progressFrame = progressFrame;
            this.progressBar = progressBar;
            this.cameraPreviewImage = cameraPreviewImage;
            this.camera = camera;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showImageProgress(progressFrame, progressBar, cameraPreviewImage, true);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Log.e("doInBackground", "is Running");
//            if (camera.getStatus().equals(Camera.CAMERA_STATUS_CONNECTION_ERROR)) {
//                this.cancel(true);
//                return null;
//            }
            //Log.e("Camera status", camera.getStatus());
            return camera.getPreviewImage();
//            URL url = null;
//            try {
//                url = new URL("http://fyp.bu5hit.xyz:5000/stream/1");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                return BitmapFactory.decodeStream(connection.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("onPostExecute", "is Running");
            showImageProgress(progressFrame, progressBar, cameraPreviewImage, false);
            cameraPreviewImage.setImageBitmap(bitmap);
//            previewImageTask = new PreviewImageTask(progressFrame, progressBar,
//                    cameraPreviewImage, camera);
//            previewImageTask.execute();
        }

        @Override
        protected void onCancelled() {
            Log.e("onCancelled()", "isRunning");
            showImageProgress(progressFrame, progressBar, cameraPreviewImage, true);
//            previewImageTask = new PreviewImageTask(progressFrame, progressBar,
//                    cameraPreviewImage, camera);
//            previewImageTask.execute();
        }
    }

//    class GetCameraPowerOnTask extends AsyncTask<Void, Void, Boolean> {
//        private final Camera camera;
//
//        public GetCameraPowerOnTask(Camera camera) {
//            super();
//            this.camera = camera;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            camera.getPowerOnState();
//            return camera.isPowerOn();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean isPowerOn) {
//
//        }
//    }

//    private Runnable KeepLoadPreviewImageThread1 = new Runnable() {
//        @Override
//        public void run() {
//            GetCameraPowerOnTask getCameraPowerOnTask = new GetCameraPowerOnTask(camera1);
//            getCameraPowerOnTask.execute();
//            if (camera1.isPowerOn()) {
//                PreviewImageTask previewImageTask = new PreviewImageTask(progressFrame1, loadImageProgress1,
//                        cameraPreviewImage1, camera1);
//                previewImageTask.execute();
//            }
//        }
//    };
}
