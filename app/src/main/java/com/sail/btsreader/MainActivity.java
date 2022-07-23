package com.sail.btsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    Context nContext;
    String currentBook, bookDirectory;
    String currentCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            findCurrentBook();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button listBooksBtn = findViewById(R.id.list_books);
        Button continueListeningBtn = findViewById(R.id.continue_listen);
        Button listenRadioBtn = findViewById(R.id.play_radio);
        View imageViewCover = findViewById(R.id.current_cover);

        Bitmap bitmap = BitmapFactory.decodeFile(currentCover);
        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);
        imageViewCover.setBackground(coverBMP);

        listBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListBookActivity.class));
            }
        });

        continueListeningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    continueReading();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listenRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    listenRadio();
                    Log.e("Button pressed","");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
          Manifest.permission.READ_EXTERNAL_STORAGE,
          android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void listenRadio() {
        Intent i = new Intent(MainActivity.this, RadioActivity.class);
        startActivity(i);
        Log.e("Launch radio", "");

    }

    public void findCurrentBook() throws Exception {

        nContext = MainActivity.this;

        FileInputStream fin = null;
        try {
            fin = nContext.openFileInput("00currentBook");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner((fin));
        scanner.useDelimiter("\\Z");
        String currentBookFileContents = scanner.next();
        scanner.close();

        currentBook = currentBookFileContents.substring(0, currentBookFileContents.indexOf("#"));
        bookDirectory = currentBookFileContents.substring(currentBookFileContents.indexOf("#") + 1,
                currentBookFileContents.indexOf("$"));
        currentCover = currentBookFileContents.substring(currentBookFileContents.indexOf("$") + 1);
    }

    public void continueReading() {

        Intent intent = new Intent(nContext, ListChapterActivity.class);
        intent.putExtra("bookName", currentBook);
        intent.putExtra("bookPath", bookDirectory);
        intent.putExtra("coverPath", currentCover);
        nContext.startActivity(intent);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    Reset current when restarting
     */
    public void onRestart()
    {
    super.onRestart();
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}