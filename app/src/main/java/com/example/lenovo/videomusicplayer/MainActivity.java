package com.example.lenovo.videomusicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnClickListener,SeekBar.OnSeekBarChangeListener ,MediaPlayer.OnCompletionListener{

    private VideoView videoView;
    private Button playVideobtn,pauseMusicbtn,playMusicbtn;
    private MediaController mediaController;
    private SeekBar volumeSeekBar,movingSeekBar;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        playVideobtn = findViewById(R.id.playVediobtn);
        playMusicbtn = findViewById(R.id.playMusicbtn);
        pauseMusicbtn = findViewById(R.id.pauseMusicbtn);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        movingSeekBar = findViewById(R.id.movingSeekBar);

        mediaController = new MediaController(this);
        mediaPlayer = MediaPlayer.create(this,R.raw.titanicsong);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        playVideobtn.setOnClickListener(this);
        playMusicbtn.setOnClickListener(this);
        pauseMusicbtn.setOnClickListener(this);

        int maximmumVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maximmumVolume);
        volumeSeekBar.setMax(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        movingSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        movingSeekBar.setMax(mediaPlayer.getDuration());



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.playVediobtn:
                Uri videoPlay = Uri.parse("android.resource://"+ getPackageName()+ "/"+ R.raw.sea);
                videoView.setVideoURI(videoPlay);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.start();
                break;

            case R.id.playMusicbtn:
                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        movingSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                },0,1000);
                break;

            case R.id.pauseMusicbtn:
                mediaPlayer.pause();
                break;

        }


    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        mediaPlayer.seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        mediaPlayer.pause();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        timer.cancel();
        Toast.makeText(this, "Song Over", Toast.LENGTH_SHORT).show();
    }
}
