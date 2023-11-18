package com.sail.btsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ListBookActivity extends AppCompatActivity {

    RecyclerView listBookView;
    BookListAdapter bookListAdapter;
    Context context;
    String nameDirectoryFiles, coverPath, author;

    String bookDirectory, bookTitle;
    File dataFiles;

    ArrayList<BookModel> allBookDirectories;
    BookProgress updateProgress;
    File[] books;
    final ArrayList<BookModel> tempBookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        updateProgress = new BookProgress();

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
        getBooks();

    }

    public void getBooks() {
        context = ListBookActivity.this;
        listBookView = findViewById(R.id.book_list_view);
        allBookDirectories = getBookList(context);

        bookListAdapter = new BookListAdapter(context, allBookDirectories);
        listBookView.setLayoutManager(new LinearLayoutManager(context));
        listBookView.setAdapter(bookListAdapter);
    }

    public ArrayList<BookModel> getBookList(final Context context) {



        String path = Environment.getExternalStorageDirectory().toString() + "/AudioBooks";

        File directory = new File(path);
        books = directory.listFiles();

        // load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        for (int i = 0; i < books.length; i++) {
            bookDirectory = books[i].getName();

                String intPath = path + "/" + bookDirectory;
                File intDir = new File(intPath);
                File[] intFiles = intDir.listFiles();

                bookTitle = null;
                author = null;

                coverPath = null;

                for (int j = 0; j < intFiles.length; j++) {
                    nameDirectoryFiles = intFiles[j].getName();
                    if (nameDirectoryFiles.endsWith(".jpg")) {
                        coverPath = intDir + "/" + nameDirectoryFiles;
                        break;
                    }
                }

                for (int k = 0; k < books.length; k++) {

                    if (intDir.listFiles().length == 0) {
                        String emptyDirectory = "Directory " + bookDirectory + " is empty";
                        customToast(emptyDirectory);
//                        Toast.makeText(this, emptyDirectory, Toast.LENGTH_LONG).show();
                        bookTitle = bookDirectory;
                        break;
                    } else {
                    String audFile = intFiles[k].getName();
                        if (audFile.endsWith(".mp3")) {
                            String filePath = intDir + "/" + audFile;
                            metaRetriever.setDataSource(filePath);
                            bookTitle = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                            if (bookTitle == null) {
                                bookTitle = bookDirectory;
                            }
                            author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                            if (author == null) {
                                author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                            }
                            if (author == null) {
                                author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
                            }
                            break;
                        }
                    }
                }

                if (books[i].isDirectory()) {
                    BookModel bookModel = new BookModel();

                    bookModel.setaTitle(bookTitle);
                    bookModel.setaCover(coverPath);
                    bookModel.setaAuthor(author);
                    bookModel.setaPath(bookDirectory);

                    tempBookList.add(bookModel);
                    addBookData(bookTitle);

                    // Sort the library alphabetically
                    Collections.sort(tempBookList, (obj1, obj2) -> obj1.getaTitle().compareToIgnoreCase(obj2.getaTitle()));
                }
            }

        return tempBookList;
    }

    public void addBookData(String title) {

        File newBook = new File(dataFiles, title);
        if (!newBook.exists()) {

            try {
                FileOutputStream fos = new FileOutputStream(newBook);
                OutputStreamWriter book = new OutputStreamWriter(fos);
                book.write("-1");
                book.close();
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int bookNo = item.getGroupId();
        switch (item.getItemId())
        {
            case 121:
            selectBook(bookNo);
            customToast("Listen to book");
            return true;

            case 122:
            resetBook(bookNo);
            return true;

            case 123:
            confirmDeleteBook(bookNo);
            return true;

            case 124:
            // no action, close menu
            return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    private void displayMessage(String message) {

//        Snackbar.make(findViewById(R.id.libraryView),message,Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void selectBook(int itemPosition) {

            String bookName = tempBookList.get(itemPosition).getaTitle();
            String bookCover = tempBookList.get(itemPosition).getaCover();
            String bookAuthor = tempBookList.get(itemPosition).getaAuthor();
            String bookDirectory = tempBookList.get(itemPosition).getaPath();

            Intent intent = new Intent(context, ListChapterActivity.class);
            intent.putExtra("bookName", bookName);
            intent.putExtra("coverPath", bookCover);
            intent.putExtra("bookPath", bookDirectory);

            context.startActivity(intent);
    }

    private void resetBook(int itemPosition) {

        String title = allBookDirectories.get(itemPosition).getaTitle();
        customToast("Resetting ");
        updateProgress.addBookProgress(dataFiles, title, -1, -1);
    }

   private void deleteBook(int itemPosition) {

        customToast("Delete book");
        String title = allBookDirectories.get(itemPosition).getaTitle();

        // Delete progress file
        File file = new File(dataFiles, title);
        boolean deletedProgress = file.delete();

       String bookFilesDir = allBookDirectories.get(itemPosition).getaPath();
       File bookPath = new File(Environment.getExternalStorageDirectory().toString() + "/AudioBooks/" + bookFilesDir);

        boolean deletedFiles = deleteDirectory(bookPath);
   }

    static public boolean deleteDirectory(File path) {

        if( path.exists() ) {
              File[] files = path.listFiles();
              if (files == null) {
                  return true;
              }
              for(int i=0; i<files.length; i++) {
                 if(files[i].isDirectory()) {
                   deleteDirectory(files[i]);
                 }
                 else {
                   files[i].delete();
                 }
              }
        }
        return( path.delete() );
    }
    public void confirmDeleteBook(int bookNo) {
        String bookName = tempBookList.get(bookNo).getaTitle();
        AlertDialog.Builder dialog = new AlertDialog.Builder(ListBookActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Are you sure you want to delete\n\n   ' " + bookName + " ' ?\n");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                deleteBook(bookNo);
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    @SuppressLint("ResourceType")
    public void customToast(String message) {
        Toast ToastMessage = Toast.makeText(this,message, Toast.LENGTH_SHORT);
        View toastView = ToastMessage.getView();
        toastView.setBackgroundResource(R.layout.toast_background_color);
        TextView messageText = toastView.findViewById(android.R.id.message);
        messageText.setTextColor(Color.parseColor("#000000"));
        messageText.setBackgroundColor(Color.parseColor("#787878"));
        ToastMessage.show();

    }
}
