package com.surveillance.surveillancesystem.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;


import com.surveillance.surveillancesystem.R;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private Button takeSPictureBtn, takeBPictureBtn, takeVideoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = (ImageView) findViewById(R.id.imageView);
        videoView = (VideoView) findViewById(R.id.videoView);
        takeSPictureBtn = (Button) findViewById(R.id.btnIntendS);
        takeBPictureBtn = (Button) findViewById(R.id.btnIntend);
        takeVideoBtn = (Button) findViewById(R.id.btnIntendV);

    }
}
