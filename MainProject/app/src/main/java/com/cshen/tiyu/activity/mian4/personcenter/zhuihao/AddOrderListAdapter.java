package com.cshen.tiyu.activity.mian4.personcenter.zhuihao;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.zhuihao.AddOrder;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.Util;


public class AddOrderListAdapter  extends BaseAdapter{
	private int defaultBit;
	private ArrayList<AddOrder> orderLists;
	private Activity _this;
	public AddOrderListAdapter(Activity activity) {
		_this = activity;	
	}
	public void setDate(ArrayList<AddOrder> orderListsFrom){
		this.orderLists = orderListsFrom;
	}
	@Override
	public int getCount() {
		if(orderLists == null){
			return 0; 
		}else{
			return orderLists.size();
		}
	}
	@Override
	public AddOrder getItem(int position) {
		if(orderLists == null){
			return null; 
		}else{
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
		AddOrder order = orderLists.get(position);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(_this, R.layout.add_order_item,null);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.money = (TextView) convertView.findViewById(R.id.money);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String lotteryName = Util.getLotteryName(Util
				.getLotteryTypeToString(order.getLotteryId()));
		if(order.getLotteryId() == ConstantsBase.PL35){
			if(!TextUtils.isEmpty(order.getPlayType())
					&&order.getPlayType().contains("排五")){
				lotteryName = "排列5";
			}else{
				lotteryName = "排列3";
			}
		}
		holder.name.setText(lotteryName);
		switch(order.getLotteryId()){
		case ConstantsBase.DLT:
			defaultBit = R.mipmap.dlticon;
			break;
		case ConstantsBase.SSQ:
			defaultBit = R.mipmap.ssq;
			break;
		case ConstantsBase.SD115:
			defaultBit = R.mipmap.sd115icon;
			break;
		case ConstantsBase.GD115:
			defaultBit = R.mipmap.gd115icon;
			break;
		case ConstantsBase.Fast3:
			defaultBit = R.mipmap.fast3icon;
			break;
		case ConstantsBase.PL35:
			if(!TextUtils.isEmpty(order.getPlayType())
					&&order.getPlayType().contains("排五")){
				defaultBit = R.mipmap.pl5icon;
			}else{
				defaultBit = R.mipmap.pl3icon;
			}
			break;
		default:
			defaultBit = R.mipmap.ic_error;
			break;
		}
		holder.icon.setImageResource(defaultBit);
		LotteryTypeData currentLotteryTypeData = MyDbUtils
				.getCurrentLotteryTypeData();
		
		if (currentLotteryTypeData!=null && currentLotteryTypeData.getLotteryList()!=null && currentLotteryTypeData.getLotteryList().size()>0) {
			for (int i = 0; i < currentLotteryTypeData.getLotteryList().size(); i++) {
				if (lotteryName!=null && lotteryName.equals(currentLotteryTypeData.getLotteryList().get(i).getTitle()) ) {
					xUtilsImageUtils.display(holder.icon,defaultBit,
							currentLotteryTypeData.getLotteryList().get(i).getIcon());
				}
			}
		}
		holder.state.setText("STOPED".equals(order.getState())?"完成追号":"追号中");
		holder.time.setText(order.getChaseTime());
		DecimalFormat df = new DecimalFormat("0.00");
		holder.money.setText(df.format(order.getTotalCost())+"元");
		return convertView;

	}
	class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView state;
		public TextView time;
		public TextView money;

	}


}
