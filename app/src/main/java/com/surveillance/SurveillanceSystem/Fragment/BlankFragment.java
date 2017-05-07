package com.surveillance.SurveillanceSystem.Fragment;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.surveillance.SurveillanceSystem.Activity.VideoLineChartReportActivity;
import com.surveillance.SurveillanceSystem.Adapter.ImageListArrayAdapter;
import com.surveillance.SurveillanceSystem.R;
import com.surveillance.SurveillanceSystem.SecondFace;
import com.surveillance.SurveillanceSystem.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends ListFragment {
    private JSONObject reportData;

    private LoadImageTask imageTask;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        imageTask.cancel(false);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_blank, container, false);
        Activity activity = getActivity();
        reportData = ((VideoLineChartReportActivity) activity).getReportData();
        imageTask = new LoadImageTask();
        imageTask.execute();
        return root;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
    }

    class LoadImageTask extends AsyncTask<Void, Void, List<SecondFace>> {

        @Override
        protected List<SecondFace> doInBackground(Void... params) {
            List<SecondFace> sfLists = new ArrayList<>();
            try {
                JSONArray jsonArray = reportData.getJSONArray("result_data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray faceimg = jsonArray.getJSONObject(i).getJSONArray("faceimg");
                    SecondFace sf = new SecondFace();
                    sf.setSeconds(jsonArray.getJSONObject(i).getInt("video_position"));
                    List<Bitmap> bitmaps = new ArrayList<>();
                    for (int j = 0; j < faceimg.length(); j++) {
                        URL url = new URL(Server.mediaPath +
                                faceimg.getJSONObject(j).getString("file_path"));
                        Log.e("image url", url.toString());
                        bitmaps.add(BitmapFactory.decodeStream(url.openStream()));
                    }
                    sf.setBitmaps(bitmaps);
                    sfLists.add(sf);
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return sfLists;
        }

        @Override
        protected void onPostExecute(List<SecondFace> list) {
            ImageListArrayAdapter adapter = new ImageListArrayAdapter(getActivity(),
                    android.R.layout.list_content, list);

            setListAdapter(adapter);
            Log.e("onPostExecute", "is Running");
//            reportListArrayAdapter = new ReportListArrayAdapter(getActivity(),
//                    android.R.layout.list_content, reportRecords);
//            setListAdapter(reportListArrayAdapter);
        }
    }
}
