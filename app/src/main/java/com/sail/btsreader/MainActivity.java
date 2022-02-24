package com.sail.btsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    RecyclerView audioListView;
    BookListAdapter bookListAdapter;
    Context context;

    ListBookActivity theList;

    BookProgress findCurrentBook;

    // UI widgets
    private ImageButton listBooksBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listBooksBtn = findViewById(R.id.list_books);
        Button continueListeningBtn = findViewById(R.id.continue_listen);

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

    public void continueReading() throws Exception {

//        File files = getFilesDir();
//        String currentBook;
//        currentBook = findCurrentBook.getCurrentBook(context, files);
//        Log.e("current Book", currentBook);
//                startActivity(new Intent(getApplicationContext(), ListBookActivity.class));
        String currentBookPlace = getFilesDir() + "/00currentBook";
        File fi = new File(currentBookPlace);
        FileInputStream fin = new FileInputStream(fi);
        String currentBookFileContents = convertStreamToString(fin);
        fin.close();
        Log.e("currentBookPlace", currentBookFileContents);
        String currentBook = currentBookFileContents.substring(0,currentBookFileContents.indexOf("/"));
        String place = currentBookFileContents.substring(currentBookFileContents.indexOf("/")+1);
        int intPlace = Integer.parseInt(place.replaceAll("[\\D]",""));
        Log.e("Book, Place", currentBook + " " + intPlace);


//        return currentBookFileContents;

    }

        public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
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
}