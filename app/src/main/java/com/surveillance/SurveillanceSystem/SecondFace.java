package com.surveillance.SurveillanceSystem;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;


public class SecondFace implements Serializable {
    private List<Bitmap> bitmaps;
    private int seconds;

    public SecondFace() {

    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void addBitmaps(Bitmap bitmap){
        bitmaps.add(bitmap);
    }
}
