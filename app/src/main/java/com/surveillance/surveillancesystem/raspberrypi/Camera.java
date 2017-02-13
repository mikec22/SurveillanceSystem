package com.surveillance.surveillancesystem.raspberrypi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
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

    public Bitmap getPreviewImage() {
        try {
            if (getStatus().equals(CAMERA_STATUS_READY)
                    || getStatus().equals(CAMERA_STATUS_RECORDING)) {
                URL url = new URL(host + "/picam/cam_pic.php?pDelay=40000");
                return BitmapFactory.decodeStream(url.openStream());
            }
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
        boolean success = false;
        try {
            if (getStatus().equals(CAMERA_STATUS_HALTED)) {
                URL url = new URL(host + "/picam/cmd_pipe.php?cmd=rm 1");
                url.openStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
