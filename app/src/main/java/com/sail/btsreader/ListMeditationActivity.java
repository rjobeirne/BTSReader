package com.sail.btsreader;

import android.content.Context;
import android.media.MediaMetadataRetriever;
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
import java.util.Collections;

public class ListMeditationActivity extends AppCompatActivity {

    RecyclerView listMeditationView;
    MeditationListAdaptor meditationListAdapter;
    Context context;
    String nameDirectoryFiles, coverPath, author;
    long dur, secs, mins;
    String seconds, minutes;

    String bookDirectory, meditationTitle;
    File dataFiles;

    ArrayList<MeditationModel> allMeditationDirectories;
    File[] books;
    final ArrayList<MeditationModel> tempMeditationList = new ArrayList<>();

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

        // Find all books in the /AudioBook directory
        getMeditations();

    }

    public void getMeditations() {
        context = ListMeditationActivity.this;
        listMeditationView = findViewById(R.id.book_list_view);
        allMeditationDirectories = getMeditationList(context);

        meditationListAdapter = new MeditationListAdaptor(context, allMeditationDirectories);
        listMeditationView.setLayoutManager(new LinearLayoutManager(context));
        listMeditationView.setAdapter(meditationListAdapter);
    }

    public ArrayList<MeditationModel> getMeditationList(final Context context) {

        final ArrayList<MeditationModel> tempMeditationList = new ArrayList<>();

        bookDirectory = Environment.getExternalStorageDirectory().toString() + "/AudioBooks/Meditation";

        File directory = new File(bookDirectory);
////        File progressFiles = null;
////        try {
////            progressFiles = getFilesDir().getCanonicalFile();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
        File[] files = directory.listFiles();
//
////        mBookTitleTextView.setText(bookTitle);
////        previousPlace = readProgress.getBookProgress(context, progressFiles, bookTitle);
//
//        if (previousPlace.contains("#")) {
//            startPlace = Integer.parseInt(previousPlace.substring(0, previousPlace.indexOf("#")));
//            lastPlace = Integer.parseInt(previousPlace.substring(previousPlace.indexOf("#") + 1));
//        } else {
//            startPlace = Integer.parseInt(previousPlace);
//            lastPlace = Integer.parseInt(previousPlace);
//        }

        for (int i = 0; i < files.length; i++) {

            meditationTitle = files[i].getName();
            String duration;
            String out;
            String chapterPath = bookDirectory + "/" + meditationTitle;
            MeditationModel meditationModel = new MeditationModel();
            // load data file
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

            if (meditationTitle.endsWith(".mp3")) {

//                chptNo = chptNo + 1;

                metaRetriever.setDataSource(chapterPath);

                // get mp3 info
                duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                dur = Long.parseLong(duration);

//                trackNumber = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
//                trackNo = Integer.valueOf(trackNumber);

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

//                if(trackNo <= lastPlace + 1) {
//                    possiblyRead = true;
//                } else {
//                    possiblyRead = false;
//                }
//
//                if(trackNo <= startPlace + 1) {
//                    alreadyRead = true;
//                } else {
//                    alreadyRead = false;
//                }

                meditationModel.setaFileNumber(i);
                meditationModel.setaTitle(meditationTitle);
//                chapterModel.setaBookDir(bookSubDirectory);
//                chapterModel.setaChapter(nameChapter);
//                chapterModel.setaDuration(out);
                meditationModel.setaRawDuration(dur);
//                chapterModel.setaPath(chapterPath);
//                chapterModel.setaCover(coverPath);
//                chapterModel.setRead(alreadyRead);
//                chapterModel.setPossiblyRead(possiblyRead);
//                chapterModel.setPreviousStart(startPlace);
//                chapterModel.setPreviousLast(lastPlace);
                tempMeditationList.add(meditationModel);
//                mClock.setFormat24Hour("HH:mm");

                // Sort the chapters chronologically
//                Collections.sort(tempMeditationList, (obj1, obj2) -> obj1.getaMeditation().compareToIgnoreCase(obj2.getaMeditation()));

//                 close object
//                metaRetriever.release();
            }
        }
        return tempMeditationList;


//
//    public void addBookData(String title) {
//
//        File newBook = new File(dataFiles, title);
//        if (!newBook.exists()) {
//
//            try {
//                FileOutputStream fos = new FileOutputStream(newBook);
//                OutputStreamWriter book = new OutputStreamWriter(fos);
//                book.write("-1");
//                book.close();
//                fos.flush();
//                fos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        int bookNo = item.getGroupId();
//        switch (item.getItemId())
//        {
//            case 121:
//            selectBook(bookNo);
//            customToast("Listen to book");
//            return true;
//
//            case 122:
//            resetBook(bookNo);
//            return true;
//
//            case 123:
//            confirmDeleteBook(bookNo);
//            return true;
//
//            case 124:
//            // no action, close menu
//            return true;
//
//            default:
//                return super.onContextItemSelected(item);
//        }
//
//    }

//    private void displayMessage(String message) {
//
////        Snackbar.make(findViewById(R.id.libraryView),message,Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }

//    private void selectMeditation(int itemPosition) {
//
//            String bookName = tempMeditationList.get(itemPosition).getaTitle();
//            String bookAuthor = tempMeditationList.get(itemPosition).getaAuthor();
//            String bookDirectory = tempMeditationList.get(itemPosition).getaPath();
//
//            Intent intent = new Intent(context, ListChapterActivity.class);
//            intent.putExtra("bookName", bookName);
//            intent.putExtra("bookPath", bookDirectory);
//
//            context.startActivity(intent);
//    }

//    private void resetBook(int itemPosition) {
//
//        String title = allBookDirectories.get(itemPosition).getaTitle();
//        customToast("Resetting ");
//        updateProgress.addBookProgress(dataFiles, title, -1, -1);
//    }

//   private void deleteBook(int itemPosition) {
//
//        customToast("Delete book");
//        String title = allBookDirectories.get(itemPosition).getaTitle();
//
//        // Delete progress file
//        File file = new File(dataFiles, title);
//        boolean deletedProgress = file.delete();
//
//       String bookFilesDir = allBookDirectories.get(itemPosition).getaPath();
//       File bookPath = new File(Environment.getExternalStorageDirectory().toString() + "/AudioBooks/" + bookFilesDir);
//
//        boolean deletedFiles = deleteDirectory(bookPath);
//   }

//    static public boolean deleteDirectory(File path) {
//
//        if( path.exists() ) {
//              File[] files = path.listFiles();
//              if (files == null) {
//                  return true;
//              }
//              for(int i=0; i<files.length; i++) {
//                 if(files[i].isDirectory()) {
//                   deleteDirectory(files[i]);
//                 }
//                 else {
//                   files[i].delete();
//                 }
//              }
//        }
//        return( path.delete() );
//    }
//    public void confirmDeleteBook(int bookNo) {
//        String bookName = tempBookList.get(bookNo).getaTitle();
//        AlertDialog.Builder dialog = new AlertDialog.Builder(ListBookActivity.this);
//        dialog.setCancelable(false);
//        dialog.setTitle("Confirm Delete");
//        dialog.setMessage("Are you sure you want to delete\n\n   ' " + bookName + " ' ?\n");
//        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                deleteBook(bookNo);
//            }
//        })
//                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//        final AlertDialog alert = dialog.create();
//        alert.show();
//    }

//    @SuppressLint("ResourceType")
//    public void customToast(String message) {
//        Toast ToastMessage = Toast.makeText(this,message, Toast.LENGTH_SHORT);
//        View toastView = ToastMessage.getView();
//        toastView.setBackgroundResource(R.layout.toast_background_color);
//        TextView messageText = toastView.findViewById(android.R.id.message);
//        messageText.setTextColor(Color.parseColor("#000000"));
//        messageText.setBackgroundColor(Color.parseColor("#787878"));
//        ToastMessage.show();
//
//    }
    }
}
