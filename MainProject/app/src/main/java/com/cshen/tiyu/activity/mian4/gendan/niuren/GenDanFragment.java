package com.cshen.tiyu.activity.mian4.gendan.niuren;

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

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.MyFollowRecommend;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.NiuRenDetil;
import com.cshen.tiyu.domain.gendan.RenZhengMatch;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


import de.greenrobot.event.EventBus;

public class GenDanFragment  extends Fragment implements OnClickListener{

	TextView zhong1,zhong2,zhong3,zhong4,zhong5;
	TextView zhong2line,zhong3line,zhong4line,zhong5line;
	TextView mingzhonglv,yinglilv,tuijianzhongjiang;
	private RefreshListView listview;
	/*******倍数相关
	 **************/
	public TextView realTime;//倍数
	public int timeIntMAX = 99999;//总倍数
	public View timekeybroad;//画的键盘和付款栏
	private TextView number10,number25,number50,number100,number500;
	private int choosePosition;
	private HashMap<Integer,Integer> psitiontime = new HashMap<Integer,Integer>();

	private Activity _this;
	private RenZhengPeopleAdapter adapter;
	private ArrayList<MyFollowRecommend> renzhenglist;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private ServiceGenDan gendanService;
	private boolean isGetting = false;
	private String userId ="",userPwd ="";
	private NiuRen niuren;
	private String id; 
	CallBackValue callBackValue;  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gendan_fragment, container,false);
		_this = this.getActivity();
		EventBus.getDefault().register(this);
		Bundle bundle = getArguments();
		id =  bundle.getString("id");//这里的values就是我们要传的值
		initView(view);// 初使化控件
		getData(); // 获取数据
		return view;
	}
	public void initView(View view){
		View headListView = View.inflate(_this, R.layout.niurendetilhead, null);
		mingzhonglv  = (TextView) headListView.findViewById(R.id.mingzhonglv); 
		yinglilv  = (TextView) headListView.findViewById(R.id.yinglilv); 
		tuijianzhongjiang  = (TextView) headListView.findViewById(R.id.tuijianzhongjiang); 

		zhong1  = (TextView) headListView.findViewById(R.id.zhong1); 
		zhong2line  = (TextView) headListView.findViewById(R.id.zhong2line); 
		zhong2  = (TextView) headListView.findViewById(R.id.zhong2); 
		zhong3line  = (TextView) headListView.findViewById(R.id.zhong3line); 
		zhong3  = (TextView) headListView.findViewById(R.id.zhong3); 
		zhong4line = (TextView) headListView.findViewById(R.id.zhong4line); 
		zhong4  = (TextView) headListView.findViewById(R.id.zhong4); 
		zhong5line  = (TextView) headListView.findViewById(R.id.zhong5line); 
		zhong5  = (TextView) headListView.findViewById(R.id.zhong5); 		

		listview = (RefreshListView) view.findViewById(R.id.listview);
		listview.addHeaderView(headListView);
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

		adapter = new RenZhengPeopleAdapter();
		listview.setAdapter(adapter);     
		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 自动生成的方法存根

				psitiontime.clear();
				realTime = null;
				requestDataFromServer(false); // 冲服务器中获取刷新的数据
			}

			@Override
			public void onLoadMore() {
				// TODO 自动生成的方法存根
				requestDataFromServer(true);
			}

			private void requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if(!isGetting){
					isGetting = true;
				}
				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					gendanService.getNiuRenDetil(_this,start,step,id,userId,userPwd,new CallBack<NiuRenDetil>() {

						@Override
						public void onSuccess(NiuRenDetil t) {
							// TODO 自动生成的方法存根
							dodata(t,false);
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							if(adapter!=null){
								adapter.notifyDataSetChanged();// 重新刷新数据
							}
							listview.onRefreshComplete(false);
							isGetting = false;
						}
					});
				} else {
					initAllOrdersForList();
				}

			}
		});
	}
	public void getData(){
		if (PreferenceUtil.getBoolean(_this,"hasLogin")) {
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
		}else{
			userId = "";
			userPwd = "";
		}
		gendanService = ServiceGenDan.getInstance();
		if(!isGetting){
			isGetting = true;
			initAllOrdersForList();
		}
	}
	private void initAllOrdersForList() {
		more = 0;
		gendanService.getNiuRenDetil(_this,more,step,id,userId,userPwd,
				new CallBack<NiuRenDetil>() {

			@Override
			public void onSuccess(NiuRenDetil t) {
				// TODO 自动生成的方法存根	
				dodata(t,true);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(adapter!=null){
					adapter.notifyDataSetChanged();// 重新刷新数据
				}
				listview.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(NiuRenDetil t,boolean isRefresh){
		if(isRefresh){
			niuren = t.getResult();
			if(niuren!=null){
				setBaseData();
			}
			if(renzhenglist== null){
				renzhenglist = new ArrayList<MyFollowRecommend>();
			}else{
				renzhenglist.clear();
			}
			if(t.getResultList()!=null&& t.getResultList().size()>0){
				renzhenglist.addAll(t.getResultList());               				
				adapter.setDate(renzhenglist);
			}
		}else{
			ArrayList<MyFollowRecommend> newlists = t.getResultList();// 加载数据结束
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(renzhenglist== null){
				renzhenglist = new ArrayList<MyFollowRecommend>();
			}
			renzhenglist.addAll(newlists);
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();// 重新刷新数据
		}
		listview.onRefreshComplete(false);
		isGetting = false;
	}
	private void setBaseData(){
		callBackValue.SendMessageValue(niuren);  
		if(TextUtils.isEmpty(niuren.getHitRate())
				||"0".equals(niuren.getHitRate())){
			mingzhonglv.setText(100+"");
		}else{
			mingzhonglv.setText(niuren.getHitRate()+"");
		}
		if(TextUtils.isEmpty(niuren.getEarningsRate())
				||"0".equals(niuren.getEarningsRate())){
			yinglilv.setText(100+"");
		}else{
			yinglilv.setText(niuren.getEarningsRate()+"");
		}
		tuijianzhongjiang.setText(niuren.getAllCopiedSchemePrize()+"元");

		if(niuren.getNearFiveResult()!=null&&niuren.getNearFiveResult().size()>0){
			if(niuren.getNearFiveResult().size() == 1){
				zhong1.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(0)){
					zhong1.setText("中");
					zhong1.setBackgroundResource(R.drawable.redball);
				}else{
					zhong1.setText("未");
					zhong1.setBackgroundResource(R.drawable.darkgreyball);
				}
			}if(niuren.getNearFiveResult().size() == 2){
				zhong1.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(0)){
					zhong1.setText("中");
					zhong1.setBackgroundResource(R.drawable.redball);
				}else{
					zhong1.setText("未");
					zhong1.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong2line.setVisibility(View.VISIBLE);
				zhong2.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(1)){
					zhong2.setText("中");
					zhong2.setBackgroundResource(R.drawable.redball);
				}else{
					zhong2.setText("未");
					zhong2.setBackgroundResource(R.drawable.darkgreyball);
				}
			}if(niuren.getNearFiveResult().size() == 3){
				zhong1.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(0)){
					zhong1.setText("中");
					zhong1.setBackgroundResource(R.drawable.redball);
				}else{
					zhong1.setText("未");
					zhong1.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong2line.setVisibility(View.VISIBLE);
				zhong2.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(1)){
					zhong2.setText("中");
					zhong2.setBackgroundResource(R.drawable.redball);
				}else{
					zhong2.setText("未");
					zhong2.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong3line.setVisibility(View.VISIBLE);
				zhong3.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(2)){
					zhong3.setText("中");
					zhong3.setBackgroundResource(R.drawable.redball);
				}else{
					zhong3.setText("未");
					zhong3.setBackgroundResource(R.drawable.darkgreyball);
				}
			}if(niuren.getNearFiveResult().size() == 4){
				zhong1.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(0)){
					zhong1.setText("中");
					zhong1.setBackgroundResource(R.drawable.redball);
				}else{
					zhong1.setText("未");
					zhong1.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong2line.setVisibility(View.VISIBLE);
				zhong2.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(1)){
					zhong2.setText("中");
					zhong2.setBackgroundResource(R.drawable.redball);
				}else{
					zhong2.setText("未");
					zhong2.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong3line.setVisibility(View.VISIBLE);
				zhong3.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(2)){
					zhong3.setText("中");
					zhong3.setBackgroundResource(R.drawable.redball);
				}else{
					zhong3.setText("未");
					zhong3.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong4line.setVisibility(View.VISIBLE);
				zhong4.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(3)){
					zhong4.setText("中");
					zhong4.setBackgroundResource(R.drawable.redball);
				}else{
					zhong4.setText("未");
					zhong4.setBackgroundResource(R.drawable.darkgreyball);
				}
			}if(niuren.getNearFiveResult().size() == 5){
				zhong1.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(0)){
					zhong1.setText("中");
					zhong1.setBackgroundResource(R.drawable.redball);
				}else{
					zhong1.setText("未");
					zhong1.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong2line.setVisibility(View.VISIBLE);
				zhong2.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(1)){
					zhong2.setText("中");
					zhong2.setBackgroundResource(R.drawable.redball);
				}else{
					zhong2.setText("未");
					zhong2.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong3line.setVisibility(View.VISIBLE);
				zhong3.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(2)){
					zhong3.setText("中");
					zhong3.setBackgroundResource(R.drawable.redball);
				}else{
					zhong3.setText("未");
					zhong3.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong4line.setVisibility(View.VISIBLE);
				zhong4.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(3)){
					zhong4.setText("中");
					zhong4.setBackgroundResource(R.drawable.redball);
				}else{
					zhong4.setText("未");
					zhong4.setBackgroundResource(R.drawable.darkgreyball);
				}
				zhong5line.setVisibility(View.VISIBLE);
				zhong5.setVisibility(View.VISIBLE);
				if(niuren.getNearFiveResult().get(4)){
					zhong5.setText("中");
					zhong5.setBackgroundResource(R.drawable.redball);
				}else{
					zhong5.setText("未");
					zhong5.setBackgroundResource(R.drawable.darkgreyball);
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
			number10.setTextColor(getResources().getColor(R.color.mainred));
			number10.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("10");
			psitiontime.put(choosePosition, 10);
			break;
		case R.id.time25:
			clearTime();
			number25.setTextColor(getResources().getColor(R.color.mainred));
			number25.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("25");
			psitiontime.put(choosePosition, 15);
			break;
		case R.id.time50:
			clearTime();
			number50.setTextColor(getResources().getColor(R.color.mainred));
			number50.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("50");
			psitiontime.put(choosePosition, 50);
			break;
		case R.id.time100:
			clearTime();
			number100.setTextColor(getResources().getColor(R.color.mainred));
			number100.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("100");
			psitiontime.put(choosePosition, 100);
			break;
		case R.id.time500:
			clearTime();
			number500.setTextColor(getResources().getColor(R.color.mainred));
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
	class RenZhengPeopleAdapter extends BaseAdapter {
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
				convertView = View.inflate(_this, R.layout.niurendetil_item,null);
				holder.date = (TextView) convertView.findViewById(R.id.buytime);
				holder.gendan = (ImageView) convertView.findViewById(R.id.gendan);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				holder.chuanfa = (TextView) convertView.findViewById(R.id.chuanfa);
				holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
				holder.matchs = (LinearLayout) convertView.findViewById(R.id.matchs);


				holder.match1 = convertView.findViewById(R.id.match1);
				holder.matchkey1 = (TextView) convertView.findViewById(R.id.matchkey1);
				holder.hostteamname1 = (TextView) convertView.findViewById(R.id.hostteamname1);
				holder.gustteamname1 = (TextView) convertView.findViewById(R.id.gustteamname1);
				holder.touzhu1 = (TextView) convertView.findViewById(R.id.touzhu1);


				holder.tuijian = convertView.findViewById(R.id.tuijian);
				holder.tuijiantitle = (TextView) convertView.findViewById(R.id.tuijiantitle);

				holder.prize = (ImageView) convertView.findViewById(R.id.prize);
				holder.timeview = convertView.findViewById(R.id.timeview);
				holder.time = (EditText)convertView.findViewById(R.id.realtime);
				disableShowSoftInput(holder.time);
				holder.tobuy = convertView.findViewById(R.id.tobuy);
				convertView.setTag(holder);
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			} 
			holder.date.setText(rzp.getCreateTime());
			switch(rzp.getSchemeStateInt()){
			case 0:
				holder.prize.setVisibility(View.INVISIBLE);
				holder.gendan.setImageResource(R.mipmap.gendan_can);
				if(rzp.isCanCopy()){
					holder.endtime.setVisibility(View.VISIBLE);
					holder.tobuy.setVisibility(View.VISIBLE);
					holder.timeview.setVisibility(View.VISIBLE);
				}else{
					holder.tobuy.setVisibility(View.INVISIBLE);
					holder.timeview.setVisibility(View.INVISIBLE);
					holder.endtime.setVisibility(View.INVISIBLE);
				}
				break;
			case 1:
				holder.gendan.setImageResource(R.mipmap.gendan_ok);
				holder.prize.setImageResource(R.mipmap.prize_yes);
				holder.prize.setVisibility(View.VISIBLE);
				holder.tobuy.setVisibility(View.INVISIBLE);
				holder.timeview.setVisibility(View.INVISIBLE);
				holder.endtime.setVisibility(View.INVISIBLE);
				break;
			case 2:
				holder.gendan.setImageResource(R.mipmap.gendan_no);
				holder.prize.setImageResource(R.mipmap.prize_no);
				holder.prize.setVisibility(View.VISIBLE);
				holder.tobuy.setVisibility(View.INVISIBLE);
				holder.timeview.setVisibility(View.INVISIBLE);
				holder.endtime.setVisibility(View.INVISIBLE);
				break;
			}
			holder.time.setText(10+"");

			if(psitiontime.size()>0&&psitiontime.containsKey(position)){
				if(psitiontime.get(position)<=0){
					holder.time.setText(1+"");
				}else{
					holder.time.setText(psitiontime.get(position)+"");
				}
			}

			holder.time.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(realTime!=null){
						realTime.setSelected(false);
					}
					realTime = holder.time;
					choosePosition = position;
					timekeybroad.setVisibility(View.VISIBLE);
					return false;
				}
			});
			holder.money.setText(rzp.getCopiedSchemePrize()+"元");
			holder.endtime.setText(rzp.getFirstMatchTime());
			if(rzp.getItems().size()>0){
				RenZhengMatch rzm1 = rzp.getItems().get(0);
				holder.matchkey1.setText(rzm1.getMatchKey());
				holder.hostteamname1.setText(rzm1.getHomeTeamName());
				holder.gustteamname1.setText(rzm1.getGuestTeamName());

				setTouZhu(holder.touzhu1,rzm1);

				holder.match1.setVisibility(View.VISIBLE);
				holder.chuanfa.setText(rzp.getPassTypeStr());
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

			if(TextUtils.isEmpty(rzp.getRecommendTitle())
					&&TextUtils.isEmpty(rzp.getRecommendContent())){
				holder.tuijian.setVisibility(View.INVISIBLE);
			}else{
				holder.tuijian.setVisibility(View.VISIBLE);
				if(!TextUtils.isEmpty(rzp.getRecommendTitle())){
					holder.tuijiantitle.setText(rzp.getRecommendTitle());
				}else{
					holder.tuijiantitle.setVisibility(View.GONE);
				}
			}
			holder.tobuy.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
						Intent intentLogin = new Intent(_this, LoginActivity.class); 
						intentLogin.putExtra("requestName", "intentLogin");
						startActivity(intentLogin);
					}else{
						String userId = MyDbUtils.getCurrentUser().getUserId();
						if(!TextUtils.isEmpty(niuren.getId())&&niuren.getId().equals(userId)){
							ToastUtils.showShort(_this, "不能跟自己的单子哦");
							return;
						}
						Intent intent = new Intent(_this,PayActivity.class);
						Bundle bundle = new Bundle();   
						bundle.putInt("lotteryid",ConstantsBase.JCZQ);
						bundle.putBoolean("useRedPacket",false);
						bundle.putString("SchemeBackupsId",rzp.getSchemeBackupsId());
						int timeInt = 0;
						if(psitiontime!=null){
							if(psitiontime.containsKey(position)){
								timeInt = psitiontime.get(position);
							}else{
								timeInt = 10;
							}
						}
						bundle.putInt("timeInt",timeInt);
						long cost = 0l;
						try{
							cost = Long.parseLong(rzp.getSchemeCost())/rzp.getMultiple();
						}catch(Exception e){
							e.printStackTrace();
						}
						if(0 == timeInt ){
							timeInt =1;
						}

						bundle.putLong("totalaccount",cost*timeInt);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			});
			return convertView;
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
		public void setTouZhu(TextView touzhu,RenZhengMatch rzm) {  
			if(TextUtils.isEmpty(rzm.getBet())){
				touzhu.setText("保密");
				touzhu.setTextColor(getResources().getColor(R.color.mainred));
			}else{
				if(rzm.getBet().contains("保密")){
					touzhu.setText("保密");
					touzhu.setTextColor(getResources().getColor(R.color.mainred));
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
	}
	class ViewHolder {
		public TextView date;
		public ImageView gendan;
		public TextView money;
		public TextView chuanfa;
		public TextView endtime;

		public LinearLayout matchs;
		public View match1;
		public TextView matchkey1;
		public TextView hostteamname1,gustteamname1,touzhu1;

		public View  tuijian;
		public TextView  tuijiantitle;

		public ImageView prize;

		public View timeview;
		public EditText time;
		public View tobuy;
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
	public void onEventMainThread(String event) {

		if (!TextUtils.isEmpty(event)) {
			if ("getDataGenDan".equals(event)) {
				getData();
			}
		}
	}@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}
	/** 
	 * fragment与activity产生关联是  回调这个方法  
	 */  
	@Override  
	public void onAttach(Activity activity) {  
		// TODO Auto-generated method stub  
		super.onAttach(activity);  
		//当前fragment从activity重写了回调接口  得到接口的实例化对象  
		callBackValue =(CallBackValue) getActivity();  
	} 
	public interface CallBackValue{  
		public void SendMessageValue(NiuRen strValue);  
	}  
}
