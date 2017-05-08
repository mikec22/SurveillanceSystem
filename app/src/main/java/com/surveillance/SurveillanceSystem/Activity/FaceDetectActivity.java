package com.surveillance.SurveillanceSystem.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.surveillance.SurveillanceSystem.Adapter.FaceImageListAdapter;
import com.surveillance.SurveillanceSystem.R;
import com.surveillance.SurveillanceSystem.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FaceDetectActivity extends AppCompatActivity {
    private WebView wvPreView;
    private ListView lvFaceImg;
    private LineChart lineChart;
    private Switch openCVSwitch;
    private LinkedList<Entry> chartValues;
    private LinkedList<List<Bitmap>> imgLists;
    private LoadStatisticsTask mTask;
    private MyTimerTask timerTask;
    Timer timer = new Timer(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);
        wvPreView = (WebView) findViewById(R.id.wvPreview);
        lvFaceImg = (ListView) findViewById(R.id.lvFaceImage);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        openCVSwitch = (Switch) findViewById(R.id.openCV_switch);
        mTask = new LoadStatisticsTask();
        imgLists = new LinkedList<>();
        chartValues = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            chartValues.add(new Entry(i, 0));
        }
        timerTask = new MyTimerTask();
        timer.schedule(timerTask, 1000, 1000);
        wvPreView.setPadding(0, 0, 0, 0);
        wvPreView.getSettings().setLoadWithOverviewMode(true);
        wvPreView.getSettings().setUseWideViewPort(true);
        wvPreView.loadUrl(Server.mediaPath+"/stream/1");
    }

//    private Runnable r1 = new Runnable() {
//        public void run() {
//            jsObjRequest = new JsonObjectRequest
//                    (Request.Method.GET, Server.mediaPath + "/current/face/json/1", null,
//                            new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//
//                                    Log.e("json", response.toString());
//                                    try {
//                                        if (chartValues.size() > 10) {
//                                            for (Entry entry : chartValues) {
//                                                entry.setX(entry.getX() - 1);
//                                            }
//                                        }
//                                        if (!chartValues.isEmpty()) {
//                                            chartValues.add(new Entry(chartValues.getLast().getX() + 1,
//                                                    response.getInt("total_face")));
//                                        } else {
//                                            chartValues.add(new Entry(0, response.getInt("total_face")));
//                                        }
//                                        LineDataSet set1 = new LineDataSet(chartValues, "Number of Face(s)");
//
//                                        // set the line to be drawn like this "- - - - - -"
//                                        set1.enableDashedLine(10f, 5f, 0f);
//                                        set1.enableDashedHighlightLine(10f, 5f, 0f);
//                                        set1.setColor(Color.rgb(95, 161, 228));
//                                        set1.setValueTextColor(Color.WHITE);
//                                        set1.setCircleColor(Color.rgb(95, 161, 228));
//                                        set1.setLineWidth(1f);
//                                        set1.setCircleRadius(3f);
//                                        set1.setDrawCircleHole(false);
//                                        set1.setValueTextSize(9f);
//                                        set1.setDrawFilled(true);
//                                        set1.setFormLineWidth(1f);
//                                        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//                                        set1.setFormSize(15.f);
//                                        set1.setFillColor(Color.rgb(95, 161, 228));
//
//                                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//                                        dataSets.add(set1);
//                                        lineChart.setData(new LineData(dataSets));
//                                        //Renew the Line Chart
//                                        lineChart.invalidate();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // TODO Auto-generated method stub
//
//                        }
//                    });
//            mUI_Handler.post(r2);
//        }
//    };
//
//    private Runnable r2 = new Runnable() {
//
//        public void run() {
//            mQueue.add(jsObjRequest);
//
//        }
//
//    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        timerTask.cancel();
        super.onDestroy();
    }

    private class MyTimerTask extends TimerTask {
        public void run() {
            mTask = new LoadStatisticsTask();
            mTask.execute();
        }
    }

    class LoadStatisticsTask extends AsyncTask<Void, Void, JSONObject> {
        private JSONObject jsonObject;

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                URL url = new URL(Server.phpPath + "/getFacesProxy.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //connection.setRequestMethod("GET");
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                //request.write(parameters);
                request.flush();
                request.close();
                String line;
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                JSONObject jsonObject = new JSONObject(stringBuilder.toString().replace("'", ""));
                Log.e("AsyncTask JSON", jsonObject.toString());
                return jsonObject;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            //MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (chartValues.size() > 10) {
                    for (Entry entry : chartValues) {
                        entry.setX(entry.getX() - 1);
                    }
                    chartValues.removeFirst();
                }
                if (!chartValues.isEmpty()) {
                    chartValues.add(new Entry(chartValues.getLast().getX() + 1,
                            jsonObject.getInt("total_face")));
                } else {
                    chartValues.add(new Entry(0, jsonObject.getInt("total_face")));
                }
                LineDataSet set1 = new LineDataSet(chartValues, "Number of Face(s)");
                // set the line to be drawn like this "- - - - - -"
                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(Color.rgb(95, 161, 228));
                set1.setValueTextColor(Color.WHITE);
                set1.setCircleColor(Color.rgb(95, 161, 228));
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);
                set1.setFillColor(Color.rgb(95, 161, 228));

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                lineChart.setData(new LineData(dataSets));
                //Renew the Line Chart
                lineChart.invalidate();

                JSONArray jsonArray = jsonObject.getJSONArray("faceimg");
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    URL imgUrl = new URL(Server.phpPath +
                            jsonArray.getJSONObject(i).getString("file_path"));
                    bitmaps.add(BitmapFactory.decodeStream(imgUrl.openStream()));
                }
                if(imgLists.size()>4){
                    imgLists.removeFirst();
                }
                imgLists.add(bitmaps);
                FaceImageListAdapter adapter = new FaceImageListAdapter(getApplicationContext()
                        ,android.R.layout.list_content,imgLists);
                lvFaceImg.setAdapter(adapter);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
