package com.sail.btsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class RadioPlayerActivity extends AppCompatActivity {

    public ExoPlayer player;
    private String url, urlMel, urlRN, urlRRR, urlPBS;
    Boolean flagPlaying = false;
    int sleepTimer = 45;  // minutes
    TextView mNowPlayingShowText;
    String nameShow;
    Boolean sleepFunction;
    ToggleButton btnPlayStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_control);

        urlMel = "http://live-radio01.mediahubaustralia.com/3LRW/mp3";
        urlRN = "http://live-radio01.mediahubaustralia.com/2RNW/mp3";
        urlRRR = "http://realtime.rrr.org.au/p13";
        urlPBS = "https://playerservices.streamtheworld.com/api/livestream-redirect/3PBS_FMAAC.m3u8";

        player = new ExoPlayer.Builder(this)
        .setMediaSourceFactory(
            new DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
                .build();

        View mNowPlayingLogo = findViewById(R.id.now_playing);
        TextView mNowPlayingText = findViewById(R.id.now_playing_text);
        mNowPlayingShowText = findViewById(R.id.now_playing_text_show);

        final ImageButton btnMel = findViewById(R.id.play_mel);
        final ImageButton btnRN = findViewById(R.id.play_rn);
        final ImageButton btnNews = findViewById(R.id.play_news);
        final ImageButton btnRRR = findViewById(R.id.play_rrr);
        final ImageButton btnPBS = findViewById(R.id.play_pbs);
        btnPlayStop = findViewById(R.id.play_button);

        ImageButton goBack = findViewById(R.id.go_back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagPlaying){
                    stopPlaying();
                }
                finish();
            }
        });

        btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);

        btnMel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNowPlayingLogo.setBackgroundResource(R.drawable.radio_melbourne);
                mNowPlayingText.setText("ABC Radio Melbourne");
                url = urlMel;
                stopPlaying();
                playRadio(url);
            }
        });

        btnRN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNowPlayingLogo.setBackgroundResource(R.drawable.radio_national);
                mNowPlayingText.setText("ABC Radio National");
                url = urlRN;
                stopPlaying();
                playRadio(url);
            }
        });

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNowPlayingLogo.setBackgroundResource(R.drawable.radio_news);
                mNowPlayingText.setText("ABC News Radio");
                url = urlRN;
                stopPlaying();
                playRadio(url);
            }
        });

        btnRRR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNowPlayingLogo.setBackgroundResource(R.drawable.rrr);
                mNowPlayingText.setText("3RRR");
                url = urlRRR;
                stopPlaying();
                playRadio(url);
            }
        });

        btnPBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNowPlayingLogo.setBackgroundResource(R.drawable.pbs);
                mNowPlayingText.setText("3PBS");
                url = urlPBS;
                stopPlaying();
                playRadio(url);
            }
        });

        btnPlayStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (url != null) {

                    if (!flagPlaying) {
                        playRadio(url);
                        btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    } else {
                        pauseRadio();
                        btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get settings from preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sleepFunction = sharedPreferences.getBoolean("prefs_sleep_function",Boolean.parseBoolean("TRUE"));
        sleepTimer = Integer.parseInt(sharedPreferences.getString("prefs_listen_time", "45"));
    }

    public void playRadio(String url) {
        if (!flagPlaying) {
            if (sleepFunction) {
                SleepTimer();
            }
            btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(url)
                    .setLiveConfiguration(
                            new MediaItem.LiveConfiguration.Builder()
                                    .setMaxPlaybackSpeed(1.02f)
                                    .build())
                    .build();
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
            flagPlaying = true;

            // load data file
            FFmpegMediaMetadataRetriever metaRetriever = new FFmpegMediaMetadataRetriever();
            metaRetriever.setDataSource(url);
            String icyMeta = metaRetriever.extractMetadata
                    (String.valueOf(FFmpegMediaMetadataRetriever.METADATA_KEY_ICY_METADATA));
            if (icyMeta != null) {
                nameShow = icyMeta.substring(icyMeta.indexOf("='") + 2, icyMeta.indexOf("';"));
            } else {
                nameShow = null;
            }
            mNowPlayingShowText.setText(nameShow);
        }
    }

    public void pauseRadio() {
       if(player!=null) {
           player.pause();
           flagPlaying = false;
       }
    }

    public void stopPlaying() {
        if(player!=null){
            player.stop();
            flagPlaying = false;
            btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);
        }
    }

    public void SleepTimer() {
        new CountDownTimer(sleepTimer * 60 * 1000, 1000) {
             public void onTick(long millisUntilFinished) {
             }
             public void onFinish() {
                 player.setVolume((float) 0.8);
                 new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         player.setVolume((float) 0.6);
                         new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                player.setVolume((float) 0.4);
                                 new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         player.setVolume((float) 0.2);
                                         new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                             @Override
                                             public void run() {
                                                 stopPlaying();
                                                 player.setVolume((float) 1.0);
                                             }
                                         }, 3000);
                                     }
                                 }, 2000);
                             }
                         }, 2000);
                     }
                 }, 2000);
             }
         }.start();
    }

    @Override
    public void onBackPressed() {
        stopPlaying();
        finish();
    }
}
