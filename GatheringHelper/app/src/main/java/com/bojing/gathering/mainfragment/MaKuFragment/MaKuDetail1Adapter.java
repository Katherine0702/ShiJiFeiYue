package com.bojing.gathering.mainfragment.MaKuFragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.MoneyOther;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/23.
 */

public class MaKuDetail1Adapter extends BaseAdapter {
    private ArrayList<MoneyOther> moneyList;
    private Activity _this;

    public MaKuDetail1Adapter(Activity context) {
        _this = context;
    }

    public void setDate(ArrayList<MoneyOther> orderListsFrom) {
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
    public MoneyOther getItem(int position) {
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
            convertView = View.inflate(_this, R.layout.moneydetail1_item, null);
            holder.tu = (ImageView) convertView.findViewById(R.id.tu);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.money.setText(moneyList.get(position).getPrice());
        if ("0".equals(moneyList.get(position).getIsHighlight())) {
            switch (moneyList.get(position).getPaymentId()) {
                case "1":
                    holder.tu.setImageResource(R.mipmap.zhifubao_hui);
                    break;
                case "2":
                    holder.tu.setImageResource(R.mipmap.weixin_hui);
                    break;
                case "3":
                    holder.tu.setImageResource(R.mipmap.zhifubao_hui);
                    break;
                case "4":
                    holder.tu.setImageResource(R.mipmap.zhifubao_hui);
                    break;
                case "5":
                    holder.tu.setImageResource(R.mipmap.zhifubao_hui);
                    break;
            }
        } else {
            switch (moneyList.get(position).getPaymentId()) {
                case "1":
                    holder.tu.setImageResource(R.mipmap.zhifubao);
                    break;
                case "2":
                    holder.tu.setImageResource(R.mipmap.weixin);
                    break;
                case "3":
                    holder.tu.setImageResource(R.mipmap.zhifubao);
                    break;
                case "4":
                    holder.tu.setImageResource(R.mipmap.zhifubao);
                    break;
                case "5":
                    holder.tu.setImageResource(R.mipmap.zhifubao);
                    break;
            }
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView tu;
        public TextView money;

    }
}
