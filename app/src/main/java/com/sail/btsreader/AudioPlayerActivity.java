package com.sail.btsreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AudioPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    ArrayList chapterList, chapterName;
    ArrayList<Long> durations;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    String mTrack, bookTitle, playStatus, bookPath, coverPath;
    int index, maxIndex;
    int itemPosition, itemPositionRelative;
    int currentIndex = 0;
    ArrayList<String> playList;
    Timer timer;
    Boolean flagPaused = false;
    int startTrack;
    public int minTime = 15 * 60 * 1000;
    ArrayList<String> playListPaths;
    private TextView mChapterDuration, mCurrentPosition, mAudioName;
    View mCoverView;
    long chapterTime, currentPosition, newPosition, remainingTime;
    TextView mBookTitleTextView;
    int previousStart, previousLast, listOffset;
    File files;

    BookProgress updateProgress;


    private Handler mHandler = new Handler();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();
        chapterList = new ArrayList<String>();

        bookTitle = intent.getStringExtra("bookTitle");
        bookPath = intent.getStringExtra("bookPath");
        itemPosition = intent.getIntExtra("position", 0);
        chapterList = intent.getStringArrayListExtra("paths");
        chapterName = intent.getStringArrayListExtra("chapterName");
        durations =(ArrayList<Long>) intent.getSerializableExtra("durations");
        playStatus = intent.getStringExtra("playStatus");
        coverPath = intent.getStringExtra("cover");
        previousStart = intent.getIntExtra("previousStart", 0);
        previousLast = intent.getIntExtra("previousLast", 0);

        // Recalc position if ScrollToPosition is invoked in ListChapterAcitivity because it
        // changes the list size for some strange reason.
        if (previousStart > 4) {
            listOffset = previousStart - 4;
        } else  {
            listOffset = 0;
        }
        itemPositionRelative = itemPosition - listOffset;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        TextView audioName = findViewById(R.id.audioName);
//        Button viewAllMediaBtn = findViewById(R.id.viewAllMedia);
        Button skipToPrevious = findViewById(R.id.skip_to_previous);
        Button skipToNext = findViewById(R.id.skip_to_next);
        Button backFivePc = findViewById(R.id.rewind_5pc);
        Button backFiveSecs = findViewById(R.id.rewind_5secs);
        Button forwardFivePc = findViewById(R.id.forward_5pc);
        Button forwardFiveSecs = findViewById(R.id.forward_5secs);
        mAudioName = findViewById(R.id.audioName);
        mBookTitleTextView = findViewById(R.id.book_title);

        updateProgress = new BookProgress();

        final ToggleButton playBtn = (ToggleButton)findViewById(R.id.playBtn);

        playBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    playBtn.setBackgroundResource(R.drawable.outline_play_circle_24);
                    mediaPlayer.pause();
                    flagPaused = true;
                } else {
                    playBtn.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.pause();
                    }
                    flagPaused = false;
                }
            }
        });

        skipToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index - 1;
                if (index >= 0) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                playChapter(index);
                }
            }
        });

        skipToNext.setOnClickListener(v -> {
            index = index + 1;
            if (index <= playListPaths.size() - 1) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                playChapter(index);
            }
        });

        files = null;
        try {
            files = getFilesDir().getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateProgress.addCurrentBook(files, bookTitle, bookPath, coverPath);

        createPlayList(itemPositionRelative);
        makeCover(coverPath);

        if (playStatus .equals("Play")) {
            playChapter(0);
        }

        backFivePc.setOnClickListener(v -> {
            newPosition = currentPosition - chapterTime * 5 / 100;
            if (newPosition >= 0) {
                mediaPlayer.seekTo(convertLongToInt(newPosition));
            }
        });

        backFiveSecs.setOnClickListener(v -> {
                newPosition = currentPosition - 5000;
                if (newPosition >= 0) {
                    mediaPlayer.seekTo(convertLongToInt(newPosition));
                }
        });

        forwardFivePc.setOnClickListener(v -> {
            newPosition = currentPosition + chapterTime * 5 / 100;
            if (newPosition <= chapterTime) {
                mediaPlayer.seekTo(convertLongToInt(newPosition));
            }
        });

        forwardFiveSecs.setOnClickListener(v -> {
                newPosition = currentPosition + 5000;
                if (newPosition <= chapterTime) {
                    mediaPlayer.seekTo(convertLongToInt(newPosition));
                }
        });

        ImageButton goBack = findViewById(R.id.go_back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                finish();
            }
        });


    }  // end of onCreate


    public ArrayList createPlayList(int startTrack) {

        Long playTime = durations.get(startTrack);
        int playChapters = 1;
        int lastTrack = startTrack;

        playListPaths = new ArrayList<String>();
        playListPaths.add((String) chapterList.get(startTrack));

        while(playTime < minTime) {
            lastTrack = lastTrack + 1;
            if(lastTrack < chapterList.size()) {
                playTime = playTime + durations.get(lastTrack);
                playChapters = playChapters + 1;
                playListPaths.add((String) chapterList.get(lastTrack));
            } else {
                break;
            }
        }
        mBookTitleTextView.setText(bookTitle);

        if (itemPosition > previousStart) {
            updateProgress.addBookProgress(files, bookTitle, startTrack + listOffset,
                    lastTrack + listOffset);
        }
        return playListPaths;
    }

    public void playChapter(int index) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playListPaths.get(index));
            mediaPlayer.prepare();
            String currentChapter = (String) chapterName.get(itemPositionRelative + index);
            mAudioName.setText(currentChapter);

        // Update chapter duration
            chapterTime = durations.get(itemPositionRelative);

        // Next and previous buttons
            Button skipToPrevious = findViewById(R.id.skip_to_previous);
            skipToPrevious.setVisibility(View.VISIBLE);
            Button skipToNext = findViewById(R.id.skip_to_next);
            skipToNext.setVisibility(View.VISIBLE);

            if (index <= 0 ) {
                skipToPrevious.setVisibility(View.INVISIBLE);
            }
            if (index >= playListPaths.size() - 1) {
                skipToNext.setVisibility(View.INVISIBLE);
            }

        // Start player unless paused
            if (flagPaused) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
                enableSeekBar();
                flagPaused = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNext(int skip) {

            mediaPlayer.reset();
            index = index + skip;
            if (index < 0 ) {
                index = 0;
            }
            if (index <= playListPaths.size() - 1) {
                playChapter(index);
            } else {
                index = playListPaths.size() - 1;
            }
    }

    /**
     * Function to add cover to player screen
     * @param coverPath
     */
    public void makeCover(String coverPath) {

        View mCoverView = findViewById(R.id.book_cover);
        Bitmap bitmap = BitmapFactory.decodeFile(coverPath);
        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);
        mCoverView.setBackground(coverBMP);
    }


    public void stopPlaying(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void enableSeekBar(){
        seekBar =  findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());

        mHandler.postDelayed(mUpdateTimeTask, 100);


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

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {

            mCurrentPosition = findViewById(R.id.current_position);
            currentPosition = mediaPlayer.getCurrentPosition();
            String dspCurrentPos = String.format("%2d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                    TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition)));
            mCurrentPosition.setText(dspCurrentPos);

            remainingTime = chapterTime - currentPosition;
            mChapterDuration = findViewById(R.id.chapter_duration);
            String dspChaptTime = String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));
            mChapterDuration.setText(dspChaptTime);


               // Running this thread after 100 milliseconds
               mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onBackPressed() {
        stopPlaying();
        finish();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if ( index < (playListPaths.size() - 1)) {
            playChapter(index + 1);
            index = index + 1;
        } else {
            mediaPlayer.stop();
            finish();
        }
    }

    /**
     * Convert times from Long to int
     * @param l
     * @return l
     */
    public static int convertLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
