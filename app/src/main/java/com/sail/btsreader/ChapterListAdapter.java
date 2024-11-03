package com.sail.btsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder> {

    private ArrayList<ChapterModel> chapterDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mChapterName, mChapterDuration;

    public  int minTime = 15 * 60 * 1000;
    Integer trackNumber, startTrack, previousStart, previousLast;
    Long duration;
    Boolean alreadyRead, possiblyRead;
    public String path, bookTitle, bookPath, bookCover;
    public ArrayList<String> paths = new ArrayList<String>();
    public ArrayList<String> nameChapters = new ArrayList<String>();
    public ArrayList<Long> durations = new ArrayList<Long>();
    public String playStatus;
    int chapterCount;
    public ArrayList<BookModel> bookList;


    public ChapterListAdapter(Context context, ArrayList<ChapterModel> chapterModelList) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
//        bookList = bookModelList;
        chapterDataSet = chapterModelList;
        bookPath = chapterDataSet.get(1).getaBookDir();
        chapterCount = chapterDataSet.size();
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
        trackNumber = chapterDataSet.get(i).getaFileNumber();
        duration = chapterDataSet.get(i).getaRawDuration();
        path = chapterDataSet.get(i).getaPath();
        bookTitle = chapterDataSet.get(i).getaTitle();
        bookCover = chapterDataSet.get(i).getaCover();
        alreadyRead = chapterDataSet.get(i).getRead();
        possiblyRead = chapterDataSet.get(i).getPossiblyRead();
        previousStart = chapterDataSet.get(i).getPreviousStart();
        previousLast = chapterDataSet.get(i).getPreviousLast();

        mChapterName = mChapterName.substring(0, mChapterName.indexOf(".")); // remove file extension

        chapterViewHolder.mChapterNoTextView.setText(mChapterName);
        chapterViewHolder.mChapterDurationTextView.setText(mChapterDuration);
//        if (alreadyRead) {
//            chapterViewHolder.mAlreadyReadView.setVisibility(View.VISIBLE);
//        } else {
            chapterViewHolder.mAlreadyReadView.setVisibility(View.INVISIBLE);
//        }

        if(possiblyRead) {
            chapterViewHolder.mAlreadyReadView.setVisibility(View.VISIBLE);
            chapterViewHolder.mAlreadyReadView.setImageResource(R.drawable.possibly_read5);
        }

        if (alreadyRead) {
            chapterViewHolder.mAlreadyReadView.setImageResource(R.drawable.already_read);
        }

        paths.add(path);
        nameChapters.add(mChapterName);
        durations.add(duration);

    }

    @Override
    public int getItemCount() {
        return chapterDataSet.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        Context nContext;
        ArrayList<ChapterModel> chapterList;
        public TextView mChapterNoTextView, mChapterDurationTextView;
        public ImageView mAlreadyReadView;
        CardView mChapterCardView;

        @SuppressLint("WrongViewCast")
        public ChapterViewHolder(Context context, ArrayList<ChapterModel> chapterModelList, View v) {
            super(v);
            nContext = context;
            chapterList = chapterModelList;
            mChapterNoTextView = v.findViewById(R.id.chapter_number);
            mAlreadyReadView = v.findViewById(R.id.already_read);
            mChapterDurationTextView = v.findViewById(R.id.chapter_duration);
            mChapterCardView = v.findViewById(R.id.mChapterCardView);
            mChapterCardView.setOnClickListener(this);
            mChapterCardView.setOnCreateContextMenuListener(this);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAbsoluteAdapterPosition();

            startTrack = itemPosition;
            playStatus = "Play";

            Intent intent = new Intent(nContext, AudioPlayerActivity.class);
            intent.putExtra("bookTitle", bookTitle);
            intent.putExtra("bookPath", bookPath);
            intent.putExtra("position", itemPosition);
            intent.putStringArrayListExtra("paths", paths);
            intent.putStringArrayListExtra("chapterName", nameChapters);
            intent.putExtra("durations", durations);
            intent.putExtra("playStatus", playStatus);
            intent.putExtra("cover", bookCover);
            intent.putExtra("previousStart", previousStart);
            intent.putExtra("previousLast", previousLast);
            intent.putExtra("chapterCount", chapterCount);
            nContext.startActivity(intent);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View vMenu, ContextMenu.ContextMenuInfo menuInfo) {

            int itemPosition = getAdapterPosition();
//            String bookName = bookList.get(itemPosition).getaTitle();
//            menu.setHeaderTitle(bookName);
            menu.add(itemPosition, 121, 0, "Reset chapter to here");
            menu.add(itemPosition, 122, 1, "Return to chapter list");

        }
    }
}



