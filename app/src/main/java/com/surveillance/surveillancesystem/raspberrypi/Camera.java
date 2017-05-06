package com.surveillance.surveillancesystem.raspberrypi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Xuan on 13/2/2017.
 */

public class Camera {

    /**
     * The Camera status
     **/
    public static final String CAMERA_STATUS_READY = "ready";
    public static final String CAMERA_STATUS_RECORDING = "video";
    public static final String CAMERA_STATUS_CAPTURE = "image";
    public static final String CAMERA_STATUS_HALTED = "halted";
    public static final String CAMERA_STATUS_CONNECTION_ERROR = "Connection Error";

    /**
     * The Camera IP address or hostname
     **/
    private String host;
    private boolean isPowerOn;

    public Camera() {
        this.host = "http://dev16.asuscomm.com";
    }

    public Camera(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStatus() {
        String status = CAMERA_STATUS_CONNECTION_ERROR;
        try {
            URL url = new URL(host + "/picam/status.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            status = new JSONObject(buffer.toString()).getString("status");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

//    public boolean isReadyToPreview() {
//        return getStatus().equals(CAMERA_STATUS_READY) || getStatus().equals(CAMERA_STATUS_RECORDING);
//    }

    public Bitmap getPreviewImage() {
        try {
//            if (getStatus().equals(CAMERA_STATUS_READY)
//                    || getStatus().equals(CAMERA_STATUS_RECORDING)) {
                URL url = new URL(host + "/picam/cam_pic.php?pDelay=40000");
//                URL url = new URL(host + "/stream/1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setConnectTimeout(5000);
//                connection.setReadTimeout(5000);
                return BitmapFactory.decodeStream(connection.getInputStream());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capturePhoto() {
        boolean success = false;
        try {
            if (getStatus().equals(CAMERA_STATUS_READY)
                    || getStatus().equals(CAMERA_STATUS_RECORDING)) {
                URL url = new URL(host + "/picam/cmd_pipe.php?cmd=im");
                url.openStream();

                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return success;
    }

    public boolean startCamera() {
        try {
            if (getStatus().equals(CAMERA_STATUS_HALTED)) {
                URL url = new URL(host + "/picam/cmd_pipe.php?cmd=ru 1");
                url.openStream();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getStatus().equals(CAMERA_STATUS_READY);
    }

    public boolean stopCamera() {
        try {
            if (getStatus().equals(CAMERA_STATUS_READY)) {
                URL url = new URL(host + "/picam/cmd_pipe.php?cmd=ru 0");
                url.openStream();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getStatus().equals(CAMERA_STATUS_HALTED);
    }

    public boolean isPowerOn() {
        return isPowerOn;
    }

    public void setPowerOnState() {
        isPowerOn = !getStatus().equals(CAMERA_STATUS_HALTED);
    }

    public boolean startRecordVideo() {
        boolean success = false;
        try {
            if (getStatus().equals(CAMERA_STATUS_READY)) {
                URL url = new URL(host + "/picam/cmd_pipe.php?cmd=ca 1");
                url.openStream();
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean stopRecordVideo() {
        boolean success = false;
        try {
            if (getStatus().equals(CAMERA_STATUS_RECORDING)) {
                URL url = new URL(host + "/picam/cmd_pipe.php?cmd=ca 0");
                url.openStream();
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

}


