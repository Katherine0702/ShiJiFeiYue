package com.bojing.gathering.mainfragment.MaKuFragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.MoneyOther;
import com.bojing.gathering.domain.MoneyOtherPic;
import com.bojing.gathering.util.xUtilsImageUtils;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/23.
 */

public class MaKuDetail2Adapter extends BaseAdapter {
    private ArrayList<MoneyOtherPic> moneyList;
    private Activity _this;

    public MaKuDetail2Adapter(Activity context) {
        _this = context;
    }

    public void setDate(ArrayList<MoneyOtherPic> orderListsFrom) {
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
    public MoneyOtherPic getItem(int position) {
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
            convertView = View.inflate(_this, R.layout.moneydetail2_item, null);
            holder.tu = (ImageView) convertView.findViewById(R.id.tu);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.msg = (TextView) convertView.findViewById(R.id.msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        xUtilsImageUtils.displayIN(holder.tu,R.mipmap.ic_launcher,getItem(position).getPicUrl());
        return convertView;
    }

    class ViewHolder {
        public ImageView tu;
        public TextView state;
        public TextView msg;

    }
}
