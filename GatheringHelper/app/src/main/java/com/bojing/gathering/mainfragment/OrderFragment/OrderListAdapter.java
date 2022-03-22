package com.bojing.gathering.mainfragment.OrderFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.OrderInfo;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {
    private ArrayList<OrderInfo> orderLists;
    private Activity _this;

    public OrderListAdapter(Activity context) {
        _this = context;
    }

    public void setDate(ArrayList<OrderInfo> orderListsFrom) {
        this.orderLists = orderListsFrom;
    }

    @Override
    public int getCount() {
        if (orderLists == null) {
            return 0;
        } else {
            return orderLists.size();
        }
    }

    @Override
    public OrderInfo getItem(int position) {
        if (orderLists == null) {
            return null;
        } else {
            return orderLists.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderInfo order = orderLists.get(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(_this, R.layout.order_item, null);
            holder.num = (TextView) convertView.findViewById(R.id.order_num_tv);
            holder.state = (TextView) convertView.findViewById(R.id.order_state_tv);
            holder.time = (TextView) convertView.findViewById(R.id.order_time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (order != null) {
            if (TextUtils.isEmpty(order.getOrderNumber())) {
                holder.num.setText("暂无");
            } else {
                holder.num.setText(order.getOrderNumber());
            }
            if (TextUtils.isEmpty(order.getOrderNumber())) {
                holder.state.setText("暂无");
            } else {
                switch (order.getOrderStatus()) {
                    case "1":
                        holder.state.setText("待支付");
                        break;
                    case "2":
                        holder.state.setText("已支付");
                        break;
                    default:
                        holder.state.setText("暂无");
                        break;
                }
            }
            if (TextUtils.isEmpty(order.getPayDate())) {
                holder.time.setText("暂无");
            } else {
                holder.time.setText(order.getPayDate());
            }
        }
        return convertView;

    }

    class ViewHolder {
        public TextView num;
        public TextView state;
        public TextView time;

    }
}
