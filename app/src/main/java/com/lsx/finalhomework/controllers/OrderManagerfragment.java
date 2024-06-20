package com.lsx.finalhomework.controllers;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsx.finalhomework.MyAuth;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.adapters.MyOrderRecyclerViewAdapter;
import com.lsx.finalhomework.adapters.UserOrderListAdapter;
import com.lsx.finalhomework.entities.Order;
import com.lsx.finalhomework.entities.OrderService;
import com.lsx.finalhomework.entities.User;

import java.util.ArrayList;
import java.util.List;

public class OrderManagerfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    List<OrderService> ostList;
    OrderService orderService;
    List<Order> orderList;
    UserOrderListAdapter Adapter;
    static OrderManagerfragment instance;

    public OrderManagerfragment() {

    }


    public static OrderManagerfragment newInstance(String param1, String param2) {
        OrderManagerfragment fragment = new OrderManagerfragment();
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
        //获取用户
        MyAuth myAuth = new MyAuth(getContext());
        List<User> userList = myAuth.getUserList();
        orderList = new ArrayList<>();
        //遍历用户
        for(int i = 0; i < userList.size(); i++){
            //获取每个用户的订单
            orderService = (new OrderService(getContext(), userList.get(i).getId()));
            orderList.addAll(orderService.getOrderList());
        }
        Adapter = new UserOrderListAdapter(orderList);
        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_manager, container, false);
        recyclerView = view.findViewById(R.id.order_manager_recycler_view);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        Adapter = new UserOrderListAdapter(orderList);
        recyclerView.setAdapter(Adapter);
        return view;
    }
}