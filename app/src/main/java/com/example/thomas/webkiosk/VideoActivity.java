package com.example.thomas.webkiosk;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    ImageButton back_button;
    VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);


        vv = (VideoView) findViewById(R.id.videoview);
        back_button = (ImageButton) findViewById(R.id.back_button);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");

        Log.d(TAG, "URL is " + value);

        //vv.setVideoPath("rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/_definst_/mp4:bigbuckbunnyiphone_400.mp4");
        vv.setVideoPath(value);

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                Log.d(TAG, "onCompletion");
                //start Previous Activity here

            }
        }); // video finish listener

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "back button clicked");
                vv.stopPlayback();
                back_button.setVisibility(View.GONE);
                Intent myIntent = new Intent(VideoActivity.this, MainActivity.class);
                VideoActivity.this.startActivity(myIntent);
                VideoActivity.this.finish();
            }
        });

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vv);
        vv.setMediaController(mediaController);
        vv.requestFocus();
        vv.start();

        Toast.makeText(getApplicationContext(),"Please wait for video to start", Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(),value, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }
}
