package com.surveillance.SurveillanceSystem.Fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.surveillance.SurveillanceSystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends ListFragment{
    private ImageView uploadedPhoto, resultPhoto;
    private TextView resultText;
    private Bitmap leftPhoto, rightPhoto;
    private String base64bit;


    public SearchResultFragment(Bitmap leftPhoto, Bitmap rightPhoto, String base64bit) {
        this.leftPhoto = leftPhoto;
        this.rightPhoto = rightPhoto;
        this.base64bit = base64bit;
    }

    public SearchResultFragment(){

    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);
        uploadedPhoto = (ImageView)root.findViewById(R.id.uploaded_photo);
        resultPhoto = (ImageView) root.findViewById(R.id.result_photo);
        resultText = (TextView) root.findViewById(R.id.result_text);
        uploadedPhoto.setImageBitmap(leftPhoto);
        byte[] decodedString = Base64.decode(base64bit, Base64.DEFAULT);
        Log.d("base64 Bit:", base64bit);
//        Log.d("Get String :" , jsonObject.getString("upload_face_base64"));
        Bitmap selectedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        resultPhoto.setImageBitmap(selectedBitmap);
        return root;
    }

    public void setResultValue(String base64bit, Bitmap resultPic, String resultTex ){
        byte[] decodedString = Base64.decode(base64bit, Base64.DEFAULT);
        Log.d("base64 Bit:", base64bit);
//        Log.d("Get String :" , jsonObject.getString("upload_face_base64"));
        Bitmap selectedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        uploadedPhoto.setImageBitmap(selectedBitmap);
        resultText.setText(resultTex);
        resultPhoto.setImageBitmap(resultPic);
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        String url;
//
//        public DownloadImageTask(String url) {
//            this.url = url;
//        }
//
//
//        protected Bitmap doInBackground(String urls) {
//
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(url).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            resultPhoto.setImageBitmap(result);
//        }
//    }
}
