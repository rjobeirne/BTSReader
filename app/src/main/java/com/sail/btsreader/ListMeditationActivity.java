package com.sail.btsreader;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ListMeditationActivity extends AppCompatActivity {

    RecyclerView listMeditationView;
    MeditationListAdapter meditationListAdapter;
    Context context;
    long dur;
    String bookDirectory, meditationTitle;
    File dataFiles;

    ArrayList<MeditationModel> allMeditationDirectories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meditation_list);

        ImageButton goBack = findViewById(R.id.go_back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            dataFiles = getFilesDir().getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Find all meditations in the /AudioBooks/Meditation directory
        getMeditations();
    }

    public void getMeditations() {
        context = ListMeditationActivity.this;
        listMeditationView = findViewById(R.id.book_list_view);
        allMeditationDirectories = getMeditationList();

        meditationListAdapter = new MeditationListAdapter(context, allMeditationDirectories);
        listMeditationView.setLayoutManager(new LinearLayoutManager(context));
        listMeditationView.setAdapter(meditationListAdapter);
    }

    public ArrayList<MeditationModel> getMeditationList() {

        final ArrayList<MeditationModel> tempMeditationList = new ArrayList<>();
        bookDirectory = Environment.getExternalStorageDirectory().toString() + "/AudioBooks/Meditation";
        File directory = new File(bookDirectory);
        File[] files = directory.listFiles();

        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            meditationTitle = files[i].getName();
            MeditationModel meditationModel = new MeditationModel();

            if (meditationTitle.endsWith(".mp3")) {
                meditationModel.setaFileNumber(i);
                meditationModel.setaTitle(meditationTitle);
                meditationModel.setaRawDuration(dur);
                tempMeditationList.add(meditationModel);
            }
        }
        return tempMeditationList;
    }
}
