package com.surveillance.SurveillanceSystem.Fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.surveillance.SurveillanceSystem.Adapter.ReportContentPagerAdapter;
import com.surveillance.SurveillanceSystem.Adapter.SearchFaceAdapter;
import com.surveillance.SurveillanceSystem.FilePath;
import com.surveillance.SurveillanceSystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.surveillance.SurveillanceSystem.Server;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFaceFragment extends Fragment {

    private Button select_Photo_btn, upload_btn;
    private ImageView selectedPhoto;
    private static final int SELECT_PICTURE = 1;
    private String selectedFilePath;
    private String imageName;
    private Bitmap selectedBitmap, mIcon_val;
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private SearchFaceAdapter adapter;


    private ImageView uploadedPhoto, resultPhoto;
    private TextView resultText;

    public SearchFaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_search_face, container, false);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        select_Photo_btn = (Button) root.findViewById(R.id.select_photo_btn);
        upload_btn = (Button) root.findViewById(R.id.upload_photo_btn);
        selectedPhoto = (ImageView) root.findViewById(R.id.selectedPhoto);
        viewPager = (ViewPager) root.findViewById(R.id.pager);


        uploadedPhoto = (ImageView)root.findViewById(R.id.uploaded_photo);
        resultPhoto = (ImageView) root.findViewById(R.id.result_photo);
        resultText = (TextView) root.findViewById(R.id.result_text);
//        fragments = new ArrayList<>();
//        fragments.add(new SearchResultFragment());
////        fragments.add(new LineChartFragment());
//        fragments.add(new VideoDetailsFragment());
//
//        adapter = new SearchFaceAdapter(getActivity().getSupportFragmentManager(), fragments);
//        Log.d("Adapter :" , adapter.toString());
//        viewPager.setAdapter(new SearchFaceAdapter(getActivity().getSupportFragmentManager(), fragments));
//        viewPager.setCurrentItem(0);

        select_Photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchPhoto().execute();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                if (data == null) {
                    return;
                }
                Uri selectedImageUri = data.getData();
                selectedFilePath = FilePath.getPath(getActivity(), selectedImageUri);
                Log.d("Select Path : ", selectedFilePath);
                if(selectedFilePath != null && !selectedFilePath.equals("")){
//                    uploads.setText(selectedFilePath);
                    File imgFile = new File(selectedFilePath);
                    imageName = imgFile.getName();
                    if(imgFile.exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                        imageView.setImageBitmap(bitmap);
                    }
                }else{
                    Toast.makeText(getActivity(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
                try {
                    //Getting the Bitmap from Gallery
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    //Setting the Bitmap to ImageView
                    selectedPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }


    class SearchPhoto extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                URL url = new URL(Server.mediaPath +
                        "/search");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
        /* 允许Input、Output，不使用Cache */
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
        /* 设置传送的method=POST */
                con.setRequestMethod("POST");
        /* setRequestProperty */
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
        /* 设置DataOutputStream */
                DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; "
                        + "name=\"file\";filename=\"" + imageName + "\"" + end);
                ds.writeBytes(end);

        /* 取得文件的FileInputStream */
                FileInputStream fStream = new FileInputStream(new File(selectedFilePath));
        /* 设置每次写入1024bytes */
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int length = -1;
        /* 从文件读取数据至缓冲区 */
                while ((length = fStream.read(buffer)) != -1) {
            /* 将资料写入DataOutputStream中 */
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);

                // -----
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data;name=\"name\"" + end);
                ds.writeBytes(end + URLEncoder.encode("xiexiezhichi", "UTF-8")
                        + end);
                // -----

                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
                fStream.close();
                ds.flush();

        /* 取得Response内容 */
                InputStream is = con.getInputStream();
                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                JSONObject jsonObject = new JSONObject(String.valueOf(b));
//            Log.d("Json get:", String.valueOf(b));
                Log.d("Json get:", String.valueOf(jsonObject));
                byte[] decodedString = Base64.decode(jsonObject.getString("upload_face_base64"), Base64.DEFAULT);
                Log.d("Get String :" , jsonObject.getString("upload_face_base64"));
                selectedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                selectedPhoto.setImageBitmap(decodedByte);
                URL newurl = new URL(Server.mediaPath + jsonObject.getString("face_path"));
                mIcon_val = BitmapFactory.decodeStream(newurl.openStream());

                ds.close();
                return jsonObject;
//            handler.sendEmptyMessage(0x12);
        /* 关闭DataOutputStream */

            } catch (Exception e) {
//            handler.sendEmptyMessage(0x13);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {

                byte[] decodedString = Base64.decode(jsonObject.getString("upload_face_base64"), Base64.DEFAULT);
                Bitmap selectedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                uploadedPhoto.setImageBitmap(selectedBitmap);
                resultPhoto.setImageBitmap(mIcon_val);
                resultText.setText("Similarly : " + jsonObject.getString("percentage"));

//                new URL()


//                fragments = new ArrayList<>();
//                fragments.add(new SearchResultFragment(selectedBitmap,selectedBitmap, jsonObject.getString("upload_face_base64")));
////        fragments.add(new LineChartFragment());
//                fragments.add(new VideoDetailsFragment());
//
//                adapter = new SearchFaceAdapter(getActivity().getSupportFragmentManager(), fragments);
////                Log.d("Adapter :" , jsonObject.toString());
//                viewPager.setAdapter(new SearchFaceAdapter(getActivity().getSupportFragmentManager(), fragments));
//                viewPager.setCurrentItem(0);
//                URL newurl = new URL(Server.mediaPath + jsonObject.getString("face_path"));
//                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openStream());
//                resultPhoto.setImageBitmap(mIcon_val);
//                setResultValue(jsonObject.getString("upload_face_base64"), selectedBitmap,jsonObject.getString("percentage"));
//                ArrayList<Entry> values = new ArrayList<>();
//                for (int i = 0; i < jsonArray.length(); ++i) {
//                    JSONObject json = jsonArray.getJSONObject(i);
//                    values.add(new Entry(i, json.getInt("total_face")));

//                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
