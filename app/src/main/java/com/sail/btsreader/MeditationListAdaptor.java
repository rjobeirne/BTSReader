package com.sail.btsreader;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MeditationListAdaptor extends RecyclerView.Adapter<MeditationListAdaptor.MeditationViewHolder> {

    private ArrayList<MeditationModel> meditationDataSet;
    private LayoutInflater mInflater;
    Context mContext;
    public String mBookTitle, mBookAuthor;
    public String bookName;

    public MeditationListAdaptor(Context context, ArrayList<MeditationModel> meditationModelList) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        meditationDataSet = meditationModelList;
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
    public void onBindViewHolder(@NonNull MeditationViewHolder bookViewHolder, int i) {

        mBookTitle = meditationDataSet.get(i).getaTitle();
//        mBookAuthor = meditationDataSet.get(i).getaAuthor();


        bookViewHolder.mTextView.setText(mBookTitle);
        bookViewHolder.mAuthorView.setText(mBookAuthor);
    }

    @Override
    public int getItemCount() {
        return meditationDataSet.size();
    }

    public static class MeditationViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        Context nContext;
        public ArrayList<MeditationModel> meditationList;
        public TextView mTextView, mAuthorView;
        CardView mMeditationCardView;

        public MeditationViewHolder(Context context, ArrayList<MeditationModel> meditationModelList, View v) {
            super(v);
            nContext = context;
            meditationList = meditationModelList;
            mTextView = v.findViewById(R.id.book_name);
            mAuthorView = v.findViewById(R.id.book_author);
            mMeditationCardView = v.findViewById(R.id.mMeditationCardView);
            mMeditationCardView.setOnClickListener(this);
            mMeditationCardView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            // opening a new intent for chapter activity

            int itemPosition = getAdapterPosition();

            String bookName = meditationList.get(itemPosition).getaTitle();
//            String bookCover = meditationList.get(itemPosition).getaCover();
//            String bookAuthor = meditationList.get(itemPosition).getaAuthor();
//            String bookDirectory = meditationList.get(itemPosition).getaPath();

            Intent intent = new Intent(nContext, ListChapterActivity.class);
            intent.putExtra("bookName", bookName);
//            intent.putExtra("coverPath", bookCover);
//            intent.putExtra("bookPath", bookDirectory);

            nContext.startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View vMenu, ContextMenu.ContextMenuInfo menuInfo) {
//
//            int itemPosition = getAdapterPosition();
//            String bookName = bookList.get(itemPosition).getaTitle();
//            menu.setHeaderTitle(bookName);
//            menu.add(itemPosition, 121, 0, "Listen to book");
//            menu.add(itemPosition, 122, 1, "Reset book to unread");
//            menu.add(itemPosition, 123, 2, "Delete book");
//            menu.add(itemPosition, 124, 3, "Return to library");
//
//        }
    }

//    /**
//     * Function to add cover to player screen
//     * @param coverPath
//     */
//    public BitmapDrawable makeCover(String coverPath) {
//
////        View mCoverView = mCoverView.findViewById();
//        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(coverPath));
//        BitmapDrawable coverBMP = new BitmapDrawable(bitmap);
////        mCoverView.setBackground(coverBMP);
//        return coverBMP;
//    }

}
