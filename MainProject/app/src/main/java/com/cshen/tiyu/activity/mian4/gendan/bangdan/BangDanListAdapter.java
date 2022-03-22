package com.cshen.tiyu.activity.mian4.gendan.bangdan;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.gendan.RenZhengUtils;
import com.cshen.tiyu.activity.mian4.gendan.niuren.NiuRenActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.gendan.RenZhengMatch;
import com.cshen.tiyu.domain.gendan.RenZhengPerson;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RotateTextView;


import de.greenrobot.event.EventBus;

public class BangDanListAdapter extends BaseAdapter{
	private ArrayList<RenZhengPerson> orderLists;
	private Activity _this;
	private int type;//2热门，3人气，0命中，1盈利，

	public BangDanListAdapter(Activity context,int typeFrom) {
		_this = context;
		type = typeFrom;
	}
	public void setDate(ArrayList<RenZhengPerson> orderListsFrom){
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
	public RenZhengPerson getItem(int position) {
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
		final ViewHolder holder;
		final RenZhengPerson rzp = getItem(position);
		if (convertView == null) {  
			holder = new ViewHolder();  			
			convertView = View.inflate(_this, R.layout.renzheng_item,null);
			holder.head_view = convertView.findViewById(R.id.head_view);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.v = (ImageView) convertView.findViewById(R.id.v);
			holder.beizhu = (TextView) convertView.findViewById(R.id.beizhu);
			holder.beizhuzhi = (TextView) convertView.findViewById(R.id.beizhuzhi);
			
			holder.beizhuother = convertView.findViewById(R.id.beizhuother);
			holder.beizhu1 = (TextView) convertView.findViewById(R.id.beizhu1);
			holder.beizhu1zhi = (TextView) convertView.findViewById(R.id.beizhu1zhi);
			holder.beizhu2 = (TextView) convertView.findViewById(R.id.beizhu2);
			holder.beizhu2zhi = (TextView) convertView.findViewById(R.id.beizhu2zhi);
			holder.shaidannum = (TextView) convertView.findViewById(R.id.shaidannum);

			holder.matchs = (LinearLayout) convertView.findViewById(R.id.matchs);

			holder.match1 = convertView.findViewById(R.id.match1);
			holder.macthkey1 = (TextView) convertView.findViewById(R.id.macthkey1);
			holder.gamename1 = (TextView) convertView.findViewById(R.id.gamename1);
			holder.hostteamname1 = (TextView) convertView.findViewById(R.id.hostteamname1);
			holder.gustteamname1 = (TextView) convertView.findViewById(R.id.gustteamname1);

			holder.tuijian = convertView.findViewById(R.id.tuijian);
			holder.tuijiantitle = (TextView) convertView.findViewById(R.id.tuijiantitle);
			holder.chuanfa = (RotateTextView) convertView.findViewById(R.id.chuanfa);
			holder.endtime = (TextView) convertView.findViewById(R.id.endtime);

			holder.tobuy = convertView.findViewById(R.id.tobuy);
			convertView.setTag(holder);
		} else {  
			holder = (ViewHolder)convertView.getTag();  
		} 

		xUtilsImageUtils.display(holder.head,R.mipmap.defaultniu,
				rzp.getSponsorUserPic());
		holder.name.setText(rzp.getSponsorName());
		String level = rzp.getSponsorUserLevelNew();
		if(TextUtils.isEmpty(level)){
			holder.v.setVisibility(View.INVISIBLE);
		}else{
			if(RenZhengUtils.getRenZhengUtils().getV(level)!=0){
				holder.v.setImageResource(RenZhengUtils.getRenZhengUtils().getV(level));
				holder.v.setVisibility(View.VISIBLE);
			}else{
				holder.v.setVisibility(View.INVISIBLE);
			}
		}

		holder.beizhu.setVisibility(View.VISIBLE);
		holder.beizhuzhi.setVisibility(View.VISIBLE);
		if(type!=4){
			int numberShaidan = rzp.getSponsorFormNumber();
			if(numberShaidan>1){
				holder.shaidannum.setVisibility(View.VISIBLE);
				if(numberShaidan>99){
					holder.shaidannum.setText("99+");
				}else{
					holder.shaidannum.setText(rzp.getSponsorFormNumber()+"");
				}
			}else {
				holder.shaidannum.setVisibility(View.GONE);
			}	
		}else{
			holder.beizhuother.setVisibility(View.GONE);
			holder.shaidannum.setVisibility(View.GONE);
		}

		switch(type){
		case 0:
			holder.beizhu.setText("近7单命中:");
			holder.beizhuzhi.setText("7中"+rzp.getTimeOfSeven());
			holder.beizhu1.setText("跟单:");
			holder.beizhu1zhi.setText(rzp.getCopiedSchemeNumber()+"人");
			holder.beizhu2.setText("自购:");
			holder.beizhu2zhi.setText(rzp.getSchemeCost()+"元");
			break;
		case 1:
			holder.beizhu.setText("近7单盈利率:");
			holder.beizhuzhi.setText(rzp.getEarningRateSeven());
			holder.beizhu1.setText("跟单:");
			holder.beizhu1zhi.setText(rzp.getCopiedSchemeNumber()+"人");
			holder.beizhu2.setText("自购:");
			holder.beizhu2zhi.setText(rzp.getSchemeCost()+"元");
			break;
		case 2:
			holder.beizhu.setText("自购金额:");
			holder.beizhuzhi.setText(rzp.getSchemeCost()+"元");
			holder.beizhu1.setText("近7单命中:");
			holder.beizhu1zhi.setText("7中"+rzp.getTimeOfSeven());
			holder.beizhu2.setText("跟单:");
			holder.beizhu2zhi.setText(rzp.getCopiedSchemeNumber()+"人");
			break;
		case 3:
			holder.beizhu.setText("跟单:");
			holder.beizhuzhi.setText(rzp.getCopiedSchemeNumber()+"人");
			holder.beizhu1.setText("近7单命中:");
			holder.beizhu1zhi.setText("7中"+rzp.getTimeOfSeven());
			holder.beizhu2.setText("自购:");
			holder.beizhu2zhi.setText(rzp.getSchemeCost()+"元");
			break;
		case 4:
			holder.beizhu.setText("周命中:");
			if(TextUtils.isEmpty(rzp.getHitRateWeek())
					||"0".equals(rzp.getHitRateWeek())){
				holder.beizhuzhi.setText("100%");
			}else{
				holder.beizhuzhi.setText(rzp.getHitRateWeek());
			}
			break;
		}
		String passtype = rzp.getPassTypeStr();
		if(!TextUtils.isEmpty(passtype)){
			if(passtype.contains(",")||passtype.contains("，")){
				String[] chuans = null;
				if(passtype.contains(",")){
					chuans = passtype.split(",");
				}
				if(passtype.contains("，")){
					chuans = passtype.split("，");
				}
				holder.chuanfa.setText(chuans[0]);
			}else{
				holder.chuanfa.setText(passtype);
			}
		}
		if(TextUtils.isEmpty(rzp.getRecommendTitle())){
			holder.tuijian.setVisibility(View.GONE);
		}else{
			holder.tuijiantitle.setText(rzp.getRecommendTitle());
			holder.tuijian.setVisibility(View.VISIBLE);
		}
		holder.endtime.setText("截止"+rzp.getFirstMatchTime()); 

		if(rzp.isCanCopy()){
			holder.tobuy.setVisibility(View.VISIBLE);
			holder.endtime.setVisibility(View.VISIBLE);
		}else{
			holder.tobuy.setVisibility(View.INVISIBLE);    
			holder.endtime.setVisibility(View.INVISIBLE);
		}

		if(rzp.getResultList().size()>0){
			RenZhengMatch rzm1 = rzp.getResultList().get(0);
			holder.macthkey1.setText(rzm1.getMatchKey());
			holder.gamename1.setText(rzm1.getGameName());
			holder.hostteamname1.setText(rzm1.getHomeTeamName());
			holder.gustteamname1.setText(rzm1.getGuestTeamName());
			holder.match1.setVisibility(View.VISIBLE);
			holder.chuanfa.setVisibility(View.VISIBLE); 
			holder.matchs.removeAllViews();
			if(rzp.getResultList().size()>1){
				for(int i=1;i<rzp.getResultList().size();i++){
					RenZhengMatch rzm2 = rzp.getResultList().get(i);
					View otherMatch = View.inflate(_this, R.layout.match_item,null);
					TextView macthkey2 = (TextView) otherMatch.findViewById(R.id.macthkey);  
					macthkey2.setText(rzm2.getMatchKey());
					TextView gamename2 = (TextView) otherMatch.findViewById(R.id.gamename);
					gamename2.setText(rzm2.getGameName());
					TextView hostteamname2 = (TextView) otherMatch.findViewById(R.id.hostteamname);
					hostteamname2.setText(rzm2.getHomeTeamName());
					TextView gustteamname2 = (TextView) otherMatch.findViewById(R.id.gustteamname);
					gustteamname2.setText(rzm2.getGuestTeamName());
					holder.matchs.addView(otherMatch);
				}
			}
		}else{
			holder.match1.setVisibility(View.GONE);
			holder.chuanfa.setVisibility(View.GONE);
		}
		holder.head_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentD3 = new Intent(_this,NiuRenActivity.class);
				intentD3.putExtra("id",rzp.getSponsorId());
				_this.startActivity(intentD3);
			}
		});
		holder.tobuy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					_this.startActivity(intentLogin);
				}else{
					String userId = MyDbUtils.getCurrentUser().getUserId();
					if( rzp.getSponsorId().equals(userId)){
						ToastUtils.showShort(_this, "不能跟自己的单子哦");
						return;
					}
					String	SchemeBackupsId = rzp.getSchemeBackupsId();
					long cost;
					try{
						cost = Long.parseLong(rzp.getSchemeCost())/rzp.getMultiple();
					}catch(Exception e){
						cost = 0l;
						e.printStackTrace();
					}	
					EventBus.getDefault().post("toPayGenDan:"+SchemeBackupsId+","+cost+","+type); // 发送本地消息请求.
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		public View head_view;
		public ImageView head;
		public TextView shaidannum;
		public TextView name;
		public ImageView v;
		public TextView beizhu,beizhuzhi;
		public View beizhuother;
		public TextView beizhu1,beizhu1zhi;
		public TextView beizhu2,beizhu2zhi;
		public LinearLayout matchs;

		public View match1;
		public TextView macthkey1;
		public TextView gamename1;
		public TextView hostteamname1,gustteamname1;

		public RotateTextView chuanfa;
		public View tuijian;
		public TextView tuijiantitle;
		public TextView endtime;
		public View tobuy;
	}
}
