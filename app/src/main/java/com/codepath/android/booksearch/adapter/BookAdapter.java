package com.codepath.android.booksearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ListClickListener mListClickListener;
    private List<Book> mBooks;

    public BookAdapter() {
        this.mBooks = new ArrayList<>();
    }

    public void setBooks(List<Book> books) {
        mBooks = books;
        notifyDataSetChanged();
    }

    public void addBooks(List<Book> books) {
        int startPosition = getItemCount();
        // TODO: Insert your code here
    }

    public void setListClickListener(ListClickListener listClickListener) {
        mListClickListener = listClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: Insert your code here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: Insert your code here
        final Book book = mBooks.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Context mContext = viewHolder.ivCover.getContext();
        viewHolder.tvTitle.setText(book.getTitle());
        viewHolder.tvAuthor.setText(book.getAuthor());
        Picasso.with(mContext)
                .load(book.getCoverUrl())
                .placeholder(R.drawable.ic_nocover)
                .into(viewHolder.ivCover);
        viewHolder.entireItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListClickListener != null)
                    mListClickListener.onBookItemClick(book);
            }
        });

    }

    @Override
    public int getItemCount() {
        // TODO: Update this function
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivCover)
        ImageView ivCover;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvAuthor)
        TextView tvAuthor;

        @BindView(R.id.entireItem)
        RelativeLayout entireItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListClickListener {
        void onBookItemClick(Book book);
    }
}
