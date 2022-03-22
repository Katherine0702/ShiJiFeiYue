package com.cshen.tiyu.activity.mian4.personcenter.redpacket;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3MainActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.activity.lottery.cai115.MainGd115Activity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.redpacket.RedPacket;
import com.cshen.tiyu.utils.Util;

public class RedPacketListAdapter extends BaseAdapter{
	private ArrayList<RedPacket> orderLists;
	private Activity _this;
	private int type;
	public RedPacketListAdapter(Activity activity,int type) {
		_this = activity;	
		this.type = type;
	}
	public void setDate(ArrayList<RedPacket> orderListsFrom){
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
	public RedPacket getItem(int position) {
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
		RedPacket order = orderLists.get(position);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(_this, R.layout.redpacket_item,null);
			holder.useno = (ImageView) convertView.findViewById(R.id.useno);
			holder.use = (TextView) convertView.findViewById(R.id.use);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.rmb = (TextView) convertView.findViewById(R.id.rmb_iv);
			holder.money = (TextView) convertView.findViewById(R.id.rmb_tv);
			holder.usefor = (TextView) convertView.findViewById(R.id.usefor);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(order!=null){
			holder.title.setText(order.getHongbaoName());
			if(order.getUsefor()!=null){
				if(order.getUsefor() == -1){
					holder.usefor.setText("全场通用");
					holder.use.setOnClickListener(new View.OnClickListener() {				
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
//							Intent intent=new Intent(_this, DLTMainActivity.class);
//							_this.startActivity(intent);
//							_this.finish();
							Intent intent=new Intent(_this, MainActivity.class);
							intent.putExtra("nowPage", "1");
							intent.putExtra("hasLogin", "yes");//
							_this.startActivity(intent);
							_this.finish();
						}
					});
				}else{
					String lotteryType = Util.getLotteryTypeToString(order.getUsefor());
					holder.usefor.setText("适用于"+lotteryType);
					// TODO Auto-generated method stub
					if(order.getUsefor() == ConstantsBase.DLT){
						holder.use.setOnClickListener(new View.OnClickListener() {				
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(_this, DLTMainActivity.class);
								_this.startActivity(intent);
								_this.finish();
								}
						});
					}if(order.getUsefor() == ConstantsBase.SD115){
						holder.use.setOnClickListener(new View.OnClickListener() {				
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(_this, Main115Activity.class);
								_this.startActivity(intent);
								_this.finish();
							}
						});
					}if(order.getUsefor() == ConstantsBase.GD115){
						holder.use.setOnClickListener(new View.OnClickListener() {				
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(_this, MainGd115Activity.class);
								_this.startActivity(intent);
								_this.finish();
							}
						});
					}if(order.getUsefor() == ConstantsBase.Fast3){
						holder.use.setOnClickListener(new View.OnClickListener() {				
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(_this, Fast3MainActivity.class);
								_this.startActivity(intent);
								_this.finish();
							}
						});
					}if(order.getUsefor() == ConstantsBase.JCZQ){
						holder.use.setOnClickListener(new View.OnClickListener() {				
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(_this, FootballMainActivity.class);
								_this.startActivity(intent);
								_this.finish();
							}
						});
					}if(order.getUsefor() == ConstantsBase.JCLQ){
						holder.use.setOnClickListener(new View.OnClickListener() {				
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(_this, BasketBallMainActivity.class);
								_this.startActivity(intent);
								_this.finish();
							}
						});
					}


				}
			}
			holder.time.setText("有效期至："+order.getAvailabilityTime());

			BigDecimal total = order.getTotalMoney();
			if(type == 0){
				holder.rmb.setTextColor(Color.parseColor("#FF3232"));
				holder.money.setTextColor(Color.parseColor("#FF3232"));
				holder.title.setTextColor(Color.parseColor("#3c3c3c"));
				holder.use.setVisibility(View.VISIBLE);
				holder.useno.setVisibility(View.GONE);
				BigDecimal used= order.getUsedMoney();
				float canUse = total.subtract(used).floatValue();
				if(total!=null
						&&used!=null){
					int one = (int)canUse;
					if(Float.parseFloat((one+"")) == canUse){
						holder.money.setText(one+"");
					}else{
						holder.money.setText(canUse+"");
					}
				}
			}else{
				holder.rmb.setTextColor(Color.parseColor("#777777"));
				holder.money.setTextColor(Color.parseColor("#777777"));
				holder.title.setTextColor(Color.parseColor("#777777"));
				holder.use.setVisibility(View.GONE);
				holder.useno.setVisibility(View.VISIBLE);
				if(order.getStatus() == 1){
					holder.useno.setImageResource(R.mipmap.redpacketused);
				}else{
					holder.useno.setImageResource(R.mipmap.redpacketpassed);
				}
				if(total!=null){
					float canToatl = total.floatValue();
					int two = (int)canToatl;
					if(Float.parseFloat((two+"")) == canToatl){
						holder.money.setText(two+"");
					}else{
						holder.money.setText(canToatl+"");
					}
				}
			}

		}
		return convertView;

	}
	class ViewHolder {
		public ImageView useno;
		public TextView use;
		public TextView title;
		public TextView rmb;
		public TextView money;
		public TextView usefor;
		public TextView time;

	}


}
