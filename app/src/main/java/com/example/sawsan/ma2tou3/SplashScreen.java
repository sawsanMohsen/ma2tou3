package com.example.sawsan.ma2tou3;

import android.content.Intent;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;


public class SplashScreen extends AppCompatActivity {

    VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        vv = (VideoView) findViewById(R.id.videoView);


        vv = (VideoView) findViewById(R.id.videoView);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.untitled));
        vv.start();



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2700);
    }

}
