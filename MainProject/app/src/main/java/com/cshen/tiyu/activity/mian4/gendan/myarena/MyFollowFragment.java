package com.cshen.tiyu.activity.mian4.gendan.myarena;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQOrderDetailActivity;
import com.cshen.tiyu.activity.mian4.gendan.RenZhengUtils;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.MyFollowRecommend;
import com.cshen.tiyu.domain.gendan.MyFollowRecommendList;
import com.cshen.tiyu.domain.gendan.RenZhengMatch;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


public class MyFollowFragment extends Fragment  implements OnClickListener{
	private Activity _this;
	private View nodata;
	private TextView gendan;
	private RefreshListView refreshListView;

	private FollowAdapter myListAdapter = null;
	private ArrayList<MyFollowRecommend> orderLists;

	private ServiceGenDan gendanService ;
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private String userId,userPwd;
	private boolean isGetting = false;
	Bundle savedInstanceState;

	/***********
	 ******倍数相关
	 **************/
	public TextView realTime;//倍数
	public int timeIntMAX = 99999;//总倍数
	public View timekeybroad;//画的键盘和付款栏
	private TextView number10,number25,number50,number100,number500;
	private int choosePosition;
	private HashMap<Integer,Integer> psitiontime = new HashMap<Integer,Integer>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		View view = inflater.inflate(R.layout.myfouce, container,false);
		_this = this.getActivity();
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}
	private void initView(View view) {
		nodata = view.findViewById(R.id.nodata);
		gendan  = (TextView) view.findViewById(R.id.guanzhu);
		((TextView) view.findViewById(R.id.guanzhutxt)).setText("暂无跟单");
		gendan.setText("立即跟单");
		gendan.setOnClickListener(this);

		timekeybroad =  view.findViewById(R.id.dlt_pay_numbertime);
		view.findViewById(R.id.suretime).setOnClickListener(this);
		view.findViewById(R.id.cleartime).setOnClickListener(this);
		view.findViewById(R.id.number0).setOnClickListener(this);
		view.findViewById(R.id.number1).setOnClickListener(this);
		view.findViewById(R.id.number2).setOnClickListener(this);
		view.findViewById(R.id.number3).setOnClickListener(this);
		view.findViewById(R.id.number4).setOnClickListener(this);
		view.findViewById(R.id.number5).setOnClickListener(this);
		view.findViewById(R.id.number6).setOnClickListener(this);
		view.findViewById(R.id.number7).setOnClickListener(this);
		view.findViewById(R.id.number8).setOnClickListener(this);
		view.findViewById(R.id.number9).setOnClickListener(this);
		number10 = (TextView) view.findViewById(R.id.time10);
		number10.setOnClickListener(this);
		number25 = (TextView) view.findViewById(R.id.time25);
		number25.setOnClickListener(this);
		number50 = (TextView) view.findViewById(R.id.time50);
		number50.setOnClickListener(this);
		number100 = (TextView) view.findViewById(R.id.time100);
		number100.setOnClickListener(this);
		number500 = (TextView) view.findViewById(R.id.time500);
		number500.setOnClickListener(this);


		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		myListAdapter = new FollowAdapter(); 
		refreshListView.setAdapter(myListAdapter);	
		// 添加监听器
		refreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 自动生成的方法存根
				psitiontime.clear();
				requestDataFromServer(false); // 冲服务器中获取刷新的数据
			}

			@Override
			public void onLoadMore() {
				// TODO 自动生成的方法存根
				requestDataFromServer(true);
			}

			private void requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					gendanService.MyFollowRecommend(_this,start,step,userId,userPwd,"COPY",new CallBack<MyFollowRecommendList>() {

						@Override
						public void onSuccess(MyFollowRecommendList t) {
							// TODO 自动生成的方法存根
							dodata(t,false);
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							if(myListAdapter!=null){
								myListAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
						}
					});
				} else {
					initAllOrdersForList();
				}

			}
		});
	}

	void initdata() {
		if(!isGetting){
			isGetting = true;
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
			gendanService = ServiceGenDan.getInstance();
			initAllOrdersForList();
		}
	}

	private void initAllOrdersForList() {
		more = 0;
		gendanService.MyFollowRecommend(_this,more,step,userId,userPwd,"COPY",
				new CallBack<MyFollowRecommendList>() {

			@Override
			public void onSuccess(MyFollowRecommendList t) {
				// TODO 自动生成的方法存根	
				dodata(t,true);
				isGetting = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(myListAdapter!=null){
					myListAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(MyFollowRecommendList t,boolean isRefresh){
		if(isRefresh){
			if(orderLists== null){
				orderLists = new ArrayList<MyFollowRecommend>();
			}else{
				orderLists.clear();
			}
			orderLists.addAll(t.getResultList());               
			if (orderLists.size()==0) {
				nodata.setVisibility(View.VISIBLE);
				refreshListView.setVisibility(View.GONE);
			}else{
				nodata.setVisibility(View.GONE);
				refreshListView.setVisibility(View.VISIBLE);
			}
			myListAdapter.setDate(orderLists);
		}else{
			ArrayList<MyFollowRecommend> newlists = t.getResultList();// 加载数据结束
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(orderLists== null){
				orderLists = new ArrayList<MyFollowRecommend>();
			}
			orderLists.addAll(newlists);
		}
		if(myListAdapter!=null){
			myListAdapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
	}
	public void setTime(String i){
		clearTime();
		int number = 0 ;
		try{
			number = Integer.parseInt(realTime.getText().toString());
		}catch(Exception e){
			e.printStackTrace();
			number = 0 ;
		}
		if(number>timeIntMAX||realTime.getText().toString().length()==5){
			ToastUtils.showShort(_this, "倍数不能超过"+timeIntMAX);
			realTime.setText(timeIntMAX+"");
		}else{
			if(number==0){
				realTime.setText(i);
			}else{
				realTime.setText(number+i);
			}
		}
		try{
			number = Integer.parseInt(realTime.getText().toString());
		}catch(Exception e){
			e.printStackTrace();
			number = 0 ;
		}
		psitiontime.put(choosePosition, number);
	}
	public void clearTime(){
		number10.setTextColor(Color.parseColor("#333333"));
		number10.setBackgroundResource(R.drawable.dlt_tzback);
		number25.setTextColor(Color.parseColor("#333333"));
		number25.setBackgroundResource(R.drawable.dlt_tzback);
		number50.setTextColor(Color.parseColor("#333333"));
		number50.setBackgroundResource(R.drawable.dlt_tzback);
		number100.setTextColor(Color.parseColor("#333333"));
		number100.setBackgroundResource(R.drawable.dlt_tzback);
		number500.setTextColor(Color.parseColor("#333333"));
		number500.setBackgroundResource(R.drawable.dlt_tzback);
	}
	class FollowAdapter extends BaseAdapter {
		private ArrayList<MyFollowRecommend> rzlist;
		public void setDate(ArrayList<MyFollowRecommend> orderListsFrom){
			this.rzlist = orderListsFrom;
		}
		@Override
		public int getCount() {
			if(rzlist == null){
				return 0;
			}else{
				return rzlist.size();
			}
		}

		@Override
		public MyFollowRecommend getItem(int position) {
			if(rzlist == null){
				return new MyFollowRecommend();
			}else{
				return (MyFollowRecommend)rzlist.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final MyFollowRecommend rzp = getItem(position);
			if (convertView == null) {  
				holder = new ViewHolder();  
				convertView = View.inflate(_this, R.layout.gendan_item_myfollow,null);
				holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
				holder.head_view = convertView.findViewById(R.id.head_view);
				holder.head = (ImageView) convertView.findViewById(R.id.head);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.v = (ImageView) convertView.findViewById(R.id.v);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				holder.detils = (TextView) convertView.findViewById(R.id.detils);

				holder.matchs = (LinearLayout) convertView.findViewById(R.id.matchs);

				holder.match1 = convertView.findViewById(R.id.match1);
				holder.matchkey1 = (TextView) convertView.findViewById(R.id.matchkey1);
				holder.hostteamname1 = (TextView) convertView.findViewById(R.id.hostteamname1);
				holder.gustteamname1 = (TextView) convertView.findViewById(R.id.gustteamname1);
				holder.touzhu1 = (TextView) convertView.findViewById(R.id.touzhu1);

				holder.chuanfa = (TextView) convertView.findViewById(R.id.chuanfa);
				holder.realtimeview = convertView.findViewById(R.id.realtimeview);
				holder.realtime = (TextView) convertView.findViewById(R.id.realtime);
				//disableShowSoftInput(holder.realtime);
				holder.tobuy = convertView.findViewById(R.id.tobuy);
				holder.prize = (ImageView) convertView.findViewById(R.id.prize);
				convertView.setTag(holder);
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			} 
			xUtilsImageUtils.display(holder.head,R.mipmap.defaultniu,
					rzp.getCopiedUserPic());
			holder.name.setText(rzp.getCopiedUserName());
			String level = rzp.getCopiedUserLevelNew();
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
					if(chuans.length>2){
						holder.chuanfa.setText(chuans[0]+","+chuans[1]+"...");
					}else{
						if(passtype.split(",").length == 1){
							holder.chuanfa.setText(chuans[0]);
						}else{
							holder.chuanfa.setText(chuans[0]+","+chuans[1]);
						}
					}
				}else{
					holder.chuanfa.setText(passtype);
				}
			}
			holder.endtime.setText(rzp.getCreateTime());
			String moneyStr = "<html><font color=\"#666666\">已跟投"
					+"</font><font color=\"#FF3232\">"+rzp.getSchemeCost()+"元"
					+ "</font></html>";
			holder.money.setText(Html.fromHtml(moneyStr));

			if(rzp.getItems().size()>0){
				RenZhengMatch rzm1 = rzp.getItems().get(0);
				holder.matchkey1.setText(rzm1.getMatchKey());
				holder.hostteamname1.setText(rzm1.getHomeTeamName());
				holder.gustteamname1.setText(rzm1.getGuestTeamName());
				holder.touzhu1.setTextColor(getResources().getColor(R.color.mainred));
				
				setTouZhu(holder.touzhu1,rzm1);
				
				holder.match1.setVisibility(View.VISIBLE);
				holder.chuanfa.setVisibility(View.VISIBLE);
				holder.matchs.removeAllViews();
				if(rzp.getItems().size()>1){
					for(int i=1;i<rzp.getItems().size();i++){
						RenZhengMatch rzm2 = rzp.getItems().get(i);
						View otherMatch = View.inflate(_this, R.layout.match2_item,null);
						TextView matchkey2 = (TextView) otherMatch.findViewById(R.id.matchkey);
						matchkey2.setText(rzm2.getMatchKey());
						TextView hostteamname2 = (TextView) otherMatch.findViewById(R.id.hostteamname);
						hostteamname2.setText(rzm2.getHomeTeamName());
						TextView gustteamname2 = (TextView) otherMatch.findViewById(R.id.gustteamname);
						gustteamname2.setText(rzm2.getGuestTeamName());
						TextView touzhu = (TextView) otherMatch.findViewById(R.id.touzhu);

						setTouZhu(touzhu,rzm2);
						holder.matchs.addView(otherMatch);
					}
				}
			}else{
				holder.match1.setVisibility(View.GONE);
				holder.chuanfa.setVisibility(View.GONE);
			}
			if(!rzp.isCanCopy()){
				holder.realtimeview.setVisibility(View.GONE);
				holder.tobuy.setVisibility(View.GONE);
			}else{
				holder.realtimeview.setVisibility(View.VISIBLE);
				holder.tobuy.setVisibility(View.VISIBLE);
			}

			switch(rzp.getSchemeStateInt()){
			case 0:
				holder.prize.setVisibility(View.INVISIBLE);
				break;
			case 1:
				holder.prize.setImageResource(R.mipmap.prize_yes);
				holder.prize.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.prize.setImageResource(R.mipmap.prize_no);
				holder.prize.setVisibility(View.VISIBLE);
				break;
			}
			holder.realtime.setText(10+"");

			if(psitiontime.size()>0&&psitiontime.containsKey(position)){
				if(psitiontime.get(position)<=0){
					holder.realtime.setText(1+"");
				}else{
					holder.realtime.setText(psitiontime.get(position)+"");
				}
			}
			holder.realtime.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(realTime!=null){
						realTime.setSelected(false);
					}
					realTime = holder.realtime;
					choosePosition = position;
					timekeybroad.setVisibility(View.VISIBLE);
					return false;
				}
			});
			holder.tobuy.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
						Intent intentLogin = new Intent(_this, LoginActivity.class); 
						intentLogin.putExtra("requestName", "intentLogin");
						startActivity(intentLogin);
					}else{
						Intent intent = new Intent(_this,PayActivity.class);
						Bundle bundle = new Bundle();   
						bundle.putInt("lotteryid",ConstantsBase.JCZQ);
						bundle.putBoolean("useRedPacket",false);
						bundle.putString("selectResourceId",rzp.getCopyParentSchemeId());
						int timeInt = 10;
                        if(psitiontime!=null){
                        	if(psitiontime.containsKey(position)){
                        		timeInt = psitiontime.get(position);
                        	}else{
                        		timeInt = 10;
                        	}
                        }
						long cost = 0l;
						try{
							cost = Long.parseLong(rzp.getSchemeCost())/rzp.getMultiple();
						}catch(Exception e){
							e.printStackTrace();
						}
						if(0 == timeInt ){
                			timeInt =1;
                		}
                	
						bundle.putInt("timeInt",timeInt);
						bundle.putLong("totalaccount",cost*timeInt);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			});
			holder.detils.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent  = new Intent(_this,JCZQOrderDetailActivity.class);
					try {
						intent.putExtra("schemeId", Integer.parseInt(rzp.getSourceSchemeId()));
					} catch (Exception e) {
						e.printStackTrace();
						intent.putExtra("schemeId", -1);
					}
					intent.putExtra("onlyClose", true);
					intent.putExtra("lotteryId", ConstantsBase.JCZQ+"");
					startActivity(intent);
				}
			});
			return convertView;
		}
		public void setTouZhu(TextView touzhu,RenZhengMatch rzm) {  
			if(TextUtils.isEmpty(rzm.getBet())){
				touzhu.setText("保密");
			}else{
				if(rzm.getBet().contains("保密")){
					touzhu.setText("保密");
				}else if(rzm.getBet().contains("{")&&
						rzm.getBet().contains("}")){
					int has1 = rzm.getBet().length() - rzm.getBet().replace("{", "").length();
					int has2 = rzm.getBet().length() - rzm.getBet().replace("}", "").length();
					StringBuffer betSB = new StringBuffer();
					if(has1 == has2){
						String betStr = rzm.getBet();
						betSB.append("<html>");
						for(int i=0;i<has1;i++){
							int index1 = betStr.indexOf("{");
							int index2 = betStr.indexOf("}");
							betSB.append("<font color=\"#454545\">"+betStr.substring(0,index1));
							betSB.append("</font><font color=\"#FF3232\">"+betStr.substring(index1+1,index2)+"</font>");
							betStr = betStr.substring(index2+1, betStr.length());
						}
						betSB.append("<font color=\"#454545\">"+betStr+"</font></html>");
						touzhu.setText(Html.fromHtml(betSB.toString()));
					}else{
						touzhu.setText(rzm.getBet());
						touzhu.setTextColor(Color.parseColor("#454545"));
					}
				}else{
					touzhu.setText(rzm.getBet());
					touzhu.setTextColor(Color.parseColor("#454545"));
				}
			}
		}
		public void disableShowSoftInput(EditText editText) {  
			if (android.os.Build.VERSION.SDK_INT <= 10) {  
				editText.setInputType(InputType.TYPE_NULL);  
			} else {  
				Class<EditText> cls = EditText.class;  
				Method method;  
				try {  
					method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);  
					method.setAccessible(true);  
					method.invoke(editText, false);  
				} catch (Exception e) {  
				}  

				try {  
					method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);  
					method.setAccessible(true);  
					method.invoke(editText, false);  
				} catch (Exception e) {  
				}  
			}  
		}  
		class ViewHolder {
			public TextView endtime;
			public View head_view;
			public ImageView head;
			public TextView name;
			public ImageView v;
			public TextView money;
			public LinearLayout matchs;
			public TextView detils;

			public View match1;
			public TextView matchkey1;
			public TextView hostteamname1,gustteamname1;
			public TextView touzhu1;

			public View realtimeview;
			public TextView realtime;
			public TextView chuanfa;
			public ImageView prize;
			public View tobuy;
		}

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.guanzhu:
			Intent intent = new Intent(_this,MainActivity.class);
			intent.putExtra("hasLogin", "yes");
			intent.putExtra("nowPage", "2");
			startActivity(intent);
			_this.finish();
			break;
		case R.id.realtime:
			timekeybroad.setVisibility(View.VISIBLE);
			break;
		case R.id.number0:
			setTime("0");
			break;
		case R.id.number1:
			setTime("1");
			break;
		case R.id.number2:
			setTime("2");
			break;
		case R.id.number3:
			setTime("3");
			break;
		case R.id.number4:
			setTime("4");
			break;
		case R.id.number5:
			setTime("5");
			break;
		case R.id.number6:
			setTime("6");
			break;
		case R.id.number7:
			setTime("7");
			break;
		case R.id.number8:
			setTime("8");
			break;
		case R.id.number9:
			setTime("9");
			break;
		case R.id.time10:
			clearTime();
			number10.setTextColor(Color.parseColor("#FF3232"));
			number10.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("10");
			psitiontime.put(choosePosition, 10);
			break;
		case R.id.time25:
			clearTime();
			number25.setTextColor(Color.parseColor("#FF3232"));
			number25.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("25");
			psitiontime.put(choosePosition, 25);
			break;
		case R.id.time50:
			clearTime();
			number50.setTextColor(Color.parseColor("#FF3232"));
			number50.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("50");
			psitiontime.put(choosePosition, 50);
			break;
		case R.id.time100:
			clearTime();
			number100.setTextColor(Color.parseColor("#FF3232"));
			number100.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("100");
			psitiontime.put(choosePosition, 100);
			break;
		case R.id.time500:
			clearTime();
			number500.setTextColor(Color.parseColor("#FF3232"));
			number500.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("500");
			psitiontime.put(choosePosition, 500);
			break;
		case R.id.cleartime:
			if(realTime.getText().toString().length()<=1){
				realTime.setText("");
			}else{
				String realtimeStr = realTime.getText().toString();
				realTime.setText(realtimeStr.substring(0, realtimeStr.length()-1));
			}
			int number;
			try{
				number = Integer.parseInt(realTime.getText().toString());
				if(number == 0){
					number = 1;
				}
			}catch(Exception e){
				e.printStackTrace();
				number = 1 ;
			}
			psitiontime.put(choosePosition, number);
			break;
		case R.id.suretime:
			timekeybroad.setVisibility(View.GONE);
			break;
		}
	}
}
