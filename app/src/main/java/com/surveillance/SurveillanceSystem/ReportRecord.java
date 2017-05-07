package com.surveillance.SurveillanceSystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.surveillance.SurveillanceSystem.Tools.DateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;


public class ReportRecord implements Serializable {

    private int id, recordBy, location, totalFace, avgFace, fps, width, height, duration;
    private String type, category, fileName, fileSize, rawPath, resultPath, thumbsPath;
    private transient Date recordDate, cDate, mDate;
    private transient Bitmap thumbImage;

    public ReportRecord() {

    }

    public ReportRecord(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            category = jsonObject.getString("category");
            recordBy = jsonObject.getInt("recordBy");
            totalFace = jsonObject.getInt("totalFace");
            avgFace = jsonObject.getInt("avgFace");
            duration = jsonObject.getInt("duration");
            fps = jsonObject.getInt("fps");
            width = jsonObject.getInt("width");
            height = jsonObject.getInt("height");
            type = jsonObject.getString("type");
            fileName = jsonObject.getString("fileName");
            fileSize = jsonObject.getString("fileSize");
            rawPath = jsonObject.getString("rawPath");
            resultPath = jsonObject.getString("resultPath");
            thumbsPath = jsonObject.getString("thumbsPath");
            recordDate = DateTools.StringToDate(jsonObject.getString("recordDate"));
            cDate = DateTools.StringToDate(jsonObject.getString("cDate"));
            mDate = DateTools.StringToDate(jsonObject.getString("mDate"));
            setThumbImageByURL();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

//    public ReportRecord(int id, String type, int recordBy, String fileName, Date recordDate,
//                        long duration, int location, int totalFace, int avgFace, String fileSize, int fps, int width, int height,
//                        String rawPath, String resultPath,String thumbsPath, Date cDate, Date mDate) {
//        this.id = id;
//        this.thumbsPath = thumbsPath;
//        this.recordBy = recordBy;
//        this.location = location;
//        this.totalFace = totalFace;
//        this.avgFace = avgFace;
//        this.type = type;
//        this.fileName = fileName;
//        this.fileSize = fileSize;
//        this.fps = fps;
//        this.width = width;
//        this.height = height;
//        this.rawPath = rawPath;
//        this.resultPath = resultPath;
//        this.duration = duration;
//        this.recordDate = recordDate;
//        this.cDate = cDate;
//        this.mDate = mDate;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordBy() {
        return recordBy;
    }

    public void setRecordBy(int recordBy) {
        this.recordBy = recordBy;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getTotalFace() {
        return totalFace;
    }

    public void setTotalFace(int totalFace) {
        this.totalFace = totalFace;
    }

    public int getAvgFace() {
        return avgFace;
    }

    public void setAvgFace(int avgFace) {
        this.avgFace = avgFace;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getRawPath() {
        return rawPath;
    }

    public void setRawPath(String rawPath) {
        this.rawPath = rawPath;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Date getcDate() {
        return cDate;
    }

    public void setcDate(Date cDate) {
        this.cDate = cDate;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getThumbsPath() {
        return thumbsPath;
    }

    public void setThumbsPath(String thumbsPath) {
        this.thumbsPath = thumbsPath;
    }

    public Bitmap getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(Bitmap thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setThumbImageByURL() {
        try {
            URL imageURL = new URL(Server.mediaPath + thumbsPath);
            HttpURLConnection imageConnection = (HttpURLConnection) imageURL.openConnection();
            //Log.e("URL", imageURL.toString());
            thumbImage = BitmapFactory.decodeStream(imageConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setThumbImageByURL(String url) {
        try {
            URL imageURL = new URL(url);
            HttpURLConnection imageConnection = (HttpURLConnection) imageURL.openConnection();
            //Log.e("URL", imageURL.toString());
            thumbImage = BitmapFactory.decodeStream(imageConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}