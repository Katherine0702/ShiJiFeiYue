package com.cshen.tiyu.activity.mian4.find;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3AccountListActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3MainActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.cai115.Account115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Accountgd115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.activity.lottery.cai115.MainGd115Activity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.activity.lottery.dlt.DLTAccountListActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCMainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQAccountListActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.activity.mian4.main.SignActivity;
import com.cshen.tiyu.activity.redpacket.RedPacketRainActivity;
import com.cshen.tiyu.activity.taocan.TaoCanMainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.activity.ActivityData;
import com.cshen.tiyu.domain.activity.ActivityEach;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceActivity;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class ActivityActivity extends BaseActivity{
	public static final int TOSign = 2;
	private ActivityActivity _this;
	private TopViewLeft tv_head;//头

	private View nodata;
	private RefreshListView refreshListView;

	private ActivityListAdapter myListAdapter = null;
	private ArrayList<ActivityEach> activityLists;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private ServiceActivity caizhongService ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityactivity);
		_this = this;
		initView();
		initdata(); 
	}
	public void initView(){
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});

		nodata = findViewById(R.id.nodataview);
		refreshListView = (RefreshListView) findViewById(R.id.rl_listview);
		myListAdapter = new ActivityListAdapter();
		refreshListView.setAdapter(myListAdapter);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				ActivityEach ae0 = activityLists.get(position);
				toUrl(ae0.getUseLocal(),ae0.getLotteryId(),ae0.getUrl(),ae0.getTitle());
			}
		});
		// 添加监听器
		refreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 自动生成的方法存根
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
					caizhongService.GetActivity(_this,step+"",start+"",
							new CallBack<ActivityData>() {

						@Override
						public void onSuccess(ActivityData t) {
							// TODO 自动生成的方法存根

							ArrayList<ActivityEach> newlists = t.getMyScheme();// 加载数据结束
							if (newlists.size()==0) {
								ToastUtils.showShort(_this, "已无更多数据");
								more--;
							}
							if(activityLists== null){
								activityLists = new ArrayList<ActivityEach>();
							}
							activityLists.addAll(newlists);
							if(myListAdapter!=null){
								myListAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
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

	private void initdata() {
		caizhongService = ServiceActivity.getInstance();		
		initAllOrdersForList();
	}

	private void initAllOrdersForList() {
		more = 0;
		if(activityLists!=null){
			activityLists.clear();
		}
		if(myListAdapter!=null){
			myListAdapter.notifyDataSetChanged();// 重新刷新数据
		}
		caizhongService.GetActivity(_this,step+"",more+"",
				new CallBack<ActivityData>() {

			@Override
			public void onSuccess(ActivityData t) {
				// TODO 自动生成的方法存根	
				if(activityLists== null){
					activityLists = new ArrayList<ActivityEach>();
				}
				activityLists.addAll(t.getMyScheme());
				if (activityLists.size()==0) {
					nodata.setVisibility(View.VISIBLE);
					refreshListView.setVisibility(View.GONE);
				}else{
					nodata.setVisibility(View.GONE);
					refreshListView.setVisibility(View.VISIBLE);
				}
				myListAdapter.setDate(activityLists);
				myListAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete(false);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根				
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(myListAdapter!=null){
					myListAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
			}
		});
	}
	class ActivityListAdapter extends BaseAdapter{
		private ArrayList<ActivityEach> orderLists;
		public void setDate(ArrayList<ActivityEach> orderListsFrom){
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
		public ActivityEach getItem(int position) {
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
			ActivityEach order = orderLists.get(position);

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.activity_item,null);
				holder.pic = (ImageView) convertView.findViewById(R.id.pic);
				holder.state = (ImageView) convertView.findViewById(R.id.state);
				holder.title = (TextView) convertView.findViewById(R.id.tv_activityname);
				holder.startTime = (TextView) convertView.findViewById(R.id.tv_time_start);
				holder.endTime = (TextView) convertView.findViewById(R.id.tv_time_end);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if(order!=null){
				try {
					xUtilsImageUtils.displayIN(holder.pic, R.mipmap.defaultactivitypic, URLEncoder.encode(order.getImageURL(), "UTF-8").replace("%3A", ":").replace("%2F", "/"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				holder.title.setText(order.getTitle());
				holder.startTime.setText(order.getStartTime());
				holder.state.setVisibility(View.INVISIBLE);
				String end = order.getEndTime();
				holder.endTime.setText(order.getEndTime());
				if(order.getStillTop() == 1){
					holder.state.setImageResource(R.mipmap.activity_chot);
					holder.state.setVisibility(View.VISIBLE);
				}
				String now = DateUtils.getNowDate();
				Date  nowD= DateUtils.toDate(now);
				Date endD = DateUtils.toDate(end);
				if(nowD.after(endD)){//当前日期在截止日之后
					holder.state.setImageResource(R.mipmap.activity_end);
					holder.state.setVisibility(View.VISIBLE);
				}
			}
			return convertView;

		}
		class ViewHolder {
			public ImageView pic;
			public ImageView state;
			public TextView title;
			public TextView startTime;
			public TextView endTime;
		}
	}
	public void toUrl(String local,String lotteryId,String url,String activityName){
		if("0".equals(local)){
			if("taocan".equals(url.trim())){
				Intent intentHelp = new Intent(_this,TaoCanMainActivity.class);
				startActivity(intentHelp);
			}else if("qiandao".equals(url.trim())){
				if (PreferenceUtil.getBoolean(_this,"hasLogin")) {
					Intent intentHelp = new Intent(_this,SignActivity.class);
					startActivity(intentHelp);
				} else {
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					startActivityForResult(intentLogin, TOSign);
				}
			}else{
				Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
				intentHelp.putExtra("url",url);
				intentHelp.putExtra("activityName",activityName);
				startActivity(intentHelp);
			}			
		}
		if("1".equals(local)){
			Intent intent;
			if((ConstantsBase.DLT+"").equals(lotteryId)){  
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERS);
				if(list==null){
					intent = new Intent(_this, DLTMainActivity.class);					
				}else{
					intent = new Intent(_this, DLTAccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("dltNumberList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.SD115+"").equals(lotteryId)){
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERS115);
				if(list==null){
					intent = new Intent(_this, Main115Activity.class);					
				}else{
					intent = new Intent(_this, Account115ListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("number115List", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.GD115+"").equals(lotteryId)){
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERSGD115);
				if(list==null){
					intent = new Intent(_this, MainGd115Activity.class);					
				}else{
					intent = new Intent(_this, Accountgd115ListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("numbergd115List", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.Fast3+"").equals(lotteryId)){
				ArrayList<NumberFast> list = (ArrayList<NumberFast>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERSFAST3);
				if(list==null){
					intent = new Intent(_this, Fast3MainActivity.class);					
				}else{
					intent = new Intent(_this, Fast3AccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("numberfastList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.JCZQ+"").equals(lotteryId)){
				intent = new Intent(_this, FootballMainActivity.class);	
				startActivity(intent);
			}else if((ConstantsBase.JCLQ+"").equals(lotteryId)){
				intent = new Intent(_this, BasketBallMainActivity.class);
				startActivity(intent);
			}else if((ConstantsBase.SFC+"").equals(lotteryId)){
				intent = new Intent(_this, SFCMainActivity.class);
				startActivity(intent);
			}else if((ConstantsBase.SSQ+"").equals(lotteryId)){
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERSSSQ);
				if(list==null){
					intent = new Intent(_this, SSQMainActivity.class);					
				}else{
					intent = new Intent(_this, SSQAccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("ssqtNumberList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if ("4".equals(lotteryId)) {
			//	MobclickAgent.onEvent(_this, "buttonredpacket");// 统计
				Intent intent4 = new Intent(_this,RedPacketRainActivity.class);
				startActivity(intent4);
			}else {
				ToastUtils.showShort(_this, "敬请期待......");
			}
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TOSign:
				Intent intentHelp = new Intent(_this,SignActivity.class);
				startActivity(intentHelp);
				break;	
			}
		}
	}
}
