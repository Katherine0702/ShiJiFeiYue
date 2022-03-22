package com.bojing.gathering.mainfragment.MaKuFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.MoneyList;
import com.bojing.gathering.domain.OrderInfo;
import com.bojing.gathering.mainfragment.OrderFragment.OrderListAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/23.
 */

public class MaKuAdapter extends BaseAdapter {
    private ArrayList<MoneyList> moneyList;
    private Activity _this;

    public MaKuAdapter(Activity context) {
        _this = context;
    }

    public void setDate(ArrayList<MoneyList> orderListsFrom) {
        this.moneyList = orderListsFrom;
    }

    @Override
    public int getCount() {
        if (moneyList == null) {
            return 0;
        } else {
            return moneyList.size();
        }
    }

    @Override
    public MoneyList getItem(int position) {
        if (moneyList == null) {
            return null;
        } else {
            return moneyList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(_this, R.layout.money_item, null);
            holder.money = (TextView) convertView.findViewById(R.id.moneyitem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.money.setText(moneyList.get(position).getPrice());
        return convertView;
    }

    class ViewHolder {
        public TextView money;

    }
}
