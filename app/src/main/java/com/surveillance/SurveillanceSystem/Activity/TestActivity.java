package com.surveillance.SurveillanceSystem.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.surveillance.SurveillanceSystem.R;

public class TestActivity extends AppCompatActivity {
    private WebView webView;
    private NetworkImageView networkImageView;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        webView = (WebView) findViewById(R.id.webView);
//        webView.loadUrl("http://fyp.bu5hit.xyz:5000/stream/1");
        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
        networkImageView = (NetworkImageView) findViewById(R.id.imageView);
        networkImageView.setImageUrl("http://fyp.bu5hit.xyz:5000/stream/1", mImageLoader);

    }
}
