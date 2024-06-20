package com.lsx.finalhomework.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsx.finalhomework.NWImageView;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.entities.Book;

import java.util.List;

public class UserBookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> bookList;
    OnItemClickListener mItemClickListener;

    public UserBookListAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }
    public interface OnItemClickListener {
        void onItemClick(View view);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_fragment_item, parent, false);
        return new UserBookListAdapter.ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 书本
        Book item = (Book) bookList.get(position);
        UserBookListAdapter.ContentViewHolder contentHolder = (UserBookListAdapter.ContentViewHolder) holder;
        contentHolder.itemView.setTag(item.getId());
        contentHolder.nameView.setText(item.getName());
        contentHolder.authorView.setText(item.getAuthor());
        contentHolder.priceView.setText(String.format("%s元", item.getPrice()));
        contentHolder.imgView.setImageURL(item.getImgUrl());
        contentHolder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(v));
    }

    private void onItemClick(View v) {
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public void setOnItemClickListener(UserBookListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameView;
        public final TextView authorView;
        public final NWImageView imgView;
        public final TextView priceView;

        public ContentViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.text_name);
            authorView = itemView.findViewById(R.id.text_author);
            imgView = itemView.findViewById(R.id.list_image);
            priceView = itemView.findViewById(R.id.text_price);
        }
    }
}
