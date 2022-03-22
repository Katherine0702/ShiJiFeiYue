package com.cshen.tiyu.activity.mian4.personcenter.orders;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.order.Order;
import com.cshen.tiyu.utils.Util;

public class OrderListAdapter extends BaseAdapter{
	private ArrayList<Order> orderLists;
	private Activity _this;
	private int type;//0全部，1中奖，2待开奖，3追号
	public OrderListAdapter(Activity context,int typeFrom) {
		_this = context;
		type = typeFrom;
	}
	public void setDate(ArrayList<Order> orderListsFrom){
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
	public Order getItem(int position) {
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
		Order order = orderLists.get(position);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = View.inflate(_this, R.layout.order_item,null);
			holder.timedate = (TextView) convertView.findViewById(R.id.tv_time_date);
			holder.timehour = (TextView) convertView.findViewById(R.id.tv_time_hour);
			holder.state = (TextView) convertView.findViewById(R.id.tv_state);
			holder.moneystate = (TextView) convertView.findViewById(R.id.tv_moneystate);
			holder.type = (TextView) convertView.findViewById(R.id.tv_lotteryType);
			holder.money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.line = (TextView) convertView.findViewById(R.id.tv_line);
			holder.lineno = (TextView) convertView.findViewById(R.id.tv_lineno);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(order!=null){
			String time = order.getCreateTimeStr();
			if(!TextUtils.isEmpty(time)
					&&time.length()>10){
				holder.timedate.setText(time.substring(5,7) + "月");
				holder.timehour.setText(time.substring(8,10) + "");
			}
    		holder.timedate.setVisibility(View.VISIBLE);
    		holder.timehour.setVisibility(View.VISIBLE);
    		holder.lineno.setVisibility(View.INVISIBLE);
    		holder.line.setVisibility(View.VISIBLE);
            if(position>0){
            	String lastTime = orderLists.get(position-1).getCreateTimeStr();
            	if(!TextUtils.isEmpty(lastTime)
    					&&lastTime.length()>10){
                	if(lastTime.substring(5,10).endsWith(time.substring(5,10))){
                		holder.timedate.setVisibility(View.INVISIBLE);
                		holder.timehour.setVisibility(View.INVISIBLE);
                		holder.lineno.setVisibility(View.VISIBLE);
                	}
            	}
            }else if(position == 0){
        		holder.line.setVisibility(View.INVISIBLE);
            }
			String lotteryType = order.getLotteryType();
			if("SFZC14".equals(order.getZcPlayType())){
				lotteryType = "胜负彩";
			}else if("SFZC9".equals(order.getZcPlayType())){
				lotteryType = "任选九";
			}else{
				lotteryType = Util.getLotteryName(lotteryType);
			}
            if(type == 3){
            	holder.type.setText(lotteryType);
            }else{
    			if(order.getChaseId()>0){
    				holder.type.setText(lotteryType+"--追号");
    			}else{
    				holder.type.setText(lotteryType);
    			}
            }
			if("Together".equals(order.getShareType())){
				holder.money.setText("合买"+order.getMyCost() + "元");
			}else  if("COPY".equals(order.getShareType())){
				holder.money.setText("跟单"+order.getMyCost() + "元");
			}else{//self
				holder.money.setText("自购"+order.getMyCost() + "元");
			}
			
			holder.moneystate.setVisibility(View.GONE);
			holder.state.setTextColor(Color.parseColor("#a0a0a0"));
			String stateString = "";
			if (order.getSchemePrintState().equals("PRINT")) {
				stateString = "委托中";
			} else if (order.getSchemePrintState().equals("SUCCESS")) {
				stateString = "出票成功";
				if (isUpdateWon(order)) {
					if (isSuccessWon(order)) {
						holder.state.setTextColor(_this.getResources().getColor(R.color.mainred));
						holder.moneystate.setVisibility(View.VISIBLE);	
						stateString = order.getPrizeAfterTax()+"元";
					} else if (isUnSuccessWon(order)) {
						//stateString="撤单";
						stateString="未出票";
					} else {
						stateString="未中奖";
					}
				} else {
					stateString="待开奖";
				}
			} else if (order.getSchemePrintState().equals("FAILED")) {
				stateString = "出票失败";
				holder.state.setTextColor(_this.getResources().getColor(R.color.mainred));
			} else if (order.getSchemePrintState().equals("FULL")) {
				//stateString = "满员";
				stateString="未出票";
			} else if (order.getSchemePrintState().equals("UNFULL")) {
				//stateString = "未满";
				stateString="未出票";
			} else if (order.getSchemePrintState().equals("UNPRINT")) {
				stateString = "未出票";
			}
			
			holder.state.setText(stateString);
		}
		return convertView;

	}
	class ViewHolder {
		public TextView timedate;
		public TextView timehour;
		public TextView type;
		public TextView money;
		public TextView state;
		public TextView moneystate;
		public TextView line,lineno;

	}
	boolean isUpdateWon(Order order) {
		return order.getWinningUpdateStatus().equals("WINNING_UPDATED") 
				|| isUpdatePrize(order);
	}
	boolean isUpdatePrize(Order order) {
		return order.getWinningUpdateStatus().equals("PRICE_UPDATED");
	}
	boolean isSuccessWon(Order order) {
		return order.isWon() && (order.getSchemeState().equals("SUCCESS")
				||order.getSchemeState().equals("MANUAlESUCCESS"));
	}
	boolean isUnSuccessWon(Order order) {
		return order.isWon() && (order.getSchemeState().equals("CANCEL") 
				|| order.getSchemeState().equals("REFUNDMENT"));
	}
}
