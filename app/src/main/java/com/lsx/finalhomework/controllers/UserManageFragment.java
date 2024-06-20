package com.lsx.finalhomework.controllers;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lsx.finalhomework.MyAuth;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.adapters.UserListAdapter;
import com.lsx.finalhomework.entities.AESUtil;
import com.lsx.finalhomework.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserManageFragment extends Fragment implements UserListAdapter.OnItemClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    MyAuth myAuth;
    private List<User> userList;
    RecyclerView recyclerView;
    UserListAdapter adapter;
    ArrayList userListArrayList;
    GridLayoutManager gridLayoutManager;

    public UserManageFragment() {

    }

    public static UserManageFragment newInstance(String param1, String param2) {
        UserManageFragment fragment = new UserManageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manage, container, false);
        myAuth = new MyAuth(getContext());

        recyclerView = view.findViewById(R.id.user_list);

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        userListArrayList = new ArrayList<>();
        userListArrayList.addAll(myAuth.getUserList());
        adapter = new UserListAdapter(userListArrayList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(View v) {
        //删除修改书本信息
        int position = recyclerView.getChildAdapterPosition(v);
//        Toast.makeText(getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();

        //设置对话框
        Bundle bundle = new Bundle();
        Object item = userListArrayList.get(position);
        if (item instanceof User) {
            User user = new User();
            bundle.putInt("id", ((User) item).getId());
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //获取布局
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit, null);

            //获取输入框
            EditText et_username = view.findViewById(R.id.et_username);
            EditText et_password = view.findViewById(R.id.et_password);
            try{
                et_username.setText(((User) item).getUsername());
                et_password.setText(AESUtil.decrypt(((User) item).getPassword()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            builder.setTitle("用户信息修改");
            builder.setMessage("输入信息用于对用户信息进行修改");
            builder.setIcon(R.drawable.usermanage);    //设置图标
            builder.setView(view);

            builder.setNeutralButton("修改", (dialog, which) -> {
                if (!et_username.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty()) {
                    try {
                        ((User) item).setPassword(AESUtil.encrypt(et_password.getText().toString()));
                        ((User) item).setUsername(et_username.getText().toString());
                        myAuth.updateUser((User) item);
                        Toast.makeText(getContext(),"用户信息修改成功",Toast.LENGTH_SHORT).show();
                        //刷新Fragment
                        UiUpdate();
                        onStart();
                    }
                    catch (Exception e) {
                        Toast.makeText(getContext(),"用户信息修改失败",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"用户信息修改失败",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("删除", (dialog, which) -> {
                myAuth.deleteuser(((User) item).getId());
                Toast.makeText(getContext(),"用户信息删除成功",Toast.LENGTH_SHORT).show();
                //刷新Fragment
                UiUpdate();
                onStart();
            });
            builder.create();
            builder.show();
        }
    }

    private void UiUpdate() {
        userListArrayList.clear();
        userListArrayList.addAll(myAuth.getUserList());
        adapter.notifyDataSetChanged();
    }
}