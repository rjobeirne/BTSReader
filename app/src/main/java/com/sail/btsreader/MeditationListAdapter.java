package com.sail.btsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MeditationListAdapter extends RecyclerView.Adapter<MeditationListAdapter.MeditationViewHolder> {

    private final ArrayList<MeditationModel> meditationDataSet;
    private final LayoutInflater mInflater;
    Context mContext;
    public String mMeditationDuration;

    Integer trackNumber;
    Long duration;
    public String path, bookPath, bookCover;
    public ArrayList<String> paths = new ArrayList<String>();
    public ArrayList<String> nameMeditations = new ArrayList<String>();
    public ArrayList<Long> durations = new ArrayList<Long>();
    public String playStatus;
    public String meditationName;
    int meditationCount;

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

        return new MeditationViewHolder(mContext, meditationDataSet,v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeditationViewHolder meditationViewHolder, int i) {

        mMeditationDuration = meditationDataSet.get(i).getaDuration();
        trackNumber = meditationDataSet.get(i).getaFileNumber();
        duration = meditationDataSet.get(i).getaRawDuration();
        meditationName = meditationDataSet.get(i).getaTitle();
        path = "storage/emulated/0/AudioBooks/Meditation/" + meditationName;
        bookCover = "storage/emulated/0/AudioBooks/Meditation/Meditate.jpg";

        meditationName = meditationName.substring(0, meditationName.indexOf(".")); // remove file extension
        meditationViewHolder.mMeditationNameTextView.setText(meditationName);

        paths.add(path);
        nameMeditations.add(meditationName);
        durations.add(duration);
    }

    @Override
    public int getItemCount() {
        return meditationDataSet.size();
    }

    public class MeditationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        ArrayList<MeditationModel> meditationList;
        public TextView mMeditationNameTextView;
        CardView mMeditationCardView;

        @SuppressLint("WrongViewCast")
        public MeditationViewHolder(Context context, ArrayList<MeditationModel> meditationModelList, View v) {
            super(v);
            nContext = context;
            meditationList = meditationModelList;
            mMeditationNameTextView = v.findViewById(R.id.meditation_name);
            mMeditationCardView = v.findViewById(R.id.mMeditationCardView);
            mMeditationCardView.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAbsoluteAdapterPosition();

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
                intent.putExtra("meditationCount", meditationCount);
                nContext.startActivity(intent);
        }
    }
}