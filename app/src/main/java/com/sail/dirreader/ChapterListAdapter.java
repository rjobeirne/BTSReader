package com.sail.dirreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder> {

    private ArrayList<ChapterModel> chapterDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mChapterName, mChapterDuration;

    public  int minTime = 15 * 60 * 1000;
    Integer trackNumber, startTrack;
    Long duration;
    public String path, bookTitle, bookCover;
    public ArrayList<String> paths = new ArrayList<String>();
    public ArrayList<String> nameChapters = new ArrayList<String>();
    public ArrayList<Long> durations = new ArrayList<Long>();
    public String playStatus;
    View screenView;


    public ChapterListAdapter(Context context, ArrayList<ChapterModel> chapterModelList) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        chapterDataSet = chapterModelList;
    }

    @NonNull
    @Override

    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create a new view
        View v = mInflater.inflate(R.layout.chapter_item, viewGroup,false);

        ChapterViewHolder vh = new ChapterViewHolder(mContext, chapterDataSet,v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder chapterViewHolder, int i) {

        mChapterName = chapterDataSet.get(i).getaChapter();
        mChapterDuration = chapterDataSet.get(i).getaDuration();
        trackNumber = chapterDataSet.get(i).getaTrackNumber();
        duration = chapterDataSet.get(i).getaRawDuration();
        path = chapterDataSet.get(i).getaPath();
        bookTitle = chapterDataSet.get(i).getaTitle();
        bookCover = chapterDataSet.get(i).getaCover();

        mChapterName = mChapterName.substring(0, mChapterName.indexOf(".")); // remove file extension

        if(!mChapterName.contains("-")){
            mChapterName = "Chapter " + (i + 1);
        }
        chapterViewHolder.mChapterNoTextView.setText(mChapterName);
        chapterViewHolder.mChapterDurationTextView.setText(mChapterDuration);

        paths.add(path);
        nameChapters.add(mChapterName);
        durations.add(duration);

    }

    @Override
    public int getItemCount() {
        return chapterDataSet.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        ArrayList<ChapterModel> chapterList;
        public TextView mChapterNoTextView, mChapterDurationTextView;

        public ChapterViewHolder(Context context, ArrayList<ChapterModel> chapterModelList, View v) {
            super(v);
            nContext = context;
            chapterList = chapterModelList;
            mChapterNoTextView = v.findViewById(R.id.chapter_number);
            mChapterDurationTextView = v.findViewById(R.id.chapter_duration);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAdapterPosition();

            startTrack = itemPosition;
            playStatus = "Play";

            Intent intent = new Intent(nContext, AudioPlayerActivity.class);
            intent.putExtra("bookTitle", bookTitle);
            intent.putExtra("position", itemPosition);
            intent.putStringArrayListExtra("paths", paths);
            intent.putStringArrayListExtra("chapterName", nameChapters);
            intent.putExtra("durations", durations);
            intent.putExtra("playStatus", playStatus);
            intent.putExtra("cover", bookCover);
            nContext.startActivity(intent);
        }
    }
}



