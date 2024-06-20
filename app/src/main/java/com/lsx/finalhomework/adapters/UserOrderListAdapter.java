package com.lsx.finalhomework.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsx.finalhomework.R;
import com.lsx.finalhomework.entities.Order;
import com.lsx.finalhomework.entities.OrderDetail;

import java.util.List;

public class UserOrderListAdapter extends RecyclerView.Adapter<UserOrderListAdapter.ViewHolder>{
    private final List<Order> orderList;

    public UserOrderListAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public UserOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_fragment_item, parent, false);
        return new UserOrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderListAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.idView.setText(String.format("%s", order.getId()));
        List<OrderDetail> orderDetails = order.getOrderDetails();
        double price = 0;
        for (OrderDetail orderDetail : orderDetails)
            price += orderDetail.getOrderPrice() * orderDetail.getQuantity();
        holder.countView.setText(String.format("%s", orderDetails.size()));
        holder.priceView.setText(String.format("%.2f", price));
        holder.timeView.setText(order.getOrderTime().format(Order.dateTimeFormatter));
        holder.u_id.setText(String.format("%s", order.getAccountId()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView idView;
        public final TextView countView;
        public final TextView priceView;
        public final TextView timeView;
        public final TextView u_id;

        public ViewHolder(View view) {
            super(view);
            idView = view.findViewById(R.id.text_order_id);
            countView = view.findViewById(R.id.text_order_count);
            priceView = view.findViewById(R.id.text_order_price);
            timeView = view.findViewById(R.id.text_order_time);
            u_id = view.findViewById(R.id.user_id);
        }
    }
}
