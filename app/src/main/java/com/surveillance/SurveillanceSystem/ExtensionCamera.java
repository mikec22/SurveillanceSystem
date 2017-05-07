package com.surveillance.SurveillanceSystem;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.surveillance.SurveillanceSystem.raspberrypi.Camera;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExtensionCamera extends Camera {
    @Override
    public Bitmap getPreviewImage() {
        try {
            URL url = new URL(super.getPreviewLink());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
