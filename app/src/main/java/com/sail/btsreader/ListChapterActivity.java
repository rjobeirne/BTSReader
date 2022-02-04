package com.sail.btsreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ListChapterActivity extends AppCompatActivity {

    RecyclerView listChapterView;
    ChapterListAdapter chapterListAdapter;
    Context context;

    String bookTitle, coverPath, bookSubDirectory;
    String nameChapter;
    Boolean alreadyRead = false;
    long dur, secs, mins;
    String seconds, minutes;

    TextView mBookTitleTextView;
    View mCoverView;
    int previousPlace;
    int chptNo =-1;

    BookProgress readProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);

        Intent intent = getIntent();

        bookTitle = intent.getStringExtra("bookName");
        coverPath = intent.getStringExtra("coverPath");
        bookSubDirectory = intent.getStringExtra("bookPath");
        mBookTitleTextView = findViewById(R.id.book_title);
        mCoverView = findViewById(R.id.cover_background);

        readProgress = new BookProgress();

        getChapters(bookTitle);

        ImageButton goBack = findViewById(R.id.go_back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getChapters(String mBookTitle) {
        context = ListChapterActivity.this;
        listChapterView = findViewById(R.id.chapter_list_view);
        ArrayList<ChapterModel> allChapters = getChapterList(context, mBookTitle);

        chapterListAdapter = new ChapterListAdapter(context, allChapters);
        listChapterView.setLayoutManager(new LinearLayoutManager(context));
        listChapterView.setAdapter(chapterListAdapter);
    }

    public ArrayList<ChapterModel> getChapterList(final Context context, String mBookTitle) {

        final ArrayList<ChapterModel> tempChapterList = new ArrayList<>();

        String bookDirectory = Environment.getExternalStorageDirectory().toString() + "/AudioBooks/" + bookSubDirectory;

        File directory = new File(bookDirectory);
        File progressFiles = getFilesDir();
        File[] files = directory.listFiles();

        mBookTitleTextView.setText(bookTitle);
        previousPlace = readProgress.getBookProgress(context, progressFiles, bookTitle);
        Log.e("Previous Place :", String.valueOf(previousPlace));

        for (int i = 0; i < files.length; i++) {

            nameChapter = files[i].getName();
            String duration;
            String out;
            String chapterPath = bookDirectory + "/" + nameChapter;
            ChapterModel chapterModel = new ChapterModel();
            // load data file
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

            if(nameChapter.endsWith(".mp3")) {

                chptNo = chptNo + 1;

                metaRetriever.setDataSource(chapterPath);

                // get mp3 info
                duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                dur = Long.parseLong(duration);

                // convert duration to minute:seconds
                secs = (dur % 60000) / 1000;
                mins = dur / 60000;

                if ( secs < 10 ) {
                    seconds = "0" + String.valueOf(secs);
                } else {
                    seconds = String.valueOf(secs);
                }

                if ( mins < 10 ) {
                    minutes = "0" + String.valueOf(mins);
                } else {
                    minutes = String.valueOf(mins);
                }
                out = minutes + ":" + seconds;

                if(chptNo <= previousPlace) {
                    alreadyRead = true;
                } else {
                    alreadyRead = false;
                }

                chapterModel.setaTrackNumber(i);
                chapterModel.setaTitle(bookTitle);
                chapterModel.setaChapter(nameChapter);
                chapterModel.setaDuration(out);
                chapterModel.setaRawDuration(dur);
                chapterModel.setaPath(chapterPath);
                chapterModel.setaCover(coverPath);
                chapterModel.setRead(alreadyRead);
                tempChapterList.add(chapterModel);

                // close object
                metaRetriever.release();
            }
        }
        makeCover(coverPath);

        return tempChapterList;
    }

    public void makeCover(String coverPath) {

        Bitmap bitmap = BitmapFactory.decodeFile(coverPath);
        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);
//        mCoverView.setBackground(coverBMP);
    }

    /*
    Reset chapter progress when exiting player
     */
    public void onRestart()
    {
    super.onRestart();
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}
