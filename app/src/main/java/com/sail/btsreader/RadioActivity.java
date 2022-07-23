package com.sail.btsreader;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class RadioActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private String url, urlMel, urlRN;
    Boolean flagPaused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_control);

        urlMel = "http://live-radio01.mediahubaustralia.com/3LRW/mp3";
        urlRN = "http://live-radio01.mediahubaustralia.com/2RNW/mp3";

        mediaPlayer = new MediaPlayer();

        final ToggleButton btnMel = (ToggleButton)findViewById(R.id.play_mel);
        final ToggleButton btnRN = (ToggleButton)findViewById(R.id.play_rn);

        btnMel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    btnMel.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        playRadio(urlMel);
                    } else {
                        pauseRadio();
                    }
                    flagPaused = false;
                    flagPaused = true;
                } else {
                    btnMel.setBackgroundResource(R.drawable.outline_play_circle_24);
                    pauseRadio();
                }
            }
        });


        btnRN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    btnRN.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        playRadio(urlRN);
                    } else {
                        pauseRadio();
                    }
                    flagPaused = false;
                    flagPaused = true;
                } else {
                    btnRN.setBackgroundResource(R.drawable.outline_play_circle_24);
                    pauseRadio();
                }
            }
        });
    }




    public void playRadio(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseRadio() {
       if(mediaPlayer!=null) {
           mediaPlayer.stop();
       }
    }

    public void stopPlaying() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
    @Override
    public void onBackPressed() {
        stopPlaying();
        finish();
    }

        @Override
        public void onCompletion(MediaPlayer arg0){
            mediaPlayer.stop();
            finish();
        }
    }

