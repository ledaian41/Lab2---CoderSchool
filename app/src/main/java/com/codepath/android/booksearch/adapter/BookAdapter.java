package com.codepath.android.booksearch.adapter;

import android.content.Context;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> mBooks;
    private Context context;
    private Listener mListener;

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public interface Listener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context) {
        this.context = context;
        this.mBooks = new ArrayList<>();
    }

    public void setBooks(List<Book> books) {
        // TODO: Insert your code here
        mBooks.clear();
        mBooks.addAll(books);
        notifyDataSetChanged();
    }

    public void addBooks(List<Book> books) {
        // int startPosition = getItemCount();
        // TODO: Insert your code here
        mBooks.addAll(mBooks.size(), books);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: Insert your code here
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: Insert your code here
        final Book book = mBooks.get(position);
        bindViewHolder((ViewHolder) holder, book);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onItemClick(book);
            }
        });
    }

    private void bindViewHolder(ViewHolder holder, Book book) {
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        Picasso.with(context).load(book.getCoverUrl()).into(holder.ivCover);
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
