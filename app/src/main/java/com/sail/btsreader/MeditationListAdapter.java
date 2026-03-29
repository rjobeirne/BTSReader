package com.sail.btsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MeditationListAdapter extends RecyclerView.Adapter<MeditationListAdapter.MeditationViewHolder> {

    private ArrayList<MeditationModel> meditationDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mMeditationName, mMeditationDuration;

    public  int minTime = 15 * 60 * 1000;
    Integer trackNumber, startTrack, previousStart, previousLast;
    Long duration;
    Boolean alreadyRead, possiblyRead;
    public String path, bookTitle, bookPath, bookCover;
    public ArrayList<String> paths = new ArrayList<String>();
    public ArrayList<String> nameMeditations = new ArrayList<String>();
    public ArrayList<Long> durations = new ArrayList<Long>();
    public String playStatus, newMeditation;
    public String meditationName;
    int meditationCount;
    public ArrayList<BookModel> bookList;


    public MeditationListAdapter(Context context, ArrayList<MeditationModel> meditationModelList) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        meditationDataSet = meditationModelList;
        bookPath = meditationDataSet.get(1).getaBookDir();
        meditationCount = meditationDataSet.size();
    }

    @NonNull
    @Override

    public MeditationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create a new view
        View v = mInflater.inflate(R.layout.meditation_item, viewGroup,false);

        MeditationViewHolder vh = new MeditationViewHolder(mContext, meditationDataSet,v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MeditationViewHolder meditationViewHolder, int i) {

        mMeditationName = meditationDataSet.get(i).getaTitle();
        mMeditationDuration = meditationDataSet.get(i).getaDuration();
        trackNumber = meditationDataSet.get(i).getaFileNumber();
        duration = meditationDataSet.get(i).getaRawDuration();
//        path = meditationDataSet.get(i).getaPath();
        meditationName = meditationDataSet.get(i).getaTitle();
        path = "storage/emulated/0/AudioBooks/Meditation/" + meditationName;
        bookCover = "storage/emulated/0/AudioBooks/Meditation/Meditate.jpg";
//        alreadyRead = meditationDataSet.get(i).getRead();
//        possiblyRead = meditationDataSet.get(i).getPossiblyRead();
//        previousStart = meditationDataSet.get(i).getPreviousStart();
//        previousLast = meditationDataSet.get(i).getPreviousLast();

        mMeditationName = mMeditationName.substring(0, mMeditationName.indexOf(".")); // remove file extension
        meditationName = meditationName.substring(0, meditationName.indexOf(".")); // remove file extension

        meditationViewHolder.mMeditationNameTextView.setText(mMeditationName);
//        meditationViewHolder.mMeditationDurationTextView.setText(mMeditationDuration);
//        meditationViewHolder.mAlreadyReadView.setVisibility(View.INVISIBLE);
//
//        if(possiblyRead) {
//            meditationViewHolder.mAlreadyReadView.setVisibility(View.VISIBLE);
//            meditationViewHolder.mAlreadyReadView.setImageResource(R.drawable.possibly_read5);
//        }
//
//        if (alreadyRead) {
//            meditationViewHolder.mAlreadyReadView.setImageResource(R.drawable.already_read);
//        }

        paths.add(path);
        nameMeditations.add(mMeditationName);
        durations.add(duration);
    }

    @Override
    public int getItemCount() {
        return meditationDataSet.size();
    }

    public class MeditationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        Context nContext;
        ArrayList<MeditationModel> meditationList;
        public TextView mMeditationNameTextView;
        public ImageView mAlreadyReadView;
        CardView mMeditationCardView;

        @SuppressLint("WrongViewCast")
        public MeditationViewHolder(Context context, ArrayList<MeditationModel> meditationModelList, View v) {
            super(v);
            nContext = context;
            meditationList = meditationModelList;
            mMeditationNameTextView = v.findViewById(R.id.meditation_name);
//            mAlreadyReadView = v.findViewById(R.id.already_read);
//            mMeditationDurationTextView = v.findViewById(R.id.meditation_duration);
            mMeditationCardView = v.findViewById(R.id.mMeditationCardView);
            mMeditationCardView.setOnClickListener(this);
            mMeditationCardView.setOnCreateContextMenuListener(this);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAbsoluteAdapterPosition();
//
//            startTrack = itemPosition;
//            if (itemPosition < previousStart - 3) {
//                String message = "Long press and reset to desired meditation";
//                Toast toastMessage = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_LONG);
//                toastMessage.show();
//            } else {
                playStatus = "Play";

                Intent intent = new Intent(nContext, AudioPlayerActivity.class);
                intent.putExtra("bookTitle", meditationName);
                intent.putExtra("bookPath", path);
                intent.putExtra("position", itemPosition);
                intent.putStringArrayListExtra("paths", paths);
                intent.putStringArrayListExtra("chapterName", nameMeditations);
                intent.putExtra("durations", durations);
                intent.putExtra("playStatus", playStatus);
                intent.putExtra("cover", bookCover);
//                intent.putExtra("previousStart", 0);
//                intent.putExtra("previousLast", 0);
                intent.putExtra("meditationCount", meditationCount);
                nContext.startActivity(intent);
//            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View vMenu, ContextMenu.ContextMenuInfo menuInfo) {

            int itemPosition = getAdapterPosition();
            newMeditation = meditationDataSet.get(itemPosition).getaMeditation();
            newMeditation = newMeditation.substring(0, newMeditation.indexOf(".")); // remove file extension
            menu.add(itemPosition, 121, 0, "Reset meditation to here");
            menu.add(itemPosition, 122, 1, "Return to meditation list");
        }
    }
}

