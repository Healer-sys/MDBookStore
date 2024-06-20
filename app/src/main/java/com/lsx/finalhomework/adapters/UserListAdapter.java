package com.lsx.finalhomework.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsx.finalhomework.R;
import com.lsx.finalhomework.entities.AESUtil;
import com.lsx.finalhomework.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<User> userList;
    OnItemClickListener mItemClickListener;

    public UserListAdapter(List<User> list ) {
        this.userList = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
    private void onItemClick(View v) {
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_fragment_item, parent, false);
        return new UserListAdapter.ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userList == null ? null : userList.get(position);
        if (user != null) {
            UserListAdapter.ContentViewHolder contentViewHolder = (UserListAdapter.ContentViewHolder) holder;
            contentViewHolder.itemView.setTag(user.getId());
            contentViewHolder.idView.setText(String.valueOf(user.getId()));
            contentViewHolder.nameView.setText(user.getUsername());
            // 解密并显示
            try {
                contentViewHolder.passwordView.setText(AESUtil.decrypt(user.getPassword()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            contentViewHolder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(v));
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void setOnItemClickListener(UserListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public final TextView idView;
        public final TextView nameView;
        public final TextView passwordView;

        public ContentViewHolder(View view) {
            super(view);
            idView = view.findViewById(R.id.user_id);
            nameView = view.findViewById(R.id.user_name);
            passwordView = view.findViewById(R.id.user_password);
        }
    }
}
