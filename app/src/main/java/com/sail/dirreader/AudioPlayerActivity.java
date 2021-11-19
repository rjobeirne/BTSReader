package com.sail.dirreader;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayerActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    String mTrack;
    Integer index, maxIndex;
    Integer currentIndex = 0;
    ArrayList<String> playList;
    Timer timer;
    Boolean flagPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();
        timer = new Timer();

        playList = intent.getStringArrayListExtra("filepath");
        Log.e("player-path :", String.valueOf(playList));

        mediaPlayer = MediaPlayer.create(this, Uri.parse(playList.get(0)));
//        mediaPlayer.start();
//        enableSeekBar();


        TextView audioName = findViewById(R.id.audioName);
//        Button playBtn = findViewById(R.id.playBtn);
        Button viewAllMediaBtn = findViewById(R.id.viewAllMedia);
        Button skipToPrevious = findViewById(R.id.skip_to_previous);
        Button skipToNext = findViewById(R.id.skip_to_next);

        final ToggleButton playBtn = (ToggleButton)findViewById(R.id.playBtn);

        playBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    playBtn.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        mediaPlayer.start();
                    } else {
                        playChapter(playList);
                    }
                    flagPaused = false;
                } else {
                    playBtn.setBackgroundResource(R.drawable.outline_play_circle_24);
                    mediaPlayer.pause();
                    flagPaused = true;
                }
            }
        });

        skipToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



//        if(audio!=null){
//            audioName.setText(audio.getaName());
//        }else{
//            audioName.setText("Piano.wav");
//        }
//
//
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMediaPlayer.start();
//                startPlaying(playList);
//            }
//        });
//
//        viewAllMediaBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopPlaying();
//                startActivity(new Intent(getApplicationContext(),MusicListActivity.class));
//                //finish();
//            }
//        });
//
//

    }  // end of onCreate
//
//    public void playTrack(String mTrack) {
//            final MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(mTrack));
//
//
//    }
//
//    public void startPlaying(String playTrack){
//        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
//            mediaPlayer.stop();
//        }
//
//        if(audio!=null){
//            Toast.makeText(getApplicationContext(),"Playing from device; "+audio.getaPath(), Toast.LENGTH_SHORT).show();
//               mediaPlayer = MediaPlayer.create(AudioPlayerActivity.this, Uri.parse(playTrack));
//               mediaPlayer.start();
//
//           }
//
//        }else{
//
//            Toast.makeText(getApplicationContext(),"Playing from app", Toast.LENGTH_SHORT).show();
//            try {
//                AssetFileDescriptor afd = getAssets().openFd("piano.wav");
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        enableSeekBar();
//
//    }

    public void playChapter(ArrayList playList) {

        mediaPlayer = MediaPlayer.create(this, Uri.parse((String) playList.get(0)));
        mediaPlayer.start();
        enableSeekBar();
        if (playList.size() > 1) playNext();
        index = 0;
        maxIndex = playList.size();
        mTrack = (String) playList.get(index);
        Log.e("play-track", mTrack);

    }

    public void playNext() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(AudioPlayerActivity.this, Uri.parse(playList.get(++index)));
            mediaPlayer.start();
            enableSeekBar();
            if (playList.size() > index+1) {
                playNext();
            }
            }
        },mediaPlayer.getDuration()+100);
    }

    public void stopPlaying(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    public void enableSeekBar(){
        seekBar =  findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Update the progress depending on seek bar
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        stopPlaying();
        super.onBackPressed();
    }

}
