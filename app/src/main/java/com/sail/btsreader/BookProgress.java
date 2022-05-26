package com.sail.btsreader;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class BookProgress {

    public void addBookProgress(File files, String title, int progress) {

         File bookTitle = new File(files, title);
         String mProgress = String.valueOf(progress);

        // Write Progress to individual book folder file
         try {
                FileOutputStream fos = new FileOutputStream(bookTitle);
                OutputStreamWriter book = new OutputStreamWriter(fos);
                book.write(mProgress);
                book.close();
                fos.flush();
                fos.close();
             Log.e("stored progress :", mProgress);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void addCurrentBook(File files, String title, String bookPath, String coverPath ) {

        File currentBook = new File(files, "00currentBook");
        String currentBookInfo = title + "#" + bookPath + "$" + coverPath;

        try {
            FileOutputStream fos = new FileOutputStream(currentBook);
            OutputStreamWriter book = new OutputStreamWriter(fos);
            book.write(currentBookInfo);
            book.close();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBookProgress(Context context, File files, String title) {

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(title);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner((fis));
        scanner.useDelimiter("\\Z");
        String content = scanner.next();
        scanner.close();

        int place = Integer.parseInt(content);
        return place;
    }

}
