package com.example.recordlearn;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayVideoActivity extends AppCompatActivity {

    private final String TAG = PlayVideoActivity.class.getSimpleName();
    private MediaPlayView mediaPlayView;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_video);
        findViewById(R.id.btnGiveUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveUp();
            }
        });

        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseThisVideo();
            }
        });

        mediaPlayView = findViewById(R.id.playView);
        videoPath = getIntent().getStringExtra("video_path");
        File tf = new File(videoPath);
        FileInputStream tfs = null;
        try {
            tfs = new FileInputStream(tf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Log.i(TAG, "videoPath：" + videoPath);

        try {
            mediaPlayView
//                    .setDataSource("/sdcard/DCIM/0ec3ee068accb81d/LIFY.mp4")
                    .setDataSource(videoPath)
                    .setLooping(true)
                    .setOnReadyListener(new MediaPlayView.OnReadyListener() {
                        @Override
                        public void onReady(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 放弃该视频
     */
    private void giveUp() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void chooseThisVideo() {
        Intent intent = new Intent();
        intent.putExtra("duration", mediaPlayView.getDuration());
        Log.i(TAG,"duration="+mediaPlayView.getDuration());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        giveUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
